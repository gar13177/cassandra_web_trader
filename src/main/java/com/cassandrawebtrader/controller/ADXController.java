package com.cassandrawebtrader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cassandrawebtrader.dto.Chart;

@Controller
public class ADXController {
	
	private static Logger logger = LoggerFactory.getLogger(ADXController.class);
	
	@RequestMapping(value = "/member/chart/adx", method = RequestMethod.GET)
	public String showChartPage(Model model) {
		Chart chart = new Chart();
		model.addAttribute("chart", chart);		
		return "cloud2";
	}

	@RequestMapping(value = "/member/chart/adx", method = RequestMethod.POST)
	public String processChartPage(@ModelAttribute("chart") Chart chart, Model model) {
		model.addAttribute("chart2", chart);
		
		return "adxchart";
	}

}
