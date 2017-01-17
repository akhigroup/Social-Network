package com.whatever.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.whatever.model.SiteUser;
import com.whatever.model.TokenType;
import com.whatever.model.VerificationToken;
import com.whatever.model.Dao.UserDao;
import com.whatever.model.Dao.VerificationDao;

@Service //pentru ca Spring sa creeze o instanta a acestei clase
public class UserService implements UserDetailsService{
	
	@Autowired //Spring vede adnotatia si merge la UserDao unde vede @Repository , creeaza o instanta a clasei UserDao si o aduce aici
	private UserDao userDao;
	
	@Autowired
	private VerificationDao verificationDao;
	
	public void register(SiteUser user){
		
		user.setRole("ROLE_USER"); //Setam pt toti, cand is creeaza cont, la "role"=ROLE_USER
		//user.setPassword(passwordEncoder.encode(user.getPassword())); //luam parola, o encodam, si o setam in user
		//Encodam parola direct in SiteUser
		userDao.save(user);
	}

	@Override //implementam UserDetailsService si asta ne obliga sa Overridam metoda loadUserByUsername
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {//returnam un Spring User Object
		
		SiteUser user = userDao.findByEmail(email);
		
		if (user == null)//daca nu gasim userul returnam null
			return null; 
		
		List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
		String password = user.getPassword();
		
		Boolean enabled = user.getEnabled();
		
		return new User(email, password, enabled, true, true, true, auth); //User este un obiect Spring catre care transmitem parametrii email, password si auth (rolul userului)
		//daca "enable==false" atunci Userul nu are permisiunea sa se logheze
	}
	
	public void save(SiteUser user){
		
		userDao.save(user);
	}
	
	public String createEmailVerificationToken(SiteUser user){
		
		VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user, TokenType.REGISTRATION);//Genereaza un random STRING
		verificationDao.save(token);
		return token.getToken();
	}
	
	public VerificationToken getVerificationToken(String token){
		
		return verificationDao.findByToken(token);
	}

	public void deleteToken(VerificationToken token) {
		verificationDao.delete(token);
	}

	public SiteUser get(String userName) { //UserName este de fapt email
		
		return userDao.findByEmail(userName);
	}

	public SiteUser get(Long id) {
		return userDao.findOne(id);
	}
	
}
