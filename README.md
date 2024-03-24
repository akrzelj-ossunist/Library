# Library App

## Table of Contents

- [App Setup](#app-setup)
- [Features](#features)
- [Technologies](#technologies)

## App Setup

### App prerequisite setup:

1. install wsl (if OS == Windows)
   - wsl --install -d ubuntu
   - wsl --update
   - wsl --ser-default-version 2
2. launch wsl with command "wsl"
3. Go root mode with command "sudo su"
4. install docker
   - sudo wget -qO - https://bonguides.com/docker | bash
5. install docker-compose
   - apt install docker-compose
6. install newest java version(in my case it's 21)
   - sudo apt install openjdk-21-jdk
   - java -version
7. install maven (check for newest version and switch 3.8.4 with it)

   - wget https://dlcdn.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz -P /tmp
   - tar xf /tmp/apache-maven-\*.tar.gz -C /opt
   - ln -s /opt/apache-maven-3.8.4 /opt/maven
   - nano /etc/profile.d/maven.sh

     ```
     export JAVA_HOME=/usr/lib/jvm/default-java
     export M2_HOME=/opt/maven
     export MAVEN_HOME=/opt/maven
     export PATH=${M2_HOME}/bin:${PATH}
     ```

   - chmod +x /etc/profile.d/maven.sh
   - source /etc/profile.d/maven.sh
   - mvn -version

### Setup simple docker app in spring boot:

1. Create Dockerfile

   ```
   FROM openjdk:21 (21 is my version if you are using different one change it)
   USER root
   COPY target/*.jar app.jar
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

2. Create docker-compose.yml file

   ```
   version: '3'
   services:
     your-service:
       image: image-name (name of the image you wanna create)
       environment:
         - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/db-name (db is tag beneath and db-name must match out database name)
         - SPRING_DATASOURCE_USERNAME=admin (must match database username)
         - SPRING_DATASOURCE_PASSWORD=admin (any password you want)
         - SPRING_JPA_HIBERNATE_DDL_AUTO=update
       ports:
         - "8081:8080"  # Adjust the port mapping as needed
       depends_on:
         - db

     db:
       image: postgres:latest
       container_name: db
       environment:
         POSTGRES_PASSWORD: admin (any password for our database)
         POSTGRES_USER: admin (any username for our database)
         POSTGRES_DB: library (any database name for our database)
       ports:
         - "5432:5432"
       volumes:
         - library_data:/var/lib/postgresql/data (where database info will be saved locally)

     cloudbeaver:
       image: dbeaver/cloudbeaver:latest
       restart: always
       container_name: cloudbeaver
       volumes:
         - library_dbeaver_data:/opt/cloudbeaver/workspace
       ports:
         - "8978:8978" (dont change anything about cloudbeaver)
   volumes:
     library_data:
       driver: local
     library_dbeaver_data:
       driver: local
   ```

3. Make sure pom file have path to main file
   ```
   <plugin>
   	<groupId>org.springframework.boot</groupId>
   	<artifactId>spring-boot-maven-plugin</artifactId>
   	<configuration>
   		<excludes>
   			<exclude>
   				<groupId>org.projectlombok</groupId>
   				<artifactId>lombok</artifactId>
   			</exclude>
   		</excludes>
   		<mainClass>com.java.example.ExampleApplication</mainClass>(path to your app)
   		<layout>JAR</layout>
   	</configuration>
   	<executions>
   		<execution>
   			<goals>
   				<goal>build-info</goal>
   				<goal>repackage</goal>
   			</goals>
   		</execution>
   	</executions>
   </plugin>
   ```
4. Start your dockerized spring app
   - mvn clean package
   - docker build -t name-of-your-image-file:latest .
   - docker images(can see if u created image)
   - docker-compose up (if you dont have installed docker-compose: apt install docker-compose)
   - docker-compose down (when you close app to remove containers)
5. Container ports:
   - http://localhost:8081 (backend port)
   - http://localhost:8978 (dbeaver port)
   - http://localhost:5432 (postgres port)
