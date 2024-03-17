#!/bin/bash

sudo curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh
sudo bash add-google-cloud-ops-agent-repo.sh --also-install

# Create log file
sudo touch /var/log/webapp.log
sudo chown -R csye6225:csye6225 /var/log/webapp.log

# Ops Agent Configuration
cat <<EOF | sudo tee /etc/google-cloud-ops-agent/config.yaml
logging:
  receivers:
    webapp-receiver:
      type: files
      include_paths:
      - /var/log/webapp.log
      record_log_file_path: true
  processors:
    webapp-processor:
      type: parse_json
      time_key: timestamp
      time_format: "%Y-%m-%dT%H:%M:%S.%L%z"
    severity-processor:
      type: modify_fields
      fields:
        severity:
          copy_from: jsonPayload.level
  service:
    pipelines:
      default_pipeline:
        receivers: [webapp-receiver]
        processors: [webapp-processor, severity-processor]
EOF

sudo systemctl restart google-cloud-ops-agent
sudo systemctl enable google-cloud-ops-agent
