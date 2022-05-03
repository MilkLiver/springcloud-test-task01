package com.milkliver.springcloud.test.springcloudtest03.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendOmsAlert {

	private static final Logger log = LoggerFactory.getLogger(SendOmsAlert.class);

	/*
	 * http("http://127.0.0.1:8084/id/A", "POST", 1000,5000);
	 * https("https://127.0.0.1:8443/id/A", "POST", 1000,5000);
	 */

	public Map http(String connectUrl, String method, int connectTimeOut, int readTimeOut) {
		log.info(this.getClass().toString() + " http Url: " + connectUrl + " connectTimeOut: " + connectTimeOut
				+ " readTimeOut: " + readTimeOut + " ...");

		URL url;
		HttpURLConnection con;
		Map returnInfos = new HashMap();
		int responseCode = 0;

		try {
			url = new URL(connectUrl);

			con = (HttpURLConnection) url.openConnection();
			// 設定方法為GET
			con.setRequestMethod(method);
			con.setConnectTimeout(connectTimeOut);
			con.setReadTimeout(readTimeOut);
			con.setUseCaches(false);
			con.setDoOutput(true);
//			con.getResponseCode();
//			InputStream is = con.getInputStream();
			responseCode = con.getResponseCode();
			returnInfos.put("statusCode", responseCode);
			if (String.valueOf(responseCode).substring(0, 1).equals("4")
					|| String.valueOf(responseCode).substring(0, 1).equals("5")) {
				returnInfos.put("status", false);
			}
			returnInfos.put("status", true);
			log.info(this.getClass().toString() + " http Url: " + connectUrl + " connectTimeOut: " + connectTimeOut
					+ " readTimeOut: " + readTimeOut + " finish");

			return returnInfos;

		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("status", false);
			return returnInfos;
		} catch (IOException e) {
			log.error("responseCode: " + String.valueOf(responseCode));
			log.error("connect http " + connectUrl + " fail");
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("statusCode", 408);
			returnInfos.put("status", false);
			return returnInfos;
		} catch (Exception e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("status", false);
			return returnInfos;
		}
	}

	public Map https(String connectUrl, String method, int connectTimeOut, int readTimeOut) {
		log.info(this.getClass().toString() + " https Url: " + connectUrl + " connectTimeOut: " + connectTimeOut
				+ " readTimeOut: " + readTimeOut + " ...");

		SSLContext sslcontext;
		HttpsURLConnection con;
		Map returnInfos = new HashMap();
		try {
			sslcontext = SSLContext.getInstance("SSL", "SunJSSE");

			sslcontext.init(null, new TrustManager[] { new MyX509TrustManager() }, new java.security.SecureRandom());
			URL url = new URL(connectUrl);
			HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
				public boolean verify(String s, SSLSession sslsession) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			// 之後任何Https協議網站皆能正常訪問
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setRequestProperty("Content-type", "application/json");
			// 必須設置為false，否則會自動redirect到重定向後的地址
			con.setInstanceFollowRedirects(false);
			con.setConnectTimeout(connectTimeOut);
			con.setReadTimeout(readTimeOut);
			con.setUseCaches(false);
			con.setDoOutput(true);
			int responseCode = con.getResponseCode();
			returnInfos.put("statusCode", responseCode);
			if (String.valueOf(responseCode).substring(0, 1).equals("4")
					|| String.valueOf(responseCode).substring(0, 1).equals("5")) {
				returnInfos.put("status", false);
			}
			returnInfos.put("status", true);
//			con.connect();

			log.info(this.getClass().toString() + " https Url: " + connectUrl + " connectTimeOut: " + connectTimeOut
					+ " readTimeOut: " + readTimeOut + " finish");

			return returnInfos;

		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("status", false);
			return returnInfos;
		} catch (NoSuchProviderException e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("status", false);
			return returnInfos;
		} catch (KeyManagementException e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("status", false);
			return returnInfos;
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("status", false);
			return returnInfos;
		} catch (IOException e) {
			log.info("connect https " + connectUrl + " fail");
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("statusCode", 408);
			returnInfos.put("status", false);
			return returnInfos;
		} catch (Exception e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			returnInfos.put("status", false);
			return returnInfos;
		}
	}

	public static boolean tcp(String ipAddress, int port, int connectTimeout, int readTimeout) {
		log.info("start connect tcp " + ipAddress + ":" + port + " ...");
		try {
			Socket socket = new Socket();
			socket.setSoTimeout(readTimeout);
			socket.connect(new InetSocketAddress(ipAddress, port), connectTimeout);
//			InputStream inFromServer = socket.getInputStream();
//			DataInputStream in = new DataInputStream(inFromServer);
//			inFromServer.read();
			log.info("connect tcp " + ipAddress + ":" + port + " finish");
			return true;
		} catch (IOException e) {
			log.info("connect tcp " + ipAddress + ":" + port + " fail");
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			return false;
		} catch (Exception e) {
			log.error(e.getMessage());
			for (StackTraceElement elem : e.getStackTrace()) {
				log.error(elem.toString());
			}
			return false;
		}
	}

}
