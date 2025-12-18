# CLAUDE.md

Project guidance for AI assistants working on this codebase.

## Project Overview

Functional operations library for Java 17+. Simple for loops instead of streams for performance.

**One source file**: `src/main/java/com/github/lukaspestalozzi/functional/ListOps.java`

## Build Commands

```bash
# Build and test (the one command you need)
mvn clean verify

# Fix formatting violations
mvn spotless:apply

# Run benchmarks
./run-benchmarks.sh --quick
```

## Code Style

- **Google Java Format** via Spotless (2-space indent)
- Run `mvn spotless:apply` before committing
- Build fails on format violations

## Architecture Principles

Simple is better than complex. Explicit is better than implicit.

- Utility class with static methods only
- For loops over streams (performance)
- New lists returned, inputs never modified
- Null checks with clear error messages
- 80% test coverage minimum

## Testing

- JUnit 5 + AssertJ
- Given/When/Then structure
- Tests live in `src/test/java/`
- Benchmarks compare against Java Streams

## CI Pipeline

GitHub Actions runs `./mvnw -B clean verify` on:
- Ubuntu, Windows, macOS
- Java 17 and 21

## Workflow

**Always verify the build before pushing:**
```bash
mvn clean verify -Dmaven.resolver.transport=wagon
```

This ensures CI won't fail. Never push code that doesn't build locally.

## Proxy Workaround (Claude Code Environment)

If Maven fails with DNS resolution errors in restricted network environments:

```bash
# Use wagon transport with settings.xml proxy configuration
mvn clean verify -Dmaven.resolver.transport=wagon
```

Create `~/.m2/settings.xml` with proxy settings extracted from environment:
```xml
<settings>
  <proxies>
    <proxy>
      <id>https-proxy</id>
      <active>true</active>
      <protocol>https</protocol>
      <host>${PROXY_HOST}</host>
      <port>${PROXY_PORT}</port>
      <username>${PROXY_USER}</username>
      <password>${PROXY_PASS}</password>
    </proxy>
  </proxies>
</settings>
```

Extract credentials from `$HTTP_PROXY` environment variable if set.
