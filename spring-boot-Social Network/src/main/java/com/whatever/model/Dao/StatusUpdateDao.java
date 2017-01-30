package com.whatever.model.Dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.whatever.model.StatusUpdate;

@Repository
public interface StatusUpdateDao extends PagingAndSortingRepository<StatusUpdate, Long>{
	
	StatusUpdate findFirstByOrderByAddedDesc();
}
