#!/bin/bash

# Завершаем скрипт при любой ошибке
set -e

echo ">>>>>>BUILDING MICROSERVICES<<<<<<"
echo ""

bash ./build_all.sh

cd ./deploy
docker-compose up --build -d