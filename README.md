# progetto-monopoly-2-dev-team

## Software required

- [Java 11](https://www.oracle.com/it/java/technologies/javase/jdk11-archive-downloads.html) 
- [Maven](https://maven.apache.org/download.cgi) 

The program was tested and executed on Java 11 or higher versions and executed on Maven 3.6.3 or higher versions 

## How to build

- Run ```mvn clean install``` to let the script install dependencies needed
- To execute the application, run ```mvn compile exec:java -Pproduction -Dexec.mainClass="it.monopoly.Application"``` 
- The application runs by default on port *8080*. To execute the application on a different port, run the above command with additional argument ```-Dexec.args="--server.port=port_number"``` where *port_number* is the port number
 - Go to a new window on your web browser and type ```localhost:8080``` on the URL search bar
 - To add more players, open a new window on your web browser, type ```localhost:8080/unique_code``` where the *unique_code* is the 16-characters alphanumeric code found on the lobby dialog or on the creator’s webpage URL
