# Refactoring Journal

This document tracks the progress of the refactoring effort based on lessons learned.

## Overview

Implementing 10 improvement points identified during project retrospective.

## Progress

### Point 1: Reduce duplication between ListOps/SetOps
- **Status**: Pending
- **Goal**: Create shared internal implementation with collector/factory pattern
- **Notes**:

### Point 2: Remove SetOps.reverse()
- **Status**: Pending
- **Goal**: Remove meaningless reverse() method from SetOps
- **Notes**:

### Point 3: Rename mapFilter/filterMap
- **Status**: Pending
- **Goal**: Rename to mapThenFilter/filterThenMap for clarity
- **Notes**:

### Point 4: Add none() method
- **Status**: Pending
- **Goal**: Add none() as complement to any() and all()
- **Notes**:

### Point 5: Consolidate test files
- **Status**: Pending
- **Goal**: One test class per Ops class instead of per method
- **Notes**:

### Point 6: Top-level TriFunction
- **Status**: Pending
- **Goal**: Move TriFunction out of MapOps to dev.thelu.function
- **Notes**:

### Point 7: Simpler benchmark setup
- **Status**: Already Done
- **Goal**: Classpath-based JMH execution
- **Notes**: Already implemented with run-benchmarks.sh

### Point 8: Single source of truth for docs
- **Status**: Pending
- **Goal**: Reduce duplication between requirements.md and Javadocs
- **Notes**:

### Point 9: Change package name
- **Status**: Pending
- **Goal**: Rename from dev.thelu to dev.thelu.funcops
- **Notes**:

### Point 10: Explicit null handling in flatMap
- **Status**: Pending
- **Goal**: Document or throw on null mapper results
- **Notes**:

## Commit Log

| Point | Commit Hash | Description |
|-------|-------------|-------------|
| - | - | - |
