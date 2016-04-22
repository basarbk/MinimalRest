# Lightweight single JAR application as a REST backend server

MinimalRest is a simple project to make an executable jar application to provide REST service.

In this project, 
* [Jetty](#http://www.eclipse.org/jetty/) is used as server.
* [Jersey](#https://jersey.java.net/) will be handling the rest requests.
* We are going to store the data in memory [H2 database](#http://h2database.com).
* Also we use [Hibernate](#http://hibernate.org/) to store/retrieve entites to/from H2 database.

> **Blog:** A step by step explanation of the project can be founded at my blog http://blog.bafoly.com