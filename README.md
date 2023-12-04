# development

- refer to [wiki](https://github.com/xyw0025/CS520_group_project/wiki) for git flow

### Setting Up and Running the Development Environment

```
cd ./backend
cp src/main/resources/.env.example src/main/resources/.env
# entering the credentials -> @ wen if you dont have them yet

cd ./backend
mvn package -Dmaven.test.skip
# target folder created

java -Dspring.profiles.active=development -jar target/cs520-0.0.1-SNAPSHOT.jar
```

[frontend document](frontend/README.md)

### development with docker

```
# make sure you're under the folder where `docker-compose.yml` exists
docker-compose build
docker-compose up
```

[check PR#10](https://github.com/xyw0025/UMaessenger/pull/10)


### api-doc
start backend service, then head to: http://localhost:3000/swagger-ui/index.html
