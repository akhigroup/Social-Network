package com.whatever.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.whatever.model.Profile;
import com.whatever.model.SiteUser;
import com.whatever.model.Dao.ProfileDao;
import com.whatever.model.Dto.SearchResultDto;

@Service
public class ProfileService {
	
	
	private static final int PAGESIZE = 10;
	
	@Autowired
	ProfileDao profileDao;
	/**Comment for JUnit Test */
	@PreAuthorize("isAuthenticated()") //Folosim optional pt cresterea securitatii, metoda se va executa doar daca userul este autentificat
	public void save(Profile profile){
		
		profileDao.save(profile);
	}
	/**Comment for JUnit Test && retrieve photo*/
	//@PreAuthorize("isAuthenticated()") 
	public Profile getUserProfile(SiteUser user){
		
		return profileDao.findByUser(user);
	}
	
	public Page<SearchResultDto> findProfilesByInterest(String interest, int pageNumber){
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "user");
		
		Page<Profile> profiles = profileDao.findByInterestsNameLike(interest, request);
		
		Page<SearchResultDto> searchResultDtos = profiles.map(SearchResultDto::new);
		
		return searchResultDtos;
	}
}
