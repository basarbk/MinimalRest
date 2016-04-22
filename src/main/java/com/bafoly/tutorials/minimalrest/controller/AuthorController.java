package com.bafoly.tutorials.minimalrest.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bafoly.tutorials.minimalrest.entity.Author;
import com.bafoly.tutorials.minimalrest.entity.Book;
import com.bafoly.tutorials.minimalrest.entity.GenericResponse;
import com.bafoly.tutorials.minimalrest.entity.View;
import com.bafoly.tutorials.minimalrest.jpa.EntityManagerUtil;
import com.fasterxml.jackson.annotation.JsonView;

@Path("/author")
public class AuthorController {

	protected final Logger logger = LogManager.getLogger(this.getClass());
	
	@GET
	@Produces(value=MediaType.APPLICATION_JSON)
	@JsonView(View.Base.class)
	public Response getAuthors(){
		logger.info("get the list of authors");

		List<Author> authors = EntityManagerUtil.getEntityManager().createQuery("Select a from Author a", Author.class).getResultList();
		
		if(authors!=null && authors.size()>0){
			return Response.ok(authors).build();
		}
		
		return Response.status(Status.NOT_FOUND).entity(new GenericResponse("author list is empty")).build();
	}

	@GET
	@Path("/{id}")
	@Produces(value=MediaType.APPLICATION_JSON)
	@JsonView(View.AuthorExtended.class)
	public Response getAuthor(@PathParam("id") long id){
		logger.info("get the author whose id is "+id);
		Author author = EntityManagerUtil.getEntityManager().find(Author.class, id);
		
		if(author!=null){
			return Response.ok(author).build();
		}
		
		return Response.status(Status.NOT_FOUND).entity(new GenericResponse("author "+id+" does not exist")).build();
	}
	
	@POST
	@JsonView(View.Base.class)
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response postAuthor(Author author){
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		
		entityManager.getTransaction().begin();
		
		entityManager.persist(author);
		
		entityManager.getTransaction().commit();
		
		return Response.ok(author).build();
	}
	
	@POST
	@Path("/{id}/book")
	@JsonView(View.BookExtended.class)
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response postBook(@PathParam("id") long id, Book book){
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		
		Author author = entityManager.find(Author.class, id);
		if(author == null){
			return Response.status(Status.NOT_FOUND).entity(new GenericResponse("author "+id+" does not exist")).build();
		}
		
		entityManager.getTransaction().begin();
		
		book.setAuthor(author);
		
		entityManager.persist(book);
		
		author.getBooks().add(book);
		entityManager.merge(author);
		
		entityManager.getTransaction().commit();

		return Response.ok(book).build();
	}
	
	
	@PUT
	@Path("/{id}")
	@JsonView(View.Base.class)
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response updateAuthor(@PathParam("id") long id, Author author){
		logger.info("updating the author: "+id);
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		
		Author inDB = entityManager.find(Author.class, id);
		
		if(inDB==null){
			return Response.status(Status.NOT_FOUND).entity(new GenericResponse("author "+id+" does not exist")).build();
		}
		
		inDB.setName(author.getName());
		inDB.setLastname(author.getLastname());
		
		entityManager.getTransaction().begin();
		
		entityManager.merge(inDB);
		
		entityManager.getTransaction().commit();
		
		return Response.ok(inDB).build();
	}
	
	
	@DELETE
	@Path("/{id}")
	@JsonView(View.Base.class)
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response deleteAuthor(@PathParam("id") long id){
		logger.info("deleting the author: "+id);
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		
		Author inDB = entityManager.find(Author.class, id);
		
		if(inDB==null){
			return Response.status(Status.NOT_FOUND).entity(new GenericResponse("author "+id+" does not exist")).build();
		}
		
		entityManager.getTransaction().begin();
		
		entityManager.remove(inDB);
		
		entityManager.getTransaction().commit();
		
		return Response.status(Status.OK).entity(new GenericResponse("author "+id+" is deleted")).build();
	}
	
	
}
