package com.whatever.controllers;

import java.io.FileNotFoundException;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.whatever.model.SiteUser;
import com.whatever.model.VerificationToken;
import com.whatever.service.EmailService;
import com.whatever.service.UserService;

@Controller
public class AuthorController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${message.registration.confirmed}")
	private String registrationConfirmedMessage;
	
	@Value("${message.invalid.user}")
	private String registrationInvalidUserMessage;
	
	@Value("${message.expired.token}")
	private String registrationExpiredTokenMessage;
	
	@Value("${message.access.denied}")
	private String accesDeniedMessage;
	
	@RequestMapping("/verifyemail")
	String emailSender(){
		return "app.verifyemail";
	}
	
	@RequestMapping("/login")
	String admin(){
		return "app.login";
	}
	
	@RequestMapping("/403")
	ModelAndView accesDenied(ModelAndView mav){
		mav.getModel().put("message", accesDeniedMessage);
		mav.setViewName("app.messages");
		return mav;
	}
	
	@RequestMapping("/confirmregister")
	ModelAndView registrationConfirmed(ModelAndView mav, @RequestParam("t") String tokenString){
		
		VerificationToken token = userService.getVerificationToken(tokenString);
		
		if (token == null){
			mav.setViewName("redirect:/invaliduser");
			userService.deleteToken(token);
			return mav;
		}
		
		Date expiryDate = token.getExpiry();
		
		if (expiryDate.before(new Date())){
			mav.setViewName("redirect:expiredtoken");
			userService.deleteToken(token);
			return mav;
		}
		
		SiteUser user = token.getUser();
		
		if (user == null){
			mav.setViewName("redirect:/invaliduser");
			userService.deleteToken(token);
			return mav;
		}
		
		userService.deleteToken(token);
		user.setEnabled(true);
		userService.save(user);
		
		mav.getModel().put("message", registrationConfirmedMessage);
		mav.setViewName("app.messages");
		return mav;
	}
	
	@RequestMapping("/invaliduser")
	ModelAndView invalidUser(ModelAndView mav){
		mav.getModel().put("message", registrationInvalidUserMessage);
		mav.setViewName("app.messages");
		return mav;
	}
	
	@RequestMapping("/expiredtoken")
	ModelAndView expiredToken(ModelAndView mav){
		mav.getModel().put("message", registrationExpiredTokenMessage);
		mav.setViewName("app.messages");
		return mav;
	}	
	
	@RequestMapping(value="/register", method={RequestMethod.GET})
	ModelAndView register(ModelAndView mav) throws FileNotFoundException{
				
		SiteUser user = new SiteUser();
		mav.getModel().put("userDemo", user);
		mav.setViewName("app.register");
		
		return mav;
	}
	
	@RequestMapping(value="/register", method={RequestMethod.POST})
	ModelAndView register(ModelAndView mav, @ModelAttribute(value="userDemo") @Valid SiteUser user, BindingResult result){	
		mav.setViewName("app.register");
		
		if(!result.hasErrors()){
			userService.register(user);
			
			String token = userService.createEmailVerificationToken(user);
			
			emailService.sendVerificationEmail(user.getEmail(), token);
			mav.setViewName("redirect:/verifyemail");
			
			user.setEnabled(true);
			userService.save(user);
			
		}
		return mav;
	}
	
}
