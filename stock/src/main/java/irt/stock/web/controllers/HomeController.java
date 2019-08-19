package irt.stock.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import irt.stock.data.jpa.MobileDeviceID;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.repositories.ComponentRepository;

@Controller
public class HomeController {

	@Autowired private ComponentRepository componentRepository;

	@GetMapping("/login")
	public String login() {
		return "home";
	}

	@GetMapping("/")
	public String home(@CookieValue(name="desiredPN", defaultValue="") String desiredPN, Model model) {

		model.addAttribute("desiredPN", desiredPN);

		String pn = desiredPN.toUpperCase().replaceAll("[^A-Z0-9_%]", "");

		final List<Component> partNumbers = componentRepository.findDistinctByPartNumberContainingOrManufPartNumberContainingOrAlternativeComponentsAltMfrPartNumberContainingOrderByPartNumber(pn, desiredPN, desiredPN);
		model.addAttribute("partNumbers", partNumbers);

		return "home";
	}

	@GetMapping("/scanned/{value}")
	public String scannedGet(@PathVariable String value, Model model) {

		componentRepository.findByPartNumberOrManufPartNumber(value, value)
		.ifPresent(
				component->{
					model.addAttribute("desiredPN", component.getPartNumber());
					final List<Component> partNumbers = new ArrayList<>();
					partNumbers.add(component);
					model.addAttribute("partNumbers", partNumbers);
				});

		return "home";
	}

	private final static Map<MobileDeviceID, String> scannedData = new HashMap<>();
	@PostMapping(value="/sync/scanned/{value}/{deviceName}/{deviceID}")
	@ResponseStatus(value = HttpStatus.OK)
	public void scannedPost(@PathVariable String value, @PathVariable String deviceName, @PathVariable String deviceID) {
		scannedData.put(new MobileDeviceID(deviceID, deviceName), value);
	}

	@GetMapping("/sync/modal/select")
	public String syncModalSelect(Model model) {
		model.addAttribute("devices", scannedData.keySet());
		return "sync/modal_device_select :: select";
	}

	@GetMapping("/sync/{deviceID}")
	@ResponseBody
	public String syncValue(@PathVariable String deviceID) {
		 return scannedData.entrySet().parallelStream().filter(entry->entry.getKey().toString().equals(deviceID)).findAny().map(Entry::getValue).orElse(null);
	}
}
