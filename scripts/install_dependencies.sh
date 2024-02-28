#!/bin/bash

# Install JDK
echo "Install JDK"
echo "y" | sudo yum install java-1.8.0-openjdk-devel

# Install maven
echo "Install Maven"
echo "y" |  sudo dnf install maven

# Create Working folder
sudo mkdir /opt/csye6225_repo
sudo chown -R $(whoami):$(whoami) /opt/csye6225_repo
