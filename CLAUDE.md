# CLAUDE.md

Project guidance for AI assistants working on this codebase.

## Project Overview

Functional operations library for Java 17+. Simple for loops instead of streams for performance.

**Source files:**
- `src/main/java/com/github/lukaspestalozzi/functional/ListOps.java` - List operations
- `src/main/java/com/github/lukaspestalozzi/functional/SetOps.java` - Set operations
- `src/main/java/com/github/lukaspestalozzi/functional/MapOps.java` - Map operations

## Build Commands

```bash
# Build and test
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

- Utility classes with static methods only
- For loops over streams (performance)
- New collections returned, inputs never modified
- Null checks with clear error messages
- 80% test coverage minimum
- One test file per method (e.g., `ListOpsMapTest.java`)

## Testing

- JUnit 5 + AssertJ
- Given/When/Then structure
- Tests in `src/test/java/` with pattern `{Class}{Method}Test.java`
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

Never push code that doesn't build locally.

## Proxy Workaround (Claude Code Environment)

If Maven fails with DNS resolution errors:

```bash
mvn clean verify -Dmaven.resolver.transport=wagon
```

Create `~/.m2/settings.xml` with proxy settings if needed.
