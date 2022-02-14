# Boogie REST

### Building and running the endpoint locally

The recommended way to do testing and new development is to run the endpoint locally running on some of the embedded raw data in the
repo. Doing so is as easy as:

```bash
# from the boogie directory - this will create an image boogie-rest:latest which is referenced in the docker-compose file
(base) MM******-PC:boogie acramer$ docker build -t boogie-rest:latest --build-arg MAVEN_USER=<codev-user> --build-arg MAVEN_PASSWORD=<codev-password> .

# still from the boogie directory - this will stand up and run the REST API which has bind-mounted into it some of our local 
# unit testing files 
(base) MM******-PC:boogie acramer$ docker-compose up

# the REST API(s) are by default hosted in localhost:8080/{arinc/routes} - or go to swagger at localhost:8080/swagger
```