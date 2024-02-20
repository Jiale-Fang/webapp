packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}

variable "GCP_ACCOUNT_JSON" {
  type      = string
  sensitive = true
}

variable "MYSQL_PASSWORD" {
  type      = string
  sensitive = true
}

source "googlecompute" "customized-img" {
  project_id          = "csye-6225-413815"
  source_image_family = "centos-stream-8"
  zone                = "us-east1-b"
  ssh_username        = "csye6225"
  machine_type        = "n1-standard-1"
  disk_size           = 100
  credentials_json    = "var.GCP_ACCOUNT_JSON"
}

build {
  name    = "webapp-image"
  sources = ["sources.googlecompute.customized-img"]

  provisioner "shell" {
    script       = "./scripts/install_dependencies.sh"
    pause_before = "3s"
    timeout      = "120s"
  }

  provisioner "shell" {
    script       = "./scripts/install_mysql.sh"
    pause_before = "3s"
    timeout      = "180s"
  }

  provisioner "shell" {
    script       = "./scripts/setup_mysql.sh"
    pause_before = "3s"
    timeout      = "10s"
    environment_vars = [
      "MYSQL_PASSWORD = var.MYSQL_PASSWORD"
    ]
  }

  provisioner "file" {
    source      = "./target/Health_Check-0.0.1-SNAPSHOT.jar"
    destination = "/usr/local/csye6225_repo/Health_Check-0.0.1-SNAPSHOT.jar"
  }

  provisioner "shell" {
    script       = "./scripts/configure_systemd.sh"
    pause_before = "3s"
    timeout      = "30s"
  }

}
