resource "aws_cloudwatch_log_group" "ecs_logs" {
  name              = "/ecs/hackathon-fiap-processamento-video"
  retention_in_days = 30
}