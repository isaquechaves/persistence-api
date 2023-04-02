package com.fatec.stacktec.persistenceapi.enumeration;


import org.springframework.core.env.Environment;

import com.fatec.stacktec.searchapi.util.BeanUtil;

import lombok.Getter;

@Getter
public enum EnvironmentType {
	
	LOCAL("local"),
	TEST("test"),
	QA("qa"),
	PRODUCTION("www");
	
	private String prefix;
	private static final String ENVIRONMENT_ATTR = "environment";
	private static final String SERVERPORT_ATTR = "server.port";
	
	EnvironmentType(String prefix){
		this.prefix = prefix;
	}
	
	public static EnvironmentType getDefault() {
		return EnvironmentType.LOCAL;
	}
		
	public static String myPrefix(){
		return myEnvironment().getPrefix();
	}
	
	public static EnvironmentType myEnvironment() {
		Environment environmentBean = BeanUtil.getBean(Environment.class);
		String environmentStr = environmentBean.getProperty(ENVIRONMENT_ATTR);
		if(environmentStr == null || environmentStr.isEmpty())
			environmentStr = EnvironmentType.getDefault().name();
		return EnvironmentType.valueOf(environmentStr.toUpperCase());
	}
	
	public static String myURL() {
		if(myEnvironment() == LOCAL) {
			String serverPort = "";
			Environment environmentBean = BeanUtil.getBean(Environment.class);
			serverPort = environmentBean.getProperty(SERVERPORT_ATTR);
			return myPrefix() + ".stacktec.com.br" + (serverPort != null && !serverPort.isEmpty() ? ":" : "" + serverPort);
		}
		return "https://" + myPrefix() + ".stacktec.com.br";
	}
}

