variable "ecs_cluster_name" {
  description = "Nome do cluster ECS"
  type        = string
  default     = "hackathon-fiap-cluster"
}

variable "service_name" {
  description = "Nome do ECS Service"
  type        = string
  default     = "service-hackathon-fiap-processamento-video"
}

variable "container_name" {
  description = "Nome do container"
  type        = string
  default     = "container-hackathon-fiap-processamento-video"
}

variable "image" {
  description = "Imagem do container"
  type        = string
  default     = "gustavozenke/hackathon-fiap-processamento-video:develop"
}

variable "cpu" {
  description = "CPU para a task"
  type        = number
  default     = 256
}

variable "memory" {
  description = "Memória para a task"
  type        = number
  default     = 512
}

variable "subnets" {
  description = "Lista de subnets para rodar o serviço ECS"
  type        = list(string)
  default     = [
    "subnet-06e3bb63a3ecd19db",
    "subnet-0c60c32538ce92175",
    "subnet-02d683049dab22a0f",
#    "subnet-01200b84029a8ae5f",
#    "subnet-0f4f3b651df6150b0",
#    "subnet-065e93b836e45de7e"
  ]
}

variable "security_groups" {
  description = "Lista de security groups"
  type        = list(string)
  default     = ["sg-0708538b995a2467c"]
}