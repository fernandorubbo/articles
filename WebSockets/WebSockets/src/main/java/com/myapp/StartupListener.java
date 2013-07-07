package com.myapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;


@WebListener 
public class StartupListener implements javax.servlet.ServletContextListener {
	
    public void contextInitialized(ServletContextEvent sce) {
    	new EventGenerator().start();
    }

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub		
	}
}
