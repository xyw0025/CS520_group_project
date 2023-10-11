# development

- refer to [wiki](https://github.com/xyw0025/CS520_group_project/wiki) for git flow

### Setting Up and Running the Development Environment

```
cd ./backend
cp src/main/resources/.env.example src/main/resources/.env
# entering the credentials -> @ wen if you dont have them yet

mvn package -Dmaven.test.skip
# target folder created

java -Dspring.profiles.active=development -jar target/cs520-0.0.1-SNAPSHOT.jar
# head to localhost:8080 see if the service is up
```
