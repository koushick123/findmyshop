<h3>findmyshop prototype </h3>

<p>findmyshop is a RESTful API which finds the nearest retail store with respect to a user's location. It takes in user's location in the form of latitude and longitude, and finds the shortest distance to a list of stores.
</p>
<p>
The API also supports adding new retail information. This is given to API in the form of shop's name, number and postcode, and it obtains the latitude and longitude for that shop and stores them.
</p>
<p>

<h3>Design of findmyshop</h3>
<p>
The design documents FindMyShop_Class_Diagram.pdf and FindMyShop_Sequence_Diagram.pdf contain Class and Sequence diagrams of the findmyshop API.
</p>
<br>
<h3><u>Building and running ShopAPI</u></h3>

<h4><b>Required installations</b></h4>

<ul>
<li>Install Gradle 2.3 from https://services.gradle.org/distributions</li>
<li>Add installation directory of Gradle 2.3 to PATH environment variable.</li>
<li>Install JDK 1.7 from http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html</li>
<li>Add JDK1.7 to PATH, CLASSPATH and JAVA_HOME environment variable.</li>
<li>Install IntelliJ community edition from https://www.jetbrains.com/idea/download/#section=windows</li>
</ul>

<br>
<h4><b>Steps to build, test and run</b></h4>
<ul>
<li>Download the ZIP file from https://github.com/koushick123/findmyshop.git.</li>
<li>Unzip the file into your working directory.</li>
<li>Open IntelliJ and choose to import a project and select build.gradle file under findmyshop-master folder.</li>
<li>Open cmd prompt and go to the findmyshop-master folder.</li>
<li>Type gradle build</li>
<li>After build is successful, type java –jar build\libs\findmystore-0.1.0.jar</li>
<li>This will bring up Spring Boot application embedded in Tomcat.</li>
<li>The ShopClient.java program having the main class is my client class for testing, will be executed, which will add a set of predefined shop details from a retailcoordinates.properties file, and find the nearest location from a random user’s latitude and longitude taken from clientcoordinates.properties file and display the closest shop to the user and the distance in the Spring Boot Console. These properties file are under findmyshop-master folder.</li>
<li>Change the properties files contents for clientcoordinates.properties, where the first line is latitude and second line is longitude.</li>
<li>Save the file, exit the Spring boot application (using Ctrl+C), and rerun java –jar build\libs\findmystore-0.1.0.jar to test for new values.</li>
<li>Change the properties files contents for retailcoordinates.properties, by adding or removing retail info “shopnumber;shopname;postcode”.</li>
<li>Save the file, exit the Spring boot application (using Ctrl+C), and rerun java –jar build\libs\findmystore-0.1.0.jar to test for new values.</li>
</ul>

<br>
<h4><b>Steps to Test with a RESTful client</b></h4>
<ul>
<li>Install Chrome and go to https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop</li>
<li>Click on Add to Chrome to install the Postman utility.</li>
<li>Open postman , choose POST from dropdown, and paste the url http://localhost:8080/shops. This is to add shop details to memory.</li>
<li>Click on Body, and select Raw. Choose JSON(application/json)</li>
<li>Paste a sample data for store like below:</li>
{
    "shopNumber":"22-25",
    "shopName":"Tesco",
    "postCode":"WC2E9EQ"
}
<li>Click on Send.</li>
<li>Below in response, you should see a HTTP 201 Created. This means the Shop details have been added to memory.</li>
<li>Choose GET from dropdown, and paste url http://localhost:8080/nearestShop?latitude=51.508749&longitude=-0.1277583. This is to get nearest shop details given existing coordinates via request params.</li>
<li>Click on Send.</li>
<li>Postman should receive json data of the nearest shop details like this:</li>

{
  "shopName": "Tesco",
  "shopNumber": "\"1-4",
  "postCode": "SW1A2DR\"",
  "latitude": "51.5071885",
  "longitude": "-0.126998",
  "closestDistance": 198
}
</ul>
