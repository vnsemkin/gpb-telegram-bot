#!/bin/bash

# Ensure Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose not found. Please install it."
    exit 1
fi

# Navigate to the correct directory
cd "$(dirname "$0")/Docker" || exit 1

sudo docker-compose down

echo "Stopped the telegram-bot container."
