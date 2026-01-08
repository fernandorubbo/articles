package com.myapp;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class StartupListener implements jakarta.servlet.ServletContextListener {

    private EventGenerator eventGenerator;

    public void contextInitialized(ServletContextEvent sce) {
        eventGenerator = new EventGenerator();
        eventGenerator.start();
    }

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (eventGenerator != null) {
            eventGenerator.interrupt();
        }
	}
}
