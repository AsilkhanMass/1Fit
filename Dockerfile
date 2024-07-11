FROM openjdk:17

WORKDIR /app
HEAD
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY . .

COPY .mvn .mvn


COPY mvnw .


RUN chmod +x mvnw


RUN ./mvnw dependency:go-offline




COPY . .

>>>>>>> origin/10-oft-1-add-docker
CMD ["./mvnw", "spring-boot:run"]
