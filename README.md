# development

- refer to [wiki](https://github.com/xyw0025/CS520_group_project/wiki) for git flow



```
cp src/main/resources/.env.example src/main/resources/.env
# entering the configs including database username and password

mvn package -Dmaven.test.skip
# target folder created 

java -Dspring.profiles.active=development -jar target/cs520-0.0.1-SNAPSHOT.jar

# head to localhost:8080 see if the service is up
```