# Grafana

To access Grafana forward the port
`kubectl port-forward service/grafana 3000:3000`
access the portal on http://localhost:3000
After entering the user and password(admin/admin), the reset password appears

# Accessing logs from our services
To access our logs from Grafana we need to setup loki :
![Microservices Diagram](pictures/grafana_loki_setup.png)

# Filtering the logs
By default all logs from the different services including mongodb, ingress and so on are available in the screenshot below we are filtering to get only logs from our services.
![Microservices Diagram](pictures/grafana_loki_logs_setup.png)

# Prometheus Setup
Prometheus will be used to collect metrics. Our application defined any custom metrics but because we have the prometheus dependency available and the required setup.
Default metrics are available. Setup Prometheus like below
![Microservices Diagram](pictures/grafana_prometheus_setup.png)

# Timeseries setup
There are many type of charts available in the example below we are setting up a number of requests per second
![Microservices Diagram](pictures/grafana_prometheus_timeseries_setup.png)

# Sample dashboard
After the above setup is done here is a sample dashboard
![Microservices Diagram](pictures/grafana_simple_dashboard.png)