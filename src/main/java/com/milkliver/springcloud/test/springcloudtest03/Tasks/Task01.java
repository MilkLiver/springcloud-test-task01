package com.milkliver.springcloud.test.springcloudtest03.Tasks;

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

	@Value("${oms.server.url:#{null}}")
	String omsServerUrlStr;

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
			System.out.println("test");

			if (omsServerSendAlertEnableHttps) {
				sendOmsAlert.https(omsServerSendAlertUrl, "GET", omsServerSendAlertConnectTimeOut,
						omsServerSendAlertReadTimeOut);
			} else {
				sendOmsAlert.http(omsServerSendAlertUrl, "GET", omsServerSendAlertConnectTimeOut,
						omsServerSendAlertReadTimeOut);
			}

		};
	}
}
