/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package irt.stock.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import irt.stock.data.jpa.repositories.PartNumberRepository;
import irt.stock.web.PartNumber;

@Controller
public class HomeController {

	@Autowired
	private PartNumberRepository partNumberRepository;

	@GetMapping("/login")
	public String login() {
		return "home";
	}

	@GetMapping("/")
	public String home(@CookieValue(name="desiredPN", defaultValue="") String desiredPN, Model model) {

		model.addAttribute("desiredPN", desiredPN);
		final List<? extends PartNumber> partNumbers = partNumberRepository.findByPartNumberContaining(desiredPN);
		model.addAttribute("partNumbers", partNumbers);

		return "home";
	}
}