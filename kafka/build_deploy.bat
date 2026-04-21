set CONSUMER_VER=0.5
set PRODUCER_VER=0.4
echo %CONSUMER_VER%
echo %PRODUCER_VER%

cd consumer

docker build -t kafka-consumer:%CONSUMER_VER% .
minikube image load kafka-consumer:%CONSUMER_VER%

cd chart

helm upgrade my-kafka-consumer .

cd ../..

cd producer

docker build -t kafka-producer:%PRODUCER_VER% .
minikube image load kafka-producer:%PRODUCER_VER%

cd chart

helm upgrade my-kafka-producer .

cd ../..