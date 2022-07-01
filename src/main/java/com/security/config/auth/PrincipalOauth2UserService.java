package com.security.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.security.auth.PrincipalDetails;
import com.security.auth.provider.GoogleUserInfo;
import com.security.auth.provider.NaverUserInfo;
import com.security.auth.provider.OAuth2UserInfo;
import com.security.model.User;
import com.security.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	private final Logger logger = LoggerFactory.getLogger(PrincipalOauth2UserService.class);
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// oauth 후처리 함수 구글 
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// TODO Auto-generated method stub
		
		logger.error(" OAuth2UserRequest = [{}]",userRequest.getClientRegistration());
		logger.error(" userRequest.getAccessToken().getTokenValue() = [{}]",userRequest.getAccessToken().getTokenValue());
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code 리턴 -> access token 요청
		//  userRequest 정보 -> loadUser 함수 -> 구글 프로필 정보를 받는다.
		logger.error(" super.loadUser(userRequest).getAttributes() = [{}]",super.loadUser(userRequest).getAttributes());
		// 자동회원 가입시 구글 어트리뷰터로 사용 
	/*
[{sub=109033658190920412584,
 name=Tech Hyun, 
 given_name=Tech, 
 family_name=Hyun, 
 picture=https://lh3.googleusercontent.com/a/AATXAJzJO8EiRdA1ysefGzfJk5qAQ8S-1BgQEYWjvuQw=s96-c,
 email=neosam32@gmail.com
 , email_verified=true
 , locale=ko}]
	 */
		// username = "google_109033658190920412584"
		// password  = "암호화(뭔데이)"
		// email        = "neosam32@gmail.com"
		// role          = "ROLE_USER"
		// provider    = "google"
		// prividerId  = "109033658190920412584"
		
		// 회원가입 강제 진행
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		logger.error(" oauth2User.getAttributes()"+oauth2User.getAttributes());
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if( userRequest.getClientRegistration().getRegistrationId().equals("google"))
		{
			logger.error(" 구글 로그인 요청");
			
			oAuth2UserInfo = new GoogleUserInfo( oauth2User.getAttributes() );
			
		}else if( userRequest.getClientRegistration().getRegistrationId().equals("naver"))
		{
			logger.error(" 네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo( oauth2User.getAttributes() );
		}
		
		String provider = oAuth2UserInfo.getProvider(); // 구글
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;
		String email =  oAuth2UserInfo.getEmail();
		String password = bCryptPasswordEncoder.encode("1111");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByEmail(email);
		
		logger.error(" userEntity = [{}]", userEntity);
		
		if( userEntity == null )
		{
			// 회원가입 
			userEntity = User.builder().username(username)
			.password(password)
			.provider(provider)
			.providerId(providerId)
			.email(email)
			.role(role)
			.build();
			logger.error(" userEntity = [{}]", userEntity);
			userRepository.save( userEntity  );
			
		}
		
		return new PrincipalDetails( userEntity , oauth2User.getAttributes() );
	}

}
