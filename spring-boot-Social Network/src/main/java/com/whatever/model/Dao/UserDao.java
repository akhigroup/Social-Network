package com.whatever.model.Dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.whatever.model.SiteUser;

@Repository
public interface UserDao extends CrudRepository<SiteUser, Long>{
	
	SiteUser findByEmail(String email);//datorita numelui metodei pe care o declaram ("find By Email"), Spring o va implementa automat->Magic, by Query
}
