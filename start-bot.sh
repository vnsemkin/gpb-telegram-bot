#!/bin/bash

# Ensure Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose not found. Please install it."
    exit 1
fi

cd "$(dirname "$0")/" || exit 1

./gradlew build

# Navigate to the correct directory (assuming your structure is like this: .../semkin-telegram-bot/Docker/docker-compose.yaml)
cd "$(dirname "$0")/Docker" || exit 1

# Use sudo for docker-compose (if needed, remove if you are part of the docker group)
sudo docker-compose up -d telegram-bot

echo "Started the telegram-bot container."
