package irt.stock.web.controllers.production.tuning;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("production")
public class TuningController {

	@GetMapping("tuning")
	public String home( Model model) {

		return "production/tuning/tuning";
	}
}
