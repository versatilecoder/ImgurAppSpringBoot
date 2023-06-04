                                                    **ImgurAppSpringBoot**
													
SpringBoot App integrated with ImgurApi to Upload, View and Delete Image


Project support below activities:
1)  Api Documentation using Swagger
2)	Kafka to save the details to DB
3)	JenkinsFile for CI/CD Pipeline
4)	Deployment to Nexus
5)	In-memory H2 Database
6)  Log4J for Logging


Assumptions:
1.  At time of registration Person provide password as well
2.  UserName should be unique at registration (Else userName already exist will be shown as response)
3.	A Person can login using Username and Password	
4.  To view List of Images, person will get urls of all images in response
5.  Every image has imageid attached even if displayed on UI.
6.  To view just one image, person on passing imageid, will get the URL
7.  All testing done on localhost(kafka,jenkins,nexus,h2)




 _Swagger Url_- http://localhost:8080/swagger-ui.html
 
 ![alt text](https://i.imgur.com/NT8nEgd.png)
 
  _H2_- http://localhost:8080/h2-console
  
  ![alt text](https://i.imgur.com/Nxvdzbr.png)
  
  _Jenkins_ - http://localhost:9090/
  
  ![alt text](https://i.imgur.com/MMZ44Wq.png)
  
  _Nexus_ - http://localhost:8081/
  
  For Nexus Server use please update the settings.xml (~.m2/settings.xml) with below details
  ```xml
 <servers>   
   <server>
            <id>nexus-release</id>
            <username>admin</username>
            <password>password</password>
        </server>
   </servers>
    <mirrors>
  <mirror>
      <id>maven-default-http-blocker</id>
      <mirrorOf>external:http:*</mirrorOf>
      <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
      <url>http://0.0.0.0/</url>
      <blocked>true</blocked>
    </mirror>
   </mirrors>
  .```
  
   and repository url in pom.xml under distributionManagement
   
   
![alt text](https://i.imgur.com/xEhMxKQ.png)

  _Kafka_ - localhost:9092



 
