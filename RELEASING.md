# Releasing to Maven Central

This document describes how to release new versions of the library to Maven Central.

## Prerequisites

Before you can release, you need to complete the following one-time setup:

### 1. Register Namespace on Sonatype Central Portal

1. Go to [central.sonatype.com](https://central.sonatype.com)
2. Sign in with GitHub
3. Register the namespace `dev.thelu`
4. Verify domain ownership via DNS TXT record or GitHub repository

### 2. Generate GPG Keys

```bash
# Generate a new RSA 4096-bit key
gpg --full-generate-key

# List keys to get your key ID
gpg --list-secret-keys --keyid-format LONG

# Export the private key (for GitHub Secrets)
gpg --armor --export-secret-keys YOUR_KEY_ID

# Publish public key to keyservers
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
```

### 3. Configure GitHub Secrets

Add the following secrets to your GitHub repository (Settings → Secrets and variables → Actions):

| Secret Name | Description |
|-------------|-------------|
| `CENTRAL_USERNAME` | Sonatype Central Portal username (from User Token) |
| `CENTRAL_PASSWORD` | Sonatype Central Portal password (from User Token) |
| `GPG_PRIVATE_KEY` | Full GPG private key (including headers) |
| `GPG_PASSPHRASE` | Passphrase for the GPG key |

To get Sonatype credentials:
1. Log in to [central.sonatype.com](https://central.sonatype.com)
2. Go to Account → Generate User Token
3. Use the generated username and password

## Releasing a New Version

### Using GitHub Actions (Recommended)

1. Go to Actions → "Release to Maven Central"
2. Click "Run workflow"
3. Select the version bump type:
   - **patch**: 1.0.0 → 1.0.1 (bug fixes)
   - **minor**: 1.0.0 → 1.1.0 (new features, backwards compatible)
   - **major**: 1.0.0 → 2.0.0 (breaking changes)
4. Click "Run workflow"

The workflow will:
1. Calculate the new version based on the current SNAPSHOT
2. Update pom.xml to the release version
3. Build, test, and verify
4. Sign artifacts with GPG
5. Deploy to Maven Central
6. Create and push a git tag
7. Create a GitHub Release
8. Update pom.xml to the next SNAPSHOT version

### Manual Release (Alternative)

If you need to release manually:

```bash
# Set the release version
./mvnw versions:set -DnewVersion=1.0.0 -DgenerateBackupPoms=false

# Build and deploy
./mvnw clean deploy -Prelease

# Tag the release
git add pom.xml
git commit -m "Release v1.0.0"
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

# Set next development version
./mvnw versions:set -DnewVersion=1.0.1-SNAPSHOT -DgenerateBackupPoms=false
git add pom.xml
git commit -m "Prepare next development iteration"
git push origin main
```

## Version Numbering

This project follows [Semantic Versioning](https://semver.org/):

- **MAJOR**: Incompatible API changes
- **MINOR**: New functionality (backwards compatible)
- **PATCH**: Bug fixes (backwards compatible)

## Troubleshooting

### GPG Signing Fails

If you see "gpg: signing failed: Inappropriate ioctl for device":
- The `--pinentry-mode loopback` argument is already configured in pom.xml
- Ensure `GPG_PASSPHRASE` secret is correctly set

### Deployment Fails

1. Verify your Sonatype credentials are valid
2. Check that your namespace is verified
3. Ensure GPG public key is published to keyservers
4. Wait a few minutes and retry (keyserver propagation)

### Artifacts Not Appearing on Maven Central

After successful deployment:
- Artifacts appear on [central.sonatype.com](https://central.sonatype.com) immediately
- Sync to [search.maven.org](https://search.maven.org) takes 15-30 minutes
