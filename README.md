# myretail case study

myRetail is a rapidly growing company with HQ in Richmond, VA and over 200 stores across the east coast. myRetail wants to make its internal data available to any number of client devices, from myRetail.com to native mobile apps. 

The goal for this exercise is to create an end-to-end Proof-of-Concept for a products API, which will aggregate product data from multiple sources and return it as JSON to the caller. 
Your goal is to create a RESTful service that can retrieve product and price details by ID. The URL structure is up to you to define, but try to follow some sort of logical convention.

Build an application that performs the following actions: 
Responds to an HTTP GET request at /products/{id} and delivers product data as JSON (where {id} will be a number. 
Example product IDs: 15117729, 16483589, 16696652, 16752456, 15643793) 
Example response: {"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}

Performs an HTTP GET to retrieve the product name from an external API. (For this exercise the data will come from redsky.target.com, but let’s just pretend this is an internal resource hosted by myRetail)  
Example: http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics

Reads pricing information from a NoSQL data store and combines it with the product id and name from the HTTP request into a single response. 

BONUS: Accepts an HTTP PUT request at the same path (/products/{id}), containing a JSON request body similar to the GET response, and updates the product’s price in the data store. 

*****************************************************************************************************************************


Please be prepared to talk about:
•    What made you pick this code?
•    Why did you choose this framework?
•    How did your solution for this problem evolve over time?
•    What was the biggest challenge related to solving this problem?Please ensure you submit several examples so that we can see  the following in your code submissions:
•    Completeness:  Were problems addressed, did the code run?  Is it production ready (if not, explain why)
•    Do you have any examples of design work?
•    Have you done any work where you have had to scale your code?
•    Testing—how did you test?

*****************************************************************************************************************************

# myretail solution

My solution uses two microservices. For that here are some things I considered:

1. The redsky service is internal. The solution assumes that it is "secure" since it could be deployed in a VPC that isn't available to the outside world. Since I can't pass credentials to it directly, I wrapped it in a service called product-name-service to simulate the security solution. Users cannot call this service directly.

2. The product gateway service exposes APIs to allow for PUT and GET requests. Since it is the gateway, it makes the calls to the product-name-service. This service also directly persists data using a nosql database. 


## Project setup
I created three projects. 
•   discovery-service - this is a Netflix Eureka server for service discovery
•   product-name-service - this is a service that calls on the redsky service directly. This accepts GET requests from the gateway.
•   product-gateway - this is a service accepts the PUT and GET request from the user.


To create these projects, I used httpie (https://httpie.org/) and start.spring.io REST API: 

http https://start.spring.io/starter.zip javaVersion==11 \
  artifactId==discovery-service name==eureka-service \
  dependencies==cloud-eureka-server baseDir==discovery-service | tar -xzvf -

http https://start.spring.io/starter.zip \
  artifactId==product-name-service name==product-name-service baseDir==product-name-service \
  dependencies==actuator,cloud-eureka,cloud-feign,data-jpa,h2,data-rest,web,devtools,lombok | tar -xzvf -

http https://start.spring.io/starter.zip \
  artifactId==product-gateway name==product-gateway baseDir==product-gateway \
  dependencies==cloud-eureka,cloud-feign,data-jpa,h2,data-rest,web,cloud-hystrix,lombok | tar -xzvf -
  
  

## Tech stack

###Spring Boot: 
	https://start.spring.io/
  Includes Maven (wrapper), Mockito, code convenience like Lombok, other dependencies.

###Service discovery with Netflix Eureka
https://cloud.spring.io/spring-cloud-netflix/
  
###Feign:
Feign is a declarative REST Client, using tools like Jersey and CXF to write java clients for ReST or SOAP services.
https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html
https://github.com/OpenFeign/feign
 
###Hystrix:
Hystrix makes it possible to add failover capabilities to the Feign clients we'll be using.
https://github.com/Netflix/Hystrix

###MongoDB:
Our nosql document database
https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/#install
  
###Testing using Mokito, Junit and Postman
https://site.mockito.org/
https://www.postman.com/


## Application flow
The product-gateway uses Feign to talk to the downstream product-name-service. If the product-name-service is not available, it will use the failover method provided using Hystrix. 

Likewise, If the product-name-service is not able to talk to the redsky service, it will use the failover method provided using Hystrix so it can return a generic name. 




## security using okta
I’ve already configured security in this microservices architecture using OAuth 2.0 and OIDC. What’s the difference between the two? OIDC is an extension to OAuth 2.0 that provides identity. It also provides discovery so all the different OAuth 2.0 endpoints can be discovered from a single URL (called an issuer).




One of the things you might’ve noticed in this example is you had to configure the OIDC properties in each application. This could be a real pain if you had 500 microservices. Yes, you could define them as environment variables and this would solve the problem. However, if you have different microservices stacks using different OIDC client IDs, this approach will be difficult. For that, a solution called Spring Cloud Config can be used to configure distributed systems. 



## zuul






