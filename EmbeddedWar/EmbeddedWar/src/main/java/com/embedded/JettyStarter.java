package com.embedded;

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class JettyStarter {
    public static void main(String[] args) throws Exception {
        String host = System.getenv("OPENSHIFT_DIY_IP");
        String portStr = System.getenv("OPENSHIFT_DIY_PORT");
        int port = portStr == null ? 8080 : Integer.parseInt(portStr);

        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        if (host != null) {
            connector.setHost(host);
        }
        connector.setPort(port);
        server.addConnector(connector);

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
