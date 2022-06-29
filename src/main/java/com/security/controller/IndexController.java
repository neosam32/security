package com.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.security.auth.PrincipalDetails;
import com.security.model.User;
import com.security.repository.UserRepository;

@Controller // view를 리턴한다.
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin( Authentication authentication , @AuthenticationPrincipal PrincipalDetails principalDetails2) {
		PrincipalDetails  principalDetails = (PrincipalDetails)authentication.getPrincipal();
		logger.error("principalDetails = [{}]", principalDetails.getUser() );
		
		logger.error(" userDetails2.getUser() = [{}]", principalDetails2.getUser() );
		return "세션정보확인";
	}
	
	@GetMapping("/test/auth/login")
	public @ResponseBody String testOauthLogin( Authentication authentication,@AuthenticationPrincipal  OAuth2User  oAuth2User2 ) {
		OAuth2User  oAuth2User = (OAuth2User)authentication.getPrincipal();
		logger.error("oAuth2User.getAttributes() principalDetails = [{}]", oAuth2User.getAttributes() );
		logger.error("oAuth2User2.getAttributes() principalDetails = [{}]", oAuth2User2.getAttributes() );
		return "auth 세션정보확인";
	}
	
	@GetMapping({"","/"})  
	public String index() {
		// 머스테치 기본폴더 src/main/resource
		// prefix : /templates
		// suffix : .mustache
		// src/main/resource/templates/index.mustach --> 
		logger.error("  index START" );
		
		return "index";
	}
	
	
	// OAuth 로그인 해도 PrincipalDetails 
	// 일반로그인 해도 PrincipalDetails 
	@GetMapping("/user")
	public @ResponseBody String  user( @AuthenticationPrincipal PrincipalDetails principalDetails  )
	{
		logger.error(" principalDetails.getUser() " + principalDetails.getUser() );
		logger.error(" principalDetails.getAttributes() = " + principalDetails.getAttributes());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String  admin()
	{
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String  manager()
	{
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String  loginForm()
	{
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public  String  joinForm()
	{
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String  join( User user )
	{
		logger.error(" join START = [{}]" , user );
		user.setRole("ROLE_USER");
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save( user  );
        // redirect 는 /loginForm 이라는 함수 호출 함 .		
		return "redirect:/loginForm";
 	}
	
	@Secured("ROLE_ADMIN")  // 메소드 하나만 걸때 쓰면 됨.
	@GetMapping("/info")
	public @ResponseBody String info()
	{
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MANAGER')")
	@GetMapping("/data")
	public @ResponseBody String data()
	{
		return "data";
	}
}
