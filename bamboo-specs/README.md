### Boogie CI

The current Boogie CI pipeline is orchestrated via Bamboo Specs, a declarative way of defining a standard Bamboo plan as a single 
(or small collection of) YML files. Allowing us to declare our CI pipeline as code within the software repo in hopes of increasing 
visibility into how it works and ease of review when changes are made.

### Current process

The [Boogie CI plan](https://pandafood.mitre.org/browse/TTFS-SHIM) is hosted on the mitre-caasd pandafood. 

Currently there are three primary <b>Stages</b> in the Boogie CI pipeline which always occur, and one manually launch-able release one. 
The stages and their responsibilities are outlined in the table below.

| Stage | Jobs |
|:------|:-----|
| Test  | Unit Tests, Integration Tests |
| Push Latest | [Snapshot Jars](http://dali.mitre.org/nexus/#welcome), [Publish Docker Image](https://repo.codev.mitre.org/artifactory/webapp/#/artifacts/browse/tree/General/idaass-docker/tdp/boogie-rest/latest) |
| Deploy Latest | [Image to Internal Openshift](https://boogie-rest.apps.epic-osc.mitre.org/boogie/index.html) |
| <b>*(Optional)*</b> Push Release | [Release Jars](http://dali.mitre.org/nexus/#welcome), [Publish Docker Image](https://repo.codev.mitre.org/artifactory/webapp/#/artifacts/browse/tree/General/idaass-docker/tdp/boogie-rest/1.0.4-release-7b7be9c), [Update Code Quality](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)

Together this gives us essentially continuous releasing of main for images and internal REST API deployments, while allowing us 
to still perform more punctuated software and image releases for projects which want to leverage stable versions of either.
