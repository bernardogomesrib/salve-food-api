#!/bin/bash

mvn clean package -DskipTests

docker compose -f 'docker-compose-dockerized.yml' up -d --build