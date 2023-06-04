FROM amazoncorretto:20.0.1
MAINTAINER marcusvaal.com
COPY target/volcano-booking-service-1.0.0.jar volcano-booking-service-1.0.0.jar
ENTRYPOINT ["java","-jar","/volcano-booking-service-1.0.0.jar"]