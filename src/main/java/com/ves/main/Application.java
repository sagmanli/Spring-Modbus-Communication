package com.ves.main;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.ves.main.controller.ScheduledEvents;

@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {
	private static final Logger logger = Logger.getLogger(Application.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws InterruptedException, IOException, JAXBException {
		try {
			prepareApp();
		} catch (Exception e) {
			logger.error("Could not initialize application! Please check permissions!");
			System.exit(-1);
		}

		ScheduledEvents.loadDeviceInfos();
		ScheduledEvents.loadUsers();

		SpringApplication application = new SpringApplication(Application.class);
		application.setShowBanner(false);
		application.run(args);
	}

	public static void prepareApp() throws IOException {
		File file = new File("Config/DeviceInfo");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File("Config/Layout");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File("Config/Log");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File("Config/User");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File("application.properties");
		if (!file.exists()) {
			file.createNewFile();
		}
	}
}
