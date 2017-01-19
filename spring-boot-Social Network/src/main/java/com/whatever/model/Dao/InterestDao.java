package com.whatever.model.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whatever.model.Interest;

@Repository
public interface InterestDao extends JpaRepository<Interest, Long>{
	
	Interest findOneByName(String name);
	
	List<String> findByNameStartingWith(String interest);
	
}
