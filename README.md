## StepZen-ChainGuard

## Inspiration
The world of **financial services is continuously evolving**, and now more than ever, banks and **financial institutions globally must innovate at scale and speed**. Even in its infancy, blockchain technology has demonstrated its ability to **revolutionize a variety of industries**. The characteristics of decentralization, transparency, and immutability appeal to businesses worldwide, but **finance is leading the way in adoption**.
According to Gartner, **blockchain applications could generate up to $3.1 trillion in new company value by 2030**, with half of that value coming from **operational efficiency applications**. On the other side, the **blockchain is a source of concern**. If you're a tiny business that uses smart contracts, you never know who will call your code next. It could be a hacker seeking to exploit a flaw in your implementation, or it could be a little sketchy figure working for an unlawful organization or organized crime ring. You have no idea who you're dealing with until the code starts running, and to make matters worse, once the code starts running, there is no turning back. **Due to the immutability of the blockchain, you should always verify who you are dealing with before proceeding with an action**. Smart contracts are decentralized digital contracts that are executed automatically when predefined terms and circumstances are met.

## What it does

Presenting our solution Chain Guard which is a **managed security solution** driven by StepZen GraphQL andWweb3 that can be deployed on any blockchain. Chain Guard necessitates a high level of integration with upstream and downstream data sources. To meet this demand, StepZen can enhance existing analytics capabilities by improving your access to the capability of advanced analytics on connected data. The smart contract integrates with web3, which is the world's quickest and only enterprise-grade graph database. The graph solution stores the graphs of known malicious users. These **users are ranked according to a risk score** that indicates the likelihood of encountering them.

![chainguard - workflow](https://user-images.githubusercontent.com/107539208/179573908-3bee07cf-1910-403a-8ec1-3050e312362b.jpg)


## How we built it

The **smart contract integrates with web3**, which is the world's quickest and only enterprise-grade graph database. The graph solution stores the graphs of known malicious users. These users are ranked according to a risk score that indicates the likelihood of encountering them.

For instance, if the user has a history of **denial-of-service attacks** or recently stole money from another contract, your contract can automatically refuse to engage with them and quickly fail the action. Chain Guard adds a layer of security not only for developers, but also for users. We keep track of contracts that are running malware based on signatures and account information. 

We want to be as secure as possible, where contracts can state that I will engage with users who have established their identity with this trusted third party. In essence, we offer a scalable source of confidence to a trust-less environment.

The Chain Guard **Data Visualization** connections empower users with enhanced insights on linked data, helping them to maximize the value of their existing investments. 
The solution for protecting smart contracts must be secure itself. StepZen GraphQL understands that we require not only performance but also safety and security.
Due to the fact that the fees for smart contract execution are determined by the transaction and the speed of execution, the API response time must be extremely fast in order to minimize costs and optimize performance. We obtain results quickly using web3. Sub-second response time for queries involving the access and computation of tens of millions of entities and relationships.  Financial institutions can expedite transformation, leverage the power of data to generate efficiencies, and enhance the client experience by leveraging the potential of collaborative innovation.


## Challenges we ran into

Key challenges were real-time integration of blockchain smart contract along with External APIs. 

## Accomplishments that we're proud of

Managed security solution driven by web3 that can be deployed on any blockchain. Chain Guard necessitates secure a high level of integration with upstream and downstream data sources. 

## What's next for Chain Guard

Integrating Artificial Intelligence enabled solution to accurately predict the risk score of users who can post threat to blockchain world.


## API Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project configuration files for tools like git, prettier, eslint, husk, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

To start your application in the dev profile, run:

```
./gradlew
```

### CG Control Center

CG Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/cg-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the chainguard application for production, run:

```
./gradlew -Pprod clean bootJar
```

To ensure everything worked, run:

```
java -jar build/libs/*.jar
```

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./gradlew -Pprod -Pwar clean bootWar
```

## Testing

To launch your application's tests, run:

```
./gradlew test integrationTest jacocoTestReport
```

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the gradle plugin.

Then, run a Sonar analysis:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube
```


## Using Docker to simplify development (optional)

You can use Docker to improve your development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./gradlew bootJar -Pprod jibDockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```



