package com.bafoly.tutorials.minimalrest.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Book {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(View.Base.class)
	long id;
	
	@JsonView(View.Base.class)
	String title;

	@JsonView(View.Base.class)
	Genre genre;
	
	@JsonView(View.Base.class)
	int publishYear;
	
	@ManyToOne
	@JsonView(View.BookExtended.class)
	Author author;
	
	public Book() {
		super();
	}

	public Book(String title, Genre genre, int publishYear) {
		super();
		this.title = title;
		this.genre = genre;
		this.publishYear = publishYear;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public int getPublishYear() {
		return publishYear;
	}

	public void setPublishYear(int publishYear) {
		this.publishYear = publishYear;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	

}
