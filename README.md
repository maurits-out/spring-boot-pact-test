# Introduction
My demo project to learn about contract testing ([Fowler][1]) and consumer driven contracts ([Robinson][2]) using
[Pact][3]. 

# RestBucks
In this project we use a simplified version of the RestBucks example as described in the book 'REST in Practise' by Jim
Webber, Savas Parastatidis and Ian Robinson. We focus on the coffee ordering service involving remote customers and the 
RestBucks server. The following use cases will be considered:

1. A customer can place a new order.
2. A customer can update an order as long as it as not been served, i.e. the state of the order is pending.
3. A customer can look up the details of this order.
4. A customer can delete an order as long as it as not been served.

# Pact Broker
This project requires a running Pact Broker instance. In this section we describe how to set this up using 
[Podman](https://podman.io/).

First obtain the images using the following commands:

```bash
podman pull postgres
podman pull dius/pact-broker
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

[1]: https://martinfowler.com/bliki/ContractTest.html "ContractTest"
[2]: https://martinfowler.com/articles/consumerDrivenContracts.html "Consumer-Driven Contracts: A Service Evolution Pattern"
[3]: https://pact.io/ "Pact"