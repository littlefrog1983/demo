package test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
/**
 * jenkins操作方法
 * @author tangwei
 *
 */
public class JenkinsAPI {
	// Credentials
	static String username = "tw";
	static String password = "0";
	// Jenkins url
	static String jenkinsUrl = "http://127.0.0.1:8080/jenkins";
	// Build token
	static String buildToken = "9b069d933ecdd93506f30b30ebe88dab";

	/**
	 * 获取构建信息
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getBuilds(String jobName) throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "/job/" + jobName + "/api/json?tree=builds[number]{0,10}";

		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpGet httpGet = new HttpGet(uri);
		// httpPost.setEntity(reqEntity);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpGet, localContext);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		return result;
	}
	/**
	 * 获取jobs
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getJobs() throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "/api/json?tree=jobs[name]{1,3},views[name,jobs[name]{2}]";

		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpGet httpGet = new HttpGet(uri);
		// httpPost.setEntity(reqEntity);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpGet, localContext);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		return result;
	}


	/**
	 * 更新job配置信息
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String updateJobConfig(String jobName,String ip) throws ClientProtocolException, IOException {
		String newConfigPath = XMLUtil.parseXML(JenkinsAPI.getJobConfig(jobName), ip);
		String urlString = jenkinsUrl + "/job/" + jobName + "/config.xml";
		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpPost httpPost = new HttpPost(uri);
		FileEntity entity = new FileEntity(new File(newConfigPath));
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "multipart/form-data;charset=UTF-8");
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);
		HttpResponse response = httpClient.execute(host, httpPost, localContext);
		String result = EntityUtils.toString(response.getEntity());
		boolean fileDel = new File(newConfigPath).delete();
		if(fileDel){
			int index = newConfigPath.lastIndexOf("/");
			String dirPath = newConfigPath.substring(0, index + 1);
			new File(dirPath).delete();
		}
		return result;
	}

	/**
	 * 创建job
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String createJob() throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "/createItem?name=TestIOSJob2";
		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpPost httpPost = new HttpPost(uri);
		FileEntity entity = new FileEntity(new File("E:\\config.xml"));
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
		// httpPost.setEntity(reqEntity);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpPost, localContext);
		String result = EntityUtils.toString(response.getEntity());
		return result;
	}

	/**
	 * 获取job配置信息
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getJobConfig(String jobName) throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "/job/" + jobName + "/config.xml";

		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpGet httpGet = new HttpGet(uri);
		// httpPost.setEntity(reqEntity);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpGet, localContext);
		String result = EntityUtils.toString(response.getEntity());
		return result;
	}
	/**
	 * 构建
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String build(String jobName) throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "/job/" + jobName + "/build?token=" + buildToken;
		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpPost httpPost = new HttpPost(uri);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);
		HttpResponse response = httpClient.execute(host, httpPost, localContext);
		return EntityUtils.toString(response.getEntity());
	}
	/**
	 * 获取构建日志
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getBuildLog(String jobName,String buildId) throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "/job/" + jobName + "/" + buildId + "/logText/progressiveText?start=0";
		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpGet httpGet = new HttpGet(uri);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);
		HttpResponse response = httpClient.execute(host, httpGet, localContext);
		return EntityUtils.toString(response.getEntity());
	}
	
	public static void main(String[] args) {
		try {
			//String result = updateJobConfig("modifyIp","tangwei ");
			//System.out.println(result);
//			String result2 = build("demo");
//			System.out.println(result2);
			System.out.println(getBuildLog("demo","23"));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
