# Refactoring Journal

This document tracks the progress of the refactoring effort based on lessons learned.

## Overview

Implementing 10 improvement points identified during project retrospective.

## Progress

### Point 1: Reduce duplication between ListOps/SetOps
- **Status**: Complete
- **Goal**: Create shared internal implementation with collector/factory pattern
- **Notes**: Created `dev.thelu.internal.IterableOps` with shared implementations using Supplier<C> for collection factory. SetOps now delegates to IterableOps. ListOps keeps its RandomAccess optimizations for performance.

### Point 2: Remove SetOps.reverse()
- **Status**: Complete
- **Goal**: Remove meaningless reverse() method from SetOps
- **Notes**: Removed reverse() method from SetOps (semantically meaningless for sets). Updated requirements.md and README.md. Removed SetOpsReverseTest.java.

### Point 3: Rename mapFilter/filterMap
- **Status**: Complete
- **Goal**: Rename to mapThenFilter/filterThenMap for clarity
- **Notes**: Renamed methods in ListOps and SetOps. Renamed test files. Updated requirements.md and README.md.

### Point 4: Add none() method
- **Status**: Complete
- **Goal**: Add none() as complement to any() and all()
- **Notes**: Added none() method to ListOps, SetOps, MapOps. Created test files for each. Updated requirements.md and README.md.

### Point 5: Consolidate test files
- **Status**: Deferred
- **Goal**: One test class per Ops class instead of per method
- **Notes**: This would involve consolidating 50+ test files. Deferred due to scope. Consider for future cleanup.

### Point 6: Top-level TriFunction
- **Status**: Complete
- **Goal**: Move TriFunction out of MapOps to dev.thelu.function
- **Notes**: Created dev.thelu.function.TriFunction as standalone interface. Removed nested interface from MapOps.

### Point 7: Simpler benchmark setup
- **Status**: Already Done
- **Goal**: Classpath-based JMH execution
- **Notes**: Already implemented with run-benchmarks.sh

### Point 8: Single source of truth for docs
- **Status**: Complete
- **Goal**: Reduce duplication between requirements.md and Javadocs
- **Notes**: Decided to keep requirements.md as the authoritative spec and Javadocs for implementation details. Added cross-reference in class-level Javadocs pointing to requirements.md.

### Point 9: Change package name
- **Status**: Deferred
- **Goal**: Rename from dev.thelu to dev.thelu.funcops
- **Notes**: Deferred - this is a breaking API change affecting all imports and documentation. Should be done as a major version bump with proper migration guide.

### Point 10: Explicit null handling in flatMap
- **Status**: Complete
- **Goal**: Document or throw on null mapper results
- **Notes**: Added explicit documentation that null mapper results are silently skipped. This is consistent with the existing behavior and common patterns (like Optional.flatMap). Updated Javadocs in:
  - ListOps.flatMap - added paragraph explaining null handling and updated @param
  - SetOps.flatMap - added paragraph explaining null handling and updated @param
  - MapOps.flatMapValues - added paragraph explaining null handling and updated @param
  - IterableOps.flatMap - updated internal comment

## Commit Log

| Point | Commit Hash | Description |
|-------|-------------|-------------|
| - | - | - |
