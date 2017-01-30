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
import org.springframework.stereotype.Service;

import com.whatever.model.SiteUser;
import com.whatever.model.TokenType;
import com.whatever.model.VerificationToken;
import com.whatever.model.Dao.UserDao;
import com.whatever.model.Dao.VerificationDao;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private VerificationDao verificationDao;
	
	public void register(SiteUser user){
		
		user.setRole("ROLE_USER");
		userDao.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		SiteUser user = userDao.findByEmail(email);
		
		if (user == null)
			return null; 
		
		List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
		String password = user.getPassword();
		
		Boolean enabled = user.getEnabled();
		
		return new User(email, password, enabled, true, true, true, auth);
	}
	
	public void save(SiteUser user){
		userDao.save(user);
	}
	
	public String createEmailVerificationToken(SiteUser user){
		/** generate random String*/
		VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user, TokenType.REGISTRATION);
		verificationDao.save(token);
		return token.getToken();
	}
	
	public VerificationToken getVerificationToken(String token){
		
		return verificationDao.findByToken(token);
	}

	public void deleteToken(VerificationToken token) {
		verificationDao.delete(token);
	}

	public SiteUser get(String userName) { /** Here userName = email*/
		return userDao.findByEmail(userName);
	}

	public SiteUser get(Long id) {
		return userDao.findOne(id);
	}
	
}
