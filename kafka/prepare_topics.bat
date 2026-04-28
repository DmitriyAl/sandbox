@REM kubectl cp kafka-client.properties my-kafka-release-broker-0:/tmp/client.properties
@REM kubectl exec -it my-kafka-release-broker-0 -- /opt/bitnami/kafka/bin/kafka-topics.sh --alter --topic test-topic --partitions 24 --bootstrap-server localhost:9092 --command-config /tmp/client.properties
kubectl exec -it my-kafka-release-broker-0 -- /opt/bitnami/kafka/bin/kafka-topics.sh --describe --topic test-topic --bootstrap-server localhost:9092 --command-config /tmp/client.properties
