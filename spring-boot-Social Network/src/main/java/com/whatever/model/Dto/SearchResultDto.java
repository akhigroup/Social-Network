package com.whatever.model.Dto;

import java.util.Set;

import com.whatever.model.Interest;
import com.whatever.model.Profile;
import com.whatever.model.SiteUser;

public class SearchResultDto {
	
	private Long userId;
	private String firstname;
	private String surname;
	private Set<Interest> interests;
	
	public SearchResultDto(Profile profile){
		SiteUser user = profile.getUser();
		
		userId = user.getId();
		firstname = user.getFirstname();
		surname = user.getSurname();
		interests = profile.getInterests();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Set<Interest> getInterests() {
		return interests;
	}

	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
	}

	@Override
	public String toString() {
		return "SearchResultDto [userId=" + userId + ", firstname=" + firstname + ", surname=" + surname
				+ ", interests=" + interests + "]";
	}

	
	
}
