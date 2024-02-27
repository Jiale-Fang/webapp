# CSYE 6225
Update Readme
Brief introduction of what your project is about.

## Prerequisites

Before building and deploying this application locally, make sure you have the following prerequisites:

- Java JDK or JRE (version 1.8 or higher): Required for compiling and running the Spring Boot application.
- Maven: Required for building the project and managing dependencies.
- Mysql (version 8.x.x): database

## Build Instructions

- source ~/.bash_profile   
- mvn clean install -Djasypt.encryptor.password="your password"

## Deploy Instructions

- java -Djasypt.encryptor.password="your password" -jar xxx.jar
