package com.whatever.model.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//import org.springframework.data.repository.Repository;

import com.whatever.model.Profile;
import com.whatever.model.SiteUser;

@Repository
public interface ProfileDao extends JpaRepository<Profile, Long>{
	
	Profile findByUser(SiteUser user);
	List<Profile> findByInterestsNameLike(String interest);
	/** Nice One!*/
	Page<Profile> findByInterestsNameLike(String interest, Pageable pageable);
	
}