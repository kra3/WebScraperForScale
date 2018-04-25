# SkyNet Scraper
> Scrape urls and stores results. Allows to get the results later.

Following API is exposed by this application:

- Submit scrape jobs:
    - `POST /scrape-jobs/`
    - json body: `{"url": "url_to_scrape"}`
    - returns a unique job id (if accepted)

- Get result of scrape jobs:
    - `GET /scrape-jobs/:jobid`
    - returns json: `{ url, title, date, content }` if jobId is valid


Following ports are exposed by docker image:

- `8080`: REST service
- `15672`: RabbitMQ monitoring (guest:guest)
- `5672`: RabbitMQ (for debugging)
- `6379`: Redis (for debugging)


## Architecture

- An app server to provide REST end-points
- de-duplication of urls through redis (in memory db)
- RabbitMQ as message bus to distribute work (scraping)
- stores result in HSQL DB. (currently runs in memory mode)
- docker to build app image
- docker-compose to layer other services (RabbitMQ & redis for now) and for swarm deployments (could be)

> PS: none of the servers are tweaked for performance or for use case.

> PS: scaling of services is not planned in docker-compose (runs as single server mode at the moment)

> PS: workers are not tuned either (number of workers, message prefetch, ack/nack, ...)

## Want to see it in action ?

### Prerequisites

- java 8
- maven
- docker

### Build & run

``` bash
./mvnw package -DskipTests=true
docker-compose up
```

Use POSTMAN or similar tools to interact with the REST endpoints.
Shell in whcih docker-compose is running will show app logs.
