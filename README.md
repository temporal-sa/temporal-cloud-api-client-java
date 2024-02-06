## Temporal Cloud Ops API Usage Example

[Temporal Cloud Ops API](https://docs.temporal.io/ops)

Uses [Temporal's API Protobufs](https://github.com/temporalio/api-cloud) and a Gradle plugin to compile at build time.

### Running the example:

* Create an API key in Temporal Cloud first. See the API docs above for instructions.
* Set a `TEMPORAL_CLOUD_API_KEY` environment variable to your API key.
* Run the server:
```
./gradlew -q bootRun -PmainClass=com.example.demo.DemoApplication
```

Then visit `http://localhost:8080/` to see a list of users and namespaces for your Temporal Cloud account.

*Debug Connection*
Run SSLTest.java to test the connection to the Temporal Cloud API.