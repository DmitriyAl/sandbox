set CONSUMER_VER=0.5
set PRODUCER_VER=0.5
echo %CONSUMER_VER%
echo %PRODUCER_VER%

@REM cd consumer
@REM
@REM docker build -t kafka-consumer:%CONSUMER_VER% .
@REM minikube image load kafka-consumer:%CONSUMER_VER%
@REM
@REM cd chart
@REM
@REM helm upgrade my-kafka-consumer .
@REM
@REM cd ../..

cd producer

docker build -t kafka-producer:%PRODUCER_VER% .
minikube image load kafka-producer:%PRODUCER_VER%

cd chart

helm upgrade my-kafka-producer .

cd ../..