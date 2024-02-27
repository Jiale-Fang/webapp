packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}

variable "zone" {
  description = "Az"
  type             = string
}

variable "project_id" {
  description = "Google project id"
  type        = string
  sensitive   = true
}

variable "mysql_password" {
  description = "Mysql password"
  type        = string
  sensitive   = true
}

variable "jasypt_encryption_key" {
  description = "Jasypt encryption key, used for decrypting sensitive app configuration parameters"
  type        = string
  sensitive   = true
}

source "googlecompute" "customized-img" {
  project_id          = var.project_id
  source_image_family = "centos-stream-8"
  zone                = var.zone
  ssh_username        = "centos"
  machine_type        = "n1-standard-1"
  disk_size           = 100
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
      "PASSWORD=${var.mysql_password}",
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
    environment_vars = [
      "JASYPT_ENCRYPTION_KEY=${var.jasypt_encryption_key}",
    ]
  }

}
