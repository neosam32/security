package com.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.model.User;
import com.security.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")
// 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행됨.

@Service
public class PrincipalDetailsService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(PrincipalDetails.class);
	
	@Autowired
	private UserRepository userRepository; 

	// 시큐리티 세션 = session( 내부 Authenticaion (내부  userDetails ) 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.error(" loadUserByUsername username =[{}] " , username );
		User userEntity = userRepository.findByEmail( username );
		
		if( userEntity != null )
		{
			return new PrincipalDetails( userEntity );
		}
		
		return null;
	}
}
