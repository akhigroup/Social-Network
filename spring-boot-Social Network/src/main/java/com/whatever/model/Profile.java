package com.whatever.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.owasp.html.PolicyFactory;

@Entity
@Table(name="profile")
public class Profile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	/** OneToOne can be EAGER and not influencing much on performance*/
	@OneToOne(targetEntity=SiteUser.class, fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", nullable=false)
	private SiteUser user;
	
	@Column(name="about", length=5000)
	@Size(max=5000, message="{editprofile.about.size}")
	private String about;
	
	@Column(name="photo_directory", length=10)
	private String photoDirectory;
	
	@Column(name="photo_name", length=20)
	private String photoName;
	
	@Column(name="photo_extension", length=5)
	private String photoExtension;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="profile_interests", joinColumns={@JoinColumn(name="profile_id", referencedColumnName = "id")}, 
										 inverseJoinColumns={@JoinColumn(name="interest_id", referencedColumnName = "id")})
	private Set<Interest> interests;
	
	@Transient
	private String firstName;
	
	@Transient
	private String surName;
	
	public Profile(){
		
	}
	/** For testing purpose */
	public Profile(SiteUser user){
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SiteUser getUser() {
		return user;
	}

	public void setUser(SiteUser user) {
		this.user = user;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
	
	public String getPhotoDirectory() {
		return photoDirectory;
	}

	public void setPhotoDirectory(String photoDirectory) {
		this.photoDirectory = photoDirectory;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoExtension() {
		return photoExtension;
	}

	public void setPhotoExtension(String photoExtension) {
		this.photoExtension = photoExtension;
	}
	

	public Set<Interest> getInterests() {
		return interests;
	}

	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
	}
	

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	
	/** Method used when we use Profile in view, we send a 'safe' copy, without unnecessary data*/
	public void safeCopyFrom(Profile other){
		if (other.about != null)
			this.about = other.about;
		if (other.interests != null)
			this.interests = other.interests;
		if (other.getUser() != null){
			this.surName = other.getUser().getSurname();
			this.firstName = other.getUser().getFirstname();
		}
	}

	/** Sanitize the profile before we edit in DB */ 
	public void editProfileAbout(Profile webProfile, PolicyFactory htmlPolicy) {
		if (webProfile.about != null)
			this.about = htmlPolicy.sanitize(webProfile.about);
	}
	
	public void setPhotoDetailes(FileInfo fileInfo){
		
		photoDirectory = fileInfo.getSubDirectory();
		photoName = fileInfo.getBaseName();
		photoExtension = fileInfo.getExtension();
	}
	
	public Path getPhoto(String baseDirectory){
		if (photoName == null)
			return null;
		
		return Paths.get(baseDirectory, photoDirectory, photoName + "." + photoExtension); 
	}
	public void addInterest(Interest interest) {
		interests.add(interest);
	}
	/** Remove 'interestName from user list'*/
	public void deleteInterest(String interestName) {
		interests.remove(new Interest(interestName));
	}
}
