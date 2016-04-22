package com.bafoly.tutorials.minimalrest.entity;

import com.fasterxml.jackson.annotation.JsonView;

public class GenericResponse {
	
	@JsonView(View.Base.class)
	String error;

	public GenericResponse(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
