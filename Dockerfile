FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src src
RUN ./mvnw clean install -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
ENTRYPOINT ["./mvnw","test"]

FROM eclipse-temurin:17-jre-alpine as prod

RUN addgroup -S demo && adduser -S demo -G demo
USER demo

ENV DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.spring_inventory.SpringInventoryApplication"]