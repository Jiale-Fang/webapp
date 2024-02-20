#!/bin/bash

# Download mysql
echo "Install Mysql"
echo "y" | sudo dnf install mysql-server

## Start Mysql
echo "Start Mysql"
sudo systemctl enable mysqld
sudo systemctl start mysqld.service
