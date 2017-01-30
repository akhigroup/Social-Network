package com.whatever.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.whatever.validation.PasswordMatch;

@Entity
@Table(name="user")
@PasswordMatch(message="{register.repeatpassword.mismatch}")/** Custom annotation*/
public class SiteUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="firstname", length=25)
	@Size(min=2, max=25, message="{register.firstname.size}")
	private String firstname;
	
	@Column(name="surname", length=25)
	@Size(min=2, max=25, message="{register.surname.size}")
	private String surname;
	
	
	@Column(name="email", unique=true)
	@Email(message="{register.email.invalid}")
	@NotBlank(message="{register.email.invalid}")
	private String email;
	
	@Transient
	@Size(min=5, max=25, message="{register.password.size}")
	private String plainPassword;
	
	@Transient
	private String repeatPassword;
	
	@Column(name="password")
	private String password;
	
	@Column(name="role")
	private String role;
	
	@Column(name="enabled")
	private boolean enabled = false;
	
	public SiteUser(){
		
	}
	/** For testing purpose */
	public SiteUser(String firstname, String surname, String email, String password){
		this.firstname = firstname;
		this.surname = surname;
		this.email = email;
		this.repeatPassword = password;
		this.setPlainPassword(password);
		this.enabled = true;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.password = new BCryptPasswordEncoder().encode(plainPassword);
		this.plainPassword = plainPassword;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "SiteUser [id=" + id + ", firstname=" + firstname + ", surname=" + surname + ", email=" + email
				+ ", plainPassword=" + plainPassword + ", repeatPassword=" + repeatPassword + ", password=" + password
				+ ", role=" + role + ", enabled=" + enabled + "]";
	}
	
	
}
