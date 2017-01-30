package com.whatever.model.Dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.whatever.model.SiteUser;

@Repository
public interface UserDao extends CrudRepository<SiteUser, Long>{
	
	SiteUser findByEmail(String email);
}
