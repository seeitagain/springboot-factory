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
package org.example.lc.beisen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Configuration {
	private static Properties defaultProperty;

	static {
		init();
	}

	/* package */static void init() {
		defaultProperty = new Properties();
		defaultProperty.setProperty("beisen.debug", "false");
		defaultProperty.setProperty("beisen.source", Constants.CONSUMER_KEY);
		// defaultProperty.setProperty("beisen.clientVersion","");
		defaultProperty.setProperty("beisen.clientURL",
				"http://api.beisen.com/-{beisen.clientVersion}.xml");
		defaultProperty.setProperty("beisen.http.userAgent",
				"beisen open http://api.beisen.com/ /{beisen.clientVersion}");
		// defaultProperty.setProperty("beisen.user","");
		// defaultProperty.setProperty("beisen.password","");
		defaultProperty.setProperty("beisen.http.useSSL", "false");
		// defaultProperty.setProperty("beisen.http.proxyHost","");
		defaultProperty.setProperty("beisen.http.proxyHost.fallback",
				"http.proxyHost");
		// defaultProperty.setProperty("beisen.http.proxyUser","");
		// defaultProperty.setProperty("beisen.http.proxyPassword","");
		// defaultProperty.setProperty("beisen.http.proxyPort","");
		defaultProperty.setProperty("beisen.http.proxyPort.fallback",
				"http.proxyPort");
		//设置一个指定的超时值（以毫秒为单位），该值将在打开到此 URLConnection 引用的资源的通信链接时使用。
		//如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。 
		defaultProperty.setProperty("beisen.http.connectionTimeout", "5000");
		//将读超时设置为指定的超时值，以毫秒为单位。用一个非零值指定在建立到资源的连接后从 Input 流读入时的超时时间。
		//如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。 
		defaultProperty.setProperty("beisen.http.readTimeout", "50000");//30秒
		defaultProperty.setProperty("beisen.http.retryCount", "1");
		defaultProperty.setProperty("beisen.http.retryIntervalSecs", "1");
		defaultProperty.setProperty("beisen.oauth.consumerKey",
				Constants.CONSUMER_KEY);
		defaultProperty.setProperty("beisen.oauth.consumerSecret",
				Constants.CONSUMER_SECRET);
		defaultProperty.setProperty("beisen.async.numThreads", "1");
		defaultProperty.setProperty("beisen.clientVersion", Version
				.getVersion());
		try {
			// Android platform should have dalvik.system.VMRuntime in the
			// classpath.
			// @see
			// http://developer.android.com/reference/dalvik/system/VMRuntime.html
			Class.forName("dalvik.system.VMRuntime");
			defaultProperty.setProperty("beisen.dalvik", "true");
		} catch (ClassNotFoundException cnfe) {
			defaultProperty.setProperty("beisen.dalvik", "false");
		}
		DALVIK = getBoolean("beisen.dalvik");
		String t4jProps = "beisen.properties";
		boolean loaded = loadProperties(defaultProperty, "."
				+ File.separatorChar + t4jProps)
				|| loadProperties(defaultProperty, Configuration.class
						.getResourceAsStream("/WEB-INF/" + t4jProps))
				|| loadProperties(defaultProperty, Configuration.class
						.getResourceAsStream("/" + t4jProps));
	}

	private static boolean loadProperties(Properties props, String path) {
		try {
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				props.load(new FileInputStream(file));
				return true;
			}
		} catch (Exception ignore) {
		}
		return false;
	}

	private static boolean loadProperties(Properties props, InputStream is) {
		try {
			props.load(is);
			return true;
		} catch (Exception ignore) {
		}
		return false;
	}

	private static boolean DALVIK;

	public static boolean isDalvik() {
		return DALVIK;
	}

	public static boolean useSSL() {
		return getBoolean("beisen.http.useSSL");
	}

	public static String getScheme() {
		return useSSL() ? "https://" : "http://";
	}

	public static String getCilentVersion() {
		return getProperty("beisen.clientVersion");
	}

	public static String getCilentVersion(String clientVersion) {
		return getProperty("beisen.clientVersion", clientVersion);
	}

	public static String getSource() {
		return getProperty("beisen.source");
	}

	public static String getSource(String source) {
		return getProperty("beisen.source", source);
	}

	public static String getProxyHost() {
		return getProperty("beisen.http.proxyHost");
	}

	public static String getProxyHost(String proxyHost) {
		return getProperty("beisen.http.proxyHost", proxyHost);
	}

	public static String getProxyUser() {
		return getProperty("beisen.http.proxyUser");
	}

	public static String getProxyUser(String user) {
		return getProperty("beisen.http.proxyUser", user);
	}

	public static String getClientURL() {
		return getProperty("beisen.clientURL");
	}

	public static String getClientURL(String clientURL) {
		return getProperty("beisen.clientURL", clientURL);
	}

	public static String getProxyPassword() {
		return getProperty("beisen.http.proxyPassword");
	}

	public static String getProxyPassword(String password) {
		return getProperty("beisen.http.proxyPassword", password);
	}

	public static int getProxyPort() {
		return getIntProperty("beisen.http.proxyPort");
	}

	public static int getProxyPort(int port) {
		return getIntProperty("beisen.http.proxyPort", port);
	}

	public static int getConnectionTimeout() {
		return getIntProperty("beisen.http.connectionTimeout");
	}

	public static int getConnectionTimeout(int connectionTimeout) {
		return getIntProperty("beisen.http.connectionTimeout",
				connectionTimeout);
	}

	public static int getReadTimeout() {
		return getIntProperty("beisen.http.readTimeout");
	}

	public static int getReadTimeout(int readTimeout) {
		return getIntProperty("beisen.http.readTimeout", readTimeout);
	}

	public static int getRetryCount() {
		return getIntProperty("beisen.http.retryCount");
	}

	public static int getRetryCount(int retryCount) {
		return getIntProperty("beisen.http.retryCount", retryCount);
	}

	public static int getRetryIntervalSecs() {
		return getIntProperty("beisen.http.retryIntervalSecs");
	}

	public static int getRetryIntervalSecs(int retryIntervalSecs) {
		return getIntProperty("beisen.http.retryIntervalSecs",
				retryIntervalSecs);
	}

	public static String getUser() {
		return getProperty("beisen.user");
	}

	public static String getUser(String userId) {
		return getProperty("beisen.user", userId);
	}

	public static String getPassword() {
		return getProperty("beisen.password");
	}

	public static String getPassword(String password) {
		return getProperty("beisen.password", password);
	}

	public static String getUserAgent() {
		return getProperty("beisen.http.userAgent");
	}

	public static String getUserAgent(String userAgent) {
		return getProperty("beisen.http.userAgent", userAgent);
	}

	public static String getOAuthConsumerKey() {
		return getProperty("beisen.oauth.consumerKey");
	}

	public static String getOAuthConsumerKey(String consumerKey) {
		return getProperty("beisen.oauth.consumerKey", consumerKey);
	}

	public static String getOAuthConsumerSecret() {
		return getProperty("beisen.oauth.consumerSecret");
	}

	public static String getOAuthConsumerSecret(String consumerSecret) {
		return getProperty("beisen.oauth.consumerSecret", consumerSecret);
	}

	public static boolean getBoolean(String name) {
		String value = getProperty(name);
		return Boolean.valueOf(value);
	}

	public static int getIntProperty(String name) {
		String value = getProperty(name);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static int getIntProperty(String name, int fallbackValue) {
		String value = getProperty(name, String.valueOf(fallbackValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static long getLongProperty(String name) {
		String value = getProperty(name);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static String getProperty(String name) {
		return getProperty(name, null);
	}

	public static String getProperty(String name, String fallbackValue) {
		String value;
		try {
			value = System.getProperty(name, fallbackValue);
			if (null == value) {
				value = defaultProperty.getProperty(name);
			}
			if (null == value) {
				String fallback = defaultProperty.getProperty(name
						+ ".fallback");
				if (null != fallback) {
					value = System.getProperty(fallback);
				}
			}
		} catch (AccessControlException ace) {
			// Unsigned applet cannot access System properties
			value = fallbackValue;
		}
		return replace(value);
	}

	private static String replace(String value) {
		if (null == value) {
			return value;
		}
		String newValue = value;
		int openBrace = 0;
		if (-1 != (openBrace = value.indexOf("{", openBrace))) {
			int closeBrace = value.indexOf("}", openBrace);
			if (closeBrace > (openBrace + 1)) {
				String name = value.substring(openBrace + 1, closeBrace);
				if (name.length() > 0) {
					newValue = value.substring(0, openBrace)
							+ getProperty(name)
							+ value.substring(closeBrace + 1);

				}
			}
		}
		if (newValue.equals(value)) {
			return value;
		} else {
			return replace(newValue);
		}
	}

	public static int getNumberOfAsyncThreads() {
		return getIntProperty("beisen.async.numThreads");
	}

	public static boolean getDebug() {
		return getBoolean("beisen.debug");

	}
}
