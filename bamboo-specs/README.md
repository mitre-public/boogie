## Boogie CI

The current Boogie CI pipeline is orchestrated via Bamboo Specs, a declarative way of defining a standard Bamboo plan as a single 
(or small collection of) YML files. This allows us to declare our CI pipeline as code within the software repo in hopes of 
increasing visibility into how it works and ease of review when changes are made.

### Outlining the current tasks

Currently there are three primary Stages in the Boogie CI pipeline:

1. Running the unit and integration-level tests
2. Updating [Sonar](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie), publishing new snapshots, and publishing a new 
image for the REST endpoint
3. Updating the [internal deployment of the REST endpoint](https://boogie-rest.apps.epic-osc.mitre.org/boogie/index.html) to be 
backed by this latest image (i.e. the internal deployment is always using the latest code)  

on top of those there is a final manually-triggerable stage which kicks off a release stage which:

1. Bumps the software version to a release version and publishes release jars to MITRE DALI
2. Creates and publishes a new image with a concrete image version tied to the software release version and the release commit

Together these CI tasks ensure whenever changes are made to the codebase or releases go live all Boogie controlled and owned 
downstream resources which clients may be depending on are updated. 