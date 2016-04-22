package com.bafoly.tutorials.minimalrest.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtil {
	
	private static EntityManager entityManager;
	
	public static EntityManager getEntityManager(){
		if(entityManager==null){
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("minimalRest");
			entityManager = emf.createEntityManager();
		}
		return entityManager;
	}
	
}
