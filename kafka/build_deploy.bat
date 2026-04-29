set CONSUMER_VER=0.9
set PRODUCER_VER=0.12
echo %CONSUMER_VER%
echo %PRODUCER_VER%

cd consumer
@REM
docker build -t kafka-consumer:%CONSUMER_VER% .
minikube image load kafka-consumer:%CONSUMER_VER%
@REM
cd chart

helm upgrade my-kafka-consumer .

cd ../..
@REM
cd producer
@REM
@REM docker build -t kafka-producer:%PRODUCER_VER% .
@REM minikube image load kafka-producer:%PRODUCER_VER%
@REM
cd chart
@REM
helm upgrade my-kafka-producer .
@REM
cd ../..