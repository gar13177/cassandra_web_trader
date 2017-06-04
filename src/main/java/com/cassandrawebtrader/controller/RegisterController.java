package com.cassandrawebtrader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cassandrawebtrader.dto.RegisterForm;

@Configuration
@RequestMapping("/member")
public class RegisterController {
	
	private static Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationForm(Model model){
		RegisterForm registerForm = new RegisterForm();
		model.addAttribute("registerForm", registerForm);
		logger.info("ola k ase");
		logger.info(registerForm.toString());
		
		return "register";
		
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String postRegistrationForm(@ModelAttribute("registerForm") RegisterForm registerForm, BindingResult result){
		if(result.hasErrors())
			return "register";
		logger.info(registerForm.toString());
		
		return "home";
		
	}
	

}
