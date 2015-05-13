package irt.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/login")
public class LoginController {

	private final Logger logger = LogManager.getLogger();


	@RequestMapping(method=RequestMethod.GET)
	public String login() {
		logger.entry();
		return "redirect:/login_error?login=true";
	}

	@RequestMapping(params="error")
	public String loginError() {
		logger.entry();
		return "redirect:/login_error";
	}

	@RequestMapping(params="logout")
	public String logout() {
		logger.entry();
		return "redirect:/";
	}
}
