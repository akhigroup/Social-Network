package com.whatever.model.Dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.whatever.model.StatusUpdate;

/*public interface StatusUpdateDao extends CrudRepository<StatusUpdate, Long>{
	StatusUpdate findFirstByOrderByAddedDesc();
}*/

//Folosim in loc de CrudRepository, PagingAndSortingRepository

@Repository
public interface StatusUpdateDao extends PagingAndSortingRepository<StatusUpdate, Long>{
	
	StatusUpdate findFirstByOrderByAddedDesc();
}
