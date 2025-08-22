## Project Information

Demo project for xpHR

**Built with**:
- Java 17
- Maven 3.3.2
- PostgreSQL 16.9
- Spring Boot 3.5.5
- Intellij Ultimate
- DataGrid
## Documents

- Query analisys:
- How security works: https://github.com/khoadeveloper/xphr-demo/blob/main/security.md
- How to use the report: https://github.com/khoadeveloper/xphr-demo/blob/main/usage.md

## Test Coverage
<img width="1326" height="470" alt="image" src="https://github.com/user-attachments/assets/8c2d76e9-c64b-45ec-82ff-61da5056159c" />

## Build and Deploy

### Prerequisite
- Maven installed
- JDK 17
- PostgreSQL installed with schema `xphr` and all tables created `xphr.project, xphr.employee, xphr.time_record`
- Tomcat 11 installed in `${TOMCAT_DIR}` directory


### Step
Navigate to project folder and run this command to compite and package application
`mvn clean package`

Copy the `xphr-demo.war` from target directory to your `${TOMCAT_DIR}/webapps`
Run this script to bring up the application `${TOMCAT_DIR}/bin/catalina.bat run`

You can access the application from http://localhost:8080/xphr-demo
