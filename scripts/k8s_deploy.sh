cd `dirname $0` 
cd ../k8s
kubectl apply -f 01-storage-deployment.yaml
kubectl apply -f 02-postgres-deployment.yaml
kubectl apply -f 03-mongo-deployment.yaml
kubectl apply -f 04-keycloak-deployment.yaml
kubectl apply -f 05-nginx.deploy.yaml
kubectl apply -f 06-ingress-deployment.yaml
kubectl apply -f 07-services-deployment.yaml
kubectl apply -f 08-grafana.yaml
kubectl apply -f 09-loki.yaml
kubectl apply -f 10-fluentd.yaml
kubectl apply -f 11-prometheus.yaml

cd ../employee-service/k8s
kubectl apply -f deployment.yaml
cd ../../department-service/k8s
