package com.security.auth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

	private Map<String , Object > attributes;
	
	public NaverUserInfo( Map<String , Object > attributes  ) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		
		return (String)attributes.get("sub");
	}

	@Override
	public String getProvider() {
		
		return "google";
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return (String)attributes.get("email");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return(String)attributes.get("name");
	}
	
}