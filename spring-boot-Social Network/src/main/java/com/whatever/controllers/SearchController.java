package com.whatever.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.whatever.model.Dto.SearchResultDto;
import com.whatever.service.InterestService;
import com.whatever.service.ProfileService;

@Controller
public class SearchController {
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	InterestService interestService;
		
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public ModelAndView search(ModelAndView mav, @RequestParam("interest") String interest, @RequestParam(name="p", defaultValue="1") int page){
		
		Page<SearchResultDto> resultPage = profileService.findProfilesByInterest(interest, page);
		List<String> interests = interestService.findByNameStartingWith(interest); 
		
		mav.setViewName("app.search");
		mav.getModel().put("interest", interest);
		mav.getModel().put("interests", interests);
		mav.getModel().put("page", resultPage);
		
		return mav;
	}

}
