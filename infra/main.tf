terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
    }
  }
  required_version = ">= 1.0.0"
}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "main" {
  name     = "quantisen-rg"
  location = "East US"
}

resource "azurerm_service_plan" "main" {
  name                = "gestionboisson-plan"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  os_type             = "Linux"
  sku_name            = "F1"
}

resource "azurerm_linux_web_app" "main" {
  name                = "quantisen-${random_integer.suffix.result}"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  service_plan_id     = azurerm_service_plan.main.id

  site_config {
    application_stack {
      docker_image     = var.docker_image
      docker_image_tag = "latest"
    }
    always_on = false
  }

  app_settings = var.environment_variables
}

resource "random_integer" "suffix" {
  min = 10000
  max = 99999
}

variable "docker_image" {
  description = "Docker image to deploy"
  type        = string
  default     = "traorecheikh/quantisen:latest"
}

variable "environment_variables" {
  description = "Environment variables for the container"
  type        = map(string)
  default     = {}
}

output "fqdn" {
  value = azurerm_linux_web_app.main.default_hostname
}