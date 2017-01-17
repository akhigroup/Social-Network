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
			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //luam din Context instanta curenta
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
		
		//In cazul in care este un nou user care nu si-a creeat un profil, il cream noi
		if (profile == null){
			profile = new Profile();
			profile.setUser(user);
			profileService.save(profile);
		}
		
		Profile webProfile = new Profile();
		webProfile.safeCopyFrom(profile); // Punem in .jsp doar varianta "safe", care are copiate doar elementele care pot fi afisate in .jsp, si nu si cele confidentiale
		
		mav.getModel().put("userId", user.getId());
		mav.getModel().put("profile", webProfile);
		
		mav.setViewName("app.profile");
		return mav;
	}
	
	@RequestMapping(value="/profile")
	public ModelAndView showProfile(){
		SiteUser user = getUser();
		ModelAndView mav = showProfile(user);
		mav.getModel().put("ownProfile", true); //setam ownProfile ca true, adica userul se uita la profilul sau
		return mav;
	}
	
	@RequestMapping(value="/profile/{id}") //cautam profilul dupa id-ul userului (care este primary key-ul din baza de date)
	public ModelAndView showProfile(@PathVariable("id") Long id){
		
		SiteUser user = userService.get(id);
		
		ModelAndView mav = showProfile(user);
		mav.getModel().put("ownProfile", false); //setam ownProfile ca false, adica userul Nu se uita la profilul sau
		
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
	@ResponseBody //Inseamna ca returnam date in format JSON (tip JSON-Java Script Object Notation) si nu un ModelAndView pentru ViewResolver
	//public PhotoUploadStatus handlePhotoUploads(ModelAndView mav, @RequestParam("file") MultipartFile file)- varianta care nu returneaza si mesajul
	public ResponseEntity<PhotoUploadStatus> handlePhotoUploads(@RequestParam("file") MultipartFile file){//MultipartFile este o clasa Spring
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		Path oldPhotoPath = profile.getPhoto(photoUploadDirectory);
		
		PhotoUploadStatus status = new PhotoUploadStatus(photoStatusOk);
		
		try {
			FileInfo photoInfo = fileService.saveImageFile(file, photoUploadDirectory, "Photos", "photo" + user.getId(), profilePhotoWidth, profilePhotoHeight);
			profile.setPhotoDetailes(photoInfo);//setam datele legate de imagine in "Profile"
			profileService.save(profile);//salvam profilul modificat
			
			if (oldPhotoPath != null) //inainte sa schimbam poza de profil, o stergem pe cea veche
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
		//return status; varianta care nu returneaza si mesajul Http
		return new ResponseEntity<PhotoUploadStatus>(status, HttpStatus.OK); // Optional putem returna si Http status: ok, forbidden, etc....
	}
	
	@RequestMapping(value="/profilephoto/{id}", method={RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<InputStreamResource> servePhoto(@PathVariable Long id) throws IOException{ //returneaza poza din profil, daca nu este setata va returna poza default
		
		SiteUser user = userService.get(id);
		Profile profile = profileService.getUserProfile(user);
		
		Path photoPath = Paths.get(photoUploadDirectory, "Default", "Profile.jpg"); //poza default la oricine nu a setat alta poza de profil
		
		if (profile != null && profile.getPhoto(photoUploadDirectory) != null)
			photoPath = profile.getPhoto(photoUploadDirectory);
		
		return ResponseEntity
				.ok()
				.contentLength(Files.size(photoPath))
				.contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(photoPath.toString())))
				.body(new InputStreamResource(Files.newInputStream(photoPath, StandardOpenOption.READ)));
	}
	
	//Adaugam daca vrem sa putem uploada dimensiuni mai mari ale fisierelor
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
		String cleanedInterestName = htmlPolicy.sanitize(interestName);//sanatizam Stringul interestName pt. a nu avea some java script hack scris de user
		
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
