export IMAGE_NAME=volcano-booking-service
export IMAGE_TAG=latest
export IMAGE=${IMAGE_NAME}:${IMAGE_TAG}

test:
	docker run -it -v ${CURDIR}:/app -w /app amazoncorretto:20.0.1 ./mvnw clean verify

package:
	docker run -it -v ${CURDIR}:/app -w /app amazoncorretto:20.0.1 ./mvnw clean package

build:
	docker build -t ${IMAGE} .

docker: build
	docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local ${IMAGE}

docker-persistent: build
	docker run -it -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local,h2persistent -v ${CURDIR}/data:/data ${IMAGE}