FROM eclipse-temurin:17-jdk-alpine
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src src
RUN ./mvnw clean install -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
ENTRYPOINT ["./mvnw","test"]