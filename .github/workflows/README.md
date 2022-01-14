### Boogie CI

The current Boogie CI pipeline is orchestrated via Github Actions, a declarative way of defining a standard Github workflow as a 
small collection of YML files. Allowing us to declare our CI pipeline as code within the software repo in hopes of increasing 
visibility into how it works and ease of review when changes are made.

### Current process

The [Boogie CI Actions](https://github.com/mitre-tdp/boogie/actions) are hosted next to the Github repo. 

Currently there are three primary software-release-based <b>Actions</b> which can be run for the Boogie repository. The actions and their responsibilities are outlined in the table below.

| Stage | Runs when? | Jobs |
|:------|:-----------|:-----|
| Test and Publish | <b>Tests</b> any time a commit is pushed to a branch, <b>Snapshot Jars + Latest Image</b> whenever a new commit is pushed/merged to main | This runs the unit and integration tests, deploys update snapshot jars to Codev, and deploys a new `latest` image to Codev as well |
| Release Image | Manually, on demand | This release a new image tagged with the current software version and the commit that it was built from |
| Release Software | Manually, on demand | This bumps the software version for the repo and then publishes a new set of release jars to Artifactory |
| Re-deploy API | On new commit to main | Coming Soon! (Via Concourse) |

Together this gives us essentially continuous releasing of main for images and internal REST API deployments, while allowing us 
to still perform more punctuated software and image releases for projects which want to leverage stable versions of either.

**Note** that all of the various `bash` scripts run by this CI process can be found [under scripts/ci](https://github.com/mitre-tdp/boogie/tree/main/scripts/ci) 
and that most of them should be adaptable to the needs of other projects if they want to do something similar.