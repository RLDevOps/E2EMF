package com.relevance.e2emf.domain;
/**
 * @author emanuel
 * E2emf Profile Business Object  
 *
 */
public class Profile extends E2emfBusinessObject{
	
	public static String profileData;
	
	public String profileInfo;

	public static String getProfileData() {
		return profileData;
	}

	public static void setProfileData(String profileData) {
		Profile.profileData = profileData;
	}

	public String getProfileInfo() {
		return profileInfo;
	}

	public void setProfileInfo(String profileInfo) {
		this.profileInfo = profileInfo;
	}
	
	

}
