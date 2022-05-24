package com.milkliver.springcloud.test.springcloudtest03.Tasks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.milkliver.springcloud.test.springcloudtest03.utils.SendOmsAlert;

@Configuration
@EnableTask
public class Task01 {

	private static final Logger log = LoggerFactory.getLogger(Task01.class);

	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();

	@Value("${oms.server.send-alert.connect-time-out:2000}")
	int omsServerSendAlertConnectTimeOut;

	@Value("${oms.server.send-alert.read-time-out:2000}")
	int omsServerSendAlertReadTimeOut;

//	@Value("${oms.server.send-alert.url}")
//	String omsServerSendAlertUrl;

//	@Value("${oms.server.send-alert.enable-https:false}")
//	boolean omsServerSendAlertEnableHttps;

	@Value("${system.command}")
	String systemCommandBase64;

	@Autowired
	SendOmsAlert sendOmsAlert;

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {

			log.info("CommandLineRunner ...");
			try {

				log.info("CommandBase64: " + systemCommandBase64);
				String systemCommand = new String(decoder.decode(systemCommandBase64));
				log.info("Command: " + systemCommand);
				log.info(
						"==================================================start==================================================");

				Process process = Runtime.getRuntime().exec(systemCommand);
//				Process process = Runtime.getRuntime().exec("powershell ls");
//				Process process = Runtime.getRuntime().exec("notepad.exe");
//				Process process = Runtime.getRuntime().exec(
//						"java -jar D:\\JavaProjects\\TestProject03\\springcloud-test-task01\\externalProgramFiles\\java-job01.jar");
//				Process process = Runtime.getRuntime().exec("python D:\\JavaProjects\\TestProject03\\springcloud-test-task01\\externalProgramFiles\\test.py");

//				process.waitFor();

//				============================================================================================
				StringBuilder execCmdRes = new StringBuilder();

//				BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
//				byte[] buffer = new byte[10240];
//				int bytesRead = 0;
//				while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
////					String chunk = new String(buffer, 0, bytesRead);
//					execCmdRes.append(new String(buffer, 0, bytesRead));
//				}

//				InputStream inputStream = process.getInputStream();
//				InputStreamReader isr = new InputStreamReader(inputStream);
//				InputStream errorStream = process.getErrorStream();
//				InputStreamReader esr = new InputStreamReader(errorStream);

//				process.getInputStream().read();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					execCmdRes.append(line);
					execCmdRes.append("\r\n");
				}
//				============================================================================================

//				log.info("exitVaule: " + String.valueOf(process.exitValue()));
				log.info("waitFor: " + String.valueOf(process.waitFor()));

				log.info(execCmdRes.toString());

				process.destroy();
				log.info(
						"===================================================end===================================================");

//				Date now = new Date();
//
//				URL sendAlertUrl = new URL(omsServerSendAlertUrl);
//
//				SimpleDateFormat alertTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH);
//
//				sendAlertUrl = new URL(sendAlertUrl,
//						alertTimeFormat.format(now) + " execute command: " + systemCommand);
//
//				log.info("sendAlertUrl: " + sendAlertUrl.toString());
//
//				if (omsServerSendAlertEnableHttps) {
//					sendOmsAlert.https(sendAlertUrl.toString(), "GET", omsServerSendAlertConnectTimeOut,
//							omsServerSendAlertReadTimeOut);
//				} else {
//					sendOmsAlert.http(sendAlertUrl.toString(), "GET", omsServerSendAlertConnectTimeOut,
//							omsServerSendAlertReadTimeOut);
//				}
			} catch (Exception e) {
				log.error(e.getMessage());
				for (StackTraceElement elem : e.getStackTrace()) {
					log.error(elem.toString());
				}
			}
			log.info("CommandLineRunner finish");

		};
	}
}
