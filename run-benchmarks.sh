#!/bin/bash

# Performance Benchmark Runner Script
# Usage: ./run-benchmarks.sh [options]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default options
WARMUP_ITERATIONS=3
MEASUREMENT_ITERATIONS=5
FORKS=2
THREADS=1
GC_PROFILER=false
QUICK_MODE=false

print_usage() {
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  -q, --quick          Quick mode (fewer iterations)"
    echo "  -g, --gc             Enable GC profiler for memory allocation tracking"
    echo "  -t, --threads N      Number of threads (default: 1)"
    echo "  -w, --warmup N       Warmup iterations (default: 3)"
    echo "  -m, --measurement N  Measurement iterations (default: 5)"
    echo "  -f, --forks N        Number of forks (default: 2)"
    echo "  -h, --help           Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                   # Run full benchmarks"
    echo "  $0 -q                # Quick benchmark run"
    echo "  $0 -g                # Run with GC profiler"
    echo "  $0 -q -g             # Quick run with GC profiler"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -q|--quick)
            QUICK_MODE=true
            WARMUP_ITERATIONS=2
            MEASUREMENT_ITERATIONS=3
            FORKS=1
            shift
            ;;
        -g|--gc)
            GC_PROFILER=true
            shift
            ;;
        -t|--threads)
            THREADS="$2"
            shift 2
            ;;
        -w|--warmup)
            WARMUP_ITERATIONS="$2"
            shift 2
            ;;
        -m|--measurement)
            MEASUREMENT_ITERATIONS="$2"
            shift 2
            ;;
        -f|--forks)
            FORKS="$2"
            shift 2
            ;;
        -h|--help)
            print_usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            print_usage
            exit 1
            ;;
    esac
done

echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}Performance Benchmark Runner${NC}"
echo -e "${BLUE}================================${NC}"
echo ""

# Print configuration
echo -e "${YELLOW}Configuration:${NC}"
echo "  Warmup iterations: $WARMUP_ITERATIONS"
echo "  Measurement iterations: $MEASUREMENT_ITERATIONS"
echo "  Forks: $FORKS"
echo "  Threads: $THREADS"
echo "  GC Profiler: $GC_PROFILER"
echo "  Quick mode: $QUICK_MODE"
echo ""

# Build benchmark JAR
echo -e "${BLUE}Step 1: Building benchmark JAR...${NC}"
./mvnw -B clean package -Pbenchmark -DskipTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build successful${NC}"
else
    echo -e "${RED}✗ Build failed${NC}"
    exit 1
fi
echo ""

# Build JMH arguments
JMH_ARGS=".* -rf json -rff target/jmh-result.json"
JMH_ARGS="$JMH_ARGS -wi $WARMUP_ITERATIONS -i $MEASUREMENT_ITERATIONS -f $FORKS -t $THREADS"

if [ "$GC_PROFILER" = true ]; then
    JMH_ARGS="$JMH_ARGS -prof gc"
fi

# Run benchmarks
echo -e "${BLUE}Step 2: Running benchmarks...${NC}"
echo "This may take several minutes..."
echo ""

java -jar target/benchmarks.jar $JMH_ARGS

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✓ Benchmarks completed successfully!${NC}"
    echo ""

    # Display results location
    echo -e "${BLUE}Results:${NC}"
    echo "  JSON output: target/jmh-result.json"

    if [ -f "target/jmh-result.json" ]; then
        echo ""
        echo -e "${BLUE}Quick Summary:${NC}"
        echo "  Use 'jq' to parse results, e.g.:"
        echo "    cat target/jmh-result.json | jq '.[] | {benchmark: .benchmark, score: .primaryMetric.score}'"
    fi

    echo ""
    echo -e "${GREEN}================================${NC}"
    echo -e "${GREEN}Benchmark run completed!${NC}"
    echo -e "${GREEN}================================${NC}"
else
    echo ""
    echo -e "${RED}✗ Benchmarks failed${NC}"
    exit 1
fi
