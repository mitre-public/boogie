# Publish Artifacts to Maven Central

Publishing to maven central is not yet automated via CI/CD.
The following steps are required to publish the artifacts to maven central.

## Prerequisites

- Create a Sonatype account following [these steps](https://central.sonatype.org/register/legacy/)

- Email central-support@sonatype.com, copying someone with access to the `org.mitre` namespace on https://central.sonatype.com/, requesting to be added to the `org.mitre` namespace.
Ask the person you copied to reply-all to vouch for you.
Something like

```shell
I vouch for Matt and give permission for him to be added to the `org.mitre` namespace.
```

- Create, test, and distribute a GPG key.
You can follow the [offical docs](https://central.sonatype.org/publish/requirements/gpg/) or [these step from caasd-commons](https://github.com/mitre-public/commons/blob/main/docs/Setup_GPG.md).


## Publishing Steps

These docs are based on [the steps outlined in caasd-commons](https://github.com/mitre-public/commons/blob/main/docs/publish-to-maven-central.md#publishing).

1. Checkout a GitHub release tag
   ```shell
   git checkout tags/vX.Y.Z
   ```

2. Build the project, pom files, and `bundle.jar` for each artifact using
   ```shell
   export VERSION=<release-version>
   git checkout ${VERSION}
   ./publish-prep.sh arinc ${VERSION}
   ./publish-prep.sh conformance ${VERSION}
   ./publish-prep.sh core ${VERSION}
   ./publish-prep.sh routes ${VERSION}
   ```
   You may be prompted to enter your GPG passphrase.

3. Log into `oss.sonatype.org`
   - Select `Staging Upload` and provide your `bundle.jar` for each artifact
      - **ALERT:** Sometimes this upload produces an "upload failed" dialog even when the upload was successful
   - Select `Staging Repositories` and find the bundle you just uploaded. The artifact repo should list all the checks
     the bundle went through.
   


## Future Work

We may automate this process in the future, e.g., following [these steps](https://central.sonatype.org/publish/publish-gradle/)