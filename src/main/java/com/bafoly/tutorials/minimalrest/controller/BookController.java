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

@Path("/book")
public class BookController {
	
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@GET
	@Produces(value=MediaType.APPLICATION_JSON)
	@JsonView(View.Base.class)
	public Response getBooks(){
		logger.info("get the list of books");

		List<Book> books = EntityManagerUtil.getEntityManager().createQuery("Select b from Book b", Book.class).getResultList();
		
		if(books!=null && books.size()>0){
			return Response.ok(books).build();
		}
		
		return Response.status(Status.NOT_FOUND).entity(new GenericResponse("book list is empty")).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(value=MediaType.APPLICATION_JSON)
	@JsonView(View.BookExtended.class)
	public Response getBook(@PathParam("id") long id){
		logger.info("get the book with id "+id);
		Book book = EntityManagerUtil.getEntityManager().find(Book.class, id);
		
		if(book!=null){
			return Response.ok(book).build();
		}
		
		return Response.status(Status.NOT_FOUND).entity(new GenericResponse("book "+id+" does not exist")).build();
	}
	
	@POST
	@JsonView(View.Base.class)
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response postBook(Book book){
		
		if(book.getAuthor()==null){
			return Response.status(Status.BAD_REQUEST).entity(new GenericResponse("Author is null. If you know the author id, post your request to /author/{{author_id}}/book")).build();	
		}
		
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		
		Author author = entityManager.find(Author.class, book.getAuthor().getId());
		
		entityManager.getTransaction().begin();
		
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
	public Response updateBook(@PathParam("id") long id, Book book){
		logger.info("updating the author: "+id);
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		
		Book inDB = entityManager.find(Book.class, id);
		
		if(inDB==null){
			return Response.status(Status.NOT_FOUND).entity(new GenericResponse("book "+id+" does not exist")).build();
		}
		
		inDB.setPublishYear(book.getPublishYear());
		inDB.setTitle(book.getTitle());
		inDB.setGenre(book.getGenre());
		
		entityManager.getTransaction().begin();
		
		entityManager.merge(inDB);
		
		entityManager.getTransaction().commit();
		
		return Response.ok(inDB).build();
	}
	
	
	@DELETE
	@Path("/{id}")
	@JsonView(View.Base.class)
	@Produces(value=MediaType.APPLICATION_JSON)
	public Response deleteBook(@PathParam("id") long id){
		logger.info("deleting the book: "+id);
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		
		Book inDB = entityManager.find(Book.class, id);
		
		if(inDB==null){
			return Response.status(Status.NOT_FOUND).entity(new GenericResponse("boook "+id+" does not exist")).build();
		}
		
		Author author = entityManager.find(Author.class, inDB.getAuthor().getId());
		
		entityManager.getTransaction().begin();
		
		author.getBooks().remove(inDB);
		
		entityManager.merge(author);
		
		entityManager.remove(inDB);
		
		entityManager.getTransaction().commit();
		
		return Response.status(Status.OK).entity(new GenericResponse("book "+id+" is deleted")).build();
	}
	
}
