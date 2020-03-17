# MyRetail case study

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

•    Have you done any work where you have had to scale your code?

•    Testing—how did you test?

*****************************************************************************************************************************

# MyRetail solution

My solution uses two microservices. For that here are some things I considered:

1. The redsky service is internal. The solution assumes that it is "secure" since it could be deployed in a VPC that isn't available to the outside world. Since I can't pass credentials to it directly, I wrapped it in a service called product-name-service to simulate the security solution. Users cannot call this service directly.

2. The product gateway service exposes APIs to allow for PUT and GET requests. Since it is the gateway, it makes the calls to the product-name-service. This service also directly persists data using a nosql database. Dummy data is written to the Mongodb instance during application startup. 


## Project setup
I created three projects. 

•   discovery-service - this is a Netflix Eureka server for service discovery

•   product-name-service - this is a service that calls on the redsky service directly. This accepts GET requests from the gateway.

•   product-gateway - this is a service accepts the PUT and GET request from the user. 



To create these projects, I used httpie (https://httpie.org/) and start.spring.io REST API: 
```
http https://start.spring.io/starter.zip javaVersion==11 \
  artifactId==discovery-service name==eureka-service \
  dependencies==cloud-eureka-server baseDir==discovery-service | tar -xzvf -

http https://start.spring.io/starter.zip \
  artifactId==product-name-service name==product-name-service baseDir==product-name-service \
  dependencies==actuator,cloud-eureka,cloud-feign,data-jpa,h2,data-rest,web,devtools,lombok | tar -xzvf -

http https://start.spring.io/starter.zip \
  artifactId==product-gateway name==product-gateway baseDir==product-gateway \
  dependencies==cloud-eureka,cloud-feign,data-jpa,h2,data-rest,web,cloud-hystrix,lombok | tar -xzvf -
```
  

## Tech stack

### Spring Boot: 
https://start.spring.io/
Includes Maven (wrapper), Mockito, code convenience like Lombok, other dependencies.

### Service discovery with Netflix Eureka
https://cloud.spring.io/spring-cloud-netflix/
  
### Feign:
Feign is a declarative REST Client, using tools like Jersey and CXF to write java clients for ReST or SOAP services.
https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html

https://github.com/OpenFeign/feign
 
### Hystrix:
Hystrix makes it possible to add failover capabilities to the Feign clients we'll be using.
https://github.com/Netflix/Hystrix

### MongoDB:
Our nosql document database which is HA
https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/#install
The app will be using the default, unsecure connection below:
spring.data.mongodb.uri=mongodb://localhost:27017/productsdb

productsdb was created suing the mongo CLI
  
### Testing using Mokito, Junit and Postman
https://site.mockito.org/

https://www.postman.com/

### Zuul
https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html



## Application flow
The product-gateway uses Feign to talk to the downstream product-name-service. If the product-name-service is not available, it will use the failover method provided using Hystrix. 

Likewise, If the product-name-service is not able to talk to the redsky service, it will use the failover method provided using Hystrix so it can return a generic name. 

```
REQUEST     to    product-gateway       to        product-name-service
------------------------------------------------------------------------------
GET               /products/{id}                  /product-names/{id}      

PUT               /products/{id}                  none
                  {Product in Request Body}
```

In each project path, run each service (./mvnw spring-boot:run)
Verify through Eureka that both services are alive:
http://localhost:8761/



## Security using okta
I’ve configured security in this microservices architecture using OAuth 2.0 and OIDC thru Okta.

Log in to your Okta Developer account (or sign up https://developer.okta.com/signup/ if you don’t have an account).

1. From the Applications page, choose Add Application.

2. On the Create New Application page, select Web.

3. Give your app a name, add http://localhost:8080/login/oauth2/code/okta as a Login redirect URI, select Refresh Token (in addition to Authorization Code), and click Done. 

You'll have to add https://getpostman.com/oauth2/callback as well to allow for Postman to call on your service. 

![Image description](https://github.com/gonziilla/myretail/blob/master/images/Okta%20-%20App%20setup.png)

In case you need to get the Auth URL and Access token URL for testing, go to your default authorization server:
https://{yourdomain}.okta.com/oauth2/default/.well-known/oauth-authorization-server

4. Once the Application is created, add yourself as a member of the application in the Assignments tab. 

5. You'll need to update the applications.properties files in both service projects. Information about the issuer, client id and client secret will be under API -> Authorization servers. 

```
okta.oauth2.issuer=$issuer
okta.oauth2.client-id=$clientId
okta.oauth2.client-secret=$clientSecret
```


### App configuration

The ProductGatewayApplication.java has Spring security configuration to enable OAuth 2.0 login and to enable the gateway as a resource server:
```
@Configuration
static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests().anyRequest().authenticated()
                .and()
            .oauth2Login()
                .and()
            .oauth2ResourceServer().jwt();
        // @formatter:on
    }
}
```

The product-name-service application will not have OAuth 2.0 login enabled but will just be a resource server. 
In order for the gateway to pass along the access token to the name service, a request interceptor is created (see UserFeignClientInterceptor.java) and the following properties were turned on:

```
feign.hystrix.enabled=true
hystrix.shareSecurityContext=true
```
Just to test that the credentials are passed on to the downstream name service, I added a Zuul route to product-name-service's HomeController (/home).  

http://localhost:8080/home



### Testing
To test with Postman, an access token request has to be made. 
![Image description](https://github.com/gonziilla/myretail/blob/master/images/Postman%20Access%20Token%20Request.png)

This should launch the Okta login page. Click Use Token once the token is received. 

Using the token requested, a simple GET request is done as follows

![Image description](https://github.com/gonziilla/myretail/blob/master/images/Postman%20GET.png)

For PUT requests, Product information related to price should be included in the payload in the following format:
```
{
    "currentPrice": {
        "currencyCode": "USD",
        "value": "100000.49"
    }
}
```
This performs an INSERT if the document is not existing. 

![Image description](https://github.com/gonziilla/myretail/blob/master/images/Postman%20PUT.png)


During testing of the (previous) payload, I verified the updated entry in MongoDb:
![Image description](https://github.com/gonziilla/myretail/blob/master/images/MongoDB%20after%20PUT.png)



