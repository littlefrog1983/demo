package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
/**
 * 获取jenkins的jobs工具方法
 * @author tangwei
 *
 */
public class JenkinsRelatedFunction {

	public static void main(String[] args) {
//		Set<String> set = getCertainJobs("MOTE","mote_backend");
//		for(String value : set){
//			System.out.println(value);
//		}
		getJobStatus("","");
	}
	public static ConcurrentHashMap<String, String> getJobStatus(String appInfoName,String appNodeName){
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String,String>();
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("xiaobingjun","xiaobingjun"));
		String url = "http://jenkinssh.cm.hfdev";
		if(!StringUtils.isEmpty(appInfoName)){
			url = url + "/job/" + appInfoName;
		}
		if(!StringUtils.isEmpty(appNodeName)){
			url = url + "/job/" + appNodeName;
		}
		WebResource webResource = client.resource(url + "/api/xml");
		ClientResponse response = webResource.get(ClientResponse.class);
		String str = response.getEntity(String.class);
		client.destroy();
		try {
			Document document = DocumentHelper.parseText(str);
			Element root = document.getRootElement();
			List<Element> jobs = root.elements("job");
			for(Element job : jobs){
				String name = job.element("name").getText();
				String color = job.element("color") == null ? "" : job.element("color").getText();
				map.put(name, color);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(String key : map.keySet()){
			System.out.println(key + " ::: " + map.get(key));
		}
		return map;
	}
	/**
	 * 根据二级应用名和逻辑节点名查询出所有子job
	 * @param appInfoName
	 * @param appNodeName
	 * @return
	 */
	public static Set<String> getCertainJobs(String appInfoName,String appNodeName){
		String url = "http://jenkinssh.cm.hfdev";
		if(!StringUtils.isEmpty(appInfoName)){
			url = url + "/job/" + appInfoName;
		}
		if(!StringUtils.isEmpty(appNodeName)){
			url = url + "/job/" + appNodeName;
		}
		List<String> list = listJobs(url);
		Set<String> set = new HashSet<String>();
		for(String value : list){
			set.add(value);
		}
		return set;
	}
	/**
	 * 根据url列出jobs
	 * @param url
	 * @return
	 */
	public static List<String> listJobs(String url) {
		Client client = Client.create();
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter("xiaobingjun", "xiaobingjun"));
		WebResource webResource = client.resource(url + "/api/xml");
		ClientResponse response = webResource.get(ClientResponse.class);
		String jsonResponse = response.getEntity(String.class);
		client.destroy();
		List<String> jobList = new ArrayList<String>();
		String[] jobs = jsonResponse.split("job>");
		for (String job : jobs) {
			String[] names = job.split("name>");
			if (names.length == 3) {
				String name = names[1];
				name = name.substring(0, name.length() - 2);
				jobList.add(name);
			}
		}
		return jobList;
	}

	public static String deleteJob(String url, String jobName) {
		Client client = Client.create();
		// client.addFilter(new
		// com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME,
		// PASSWORD));
		WebResource webResource = client.resource(url + "/job/" + jobName + "/doDelete");
		ClientResponse response = webResource.post(ClientResponse.class);
		String jsonResponse = response.getEntity(String.class);
		client.destroy();
		// System.out.println("Response deleteJobs:::::"+jsonResponse);
		return jsonResponse;
	}

	public static String copyJob(String url, String newJobName, String oldJobName) {
		Client client = Client.create();
		// client.addFilter(new
		// com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME,
		// PASSWORD));
		WebResource webResource = client
				.resource(url + "/createItem?name=" + newJobName + "&mode=copy&from=" + oldJobName);
		ClientResponse response = webResource.type("application/xml").get(ClientResponse.class);
		String jsonResponse = response.getEntity(String.class);
		client.destroy();
		// System.out.println("Response copyJob:::::"+jsonResponse);
		return jsonResponse;
	}

	public static String createJob(String url, String newJobName, String configXML) {
		Client client = Client.create();
		// client.addFilter(new
		// com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME,
		// PASSWORD));
		WebResource webResource = client.resource(url + "/createItem?name=" + newJobName);
		ClientResponse response = webResource.type("application/xml").post(ClientResponse.class, configXML);
		String jsonResponse = response.getEntity(String.class);
		client.destroy();
		System.out.println("Response createJob:::::" + jsonResponse);
		return jsonResponse;
	}
}
