package irt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@ModelAttribute("title") String title(){ return "IRT Technologies Inc."; }

	@RequestMapping({"/", "/irt"})
	public String home(){
		return "home";
	}
}
