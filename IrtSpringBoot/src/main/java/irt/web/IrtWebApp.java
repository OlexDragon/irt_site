package irt.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IrtWebApp {

	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) throws Exception {
		logger.entry();
		SpringApplication.run(IrtWebApp.class, args);
	}
}
