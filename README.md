# Scala http4s Tutorial Deploy on Blossom

A ready-to-deploy Scala http4s app to get you started quickly on [Blossom](https://blossom-cloud.com).

## Quick Start

```bash
# Install dependencies and build the application
sbt assembly

# Run the app
java -jar target/scala-3.3.3/quickstart-assembly-0.0.1-SNAPSHOT.jar
```

Visit `http://localhost:8080` in your browser to see the demo application.

You can also use `bin/web` which uses `sbt run` for development.

<details>
<summary>Additional Information</summary>

### Environment Variables
- `PORT`: Change the port (default: 8080)

### API Endpoints
```bash
# Get a greeting
curl http://localhost:8080/hello/world

# Get a random joke
curl http://localhost:8080/joke
```
</details>
