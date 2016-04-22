package com.bafoly.tutorials.minimalrest;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;

import com.bafoly.tutorials.minimalrest.entity.Author;
import com.bafoly.tutorials.minimalrest.entity.Book;
import com.bafoly.tutorials.minimalrest.entity.Genre;
import com.bafoly.tutorials.minimalrest.jpa.EntityManagerUtil;

public class App {
	
	protected final static Logger logger = LogManager.getLogger("App");
	
	static final int port = 7070;
	static final String h2WebPort = "7071";
	
    public static void main( String[] args ) throws Exception{

    	// start the web interface of H2 Database
    	// web interface can be reached by http://localhost:7071
    	org.h2.tools.Server.createWebServer(new String[]{"-web","-webAllowOthers","-webPort",h2WebPort}).start();
    	
    	// create servlet context
    	ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    	servletContextHandler.setContextPath("/");
    	
    	// create jetty server
    	Server jettyServer = new Server(port);
    	jettyServer.setHandler(servletContextHandler);
    	
    	// add the jersey servlet to jetty servlet context
    	ServletHolder jerseyServlet = servletContextHandler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
    	
    	// make sure jersey to scan the packages of your project
    	jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.bafoly.tutorials.minimalrest");
    	// adding jackson to convert responses to JSON format
    	jerseyServlet.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, "org.glassfish.jersey.jackson.JacksonFeature");
    	
	 	// create initial database content
    	generateInitialContent();
    	
		try {
			// jetty is started here
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
		
   
    }
    
    private static void generateInitialContent(){
    	
    	logger.info("lets add some entities");
    	
    	EntityManager entityManager = EntityManagerUtil.getEntityManager();
    	
    	// before persisting an object to db, starting the transaction
    	entityManager.getTransaction().begin();
    	
    	// first entity
    	Author author1 = new Author("Fyodor", "Dostoyevsky");

    	// persisting it to database
    	entityManager.persist(author1);
    	
    	// adding books
    	Book book1 = new Book("Crime and Punishment", Genre.NOVEL, 1866);
    	
    	// making the relation between entities
    	book1.setAuthor(author1);
    	
    	entityManager.persist(book1);
    	
    	// second book with same steps
    	Book book2 = new Book("The Brothers Karamazov", Genre.NOVEL, 1880);
    	book2.setAuthor(author1);
    	entityManager.persist(book2);
    	
    	// we also need to make sure these books are added to the book list of author
    	author1.getBooks().add(book1);
    	author1.getBooks().add(book2);
    	
    	// updating the author1 entity with the book information.
    	entityManager.merge(author1);
    	
    	
    	// adding another author and book
    	Author author2 = new Author("Friedrich","Nietzsche");
    	entityManager.persist(author2);
    	
    	Book book3 = new Book("Thus Spoke Zarathustra", Genre.PHILOSOPHICAL_NOVEL, 1883);
    	book3.setAuthor(author2);
    	entityManager.persist(book3);
    	
    	author2.getBooks().add(book3);
    	entityManager.merge(author2);
    	
    	
    	entityManager.getTransaction().commit();
    }
    
}
