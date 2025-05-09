# Scala http4s Tutorial Deploy on Blossom

[![Blossom Badge](https://img.boltops.com/images/blossom/logos/blossom-readme.png)](https://blossom-cloud.com)

A ready-to-deploy Scala http4s app to get you started quickly on [Blossom](https://blossom-cloud.com).

## Quick Start

```bash
# Install dependencies and build the application
sbt assembly

# Run the app
sbt run
```

Visit http://localhost:8080 in your browser to see the demo application.

You can also use `bin/web` which uses `sbt run` for development.

This is based on the [http4s quickstart](https://http4s.org/v0.23/docs/quickstart.html)

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
