#!/bin/bash

# Завершаем скрипт при любой ошибке
set -e

# Task  Service
echo "Building TaskService..."
cd ../TaskService
./gradlew clean build -x test


# Project  Service
echo "Building ProjectService..."
cd ../ProjectService
./gradlew clean build -x test


# User  Service
echo "Building UserService..."
cd ../UserService
./gradlew clean build -x test


# AnalyticsService
echo "Building AnalyticsService..."
cd ../AnalyticsService
./gradlew clean build -x test


# API Gateway
echo "Building ApiGateway..."
cd ../ApiGateway
./gradlew clean build -x test