package org.example.lc.beisen.assessment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.example.lc.beisen.BeisenException;
import org.example.lc.beisen.BeisenSupport;
import org.example.lc.beisen.Configuration;
import org.example.lc.beisen.Constants;
import org.example.lc.beisen.assessment.model.AccountToken;
import org.example.lc.beisen.assessment.model.ActivityInfo;
import org.example.lc.beisen.assessment.model.ActivityInfoList;
import org.example.lc.beisen.assessment.model.ActivityTesteeResult;
import org.example.lc.beisen.assessment.model.ActivityTesteeResultList;
import org.example.lc.beisen.assessment.model.ActivityTesteeState;
import org.example.lc.beisen.assessment.model.ActivityTesteeStateList;
import org.example.lc.beisen.assessment.model.EmailSNParameter;
import org.example.lc.beisen.assessment.model.OpenAPIRemindResult;
import org.example.lc.beisen.assessment.model.Report;
import org.example.lc.beisen.assessment.model.TemplateList;
import org.example.lc.beisen.assessment.model.TestCompletedMessage;
import org.example.lc.beisen.assessment.model.TestCompletedMessageList;
import org.example.lc.beisen.assessment.model.TestInfoParameter;
import org.example.lc.beisen.assessment.model.TesteeInfoParameter;
import org.example.lc.beisen.assessment.model.TesteeInfoParameterList;
import org.example.lc.beisen.assessment.model.TesteeMessageEx;
import org.example.lc.beisen.assessment.model.TesteeResult;
import org.example.lc.beisen.http.AccessToken;
import org.example.lc.beisen.http.HttpClient;
import org.example.lc.beisen.http.PostParameter;
import org.example.lc.beisen.http.RequestToken;
import org.example.lc.beisen.http.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Assessment extends BeisenSupport implements java.io.Serializable {
	private String baseURL = Configuration.getScheme() + "api.beisenapp.com/assess";
	private String data_format = "json";
	private static final long serialVersionUID = -1486360080128882436L;

	/**
	 * 
	 * @param consumerKey    OAuth consumer key
	 * @param consumerSecret OAuth consumer secret
	 * @since Tita 1.0.0
	 */
	public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
		this.http.setOAuthConsumer(consumerKey, consumerSecret);
	}

	/**
	 * Retrieves a request token
	 * 
	 * @return generated request token.
	 * @throws BeisenException when Beisen service or network is unavailable
	 * @since Tita 1.0.0
	 * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Core 1.0 - 6.1.
	 *      Obtaining an Unauthorized Request Token</a>
	 */
	public RequestToken getOAuthRequestToken() throws BeisenException {
		return http.getOAuthRequestToken();
	}

	public RequestToken getOAuthRequestToken(String callback_url) throws BeisenException {
		return http.getOauthRequestToken(callback_url);
	}

	/**
	 * Retrieves an access token assosiated with the supplied request token.
	 * 
	 * @param requestToken the request token
	 * @return access token associsted with the supplied request token.
	 * @throws BeisenException when Beisen service or network is unavailable, or the
	 *                         user has not authorized
	 * @see <a href="http://api.beisen.com/wiki/Oauth/access_token">Oauth/access
	 *      token </a>
	 * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.
	 *      Obtaining User Authorization</a>
	 * @since Tita 1.0.0
	 */
	public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws BeisenException {
		return http.getOAuthAccessToken(requestToken);
	}

	/**
	 * Retrieves an access token assosiated with the supplied request token and sets
	 * userId.
	 * 
	 * @param requestToken the request token
	 * @param pin          pin
	 * @return access token associsted with the supplied request token.
	 * @throws BeisenException when Beisen service or network is unavailable, or the
	 *                         user has not authorized
	 * @see <a href="http://api.beisen.com/wiki/Oauth/access_token">Oauth/access
	 *      token </a>
	 * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.
	 *      Obtaining User Authorization</a>
	 * @since Tita 1.2.0
	 */
	public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String pin) throws BeisenException {
		AccessToken accessToken = http.getOAuthAccessToken(requestToken, pin);
		// setUserId(accessToken.getScreenName());
		return accessToken;
	}

	/**
	 * Retrieves an access token assosiated with the supplied request token and sets
	 * userId.
	 * 
	 * @param token       request token
	 * @param tokenSecret request token secret
	 * @return access token associsted with the supplied request token.
	 * @throws BeisenException when Beisen service or network is unavailable, or the
	 *                         user has not authorized
	 * @see <a href="http://api.beisen.com/wiki/Oauth/access_token">Oauth/access
	 *      token </a>
	 * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.
	 *      Obtaining User Authorization</a>
	 * @since Tita 1.2.0
	 */
	public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws BeisenException {
		AccessToken accessToken = http.getOAuthAccessToken(token, tokenSecret);
		// setUserId(accessToken.getScreenName());
		return accessToken;
	}

	/**
	 * Retrieves an access token assosiated with the supplied request token.
	 * 
	 * @param token          request token
	 * @param tokenSecret    request token secret
	 * @param oauth_verifier oauth_verifier or pin
	 * @return access token associsted with the supplied request token.
	 * @throws BeisenException when Beisen service or network is unavailable, or the
	 *                         user has not authorized
	 * @see <a href="http://api.beisen.com/wiki/Oauth/access_token">Oauth/access
	 *      token </a>
	 * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.
	 *      Obtaining User Authorization</a>
	 * @since Tita 1.2.0
	 */
	public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret, String oauth_verifier)
			throws BeisenException {
		return http.getOAuthAccessToken(token, tokenSecret, oauth_verifier);
	}

	/**
	 * Sets the access token
	 * 
	 * @param accessToken accessToken
	 * @since Tita 1.2.0
	 */
	public void setOAuthAccessToken(AccessToken accessToken) {
		this.http.setOAuthAccessToken(accessToken);
	}

	/**
	 * Sets the access token
	 * 
	 * @param token       token
	 * @param tokenSecret token secret
	 * @since Tita 1.2.0
	 */
	public void setOAuthAccessToken(String token, String tokenSecret) {
		setOAuthAccessToken(new AccessToken(token, tokenSecret));
	}

	private SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Assessment assess = (Assessment) o;

		if (!baseURL.equals(assess.baseURL))
			return false;
		if (!format.equals(assess.format))
			return false;
		if (!http.equals(assess.http))
			return false;
		if (!source.equals(assess.source))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = http.hashCode();
		result = 31 * result + baseURL.hashCode();
		result = 31 * result + source.hashCode();
		result = 31 * result + format.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Tita{" + "http=" + http + ", baseURL='" + baseURL + '\'' + ", source='" + source + '\'' + ", format="
				+ format + '}';
	}

	// --------------base method----------
	public Assessment() {
		super();
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		http.setRequestTokenURL(Configuration.getScheme() + "api.beisen.com/oauth/request_token");
		http.setAuthorizationURL(Configuration.getScheme() + "api.beisen.com/oauth/authorize");
		http.setAccessTokenURL(Configuration.getScheme() + "api.beisen.com/oauth/access_token");
	}

	/**
	 * Sets token information
	 * 
	 * @param token
	 * @param tokenSecret
	 */
	public void setToken(String token, String tokenSecret) {
		http.setToken(token, tokenSecret);
	}

	public Assessment(String baseURL) {
		this();
		this.baseURL = baseURL;
	}

	/**
	 * Sets the base URL
	 * 
	 * @param baseURL String the base URL
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * Returns the base URL
	 * 
	 * @return the base URL
	 */
	public String getBaseURL() {
		return this.baseURL;
	}

	/**
	 * Issues an HTTP GET request.
	 * 
	 * @param url          the request url
	 * @param authenticate if true, the request will be sent with BASIC
	 *                     authentication header
	 * @return the response
	 * @throws BeisenException when Beisen service or network is unavailable
	 */

	public Response get(String url, boolean authenticate) throws BeisenException {
		return get(url, null, authenticate);
	}

	/**
	 * Issues an HTTP GET request.
	 * 
	 * @param url          the request url
	 * @param authenticate if true, the request will be sent with BASIC
	 *                     authentication header
	 * @param name1        the name of the first parameter
	 * @param value1       the value of the first parameter
	 * @return the response
	 * @throws BeisenException when Beisen service or network is unavailable
	 */

	protected Response get(String url, String name1, String value1, boolean authenticate) throws BeisenException {
		return get(url, new PostParameter[] { new PostParameter(name1, value1) }, authenticate);
	}

	/**
	 * Issues an HTTP GET request.
	 * 
	 * @param url          the request url
	 * @param name1        the name of the first parameter
	 * @param value1       the value of the first parameter
	 * @param name2        the name of the second parameter
	 * @param value2       the value of the second parameter
	 * @param authenticate if true, the request will be sent with BASIC
	 *                     authentication header
	 * @return the response
	 * @throws BeisenException when Beisen service or network is unavailable
	 */

	protected Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate)
			throws BeisenException {
		return get(url, new PostParameter[] { new PostParameter(name1, value1), new PostParameter(name2, value2) },
				authenticate);
	}

	/**
	 * Issues an HTTP GET request.
	 * 
	 * @param url          the request url
	 * @param params       the request parameters
	 * @param authenticate if true, the request will be sent with BASIC
	 *                     authentication header
	 * @return the response
	 * @throws BeisenException when Beisen service or network is unavailable
	 */
	protected Response get(String url, PostParameter[] params, boolean authenticate) throws BeisenException {
		if (url.indexOf("?") == -1) {
			url += "?source=" + Constants.CONSUMER_KEY;
		} else if (url.indexOf("source") == -1) {
			url += "&source=" + Constants.CONSUMER_KEY;
		}
		if (null != params && params.length > 0) {
			url += "&" + HttpClient.encodeParameters(params);
		}
		return http.get(url, authenticate);
	}

	private PostParameter[] generateParameterArray(Map<String, String> parames) throws BeisenException {
		PostParameter[] array = new PostParameter[parames.size()];
		int i = 0;
		for (String key : parames.keySet()) {
			if (parames.get(key) != null) {
				array[i] = new PostParameter(key, parames.get(key));
				i++;
			}
		}
		return array;
	}

	public final static Device IM = new Device("im");
	public final static Device SMS = new Device("sms");
	public final static Device NONE = new Device("none");

	static class Device {
		final String DEVICE;

		public Device(String device) {
			DEVICE = device;
		}
	}

	// -----------------assess start--------------------

	/// <summary>
	/// 通过email获取活动下受测者作答结果
	/// <summary>
	public ActivityTesteeResult getActivityTesteeResult(AccountToken token, Integer activity_id, String emails)
			throws BeisenException {
		Gson gson = new Gson();
		String url = getBaseURL() + "/" + token.tenant_id + "/activity_testee_result?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&emails=" + emails + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		return gson.fromJson(response.asString(), ActivityTesteeResult.class);
	}

	/// <summary>
	/// 获取活动下受测者作答状态
	/// <summary>
	public ActivityTesteeState getActivityTesteeState(AccountToken token, Integer activity_id, String emails)
			throws BeisenException {
		Gson gson = new Gson();
		String url = getBaseURL() + "/" + token.tenant_id + "/activity_testee_state?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&emails=" + emails + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		return gson.fromJson(response.asString(), ActivityTesteeState.class);
	}

	/// <summary>
	/// 通过email获取租户下受测者作答结果
	/// <summary>
	public ActivityTesteeResultList getActivityTesteeResultList(AccountToken token, String emails)
			throws BeisenException {
		Gson gson = new Gson();
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/result/email?beisen_account="
				+ token.beisen_account + "&emails=" + emails + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		return gson.fromJson(response.asString(), ActivityTesteeResultList.class);
	}

	/// <summary>
	/// 获取受测者作答状态
	/// <summary>
	public ActivityTesteeStateList getActivityTesteeStateList(AccountToken token, String emails)
			throws BeisenException {
		Gson gson = new Gson();
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/state?beisen_account="
				+ token.beisen_account + "&emails=" + emails + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		return gson.fromJson(response.asString(), ActivityTesteeStateList.class);
	}

	/// <summary>
	/// 通过email获取租户下受测者作答结果(已过期)
	/// <summary>
	public ActivityTesteeResultList getActivitiesTesteeResult(AccountToken token, String emails)
			throws BeisenException {
		Gson gson = new Gson();
		String url = getBaseURL() + "/" + token.tenant_id + "/activities_testee_result?beisen_account="
				+ token.beisen_account + "&emails=" + emails + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		return gson.fromJson(response.asString(), ActivityTesteeResultList.class);
	}

	/// <summary>
	/// 获取受测者作答状态(已过期)
	/// <summary>
	public ActivityTesteeStateList getActivitiesTesteeState(AccountToken token, String emails) throws BeisenException {
		Gson gson = new Gson();
		String url = getBaseURL() + "/" + token.tenant_id + "/activities_testee_state?beisen_account="
				+ token.beisen_account + "&emails=" + emails + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		return gson.fromJson(response.asString(), ActivityTesteeStateList.class);
	}

	/// <summary>
	/// 创建活动同时要求受测者
	/// <summary>
	public TesteeResult addactivityandInviteTestee(AccountToken token, List<TestInfoParameter> test_info_parameter_list,
			List<TesteeInfoParameter> testee_info_parameter, String start_time, String end_time)
			throws BeisenException {
		Gson gson = new Gson();
		String test_info_parameter_listJson = gson.toJson(test_info_parameter_list,
				new TypeToken<List<TestInfoParameter>>() {
				}.getType());
		String testee_info_parameterJson = gson.toJson(testee_info_parameter,
				new TypeToken<List<TesteeInfoParameter>>() {
				}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee_invite?beisen_account="
				+ token.beisen_account + "&format=" + data_format;
		PostParameter[] postParams = new PostParameter[] {
				new PostParameter("test_info_parameter_list", test_info_parameter_listJson),
				new PostParameter("testee_info_parameter", testee_info_parameterJson),
				new PostParameter("start_time", start_time.toString()),
				new PostParameter("end_time", end_time.toString()) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), TesteeResult.class);
	}

	/// <summary>
	/// 获取受测者作答完成消息
	/// <summary>
	public TestCompletedMessageList getTestCompletedMessage(AccountToken token) throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/message/answer_completed?beisen_account="
				+ token.beisen_account + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), TestCompletedMessageList.class);
	}

	/// <summary>
	/// 轮询方式，获取受测者作答完成消息
	/// <summary>
	public List<TesteeMessageEx> getTestCompletedMessageEx(AccountToken token) throws BeisenException {
		List<TesteeMessageEx> resultList = new ArrayList<TesteeMessageEx>();
		TestCompletedMessageList messages = new TestCompletedMessageList();
		messages.message_list = new ArrayList<TestCompletedMessage>();
		for (int i = 0; i < 20; i++) {
			TestCompletedMessageList messageList = getTestCompletedMessage(token);
			if (messageList == null || !messageList.has_next)
				break;

			messages.message_list.addAll(messageList.message_list);

			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}
		}

		for (TestCompletedMessage item : messages.message_list) {
			TesteeMessageEx first = null;
			for (TesteeMessageEx message : resultList) {
				if (item.activity_id == message.activity_id && item.tenant_id == message.tenant_id) {
					first = message;
					break;
				}
			}
			if (first == null) {
				first = new TesteeMessageEx();
				first.message_sn_list = new ArrayList<EmailSNParameter>();
				first.activity_id = item.activity_id;
				first.tenant_id = item.tenant_id;
				resultList.add(first);
			}
			EmailSNParameter emailsn = new EmailSNParameter();
			emailsn.email = item.email;
			emailsn.sn = item.sn;
			first.message_sn_list.add(emailsn);
		}

		return resultList;
	}

	/// <param name="tokenDict">
	/// <param name="singleCount">每次轮询的获取的数量，一般10即可，最大20
	/// <returns>key:租户id，value：作答结果</returns>
	public ActivityTesteeResultList getTesteeResultByCompletedMessageEx(AccountToken token, int singleCount)
			throws BeisenException {
		if (singleCount > 20)
			throw new BeisenException("singleCount最大允许20");

		ActivityTesteeResultList result = new ActivityTesteeResultList();

		List<TesteeMessageEx> messageList = null;
		messageList = getTestCompletedMessageEx(token);
		if (messageList == null)
			return result;

		for (TesteeMessageEx tenantActivity : messageList) {

			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				List<EmailSNParameter> emailSnParameter = new ArrayList<EmailSNParameter>();
				for (int index = i * singleCount; index < (i + 1) * singleCount; index++) {
					if (index < tenantActivity.message_sn_list.size()) {
						emailSnParameter.add(tenantActivity.message_sn_list.get(index));
					}
				}

				if (emailSnParameter.size() == 0)
					break;

				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
				ActivityTesteeResultList rs = getTesteeResultByEmailAndSN(token, tenantActivity.activity_id,
						emailSnParameter);
				ActivityTesteeResult activityResult = null;
				for (ActivityTesteeResult item : result) {
					if (item.activity_id == tenantActivity.activity_id) {
						activityResult = item;
						break;
					}
				}

				if (activityResult != null) {
					for (ActivityTesteeResult item : rs) {
						activityResult.testee_list.addAll(item.testee_list);
					}
				} else {
					if (rs.size() > 0) {
						ActivityTesteeResult aResult = rs.get(0);
						result.add(aResult);
					}
				}
			}
		}

		return result;
	}

	/// <summary>
	/// 获取租户下所有测评活动列表，返回结果按照创建时间倒序排列，page_size最大100
	/// <summary>
	public ActivityInfoList getActivityList(AccountToken token, Integer page_size, Integer page_index)
			throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/activity?beisen_account=" + token.beisen_account
				+ "&page_size=" + page_size + "&page_index=" + page_index + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), ActivityInfoList.class);
	}

	/// <summary>
	/// 获取活动的详细信息
	/// <summary>
	public ActivityInfo getActivityDetail(AccountToken token, Integer activity_id) throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/detail?beisen_account=" + token.beisen_account
				+ "&activity_id=" + activity_id + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), ActivityInfo.class);
	}

	/// <summary>
	/// 生成通行证号(仅限于个人通行证)
	/// <summary>
	public List<String> createSerialNumber(AccountToken token, Integer activity_id, Integer count, String start_time,
			String end_time) throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/serial_number?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&count=" + count + "&start_time=" + start_time
				+ "&end_time=" + end_time + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.post(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), new TypeToken<List<String>>() {
		}.getType());

	}

	/// <summary>
	/// 获取所有邮件模板
	/// <summary>
	public TemplateList getEmailTemplateList(AccountToken token) throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/template/email?beisen_account=" + token.beisen_account
				+ "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), TemplateList.class);
	}

	/// <summary>
	/// 获取所有短信模板
	/// <summary>
	public TemplateList getSMSTemplateList(AccountToken token) throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/template/sms?beisen_account=" + token.beisen_account
				+ "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), TemplateList.class);
	}

	/// <summary>
	/// 获取租户可用的邮件催促模板
	/// <summary>
	public TemplateList getEmailRemindTemplate(AccountToken token) throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/template/remind_email?beisen_account="
				+ token.beisen_account + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), TemplateList.class);
	}

	/// <summary>
	/// 获取租户可用的短信催促模板
	/// <summary>
	public TemplateList getSMSRemindTemplate(AccountToken token) throws BeisenException {
		String url = getBaseURL() + "/" + token.tenant_id + "/template/remind_sms/?beisen_account="
				+ token.beisen_account + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		Gson gson = new Gson();
		return gson.fromJson(response.asString(), TemplateList.class);
	}

	/// <summary>
	/// 邀请受测者(不发送邮件和短信)
	/// <summary>
	public TesteeResult inviteTestee(AccountToken token, Integer activity_id, String start_time, String end_time,
			TesteeInfoParameterList testee_list) throws BeisenException {
		Gson gson = new Gson();
		String testee_listJson = gson.toJson(testee_list, new TypeToken<TesteeInfoParameterList>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/invite?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&start_time=" + start_time + "&end_time="
				+ end_time + "&format=" + data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("testee_list", testee_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), TesteeResult.class);
	}

	/// <summary>
	/// 发送邮件和短信邀请受测者
	/// <summary>
	public TesteeResult sendEmailAndsMsInviteTestee(AccountToken token, Integer activity_id, String start_time,
			String end_time, String email_template, String sms_template, TesteeInfoParameterList testee_list)
			throws BeisenException {
		Gson gson = new Gson();
		String testee_listJson = gson.toJson(testee_list, new TypeToken<TesteeInfoParameterList>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/email_sms_invite?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&start_time=" + start_time + "&end_time="
				+ end_time + "&email_template=" + email_template + "&sms_template=" + sms_template + "&format="
				+ data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("testee_list", testee_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), TesteeResult.class);
	}

	/// <summary>
	/// 发送邮件邀请受测者
	/// <summary>
	public TesteeResult sendEmailInviteTestee(AccountToken token, Integer activity_id, String start_time,
			String end_time, String email_template, TesteeInfoParameterList testee_list) throws BeisenException {
		Gson gson = new Gson();
		String testee_listJson = gson.toJson(testee_list, new TypeToken<TesteeInfoParameterList>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/email_invite?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&start_time=" + start_time + "&end_time="
				+ end_time + "&email_template=" + email_template + "&format=" + data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("testee_list", testee_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), TesteeResult.class);
	}

	/// <summary>
	/// 发送短信邀请受测者
	/// <summary>
	public TesteeResult sendsMsInviteTestee(AccountToken token, Integer activity_id, String start_time, String end_time,
			String sms_template, TesteeInfoParameterList testee_list) throws BeisenException {
		Gson gson = new Gson();
		String testee_listJson = gson.toJson(testee_list, new TypeToken<TesteeInfoParameterList>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/sms_invite?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&start_time=" + start_time + "&end_time="
				+ end_time + "&sms_template=" + sms_template + "&format=" + data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("testee_list", testee_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), TesteeResult.class);
	}

	/// <summary>
	/// 发送邮件和短信催促受测者
	/// <summary>
	public OpenAPIRemindResult sendEmailAndsMsRemindTestee(AccountToken token, String email_template,
			String sms_template, List<EmailSNParameter> email_sn_list, Integer activity_id) throws BeisenException {
		Gson gson = new Gson();
		String email_sn_listJson = gson.toJson(email_sn_list, new TypeToken<List<EmailSNParameter>>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/email_sms_remind?beisen_account="
				+ token.beisen_account + "&email_template=" + email_template + "&sms_template=" + sms_template
				+ "&activity_id=" + activity_id + "&format=" + data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("email_sn_list", email_sn_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), OpenAPIRemindResult.class);
	}

	/// <summary>
	/// 发送邮件催促受测者
	/// <summary>
	public OpenAPIRemindResult sendEmailRemindTestee(AccountToken token, String email_template,
			List<EmailSNParameter> email_sn_list, Integer activity_id) throws BeisenException {
		Gson gson = new Gson();
		String email_sn_listJson = gson.toJson(email_sn_list, new TypeToken<List<EmailSNParameter>>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/email_remind?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&email_template=" + email_template
				+ "&format=" + data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("email_sn_list", email_sn_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), OpenAPIRemindResult.class);
	}

	/// <summary>
	/// 发送短信催促受测者
	/// <summary>
	public OpenAPIRemindResult sendsMsRemindTestee(AccountToken token, String sms_template,
			List<EmailSNParameter> email_sn_list, Integer activity_id) throws BeisenException {
		Gson gson = new Gson();
		String email_sn_listJson = gson.toJson(email_sn_list, new TypeToken<List<EmailSNParameter>>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/testee/remind/sms_remind?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&sms_template=" + sms_template + "&format="
				+ data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("email_sn_list", email_sn_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), OpenAPIRemindResult.class);
	}

	/// <summary>
	/// 通过通行证号和邮箱获取租户下受测者作答结果
	/// <summary>
	public ActivityTesteeResultList getTesteeResultByEmailAndSN(AccountToken token, Integer activity_id,
			List<EmailSNParameter> email_sn_list) throws BeisenException {
		Gson gson = new Gson();
		String email_sn_listJson = gson.toJson(email_sn_list, new TypeToken<List<EmailSNParameter>>() {
		}.getType());
		String url = getBaseURL() + "/" + token.tenant_id + "/activity/testee/result/sn_email?beisen_account="
				+ token.beisen_account + "&activity_id=" + activity_id + "&format=" + data_format;
		PostParameter[] postParams = new PostParameter[] { new PostParameter("email_sn_list", email_sn_listJson) };
		setToken(token.token, token.token_secret);
		Response response = http.post(url, postParams, true);
		return gson.fromJson(response.asString(), ActivityTesteeResultList.class);
	}

	/// <summary>
	/// 通过报告编号获取报告
	/// <summary>
	public String getReportDownloadUrl(AccountToken token, String report_no) throws BeisenException {

		String url = getBaseURL() + "/" + token.tenant_id
				+ "/activity/testee/result/report_download_url?beisen_account=" + token.beisen_account + "&report_no="
				+ report_no + "&format=" + data_format;
		setToken(token.token, token.token_secret);
		Response response = http.get(url, true);
		return response.asString();

	}

	// -----------------assess end--------------------
}