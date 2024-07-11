FROM openjdk:17

WORKDIR /app


COPY .mvn .mvn


COPY mvnw .


RUN chmod +x mvnw


RUN ./mvnw dependency:go-offline




COPY . .

CMD ["./mvnw", "spring-boot:run"]
