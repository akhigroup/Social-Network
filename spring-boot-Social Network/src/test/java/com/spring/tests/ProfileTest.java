package com.spring.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
public class ProfileTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private InterestService interestService;
	
	private SiteUser[] users = {
			new SiteUser("Ana", "Pop", "test1@test.com", "11111"),
			new SiteUser("Dumitru", "George", "test2@test.com", "22222"),
			new SiteUser("Alex", "Alexandru", "test3@test.com", "33333")
	};
	
	private String[][] interests = {
			{"music", "guitar_sdfsfs", "plants"},
			{"music", "music", "philosophy_qwewqrfqw"},
			{"philosophy_qwewqrfqw", "football"}
	};
	
	@Test
	public void testInterests(){
		
		for (int i=0; i < users.length; i++){
			
			SiteUser user = users[i];
			String[] interestArray = interests[i];
			
			userService.register(user);
			
			Set<Interest> interestSet = new HashSet<>();
			
			for (String interestText : interestArray){
				
				Interest interest = interestService.createIfNotExists(interestText);
				interestSet.add(interest);
				
				assertNotNull("interest should not be empty", interest);
				assertNotNull("interest should have an ID", interest.getId());
				assertEquals("Text should match", interestText, interest.getName());
			}
			
			Profile profile = new Profile(user);
			profile.setInterests(interestSet);
			profileService.save(profile); //Trebuie comentat in clasa ProfileService @PreAuthorize("isAuthenticated()"), altfel nu se vor executa cele 2 metode
			
			Profile retrievedProfile = profileService.getUserProfile(user);
			assertEquals("Interests should match", retrievedProfile.getInterests(), interestSet);
			
		}
		
	}

}
