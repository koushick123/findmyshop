findmyshop is a RESTful API which finds the nearest retail store with respect to a user's location.
It also can be used to add more retail destinations.

Below are the steps required to build, test and run the jar file. You can also use a RESTful client to test it.

Building and running ShopAPI

Required installations

1.	Install Gradle 2.3 from https://services.gradle.org/distributions
2.	Add installation directory of Gradle 2.3 to PATH environment variable.
3.	Install JDK 1.7 from http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
4.	Add JDK1.7 to PATH, CLASSPATH and JAVA_HOME environment variable.
5.	Install IntelliJ community edition from https://www.jetbrains.com/idea/download/#section=windows


Steps to build, test and run

1.	Download the ZIP file from https://github.com/koushick123/findmyshop.git.
2.	Unzip the file into your working directory.
3.	Open IntelliJ and choose to import a project and select build.gradle file under findmyshop-master folder.
3.	Open cmd prompt and go to the findmyshop-master folder.
4.	Type gradle build
5.	After build is successful, type java –jar build\libs\findmystore-0.1.0.jar
6.	This will bring up Spring Boot application embedded in Tomcat.
7.	The ShopClient.java program having the main class is my client class for testing, will be executed, which will add a set of predefined shop details from a retailcoordinates.properties file, and find the nearest location from a random user’s latitude and longitude taken from clientcoordinates.properties file and display the closest shop to the user and the distance in the Spring Boot Console. These properties file are under findmyshop-master folder.
8.	Change the properties files contents for clientcoordinates.properties, where the first line is latitude and second line is longitude.
9.	Save the file, exit the Spring boot application (using Ctrl+C), and rerun java –jar build\libs\findmystore-0.1.0.jar to test for new values.
10.	Change the properties files contents for retailcoordinates.properties, by adding or removing retail info “shopnumber;shopname;postcode”.
11.	Save the file, exit the Spring boot application (using Ctrl+C), and rerun java –jar build\libs\findmystore-0.1.0.jar to test for new values.


Steps to Test with a RESTful client

1.	Install Chrome and go to https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop
2.	Click on Download to install the Postman utility.
3.	Open postman and paste the url http://localhost:8080/shops. This is to add shop details to memory.
4.	Click on Body, and select Raw. Choose JSON(application/json)
5.	Paste a sample data for store like below:
{
    "shopNumber":"22-25",
    "shopName":"Tesco",
    "postCode":"WC2E9EQ"
}
6.	Click on Send.
7.	On the right side, you should see a HTTP 200 OK. This means the Shop details have been added to memory.
8.	Paste url http://localhost:8080/nearestShop?latitude=51.508749&longitude=-0.1277583. This is to get nearest shop details given existing coordinates via request params.
9.	Click on Send.
10.	Postman should receive json data of the nearest shop details like this:

{
  "shopName": "Tesco",
  "shopNumber": "\"1-4",
  "postCode": "SW1A2DR\"",
  "latitude": "51.5071885",
  "longitude": "-0.126998",
  "closestDistance": 198
}
