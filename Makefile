export IMAGE_NAME=volcano-booking-service
export IMAGE_TAG=latest
export IMAGE=${IMAGE_NAME}:${IMAGE_TAG}

build:
	mvnw clean package
	docker build -t ${IMAGE} .

docker: build
	docker run -p 8080:8080 ${IMAGE}