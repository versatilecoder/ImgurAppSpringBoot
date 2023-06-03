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
1.	A Person can login using Username and Password
2.	At time of registration Person provide password as well
3.  To view List of Images, person will get urls of all images in response
4.  To view just one image, person on passing imageid, will get the URL
5.  All testing done on localhost(kafka,jenkins,nexus,h2)




 _Swagger Url_- http://localhost:8080/swagger-ui.html
  _H2_- http://localhost:8080/h2-console
  _Jenkins_ - http://localhost:9090/
  _Nexus_ - http://localhost:8081/
  _Kafka_ - localhost:9092



 
