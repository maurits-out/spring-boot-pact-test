# Introduction
My demo project to learn about contract testing using [Pact](https://docs.pact.io/). 

# Prerequisites
This project requires a running Pact Broker instance. In this section we describe how to set this up using 
[Podman](https://podman.io/).

First obtain the images using the following commands:

```bash
docker pull postgres
docker pull dius/pact-broker
```

Next create a pod as follows:

```bash
podman pod create --name pact-pod -p 8080:80  
```

If port 8080 is already taken then specify an alternative port.

Now start a Postgres container:

```bash
podman run --health-cmd "psql postgres --command 'select 1' -U postgres" --detach --env POSTGRES_USER=postgres --env POSTGRES_PASSWORD=password --env POSTGRES_DB=postgres --pod pact-pod --name pact-postgres postgres
```

The final step is to start the Pact Broker container:

```bash
podman run --detach --env PACT_BROKER_DATABASE_USERNAME=postgres --env PACT_BROKER_DATABASE_PASSWORD=password --env PACT_BROKER_DATABASE_HOST=localhost --env PACT_BROKER_DATABASE_NAME=postgres --env PACT_BROKER_LOG_LEVEL=DEBUG --pod pact-pod --name pact-broker dius/pact-broker 
```

To verify all is running properly open a browser and navigate to http://localhost:8080. You should see the web interface
of the Pact Broker.
