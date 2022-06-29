package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.security.config.auth.PrincipalOauth2UserService;



@EnableWebSecurity // 시큐리티 필터 추가 , 스프링 시큐리티가 활성화가 되어 있는데 어떤 설정을 해당파일에서 하겠다.
@EnableGlobalMethodSecurity( securedEnabled = true , prePostEnabled = true )  // 시큐어 어노테이션 활성화 , preAutorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
//
////	@Autowired
////	private PrincipalDetailService principalDetialService; 
// 
    @Bean // 해당메서드의 오브젝트가  Ioc가 됨.
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    PrincipalOauth2UserService principalOauth2UserService;
//    
////    // 시큐리티가 대신 로그인해주는데 뭘로 해쉬가 되어 회원가입이 되었는지 알아야 같은 해쉬로 
////    // 암호화 해서 db에 있는 해쉬랑 비교할 수 있음.
////    @Override
////    protected void configure( AuthenticationManagerBuilder auth ) throws Exception{
////    	auth.userDetailsService(principalDetialService).passwordEncoder(encodePWD());
////    }
//    
// 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http .csrf().disable()  // csrf 토큰 비활성화
             .authorizeRequests()
             .antMatchers("/user/**").authenticated()
             .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
             .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
             .anyRequest().permitAll()
         .and()
            .formLogin()
            .loginPage("/loginForm")
            .usernameParameter("email")  // default username 을 다른 이릉으로 변경 할때 사용 
            .loginProcessingUrl("/login")  // /login 주소가 호출되면 시큐리티가 낚아 채서 대신 로그인을 진행한다.
            .defaultSuccessUrl("/")
            .and()
            .oauth2Login()
            .loginPage("/loginForm") // 구글 로그인이 된후의 후처리 로직이 필요함.
            .userInfoEndpoint()
            .userService(principalOauth2UserService)
              ; 
             // 1. 코드 받기 ( 인증 ) 2. 엑세스 토큰 ( 사용자 정보 접근 권한 ) , 3. 사용자 프로필 정보 가져옴 . 4. 정보를 토대로 자동
             // 회원 가입 혹은 정보가 추가 구성이 필요 하다면 추가 회원 가입창이 나와서 회원가입 
             // 구글로그인이 완료되면 ( 엑세스토큰 + 사용자 프로필 ) 이 함께옴.
 

    }
    
	
}