package com.whatever.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.whatever.model.StatusUpdate;
import com.whatever.service.StatusUpdateService;

@Controller
public class StatusUpdateController {
	
	@Autowired
	private StatusUpdateService statusUpdateService;
	
	@RequestMapping(value="/viewstatus", method={RequestMethod.GET})
	ModelAndView viewStatus(ModelAndView mav, @RequestParam(name="p", defaultValue="1") int pageNumber){//parametrii din URL de dupa "?"
		// ex: http://localhost:8080/viewstatus?p=4
		
		Page<StatusUpdate> page = statusUpdateService.getPage(pageNumber);
		mav.getModel().put("page", page);
		
		mav.setViewName("app.viewStatus");
		return mav;
	}
		
	@RequestMapping(value="/addstatus", method={RequestMethod.GET})
	ModelAndView addStatus(ModelAndView mav, @ModelAttribute("statusUpdate") StatusUpdate statusUpdate){
		
		mav.setViewName("app.addStatus");
		
		StatusUpdate latestStatusUpdate = statusUpdateService.getLatest();
		
		mav.getModel().put("latestStatusUpdate", latestStatusUpdate);
		return mav;
	}
	
	@PreAuthorize("hasRole('ADMIN')") /** Only users with ROLE_ADMIN can acces this method*/
	@RequestMapping(value="/addstatus", method={RequestMethod.POST})
	ModelAndView addStatus(ModelAndView mav, @ModelAttribute(value = "statusUpdate") @Valid StatusUpdate statusUpdate, BindingResult result){// @Valid verifica daca statusUpdate
		// a trecut de validarile necesare cerute in Model si da rezultatul in "BindingResult result"
		
		mav.setViewName("app.addStatus");
		
		if (!result.hasErrors()){ //Daca nu exista errori salvam in baza de date si golim contentul din Form
			statusUpdateService.save(statusUpdate);
			mav.getModel().put("statusUpdate", new StatusUpdate());
			mav.setViewName("redirect:/viewstatus"); //Redirectionam catre pagina "/viewstatus"
		}
				
		StatusUpdate latestStatusUpdate = statusUpdateService.getLatest();
		mav.getModel().put("latestStatusUpdate", latestStatusUpdate);
				 				
		return mav;
	}
	
	@RequestMapping(value="/deletestatus", method=RequestMethod.GET)
	ModelAndView deleteStatus(ModelAndView mav, @RequestParam(name="id") Long id){//@RequestParam /deletestatus?id= 
		
		statusUpdateService.delete(id);
		mav.setViewName("redirect:/viewstatus");
		return mav;
	}
	
	@RequestMapping(value="/editstatus", method=RequestMethod.GET)
	ModelAndView editStatus(ModelAndView mav, @RequestParam(name="id") Long id){
		
		StatusUpdate statusUpdate = statusUpdateService.get(id);
		mav.getModel().put("statusUpdateXXX", statusUpdate);
		mav.setViewName("app.editStatus");
		return mav;
	}
	
	@RequestMapping(value="/editstatus", method={RequestMethod.POST})
	ModelAndView editStatus(ModelAndView mav, @Valid StatusUpdate statusUpdate, BindingResult result){	
		mav.setViewName("app.editStatus");
		
		if (!result.hasErrors()){
			statusUpdateService.save(statusUpdate);
			mav.setViewName("redirect:/viewstatus");
		}
		return mav;
	}
}
