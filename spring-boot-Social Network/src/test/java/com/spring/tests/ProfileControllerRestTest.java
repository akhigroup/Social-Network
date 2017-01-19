package com.spring.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class ProfileControllerRestTest {
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WebApplicationContext webApplicationcontext;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationcontext).build();
	}
	
	@Test
	@WithMockUser(username = "q@q.com") /** We mock a real user*/
	public void testSaveAndDeleteInterest() throws Exception{
		
		String interestText = "some interest_here";
		
		mockMvc.perform(post("/save-interest").param("name", interestText)).andExpect(status().isOk());
		
		Interest interest = interestService.get(interestText);
		
		assertNotNull("interest should not be null", interest);
		assertEquals("Retrived interest should be same", interestText, interest.getName());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		SiteUser user = userService.get(email); 
		Profile profile = profileService.getUserProfile(user);
		
		System.out.println("User:\n" + user + email);
		
		assertTrue("Interest should exists in Profile", profile.getInterests().contains(new Interest(interestText))); //!!!!
		
		mockMvc.perform(post("/delete-interest").param("name", interestText)).andExpect(status().isOk());
		
		profile = profileService.getUserProfile(user);
		
		assertFalse("Profile should not be in there", profile.getInterests().contains(new Interest(interestText)));
	}

}
