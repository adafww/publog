FROM mysql:8.0

# Set environment variables for the MySQL configuration
ENV MYSQL_DATABASE=mydatabase \
    MYSQL_USER=myuser \
    MYSQL_PASSWORD=mypassword \
    MYSQL_ROOT_PASSWORD=secret

# Copy the database initialization script to the container
COPY init.sql /docker-entrypoint-initdb.d/
