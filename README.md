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

1. Create Dockerfile for backend side

   ```
   FROM openjdk:21 (21 is my version if you are using different one change it)
   USER root
   COPY target/*.jar app.jar
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

2. Create Dockerfile for frontend side

   ```
   FROM node:20-alpine
   WORKDIR /app
   COPY package.json .
   RUN npm install
   COPY . .
   RUN npm run build
   EXPOSE 3000
   CMD [ "npm", "run", "preview" ]
   ```

3. Make sure to setup config(vite.config.ts) for frontend so app is starting on correct port

   ```
   import { defineConfig } from "vite";
   import react from "@vitejs/plugin-react";
   
   export default defineConfig({
     base: "/",
     plugins: [react()],
     preview: {
       port: 3000,
       strictPort: true,
     },
     server: {
       port: 3000,
       strictPort: true,
       host: true,
       origin: "http://localhost:3000",
     },
   });
   
   ```

4. Create docker-compose.yml file

   ```
   version: '3'
   services:
    your-service:
      image: ${IMAGE_NAME}
      environment:
        - SPRING_DATASOURCE_URL=${DB_URL}
        - SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
        - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
        - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      ports:
        - ${BACKEND_PORT}
      depends_on:
        - ${DB_CONTAINER_NAME}

    library-fe:
      image: library-react
      container_name: library-frontend
      depends_on:
        - your-service
      volumes:
        - library_fe_data:/var/lib/library_fe/data
      ports:
        - "3001:3000"

    db:
      image: ${DB_IMAGE}
      container_name: ${DB_CONTAINER_NAME}
      environment:
        POSTGRES_PASSWORD: ${DB_PASSWORD}
        POSTGRES_USER: ${DB_USERNAME}
        POSTGRES_DB: ${DB_NAME}
      ports:
        - ${DB_PORT}
      volumes:
        - library_data:/var/lib/postgresql/data

    cloudbeaver:
      image: dbeaver/cloudbeaver:latest
      restart: always
      container_name: cloudbeaver
      volumes:
        - library_dbeaver_data:/opt/cloudbeaver/workspace
      ports:
        - ${CLOUDBEAVER_PORT}

    volumes:
      library_fe_data:
        driver: local
      library_data:
        driver: local
      library_dbeaver_data:
        driver: local

   ```

5. Make sure pom file have path to main file
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
6. Start your dockerized spring app
   - mvn clean package
   - docker build -t name-of-your-image-file:latest . (build frontend and backend images)
   - docker images(can see if u created images)
   - docker-compose up (if you dont have installed docker-compose: apt install docker-compose)
   - docker-compose down (when you close app to remove containers)
7. Container ports:
   - http://localhost:3001 (frontend port)
   - http://localhost:8081 (backend port)
   - http://localhost:8978 (dbeaver port)
   - http://localhost:5432 (postgres port)
