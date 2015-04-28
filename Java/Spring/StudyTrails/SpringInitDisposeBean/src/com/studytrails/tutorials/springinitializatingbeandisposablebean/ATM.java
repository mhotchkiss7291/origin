package com.studytrails.tutorials.springinitializatingbeandisposablebean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ATM implements InitializingBean, DisposableBean {

	public void afterPropertiesSet() throws Exception {
		System.out
				.println("ATM's initialization method called. Connecting to bank's network.");

	}

	public void destroy() throws Exception {
		System.out
				.println("ATM's destroy method called. Disconnecting from bank's network.");

	}

}