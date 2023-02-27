# Publog
The Publog blog engine allows users to publish articles and leave comments, while visitors can browse articles sorted by creation date, tag, keyword, or article popularity. The blog includes the ability to authenticate, register new users, and restore passwords. If a user is a moderator, there is the ability to moderate and edit global settings. It is possible to view the statistics of the entire blog or the statistics of an authenticated user.

Currently, the project is deployed on my home server running Ubuntu Server OS using MicroK8s. When changes are pushed to the "master" branch, the project is automatically built, tested, and pushed to Docker Hub. Then, the manifest in MicroK8s loads the latest container image from Docker Hub and deploys it on the server. I also registered an SSL certificate using cert-manager and a domain on the no-ip service. As a result, the project is accessible via the domain name [https://pet-projects.ddns.net/].

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
