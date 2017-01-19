package com.whatever.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whatever.model.Interest;
import com.whatever.model.Dao.InterestDao;


@Service
public class InterestService {

	@Autowired
	private InterestDao interestDao;
	
	public Interest get(String interestName){
		return interestDao.findOneByName(interestName);
	}
	
	public Long count(){
		return interestDao.count();
	}
	
	public void save(Interest interest){
		interestDao.save(interest);
	}
	
	public Interest createIfNotExists(String InterestName){
		Interest interest = interestDao.findOneByName(InterestName);
		
		if (interest == null){
			interest = new Interest(InterestName);
			interestDao.save(interest);
		}
		
		return interest;
	}
	
	public List<String> findByNameStartingWith(String interest){
		return interestDao.findByNameStartingWith(interest);
	}
}
