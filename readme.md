java spring boot in memory
docker
add sql db 
containerize it



docker network create airways --driver bridge
docker network connect airways ms  

docker run -it --name airport --network airways -p 8080:8080  myairports
docker run  --name ms -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql 

-> add profiles:
    Dockerized
        Dev     ------> Mysql Dev  instance     : ms-dev
        QA      ------> Mysql Test  instance    : ms-test
        Prod    ------> Mysql Prod  instance    : ms-prod
    properties files :
        application-dev.properties
        application-qa.properties
        application-prod.properties
    
compose 
Paggination and HATEOES
A proper API
Swagger
Unit tests
CICD

AUth