package com.embedded;

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;

public class JettyStarter {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ProtectionDomain domain = JettyStarter.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(location.toExternalForm());

        server.setHandler(webapp);
        server.start();
        server.join();
    }
}
