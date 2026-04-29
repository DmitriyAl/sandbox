set CONSUMER_VER=0.6
set PRODUCER_VER=0.8
echo %CONSUMER_VER%
echo %PRODUCER_VER%

cd consumer
@REM
docker build -t kafka-consumer:%CONSUMER_VER% .
minikube image load kafka-consumer:%CONSUMER_VER%
@REM
cd chart

helm install my-kafka-consumer .

cd ../..
@REM
cd producer
@REM
docker build -t kafka-producer:%PRODUCER_VER% .
minikube image load kafka-producer:%PRODUCER_VER%
@REM
cd chart
@REM
helm install my-kafka-producer .
@REM
cd ../..