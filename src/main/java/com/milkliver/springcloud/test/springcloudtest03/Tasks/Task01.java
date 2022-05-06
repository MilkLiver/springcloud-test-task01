package com.milkliver.springcloud.test.springcloudtest03.Tasks;

import java.net.URL;
import java.text.SimpleDateFormat;
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

	@Value("${oms.server.send-alert.connect-time-out:2000}")
	int omsServerSendAlertConnectTimeOut;

	@Value("${oms.server.send-alert.read-time-out:2000}")
	int omsServerSendAlertReadTimeOut;

	@Value("${oms.server.send-alert.url}")
	String omsServerSendAlertUrl;

	@Value("${oms.server.send-alert.enable-https:false}")
	boolean omsServerSendAlertEnableHttps;

	@Autowired
	SendOmsAlert sendOmsAlert;

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {

			log.info("CommandLineRunner ...");
			try {
				Date now = new Date();

				URL sendAlertUrl = new URL(omsServerSendAlertUrl);

				SimpleDateFormat alertTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH);

				sendAlertUrl = new URL(sendAlertUrl, alertTimeFormat.format(now));

				log.info("sendAlertUrl: " + sendAlertUrl.toString());

				if (omsServerSendAlertEnableHttps) {
					sendOmsAlert.https(sendAlertUrl.toString(), "GET", omsServerSendAlertConnectTimeOut,
							omsServerSendAlertReadTimeOut);
				} else {
					sendOmsAlert.http(sendAlertUrl.toString(), "GET", omsServerSendAlertConnectTimeOut,
							omsServerSendAlertReadTimeOut);
				}
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
