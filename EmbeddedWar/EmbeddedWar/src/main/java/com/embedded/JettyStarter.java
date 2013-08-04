package com.embedded;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;


public class JettyStarter {
	public static void main(String[] args) throws Exception{

        ProtectionDomain domain = JettyStarter.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();
        
        // create a web app and configure it to the root context of the server
        WebAppContext webapp = new WebAppContext();
        webapp.setDescriptor("WEB-INF/web.xml");
        webapp.setConfigurations(new Configuration[]{ new AnnotationConfiguration() 
        		, new WebXmlConfiguration(), new WebInfConfiguration(), new MetaInfConfiguration()        		
        		//, new FragmentConfiguration(), new EnvConfiguration(), new PlusConfiguration()
        	});
        webapp.setContextPath("/");
        webapp.setWar(location.toExternalForm());
        
        // starts the embedded server and bind it on port

        //$OPENSHIFT_DIY_IP:$OPENSHIFT_DIY_PORT
        String host = System.getenv("OPENSHIFT_DIY_IP");
        if(host==null){
        	host = "127.0.0.1";
        }

        String port = System.getenv("OPENSHIFT_DIY_PORT");
        if(port==null){
        	port = "8080";
        }
        System.out.println(host + ":" + port);
        
        InetSocketAddress sa = new InetSocketAddress(InetAddress.getByName(host), Integer.valueOf(port));
		Server server = new Server(sa);
        server.setHandler(webapp);        
        server.start();
        server.join();	
	}
}
