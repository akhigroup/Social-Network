package com.whatever.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.whatever.model.Interest;
import com.whatever.model.StatusUpdate;
import com.whatever.service.StatusUpdateService;

@Controller
public class PageController {
	
	@Autowired
	private StatusUpdateService statusUpdateService;
	
	@RequestMapping(value="/", method={RequestMethod.GET})
	ModelAndView home(ModelAndView mav){
		
		Interest searchedInterest = new Interest();
		
		mav.setViewName("app.homepage");
		StatusUpdate statusUpdate = statusUpdateService.getLatest();	
		mav.getModel().put("statusUpdate", statusUpdate);
		mav.getModel().put("searchedInterest", searchedInterest);
		
		return mav;
	}
		
	@RequestMapping(value="/about")
	String about(){
		return "app.about";
	}
	
}