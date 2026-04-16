# Самый надежный способ для Windows:

# 1. Переключиться на Docker внутри Minikube
& minikube docker-env --shell powershell | Invoke-Expression

# 2. Теперь docker команды выполняются внутри Minikube
docker build -t kafka-producer:0.1 .

# 3. Отметить для registry
docker tag kafka-producer:0.1 localhost:5000/kafka-producer:0.1

# 4. Push (теперь это внутри Minikube, поэтому нет сетевых проблем)
docker push localhost:5000/kafka-producer:0.1
