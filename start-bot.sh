#!/bin/bash

# Ensure Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose not found. Please install it."
    exit 1
fi

cd "$(dirname "$0")/" || exit 1

./gradlew build

# Navigate to the correct directory
cd "$(dirname "$0")/Docker" || exit 1

sudo docker-compose up -d telegram-bot

echo "Started the telegram-bot container."
