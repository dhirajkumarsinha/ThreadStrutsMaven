package com.LRPThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LRPProcessing implements Runnable {

	String path;
	Thread t;

	LRPProcessing(String threadName) {
		path = threadName;
		t = new Thread(this, path);
		System.out.println("New Thread : " + t);

		t.start();
	}

	public void run() {
		try {
			// process 1000 at a time
			int rowstart = 13001;
			int rowend = 14000;
			for (int i = 0; i < 20; i++) {
				System.out.println("rowstart :" + rowstart + " #rowstart :" + rowend);
				test();// to check if the service is responding
				getUserIdFromExcel(path, rowstart, rowend);
				rowstart = rowend + 1;
				rowend = rowend + 1000;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void test() throws Exception {
		String[] elementsToWrite = validateUserContext("dhisinha", getAuthtoken("stage"));
		System.out.println(elementsToWrite[0]);

	}

	public static String[] validateUserContext(String userId, String token) {
		HttpClient client = null;
		HttpResponse response = null;
		String scheme = "https";
		String host = "wsgx-dev.cisco.com";
		String path = "/as/svo/restservices/viewOrder_sw2/88914052";
		String content = null;
		String[] elements = { "NA", "NA" };

		try {
			client = HttpClientBuilder.create().build();
			// Build API URI
			URIBuilder uriBuilder = new URIBuilder();
			uriBuilder.setScheme(scheme).setHost(host).setPath(path);
			uriBuilder.setParameter("Requestedfor", "svorma8");
			uriBuilder.setParameter("Requestedversion", "2");			
			URI uri = uriBuilder.build();
			// Http Get Request
			HttpGet request = new HttpGet(uri);
			request.addHeader("Authorization", "Basic " + token);
			request.addHeader("Content-type", "application/json");
			//request.addHeader("accept",);
			//request.addHeader("","");
			// Response and parsing
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			// System.out.println(content);
			elements = parseContent(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(client);
		}
		return elements;
	}

	private static String[] parseContent(String content) {
		JSONObject jsonObject;
		String accessLevel = "NA";
		JSONArray internalRoleList;
		JSONArray companyList;
		String smartRole = "NA";
		String[] elements = { "NA", "NA" };
		try {
			jsonObject = new JSONObject(content);
			accessLevel = jsonObject.getJSONObject("user").get("accessLevel").toString();
			elements[0] = accessLevel;
			internalRoleList = jsonObject.getJSONArray("internalRoleList");
			if (internalRoleList.length() > 0) {
				smartRole = "Smart Admin";
			} else {
				companyList = jsonObject.getJSONArray("companyList");
				if (companyList.length() > 0) {
					smartRole = "Smart User";
				}
			}
			elements[1] = smartRole;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return elements;
	}

	private static String getAuthtoken(String env) throws Exception {
		HttpClient client = null;
		String authToken = null;
		String clientId = null;
		String clientSecret = null;
		String cloudSSOUrl = null;
		String grantTypeKey = "grant_type";
		String grantTypeValue = "client_credentials";
		String clientIdKey = "client_id";
		String clientSecretKey = "client_secret";
		if (env.equalsIgnoreCase("prod")) {
			cloudSSOUrl = "https://cloudsso.cisco.com/as/token.oauth2";
			clientId = "xyz";
			clientSecret = "xyz";
		} else if (env.equalsIgnoreCase("stage")) {
			cloudSSOUrl = "https://cloudsso-test.cisco.com/as/token.oauth2";
			clientId = "xyz";
			clientSecret = "xyz";
		}

		HttpPost post;
		URIBuilder builder;
		HttpResponse response = null;
		try {
			client = HttpClientBuilder.create().build();

			builder = new URIBuilder(cloudSSOUrl).addParameter(grantTypeKey, grantTypeValue)
					.addParameter(clientIdKey, clientId).addParameter(clientSecretKey, clientSecret);
			URI uri = builder.build();
			post = new HttpPost(uri);

			StringEntity input = new StringEntity("");
			post.setEntity(input);
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String tokenResponse = EntityUtils.toString(entity);
			JSONObject jsonObject = new JSONObject(tokenResponse);
			authToken = jsonObject.get("access_token").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(client);
		}
		return authToken;
	}

	private static void getUserIdFromExcel(String excelFilePath, int start, int end) throws Exception {
		Row row;
		Cell cell;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		String[] elementsToWrite = { "NA", "NA" };
		String token = getAuthtoken("prod");

		for (int r = start; r <= end; r++) {
			row = firstSheet.getRow(r);
			if (row != null) {
				cell = row.getCell(0);
				elementsToWrite = validateUserContext(cell.toString(), token);
				row.createCell(1).setCellValue(elementsToWrite[0]);
				row.createCell(2).setCellValue(elementsToWrite[1]);
			}
		}
		FileOutputStream os = new FileOutputStream(excelFilePath);
		workbook.write(os);
		// Close workbook, OutputStream and Excel file to prevent leak
		os.close();
		workbook.close();
		inputStream.close();
	}
	
	private static void getUserIdFromExcel(String excelFilePath) throws Exception {
		Row row;
		Cell cell;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		String[] elementsToWrite = { "NA", "NA" };
		String token = getAuthtoken("prod");

		
			row = firstSheet.getRow(0);
			if (row != null) {
				cell = row.getCell(0);
				elementsToWrite = validateUserContext(cell.toString(), token);
				row.createCell(1).setCellValue(elementsToWrite[0]);
				row.createCell(2).setCellValue(elementsToWrite[1]);
			}
		
		FileOutputStream os = new FileOutputStream(excelFilePath);
		workbook.write(os);
		// Close workbook, OutputStream and Excel file to prevent leak
		os.close();
		workbook.close();
		inputStream.close();
	}
	
	public static void main(String[] args) {
		//validateUserContext("dhisinha", "c3Zvcm1hODphcG9sbG8=");
		getUserIdFromExcel(path);
	}

}
