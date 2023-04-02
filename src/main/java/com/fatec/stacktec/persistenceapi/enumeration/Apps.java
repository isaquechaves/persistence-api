package com.fatec.stacktec.persistenceapi.enumeration;

import lombok.Getter;

@Getter
public enum Apps {
	
	ADMIN("Admin", "Admin", "AD", "admin.stacktec.com.br", null);
	
	private String label;
	private String shortName;
	private String abbreviation;
	private String url;
	private String icon;
	
	Apps(String label, String shortName, String abbreviation, String url, String icon){
		this.label = label;
		this.shortName = shortName;
		this.abbreviation = abbreviation;
		this.url = url;
		this.icon = icon;
	}
	
	public String getUrl() {
		return "https://" + (EnvironmentType.myPrefix() != null ? EnvironmentType.myPrefix() + "-" : "") + url;
	}
	
	public static Apps valueOf(Integer ordinal) {
		if(ordinal != null) {
			for(Apps apps : Apps.values()) {
				if(apps.ordinal() == ordinal) {
					return apps;
				}
			}
		}
		return null;
	}
	
}
