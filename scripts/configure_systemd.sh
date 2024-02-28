#!/bin/bash

sudo useradd -s /usr/sbin/nologin csye6225
sudo chown -R csye6225:csye6225 /usr/local/csye6225_repo
cat << EOF | sudo tee /etc/systemd/system/csye6225.service >/dev/null
[Unit]
Description=CSYE 6225 App
ConditionPathExists=/usr/local/csye6225_repo/startup.sh
After=network.target

[Service]
Type=simple
User=csye6225
Group=csye6225
WorkingDirectory=/usr/local/csye6225_repo
ExecStart=/usr/local/csye6225_repo/startup.sh
Restart=always
RestartSec=3
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=csye6225

[Install]
WantedBy=multi-user.target
EOF

sudo chown -R csye6225:csye6225 /etc/systemd/system/csye6225.service

sudo systemctl daemon-reload
sudo systemctl enable csye6225
sudo systemctl start csye6225
