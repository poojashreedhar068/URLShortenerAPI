# Origin Energy URL Shortener API

To develop a simple URL Shortener API

Requirements:

API Endpoints:

○ Create ShortURL:

■ POST /v1/url-shortener

Request body:json
{
"url": "string"
}

Response: HTTP 200 OK
Response body : json
{
"shortUrl": "string",
"errorMessage": {
"id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
"message": "string"
},
"created": "2025-09-30T11:40:51.953Z"
}


○ Retrieve Original URL:

■ GET /v1/url-shortener
Query Param :: shortURrl = "string"

Response: HTTP 200 OK
Response body : json
{
"originalUrl": "string",
"errorMessage": {
"id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
"message": "string"
},
"created": "2025-09-30T11:42:14.743Z"
}

○ Redirect to Original URL from shortURL:

■ GET /v1/url-shortener/{code}

Path Parameter :: code i.e alphanumeric code of shortened url

Response: Will be redirected to Original URL

## Authors

- [@poojashreedhar068](https://www.github.com/poojashreedhar068)


## PreRequisites

* Kotlin
* In-Memory Storage using json file
* Maven
* IntelliJ
* Docker if docker image needs to be generated

## Tech Stack

Framework : SpringBoot 3.*

Programming Language :  Kotlin

Log Framework : Logback with JSON Formatter

Monitoring tools : Actuator

OpenAPI : Swagger

Containerization : Docker


## Swagger Documentation

Swagger Documentation can be viewed on path :: /swaggerDoc 


## Used By

This project is used by the following:

- Pooja Shreedhar
- Origin Energy to access


## Deployment

```````To run on Intellij Or IDE```````````
* Run the main class UrlShortenerApplication.kt


```````To run on Tomcat```````````
* Install Required version tomcat on local
* build the jar file :: mvn clean install
* Jar would be generated on target folder
* Place Jar file on Tomcat/WEBAPPS folder
* Start tomcat using either command or .sh script


```Containerization````
* Generate Docker image using DockerFile provided
  docker build -t urlShortener:latest .

## Running Tests

Manual Test:

Swagger URL can be used to manual testing

Unit Test:

Unit tests can be run by mvn install or mvn test commands

Performance Test:

Jmeter or any other performance testing tool can be used to perform testing.


