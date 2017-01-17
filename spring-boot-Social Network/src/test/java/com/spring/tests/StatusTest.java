package com.spring.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Date;

import javax.sound.midi.Soundbank;
import javax.transaction.Transactional;
import javax.validation.constraints.AssertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.whatever.App;
import com.whatever.model.StatusUpdate;
import com.whatever.model.Dao.StatusUpdateDao;
import com.whatever.service.StatusUpdateService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
@WebAppConfiguration
@Transactional //Nu salveaza efectiv la finalizarea testului in baza de date
public class StatusTest {

	@Autowired
	private StatusUpdateDao statusUpdateDao;
	
	@Autowired
	private StatusUpdateService statusUpdateService;
	
	@Autowired
	private WebApplicationContext webApplicationcontext;
	
	private MockMvc mockMvc;
	
	@Before
	public void iniMock() {
		// TODO Auto-generated method stub
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationcontext).build();
	}
	
	//@Ignore
	@Test
	public void testSave() {
		StatusUpdate status = new StatusUpdate("This is a test status update");
		
		statusUpdateDao.save(status); //Salveaza in baza de date
		
		assertNotNull("Non null ID", status.getId()); //Verifica daca Id este inserat
		assertNotNull("Non null Date", status.getAdded());
		
		StatusUpdate retrieved = statusUpdateDao.findOne(status.getId());
		
		assertEquals("Matching StatusUpdate", status, retrieved);
		
	}
	
	//@Ignore
	@Test
	public void testFindLatest(){
		Calendar calendar = Calendar.getInstance();
		StatusUpdate lastStatusUpdate = null;
		
		for (int i = 0; i < 10; i++){
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			
			StatusUpdate status = new StatusUpdate("Status update " + i, calendar.getTime());
			statusUpdateDao.save(status);
			lastStatusUpdate = status;				
		}
		
		StatusUpdate retrieved = statusUpdateDao.findFirstByOrderByAddedDesc();
		assertEquals("Latest status update", lastStatusUpdate, retrieved);
		
		System.out.println(retrieved + "\n=====================================\n" + lastStatusUpdate);
	}
	
	//@Ignore
	@Test
	@WithMockUser(roles = "ADMIN") //We mock a user with role admin
	public void testAddNewStatus() throws Exception {
		
		for (int i = 0; i < 300; i++){
			
			String newStatus = "Mock Status created on: " + new Date().toString();
		
			mockMvc.perform(post("/addstatus").param("text", newStatus)).andExpect(status().is3xxRedirection());
			assertTrue("The latest Status should exist", statusUpdateService.getLatest().getText().equals(newStatus));
		
		}
		
	}
}

