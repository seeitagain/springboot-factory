/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.example.lc.beisen.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import org.example.lc.beisen.BeisenException;
import org.example.lc.beisen.Configuration;
import org.example.lc.beisen.Constants;


;

/**
 * A utility class to handle HTTP request/response.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class HttpClient implements java.io.Serializable {
	private static final int OK = 200;// OK: Success!
	private static final int NOT_MODIFIED = 304;// Not Modified: There was no
												// new data to return.
	private static final int BAD_REQUEST = 400;// Bad Request: The request was
												// invalid. An accompanying
												// error message will explain
												// why. This is the status code
												// will be returned during rate
												// limiting.
	private static final int NOT_AUTHORIZED = 401;// Not Authorized:
													// Authentication
													// credentials were missing
													// or incorrect.
	private static final int FORBIDDEN = 403;// Forbidden: The request is
												// understood, but it has been
												// refused. An accompanying
												// error message will explain
												// why.
	private static final int NOT_FOUND = 404;// Not Found: The URI requested is
												// invalid or the resource
												// requested, such as a user,
												// does not exists.
	private static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by
													// the Search API when an
													// invalid format is
													// specified in the request.
	private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server
															// Error: Something
															// is broken. Please
															// post to the group
															// so the Beisen
															// team can
															// investigate.
	private static final int BAD_GATEWAY = 502;// Bad Gateway: Beisen is down or
												// being upgraded.
	private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable:
														// The Beisen servers
														// are up, but
														// overloaded with
														// requests. Try again
														// later. The search and
														// trend methods use
														// this to indicate when
														// you are being rate
														// limited.

	private enum Method {
		GET, DELETE, PUT, POST
	};

	private final static boolean DEBUG = Configuration.getDebug();

	private int retryCount = Configuration.getRetryCount();
	private int retryIntervalMillis = Configuration.getRetryIntervalSecs() * 1000;
	private String userId = Configuration.getUser();
	private String password = Configuration.getPassword();
	private String proxyHost = Configuration.getProxyHost();
	private int proxyPort = Configuration.getProxyPort();
	private String proxyAuthUser = Configuration.getProxyUser();
	private String proxyAuthPassword = Configuration.getProxyPassword();
	private int connectionTimeout = Configuration.getConnectionTimeout();
	private int readTimeout = Configuration.getReadTimeout();
	private static final long serialVersionUID = 808018030183407996L;
	private static boolean isJDK14orEarlier = false;
	private Map<String, String> requestHeaders = new HashMap<String, String>();
	private OAuth oauth = null;
	private String requestTokenURL = Configuration.getScheme()+ "api.beisen.com/oauth/request_token";
	private String authorizationURL = Configuration.getScheme()+ "api.beisen.com/oauth/authorize";
	private String authenticationURL = Configuration.getScheme()+ "api.beisen.com/oauth/authenticate";
	private String accessTokenURL = Configuration.getScheme()+ "api.beisen.com/oauth/access_token";
	private OAuthToken oauthToken = null;
	private String token = null;
	
	private static final String ENCODING = "UTF-8";
	private static final Pattern NVP = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
	private static final String AUTH_SCHEME = "OAuth";
	private static final Pattern AUTHORIZATION = Pattern.compile("\\s*(\\w*)\\s+(.*)");

	static {
		try {
			String versionStr = System
					.getProperty("java.specification.version");
			if (null != versionStr) {
				isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
			}
		} catch (AccessControlException ace) {
			isJDK14orEarlier = true;
		}
	}

	public HttpClient() {
		setUserAgent(null);
		setOAuthConsumer(null, null);
		setRequestHeader("Accept-Encoding", "gzip");
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public boolean isAuthenticationEnabled() {
		return null != oauth;
	}

	/**
	 * Sets the consumer key and consumer secret.<br>
	 * System property -beisen.oauth.consumerKey and -Dhttp.oauth.consumerSecret
	 * override this attribute.
	 * 
	 * @param consumerKey
	 *            Consumer Key
	 * @param consumerSecret
	 *            Consumer Secret
	 * @since Beisen 1.0.0
	 * @see <a href="http://ap.beisen.com/wiki/Oauth">Applications Using
	 *      Beisen</a>
	 */
	public void setOAuthConsumer(String consumerKey, String consumerSecret) {
		consumerKey = Configuration.getOAuthConsumerKey(consumerKey);
		consumerSecret = Configuration.getOAuthConsumerSecret(consumerSecret);
		if (null != consumerKey && null != consumerSecret
				&& 0 != consumerKey.length() && 0 != consumerSecret.length()) {
			this.oauth = new OAuth(consumerKey, consumerSecret);
		}
	}

	public RequestToken setToken(String token, String tokenSecret) {
		this.token = token;
		this.oauthToken = new RequestToken(token, tokenSecret);
		return (RequestToken) this.oauthToken;
	}

	/**
	 * 
	 * @return request token
	 * @throws BeisenException
	 *             tw
	 * @since Beisen 1.0.0
	 */
	public RequestToken getOAuthRequestToken() throws BeisenException {
		this.oauthToken = new RequestToken(httpRequest(requestTokenURL, null,
				true, Method.GET), this);
		return (RequestToken) this.oauthToken;
	}

	/**
	 * @param callback_url
	 *            callback url
	 * @return request token
	 * @throws BeisenException
	 *             tw
	 * @since Beisen 1.0.0
	 */
	public RequestToken getOauthRequestToken(String callback_url)
			throws BeisenException {
		this.oauthToken = new RequestToken(httpRequest(requestTokenURL,
				new PostParameter[] { new PostParameter("oauth_callback",
						callback_url) }, true, Method.POST), this);
		return (RequestToken) this.oauthToken;
	}

	/**
	 * 
	 * @param token
	 *            request token
	 * @return access token
	 * @throws BeisenException
	 * @since Beisen 1.0.0
	 */
	public AccessToken getOAuthAccessToken(RequestToken token)
			throws BeisenException {
		try {
			this.oauthToken = token;
			this.oauthToken = new AccessToken(httpRequest(accessTokenURL,
					new PostParameter[0], true, Method.POST));
		} catch (BeisenException te) {
			throw new BeisenException(
					"The user has not given access to the account.", te, te
							.getStatusCode());
		}
		return (AccessToken) this.oauthToken;
	}

	/**
	 * 
	 * @param token
	 *            request token
	 * @return access token
	 * @throws BeisenException
	 * @since Beisen 1.0.0
	 */
	public AccessToken getOAuthAccessToken(RequestToken token, String pin)
			throws BeisenException {
		try {
			this.oauthToken = token;
			this.oauthToken = new AccessToken(httpRequest(accessTokenURL,
					new PostParameter[] { new PostParameter("oauth_verifier",
							pin) }, true, Method.POST));
		} catch (BeisenException te) {
			throw new BeisenException(
					"The user has not given access to the account.", te, te
							.getStatusCode());
		}
		return (AccessToken) this.oauthToken;
	}

	/**
	 * 
	 * @param token
	 *            request token
	 * @param tokenSecret
	 *            request token secret
	 * @return access token
	 * @throws BeisenException
	 * @since Beisen 1.0.0
	 */
	public AccessToken getOAuthAccessToken(String token, String tokenSecret)
			throws BeisenException {
		try {
			this.oauthToken = new OAuthToken(token, tokenSecret) {
			};
			this.oauthToken = new AccessToken(httpRequest(accessTokenURL,
					new PostParameter[0], true, Method.POST));
		} catch (BeisenException te) {
			throw new BeisenException(
					"The user has not given access to the account.", te, te
							.getStatusCode());
		}
		return (AccessToken) this.oauthToken;
	}

	/**
	 * 
	 * @param token
	 *            request token
	 * @param tokenSecret
	 *            request token secret
	 * @param oauth_verifier
	 *            oauth_verifier or pin
	 * @return access token
	 * @throws BeisenException
	 * @since Beisen 1.0.0
	 */
	public AccessToken getOAuthAccessToken(String token, String tokenSecret,
			String oauth_verifier) throws BeisenException {
		try {
			this.oauthToken = new OAuthToken(token, tokenSecret) {
			};
			this.oauthToken = new AccessToken(httpRequest(accessTokenURL,
					new PostParameter[] { new PostParameter("oauth_verifier",
							oauth_verifier) }, true, Method.POST));
		} catch (BeisenException te) {
			throw new BeisenException(
					"The user has not given access to the account.", te, te
							.getStatusCode());
		}
		return (AccessToken) this.oauthToken;
	}

	public AccessToken getXAuthAccessToken(String userId, String passWord,
			String mode) throws BeisenException {
		this.oauthToken = new AccessToken(httpRequest(accessTokenURL,
				new PostParameter[] {
						new PostParameter("x_auth_username", userId),
						new PostParameter("x_auth_password", passWord),
						new PostParameter("x_auth_mode", mode) }, true,
				Method.POST));
		return (AccessToken) this.oauthToken;
	}

	/**
	 * Sets the authorized access token
	 * 
	 * @param token
	 *            authorized access token
	 * @since Beisen 1.0.0
	 */

	public void setOAuthAccessToken(AccessToken token) {
		this.oauthToken = token;
	}

	public void setRequestTokenURL(String requestTokenURL) {
		this.requestTokenURL = requestTokenURL;
	}

	public String getRequestTokenURL() {
		return requestTokenURL;
	}

	public void setAuthorizationURL(String authorizationURL) {
		this.authorizationURL = authorizationURL;
	}

	public String getAuthorizationURL() {
		return authorizationURL;
	}

	public String getAuthenticationURL() {
		return authenticationURL;
	}

	public void setAuthenticationURL(String authenticationURL) {
		this.authenticationURL = authenticationURL;
	}

	/**
	 * since Beisen 1.0.0
	 */
	public String getAuthenticationRL() {
		return authenticationURL;
	}

	public void setAccessTokenURL(String accessTokenURL) {
		this.accessTokenURL = accessTokenURL;
	}

	public String getAccessTokenURL() {
		return accessTokenURL;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Sets proxy host. System property -beisen.http.proxyHost or http.proxyHost
	 * overrides this attribute.
	 * 
	 * @param proxyHost
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = Configuration.getProxyHost(proxyHost);
	}

	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets proxy port. System property -beisen.http.proxyPort or
	 * -Dhttp.proxyPort overrides this attribute.
	 * 
	 * @param proxyPort
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = Configuration.getProxyPort(proxyPort);
	}

	public String getProxyAuthUser() {
		return proxyAuthUser;
	}

	/**
	 * Sets proxy authentication user. System property -beisen.http.proxyUser
	 * overrides this attribute.
	 * 
	 * @param proxyAuthUser
	 */
	public void setProxyAuthUser(String proxyAuthUser) {
		this.proxyAuthUser = Configuration.getProxyUser(proxyAuthUser);
	}

	public String getProxyAuthPassword() {
		return proxyAuthPassword;
	}

	/**
	 * Sets proxy authentication password. System property
	 * -beisen.http.proxyPassword overrides this attribute.
	 * 
	 * @param proxyAuthPassword
	 */
	public void setProxyAuthPassword(String proxyAuthPassword) {
		this.proxyAuthPassword = Configuration
				.getProxyPassword(proxyAuthPassword);
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Sets a specified timeout value, in milliseconds, to be used when opening
	 * a communications link to the resource referenced by this URLConnection.
	 * System property -beisen.http.connectionTimeout overrides this attribute.
	 * 
	 * @param connectionTimeout
	 *            - an int that specifies the connect timeout value in
	 *            milliseconds
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = Configuration
				.getConnectionTimeout(connectionTimeout);

	}

	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Sets the read timeout to a specified timeout, in milliseconds. System
	 * property -beisen.http.readTimeout overrides this attribute.
	 * 
	 * @param readTimeout
	 *            - an int that specifies the timeout value to be used in
	 *            milliseconds
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = Configuration.getReadTimeout(readTimeout);
	}

	public void setRetryCount(int retryCount) {
		if (retryCount >= 0) {
			this.retryCount = Configuration.getRetryCount(retryCount);
		} else {
			throw new IllegalArgumentException("RetryCount cannot be negative.");
		}
	}

	public void setUserAgent(String ua) {
		setRequestHeader("User-Agent", Configuration.getUserAgent(ua));
	}

	public String getUserAgent() {
		return getRequestHeader("User-Agent");
	}

	public void setRetryIntervalSecs(int retryIntervalSecs) {
		if (retryIntervalSecs >= 0) {
			this.retryIntervalMillis = Configuration
					.getRetryIntervalSecs(retryIntervalSecs) * 1000;
		} else {
			throw new IllegalArgumentException(
					"RetryInterval cannot be negative.");
		}
	}

	public Response post(String url, PostParameter[] postParameters,
			boolean authenticated) throws BeisenException {
		return httpRequest(url, postParameters, authenticated, Method.POST);
	}

	public Response put(String url, PostParameter[] postParameters,
			boolean authenticated) throws BeisenException {
		return httpRequest(url, postParameters, authenticated, Method.PUT);
	}

	public Response post(String url, String key, String value,
			boolean authenticated) throws BeisenException {
		return post(url, new PostParameter[] { new PostParameter(key, value) },
				authenticated);
	}

	public Response put(String url, String key, String value,
			boolean authenticated) throws BeisenException {
		return post(url, new PostParameter[] { new PostParameter(key, value) },
				authenticated);
	}

	public Response delete(String url, boolean authenticated)
			throws BeisenException {
		return httpRequest(url, null, authenticated, "DELETE");
	}

	private org.apache.commons.httpclient.HttpClient getHttpClient() {
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		if (proxyHost != null && !proxyHost.equals("")) {
			client.getHostConfiguration().setProxy(proxyHost, proxyPort);
			client.getParams().setAuthenticationPreemptive(true);
			if (proxyAuthUser != null && !proxyAuthUser.equals("")) {
				client.getState().setProxyCredentials(
						AuthScope.ANY,
						new UsernamePasswordCredentials(proxyAuthUser,
								proxyAuthPassword));
				log("Proxy AuthUser: " + proxyAuthUser);
				log("Proxy AuthPassword: " + proxyAuthPassword);
			}
		}
		return client;
	}

	private static class ByteArrayPart extends PartBase {
		private byte[] mData;
		private String mName;

		public ByteArrayPart(byte[] data, String name, String type)
				throws IOException {
			super(name, type, "UTF-8", "binary");
			mName = name;
			mData = data;
		}

		protected void sendData(OutputStream out) throws IOException {
			out.write(mData);
		}

		protected long lengthOfData() throws IOException {
			return mData.length;
		}

		protected void sendDispositionHeader(OutputStream out)
				throws IOException {
			super.sendDispositionHeader(out);
			StringBuilder buf = new StringBuilder();
			buf.append("; filename=\"").append(mName).append("\"");
			out.write(buf.toString().getBytes());
		}
	}

	public Response post(String url, boolean authenticated)
			throws BeisenException {
		return httpRequest(url, new PostParameter[0], authenticated,
				Method.POST);
	}

	public Response put(String url, boolean authenticated)
			throws BeisenException {
		return httpRequest(url, new PostParameter[0], authenticated, Method.PUT);
	}

	public Response post(String url, PostParameter[] PostParameters)
			throws BeisenException {
		return httpRequest(url, PostParameters, false, Method.POST);
	}

	public Response put(String url, PostParameter[] PostParameters)
			throws BeisenException {
		return httpRequest(url, PostParameters, false, Method.PUT);
	}

	public Response post(String url) throws BeisenException {
		return httpRequest(url, new PostParameter[0], false, Method.POST);
	}

	public Response put(String url) throws BeisenException {
		return httpRequest(url, new PostParameter[0], false, Method.PUT);
	}

	public Response get(String url, boolean authenticated)
			throws BeisenException {
		return httpRequest(url, null, authenticated, Method.GET);
	}
	
	public String getSignatureUrl(String url)
	throws BeisenException {
		
	 String authorizationStr = getAuthorizationStr(url,null,Method.GET.toString());
	 Map<String,String> map = decodeAuthorization(authorizationStr);
	 for(Map.Entry<String, String> entry:map.entrySet()){     
	    url += "&" + entry.getKey()+"="+entry.getValue();
	}  
     return url;

}


	public static Map<String,String> decodeAuthorization(String authorization) {
		Map<String,String> into = new HashMap<String, String>();
        if (authorization != null) {
            Matcher m = AUTHORIZATION.matcher(authorization);
            if (m.matches()) {
                if (AUTH_SCHEME.equalsIgnoreCase(m.group(1))) {
                    for (String nvp : m.group(2).split("\\s*,\\s*")) {
                        m = NVP.matcher(nvp);
                        if (m.matches()) {
                            String name = decodePercent(m.group(1));
                            //String value = decodePercent(m.group(2));
                            String value = m.group(2);
                            into.put(name, value);
                        }
                    }
                }
            }
        }
        return into;
    }
	
	public static String decodePercent(String s) {
        try {
            return URLDecoder.decode(s, ENCODING);
            // This implements http://oauth.pbwiki.com/FlexibleDecoding
        } catch (java.io.UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }
	
	public Response get(String url) throws BeisenException {
		return httpRequest(url, null, false, Method.GET);
	}

	protected Response httpRequest(String url, PostParameter[] postParams,
			boolean authenticated, Method method) throws BeisenException {
		// 统一增加source 参数
		int len = 1;
		PostParameter[] newPostParameters = postParams;
		if (postParams != null) {
			len = postParams.length + 1;
			newPostParameters = new PostParameter[len];
			for (int i = 0; i < postParams.length; i++) {
				newPostParameters[i] = postParams[i];
			}
			newPostParameters[postParams.length] = new PostParameter("source",
					Constants.CONSUMER_KEY);
		}
		return httpRequest(url, newPostParameters, authenticated, method
				.toString());
	}

	public Response httpRequest(String url, PostParameter[] postParams,
			boolean authenticated, String httpMethod) throws BeisenException {

		int retriedCount;
		int retry = retryCount + 1;
		Response res = null;
		for (retriedCount = 0; retriedCount < retry; retriedCount++) {
			int responseCode = -1;
			try {
				HttpURLConnection con = null;
				OutputStream osw = null;
				try {
					con = getConnection(url);
					con.setDoInput(true);
					setHeaders(url, postParams, con, authenticated, httpMethod);
					if (null != postParams
							|| ("POST".equals(httpMethod) || "PUT"
									.equals(httpMethod))) {
						if ("POST".equals(httpMethod))
							con.setRequestMethod("POST");
						else if ("PUT".equals(httpMethod))
							con.setRequestMethod("PUT");

						con.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");
						con.setDoOutput(true);
						String postParam = "";
						if (postParams != null) {
							postParam = encodeParameters(postParams);
						}
						log("Post Params: ", postParam);
						byte[] bytes = postParam.getBytes("UTF-8");

						con.setRequestProperty("Content-Length", Integer
								.toString(bytes.length));

						osw = con.getOutputStream();
						osw.write(bytes);
						osw.flush();
						osw.close();
					} else if ("DELETE".equals(httpMethod)) {
						con.setRequestMethod("DELETE");
					} else {
						con.setRequestMethod("GET");
					}

					res = new Response(con);
					responseCode = con.getResponseCode();
					if (DEBUG) {
						log("Response: ");
						Map<String, List<String>> responseHeaders = con
								.getHeaderFields();
						for (String key : responseHeaders.keySet()) {
							List<String> values = responseHeaders.get(key);
							for (String value : values) {
								if (null != key) {
									log(key + ": " + value);
								} else {
									log(value);
								}
							}
						}
					}
					if (responseCode != OK) {
						if (responseCode < INTERNAL_SERVER_ERROR
								|| retriedCount == retryCount) {
							throw new BeisenException(getCause(responseCode)
									+ "\n" + res.asString(), responseCode);
						}
						// will retry if the status code is
						// INTERNAL_SERVER_ERROR
					} else {
						break;
					}
				} finally {
					try {
						osw.close();
					} catch (Exception ignore) {
					}
				}
			} catch (IOException ioe) {
				// connection timeout or read timeout
				if (retriedCount == retryCount) {
					throw new BeisenException(ioe.getMessage(), ioe,
							responseCode);
				}
			}
			try {
				if (DEBUG && null != res) {
					res.asString();
				}
				//log("Sleeping " + retryIntervalMillis+ " millisecs for next retry.");
				Thread.sleep(retryIntervalMillis);
			} catch (InterruptedException ignore) {
				// nothing to do
			}
		}
		return res;
	}

	public static String encodeParameters(PostParameter[] postParams) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < postParams.length; j++) {
			if (j != 0) {
				buf.append("&");
			}
			try {
				buf
						.append(URLEncoder.encode(postParams[j].name, "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(postParams[j].value, "UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
			}
		}
		return buf.toString();

	}

	/**
	 * sets HTTP headers
	 * 
	 * @param connection
	 *            HttpURLConnection
	 * @param authenticated
	 *            boolean
	 */
	private void setHeaders(String url, PostParameter[] params,
			HttpURLConnection connection, boolean authenticated,
			String httpMethod) {
		log("Request: ");
		log(httpMethod + " ", url);

		if (authenticated) {
			if (oauth == null) {
			}
			String authorization = null;
			if (null != oauth) {
				// use OAuth
				authorization = oauth.generateAuthorizationHeader(httpMethod,
						url, params, oauthToken);
			} else {
				throw new IllegalStateException(
						"Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
			}
			connection.addRequestProperty("Authorization", authorization);
			log("Authorization: " + authorization);
		}
		for (String key : requestHeaders.keySet()) {
			connection.addRequestProperty(key, requestHeaders.get(key));
			log(key + ": " + requestHeaders.get(key));
		}
	}
	
	public String getAuthorizationStr(String url, PostParameter[] params,
			String httpMethod)
	{

		if (oauth == null) {
		}
		String authorization = null;
		if (null != oauth) {
			// use OAuth
			authorization = oauth.generateAuthorizationHeader(httpMethod,url, params, oauthToken);
		} else {
			throw new IllegalStateException(
					"Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
		}
		log("Authorization: " + authorization);
		
		return authorization;
	}

	public void setRequestHeader(String name, String value) {
		requestHeaders.put(name, value);
	}

	public String getRequestHeader(String name) {
		return requestHeaders.get(name);
	}

	private HttpURLConnection getConnection(String url) throws IOException {
		HttpURLConnection con = null;
		if (proxyHost != null && !proxyHost.equals("")) {
			if (proxyAuthUser != null && !proxyAuthUser.equals("")) {
				log("Proxy AuthUser: " + proxyAuthUser);
				log("Proxy AuthPassword: " + proxyAuthPassword);
				Authenticator.setDefault(new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						// respond only to proxy auth requests
						if (getRequestorType().equals(RequestorType.PROXY)) {
							return new PasswordAuthentication(proxyAuthUser,
									proxyAuthPassword.toCharArray());
						} else {
							return null;
						}
					}
				});
			}
			final Proxy proxy = new Proxy(Type.HTTP, InetSocketAddress
					.createUnresolved(proxyHost, proxyPort));
			if (DEBUG) {
				log("Opening proxied connection(" + proxyHost + ":" + proxyPort
						+ ")");
			}
			con = (HttpURLConnection) new URL(url).openConnection(proxy);
		} else {
			con = (HttpURLConnection) new URL(url).openConnection();
		}
		if (connectionTimeout > 0 && !isJDK14orEarlier) {
			con.setConnectTimeout(connectionTimeout);
		}
		if (readTimeout > 0 && !isJDK14orEarlier) {
			con.setReadTimeout(readTimeout);
		}
		return con;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessTokenURL == null) ? 0 : accessTokenURL.hashCode());
		result = prime
				* result
				+ ((authenticationURL == null) ? 0 : authenticationURL
						.hashCode());
		result = prime
				* result
				+ ((authorizationURL == null) ? 0 : authorizationURL.hashCode());
		result = prime * result + connectionTimeout;
		result = prime * result + ((oauth == null) ? 0 : oauth.hashCode());
		result = prime * result
				+ ((oauthToken == null) ? 0 : oauthToken.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime
				* result
				+ ((proxyAuthPassword == null) ? 0 : proxyAuthPassword
						.hashCode());
		result = prime * result
				+ ((proxyAuthUser == null) ? 0 : proxyAuthUser.hashCode());
		result = prime * result
				+ ((proxyHost == null) ? 0 : proxyHost.hashCode());
		result = prime * result + proxyPort;
		result = prime * result + readTimeout;
		result = prime * result
				+ ((requestHeaders == null) ? 0 : requestHeaders.hashCode());
		result = prime * result
				+ ((requestTokenURL == null) ? 0 : requestTokenURL.hashCode());
		result = prime * result + retryCount;
		result = prime * result + retryIntervalMillis;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpClient other = (HttpClient) obj;
		if (accessTokenURL == null) {
			if (other.accessTokenURL != null)
				return false;
		} else if (!accessTokenURL.equals(other.accessTokenURL))
			return false;
		if (authenticationURL == null) {
			if (other.authenticationURL != null)
				return false;
		} else if (!authenticationURL.equals(other.authenticationURL))
			return false;
		if (authorizationURL == null) {
			if (other.authorizationURL != null)
				return false;
		} else if (!authorizationURL.equals(other.authorizationURL))
			return false;
		if (connectionTimeout != other.connectionTimeout)
			return false;
		if (oauth == null) {
			if (other.oauth != null)
				return false;
		} else if (!oauth.equals(other.oauth))
			return false;
		if (oauthToken == null) {
			if (other.oauthToken != null)
				return false;
		} else if (!oauthToken.equals(other.oauthToken))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (proxyAuthPassword == null) {
			if (other.proxyAuthPassword != null)
				return false;
		} else if (!proxyAuthPassword.equals(other.proxyAuthPassword))
			return false;
		if (proxyAuthUser == null) {
			if (other.proxyAuthUser != null)
				return false;
		} else if (!proxyAuthUser.equals(other.proxyAuthUser))
			return false;
		if (proxyHost == null) {
			if (other.proxyHost != null)
				return false;
		} else if (!proxyHost.equals(other.proxyHost))
			return false;
		if (proxyPort != other.proxyPort)
			return false;
		if (readTimeout != other.readTimeout)
			return false;
		if (requestHeaders == null) {
			if (other.requestHeaders != null)
				return false;
		} else if (!requestHeaders.equals(other.requestHeaders))
			return false;
		if (requestTokenURL == null) {
			if (other.requestTokenURL != null)
				return false;
		} else if (!requestTokenURL.equals(other.requestTokenURL))
			return false;
		if (retryCount != other.retryCount)
			return false;
		if (retryIntervalMillis != other.retryIntervalMillis)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	private static void log(String message) {
		if (DEBUG) {
			System.out.println("[" + new java.util.Date() + "]" + message);
		}
	}

	private static void log(String message, String message2) {
		if (DEBUG) {
			log(message + message2);
		}
	}

	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Beisen team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Beisen is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Beisen servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}
}
