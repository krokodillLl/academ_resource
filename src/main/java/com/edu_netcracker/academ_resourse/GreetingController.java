package com.edu_netcracker.academ_resourse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

	@GetMapping
	public String greeting(@RequestParam(name="name", required=false, defaultValue="Academ Resourse") String name,
						   Model model) {
		model.addAttribute("name", name);
		return "main";
	}

}