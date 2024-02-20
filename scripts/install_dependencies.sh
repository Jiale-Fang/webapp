#!/bin/bash

# Install JDK
echo "Install JDK"
echo "y" | sudo yum install java-1.8.0-openjdk-devel

# Install maven
echo "Install Maven"
echo "y" |  sudo dnf install maven

# Install expect
echo "Install expect"
echo "y" | sudo yum install expect

# Create Working folder
sudo mkdir /usr/local/csye6225_repo
sudo chown -R $(whoami):$(whoami) /usr/local/csye6225_repo
