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
	@Size(max=5000, message="{editprofile.about.size}") //setam un numar maxim de caractere (care include si tagurile html) si un mesaj de eroare
	private String about;
	
	@Column(name="photo_directory", length=10)
	private String photoDirectory;
	
	@Column(name="photo_name", length=20)
	private String photoName;
	
	@Column(name="photo_extension", length=5)
	private String photoExtension;
	
	/** Not ok to use EAGER when we are PAGING profiles on a @OneToMany or @ManyToMany*/
	@ManyToMany(fetch=FetchType.EAGER)//Eager inseamna ca atunci cand citim profilul unui user, se va citii si interest
	//FetchType.LAZY by default inseamna ca Interest se va citii doar cand avem nevoie de el si nu deodata cu profilul
	// sau putem face o enitate(tabel) noua "profile_interests" si atunci avem @OneToMany si @OneToMany
	//facem join la tabelul "profile" cu "interests" in noul tabel "profile_interests" dupa coloanele "id" respectiv "id" si le numim : "profile_id" si "interest_id"
	@JoinTable(name="profile_interests", joinColumns={@JoinColumn(name="profile_id", referencedColumnName = "id")}, 
										 inverseJoinColumns={@JoinColumn(name="interest_id", referencedColumnName = "id")})
	//@OrderColumn(name="display_order")//Mai cream o coloana in care avem lista de interese ordonata (optional)-> Nu functioneaza 
	private Set<Interest> interests;
	
	@Transient
	private String firstName;
	
	@Transient
	private String surName;
	
	//For testing purpose
	public Profile(){
		
	}
	//For testing purpose
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
	//Facem o metoda care copiaza DOAR "other" adica textul profilului fara alte date "confidentiale" intr-un alt nou profil
	//pentru a elimina posibilitatea de a gresii cand lucram cu .jsp si a afisa aceste date in browser
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

	//Salvam un profil care este html "sanitize" 
	public void editProfileAbout(Profile webProfile, PolicyFactory htmlPolicy) {
		if (webProfile.about != null)
			this.about = htmlPolicy.sanitize(webProfile.about);
	}
	
	//facem o metoda care ne seteaza cele 3 variabile ale imaginii
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
	public void deleteInterest(String interestName) {
		interests.remove(new Interest(interestName)); //Stergem InterestName din lista userului de interest nu din Tabelul de Interest
	}
}
