# CLAUDE.md

Project guidance for AI assistants working on this codebase.

## Project Overview

Functional operations library for Java 17+. Simple for loops instead of streams for performance.

**Source files:**
- `src/main/java/dev/thelu/ListOps.java` - List operations
- `src/main/java/dev/thelu/SetOps.java` - Set operations
- `src/main/java/dev/thelu/MapOps.java` - Map operations

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

## Development Principles

### 1. Think Before Coding

Don't assume. Don't hide confusion. Surface tradeoffs.

Before implementing:

- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them — don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

### 2. Simplicity First

Minimum code that solves the problem. Nothing speculative.

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.
- Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

### 3. Surgical Changes

Touch only what you must. Clean up only your own mess.

When editing existing code:

- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it — don't delete it.

When your changes create orphans:

- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

### 4. Goal-Driven Execution

Define success criteria. Loop until verified.

Transform tasks into verifiable goals:

- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:

1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

These guidelines are working if: fewer unnecessary changes in diffs, fewer rewrites due to overcomplication, and clarifying questions come before implementation rather than after mistakes.

## Workflow

**Always verify the build before pushing:**
```bash
mvn clean verify -Dmaven.resolver.transport=wagon
```

Never push code that doesn't build locally.

**Always update documentation after implementing features:**
- Update `README.md` with new methods in the features list
- Update `requirements.md` with method signatures and descriptions
- Keep method counts accurate in project structure section

## Proxy Workaround (Claude Code Environment)

If Maven fails with DNS resolution errors:

```bash
mvn clean verify -Dmaven.resolver.transport=wagon
```

Create `~/.m2/settings.xml` with proxy settings if needed.
