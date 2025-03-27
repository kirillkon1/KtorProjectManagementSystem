#!/bin/bash

# Завершаем скрипт при любой ошибке
set -e

#cd ..
#
## Task Management Service
#cd ./TaskManagementService
#./gradlew clean build -x test
#cd ..
#
## Project Management Service
#cd ./ProjectManagementService
#./gradlew clean build -x test
#cd ..
#
## User Management Service
#cd ./UserManagementService
#./gradlew clean build -x test
#cd ..
#
## API Gateway
#cd ./AnalyticsService
#./gradlew clean build -x test
#cd ..
#
## Cloud Config Server
#cd ./CloudConfig
#./gradlew clean build -x test
#cd ..
#
## API Gateway
#cd ./ApiGateway
#./gradlew clean build -x test
#cd ..

# Сборка Docker образов без использования кэша
docker-compose build --no-cache task-manager-service project-manager-service
docker-compose build --no-cache user-manager-service config-server
docker-compose build --no-cache api-gateway analytics-service
#docker-compose build --no-cache user-manager-service

#docker-compose down task-manager-service project-manager-service user-manager-service config-server
docker-compose up --build -d task-manager-service project-manager-service
docker-compose up --build -d user-manager-service config-server
docker-compose up --build -d api-gateway analytics-service


#docker-compose up --build
