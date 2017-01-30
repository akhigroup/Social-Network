package com.spring.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;
import javax.validation.constraints.AssertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.whatever.App;
import com.whatever.model.Interest;
import com.whatever.model.Profile;
import com.whatever.model.SiteUser;
import com.whatever.service.InterestService;
import com.whatever.service.ProfileService;
import com.whatever.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
@WebAppConfiguration
@Transactional
public class BulkTests {
	
	private final String hobbieFile = "/com/spring/tests/data/hobbies.txt";
	private final String firstNamesFile = "/com/spring/tests/data/firstnames.txt";
	private final String surNamesFile = "/com/spring/tests/data/surnames.txt";
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private InterestService interestService;

	private List<String> loadFile(String pathName, int maxLength) throws IOException{
		
		Path filePath = new ClassPathResource(pathName).getFile().toPath();
		
		Stream<String> stream = Files.lines(filePath);
		
		List<String> result = stream
			.filter(line -> !line.isEmpty()) 
			.map(line -> line.trim())
			.filter(line -> line.length() <= maxLength)
			.map(line -> line.substring(0, 1).toUpperCase() + line.substring(1).toLowerCase())
			//.map(line -> line.replaceAll("\\s|\\W", "")) not Ok for Hobbies
			.collect(Collectors.toList());
			//.forEach(System.out :: println);
		
		stream.close();
		return result;
		
	}
	
	//@Ignore
	@Test
	public void createTestData() throws IOException{
		
		List<String> interests = loadFile(hobbieFile, 25);
		List<String> fnames = loadFile(firstNamesFile, 25);
		List<String> snames = loadFile(surNamesFile, 25);
		
		Random random = new Random();
		
		for (int i = 0; i < 500; i++){
			
			
			String firstname = fnames.get(random.nextInt(fnames.size())).replaceAll("\\s|\\W", "");
			String surname = snames.get(random.nextInt(snames.size())).replaceAll("\\s|\\W", "");
			
			String email = (firstname.toLowerCase() + surname.toLowerCase() + "@example.com.ro");
			
			if (userService.get(email) != null)
				continue;
			
			String password = "pass" + firstname.toLowerCase();
			password = password.substring(0, Math.min(15, password.length()));
			//assertTrue(password.length() <= 15);
			
			SiteUser user = new SiteUser(firstname, surname, email, password);
			
			userService.register(user);
			
			Profile profile = new Profile(user);
			
			int nbOfInterest = random.nextInt(6) + 1; /** generate between 1 and 7 interests per user*/
			Set<Interest> userInterests = new HashSet<>();
			
			for (int j = 0; j < nbOfInterest; j++){
			
				String interestText = interests.get(random.nextInt(interests.size()));
				Interest interest = interestService.createIfNotExists(interestText);
				userInterests.add(interest);
			}
			
			profile.setInterests(userInterests);
			profileService.save(profile);
			
		}
		
		assertTrue(true);
	}
}
