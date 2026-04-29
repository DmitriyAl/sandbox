set CONSUMER_VER=0.6
set PRODUCER_VER=0.11
echo %CONSUMER_VER%
echo %PRODUCER_VER%

cd consumer
@REM
@REM docker build -t kafka-consumer:%CONSUMER_VER% .
@REM minikube image load kafka-consumer:%CONSUMER_VER%
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