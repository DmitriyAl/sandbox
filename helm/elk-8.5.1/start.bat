kubectl create namespace logging
helm install elasticsearch ./elasticsearch --namespace logging --set replicas=1 --set minimumMasterNodes=1 --set resources.requests.cpu="100m"
--set resources.requests.memory="512Mi" --set resources.limits.cpu="1000m" --set resources.limits.memory="2Gi"
helm install kibana ./kibana --namespace logging --set elasticsearchHosts="https://elasticsearch-master:9200"
helm install filebeat ./filebeat --namespace logging --set elasticsearchHosts="https://elasticsearch-master:9200"