*Under construction*

# Introduction
My demo project to learn about contract testing ([Fowler][1]) and consumer driven contracts ([Robinson][2]) using
 [Pact][3]. I recommend you to read these blog posts before moving on.

# RestBucks: A Little Coffee Shop
In this project we use a simplified version of the RestBucks example as described in the book 'REST in Practise' by Jim
Webber, Savas Parastatidis and Ian Robinson. We focus on the coffee ordering service involving remote customers and the 
RestBucks server. The following use cases will be considered:

1. *A customer can place a new order.*\
This is done by sending a POST request containing the order details to the RestBucks server containing the order
details. The RestBucks server assigns a unique ID to the order and sets the state to 'pending'.
2. *A customer can update an order.*\
Updating an order consists of sending a PUT request containing the new order details. An order can only be updated if
the state is 'pending'. If the state is 'served' then the RestBucks server rejects the request.
3. *A customer can look up the order.*\
Looking up the order is as simply as sending a GET request to the RestBucks server.
4. *A customer can cancel an order.*\
An order can be cancelled by sending a DELETE request to the RestBucks server. An order can only be cancelled if the
state is 'pending'. If the state is 'served' then the RestBucks server rejects the request.

# Pact Broker
A running Pact Broker instance is required. In this section we describe how to set this up using
[Podman](https://podman.io/).

First obtain the images using the following commands:

```bash
podman pull postgres
podman pull dius/pact-broker
podman pull pactfoundation/pact-cli
```

Next create a pod as follows:

```bash
podman pod create --name pact-pod -p 8080:80  
```

If port 8080 is already taken then specify an alternative port.

Now start a Postgres container:

```bash
podman run --health-cmd "psql postgres --command 'select 1' -U postgres" \
    --detach --env POSTGRES_USER=postgres --env POSTGRES_PASSWORD=password \
    --env POSTGRES_DB=postgres --pod pact-pod --name pact-postgres postgres
```

The final step is to start the Pact Broker container:

```bash
podman run --detach --env PACT_BROKER_DATABASE_USERNAME=postgres \
    --env PACT_BROKER_DATABASE_PASSWORD=password --env PACT_BROKER_DATABASE_HOST=localhost \ 
    --env PACT_BROKER_DATABASE_NAME=postgres --env PACT_BROKER_LOG_LEVEL=DEBUG \ 
    --pod pact-pod --name pact-broker dius/pact-broker 
```

To verify all is running properly open a browser and navigate to http://localhost:8080. You should be able to see the 
web interface of the Pact Broker.

Running the Pact CLI is a bit cumbersome:

```bash
podman run --rm --pod pact-pod pactfoundation/pact-cli:latest broker describe-version \
    --pacticipant=RestBucksClient --broker-base-url=http://localhost
```

# Project structure
The pact-test is a Spring Boot project consisting of two submodules. The first module is *restbucks-consumer* and it
implements the customer. It consists of a single Java class `com.restbucks.pact.client.RestBucksClient` 
that uses JAX-WS to consume the RESTful web service provided by the RestBucks server.
The RestBucks server has been implemented in the second submodule, *restbucks-producer*. It is a Spring Boot application
that exposes a RESTful Web Service.

**Note**: Normally the consumer and the provider will be separate projects, typically maintained in different
repositories by different teams. However, for simplicity we place both in the same project here.

# Defining the contract
Contract testing focuses on the messages that are exchanged between a consumer and a producer. It ensures that the
consumer and the provider have a shared and accurate understanding of the contents of these messages.
With consumer-driven contracts it is the consumer that sets the expectations of these messages by creating the contract.
That contract is subsequently shared with the provider so that the provider knows what to implement in order to fulfil
 this contract.

The consumer project contains the unit test `com.restbucks.pact.client.RestBucksClientTest` that defines the contract. 
This is done by executing interactions between the consumer and a mock provider. These interactions are recorded in a
Pact file `RestBucksClient-RestBucksProvider.json` that can be found in the `target/pacts` directory. This PACT file
forms the actual contract.

# Sharing the contract
The next step is to share the contract to inform the provider. This can be done by issuing the following Maven command
from the top project folder:

```bash
mvn pact:publish -pl restbucks-consumer
```

# Implementing and verifying the provider
The provider module contains an integration test `com.restbucks.pact.producer.RestBucksProviderContractIT` that fetches
the Pact from the Pact Broker and runs the scenarios to verify if the provider satisfies the contract. This can be done
in a TDD manner. To publish the verification results issue the following Maven command from the top project folder:

```bash
mvn verify -pl restbucks-producer -Dpact.verifier.publishResults=true
```
   
[1]: https://martinfowler.com/bliki/ContractTest.html "ContractTest"
[2]: https://martinfowler.com/articles/consumerDrivenContracts.html "Consumer-Driven Contracts: A Service Evolution 
Pattern"
[3]: https://pact.io/ "Pact"
