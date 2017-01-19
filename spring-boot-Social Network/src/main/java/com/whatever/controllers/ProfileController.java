package com.whatever.controllers;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.MultipartConfigElement;
import javax.validation.Valid;

import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.whatever.exceptions.ImageTooSmallException;
import com.whatever.exceptions.InvalidFileException;
import com.whatever.model.FileInfo;
import com.whatever.model.Interest;
import com.whatever.model.Profile;
import com.whatever.model.SiteUser;
import com.whatever.service.FileService;
import com.whatever.service.InterestService;
import com.whatever.service.ProfileService;
import com.whatever.service.UserService;
import com.whatever.status.PhotoUploadStatus;

@Controller
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PolicyFactory htmlPolicy;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	InterestService interestService;
	
	@Value("${photo.upload.directory}")
	private String photoUploadDirectory;
	
	@Value("${profile.photo.width.size}")
	private int profilePhotoWidth;
	
	@Value("${profile.photo.height.size}")
	private int profilePhotoHeight;
	
	@Value("${photo.upload.ok}")
	private String photoStatusOk;
	
	@Value("${photo.upload.invalid}")
	private String photoStatusInvalid;
	
	@Value("${photo.upload.ioexception}")
	private String photoStatusIOexception;
	
	@Value("${photo.upload.imagetoosmall}")
	private String photoStatusTooSmall;
		
	public SiteUser getUser(){
			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		return userService.get(userName);	
	}
	
	private ModelAndView showProfile(SiteUser user){
		ModelAndView mav = new ModelAndView();
		
		if (user == null){
			mav.setViewName("redirect:/");
			return mav;
		}
		
		Profile profile = profileService.getUserProfile(user);
		
		/** If there is a new user without a profile, we will create by default*/
		if (profile == null){
			profile = new Profile();
			profile.setUser(user);
			profileService.save(profile);
		}
		
		Profile webProfile = new Profile();
		/** We use a 'safe' form to send to data to the view*/
		webProfile.safeCopyFrom(profile);
		
		mav.getModel().put("userId", user.getId());
		mav.getModel().put("profile", webProfile);
		
		mav.setViewName("app.profile");
		return mav;
	}
	
	@RequestMapping(value="/profile")
	public ModelAndView showProfile(){
		SiteUser user = getUser();
		ModelAndView mav = showProfile(user);
		mav.getModel().put("ownProfile", true);
		return mav;
	}
	
	@RequestMapping(value="/profile/{id}")
	public ModelAndView showProfile(@PathVariable("id") Long id){
		
		SiteUser user = userService.get(id);
		
		ModelAndView mav = showProfile(user);
		mav.getModel().put("ownProfile", false);
		
		return mav;
	}
	
	@RequestMapping(value="/edit-profile-about", method={RequestMethod.GET})
	public ModelAndView editProfile(ModelAndView mav){
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		Profile webProfile = new Profile();
		webProfile.safeCopyFrom(profile);
		
		mav.getModel().put("profile", webProfile);
		mav.setViewName("app.editProfile");
		
		return mav;
	}

	@RequestMapping(value="/edit-profile-about", method={RequestMethod.POST})
	public ModelAndView editProfile(ModelAndView mav, @Valid Profile webProfile, BindingResult result){
		
		mav.setViewName("app.editProfile");
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		profile.editProfileAbout(webProfile, htmlPolicy);
		
		if (!result.hasErrors()){
			profileService.save(profile);
			mav.setViewName("redirect:/profile");
		}
		
		return mav;
	}
	
	@RequestMapping(value="/upload-profile-photo", method={RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<PhotoUploadStatus> handlePhotoUploads(@RequestParam("file") MultipartFile file){
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		Path oldPhotoPath = profile.getPhoto(photoUploadDirectory);
		
		PhotoUploadStatus status = new PhotoUploadStatus(photoStatusOk);
		
		try {
			FileInfo photoInfo = fileService.saveImageFile(file, photoUploadDirectory, "Photos", "photo" + user.getId(), profilePhotoWidth, profilePhotoHeight);
			profile.setPhotoDetailes(photoInfo);
			profileService.save(profile);
			
			if (oldPhotoPath != null)
				Files.delete(oldPhotoPath);
				
		} catch (InvalidFileException e) {
			status.setMessage(photoStatusInvalid);
			e.printStackTrace();
		} 
		catch (IOException e){
			status.setMessage(photoStatusIOexception);
			e.printStackTrace();
		}
		catch (ImageTooSmallException e) {
			status.setMessage(photoStatusTooSmall);
			e.printStackTrace();
		}	
		
		return new ResponseEntity<PhotoUploadStatus>(status, HttpStatus.OK);
	}
	
	@RequestMapping(value="/profilephoto/{id}", method={RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<InputStreamResource> servePhoto(@PathVariable Long id) throws IOException{
		
		SiteUser user = userService.get(id);
		Profile profile = profileService.getUserProfile(user);
		
		Path photoPath = Paths.get(photoUploadDirectory, "Default", "Profile.jpg");
		
		if (profile != null && profile.getPhoto(photoUploadDirectory) != null)
			photoPath = profile.getPhoto(photoUploadDirectory);
		
		return ResponseEntity
				.ok()
				.contentLength(Files.size(photoPath))
				.contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(photoPath.toString())))
				.body(new InputStreamResource(Files.newInputStream(photoPath, StandardOpenOption.READ)));
	}
	
	/** Used if to change the default maxSize limit*/
	@Bean
	MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setMaxFileSize("5120MB");
	    factory.setMaxRequestSize("5120MB");
	    return factory.createMultipartConfig();
	}
	
	@RequestMapping(value="/save-interest", method={RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<?> saveInterest(@RequestParam("name") String interestName){
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		/** Sanitize the String before we save it in our DB (avoid SQL injection)*/
		String cleanedInterestName = htmlPolicy.sanitize(interestName);
		
		Interest interest = interestService.createIfNotExists(cleanedInterestName);
		profile.addInterest(interest);
		profileService.save(profile);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@RequestMapping(value="/delete-interest", method={RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<?> deleteInterest(@RequestParam("name") String interestName){
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		profile.deleteInterest(interestName);
		
		profileService.save(profile);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
}
