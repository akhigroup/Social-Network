package com.whatever.model.Dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.whatever.model.VerificationToken;

@Repository
public interface VerificationDao extends CrudRepository<VerificationToken, Long>{
	
	VerificationToken findByToken(String token);
}
