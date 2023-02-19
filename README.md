# Publog
The DevPub blog engine allows users to publish articles and leave comments, while visitors can browse articles sorted by creation date, tag, keyword, or article popularity. The blog includes the ability to authenticate, register new users, and restore passwords. If a user is a moderator, there is the ability to moderate and edit global settings. It is possible to view the statistics of the entire blog or the statistics of an authenticated user.

### Technology stack
- Java 11
- Spring Boot 2.2.4.RELEASE
- Maven
- MySQL 8
- Git
- Docker-compose
- Microk8s

### How to start
To run the project locally using docker-compose, navigate to the root of the project and run the bash script:
```bash
docker-compose up --build
```
Passwords for test users are encrypted in the database using BCrypt (12 rounds).
