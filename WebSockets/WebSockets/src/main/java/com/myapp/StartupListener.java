package com.myapp;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class StartupListener implements jakarta.servlet.ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
	new EventGenerator().start();
    }

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}
}
