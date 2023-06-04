FROM amazoncorretto:20.0.1 AS build
MAINTAINER marcusvaal.com
RUN yum update -y && yum install -y make
WORKDIR /src
COPY . .
RUN ./mvnw clean package

FROM amazoncorretto:20.0.1-alpine
MAINTAINER marcusvaal.com
RUN mkdir -p /data/volcanocampsite
WORKDIR /app
COPY --from=build /src/target/volcano-booking-service-1.0.0.jar .
ENTRYPOINT ["java","-jar","/app/volcano-booking-service-1.0.0.jar"]