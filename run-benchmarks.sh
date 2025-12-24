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
BENCHMARK_PATTERN=".*"
OUTPUT_FILE=""

print_usage() {
    echo "Usage: $0 [options] [pattern]"
    echo ""
    echo "Options:"
    echo "  -q, --quick          Quick mode (fewer iterations)"
    echo "  -g, --gc             Enable GC profiler for memory allocation tracking"
    echo "  -o, --output FILE    Output JSON file (default: target/jmh-result.json)"
    echo "  -t, --threads N      Number of threads (default: 1)"
    echo "  -w, --warmup N       Warmup iterations (default: 3)"
    echo "  -m, --measurement N  Measurement iterations (default: 5)"
    echo "  -f, --forks N        Number of forks (default: 2)"
    echo "  -h, --help           Show this help message"
    echo ""
    echo "Arguments:"
    echo "  pattern              JMH benchmark pattern (default: .* for all)"
    echo ""
    echo "Examples:"
    echo "  $0                   # Run all benchmarks"
    echo "  $0 -q                # Quick benchmark run"
    echo "  $0 -g                # Run with GC profiler"
    echo "  $0 -q -g             # Quick run with GC profiler"
    echo "  $0 \".*map.*\"         # Run only map benchmarks"
    echo "  $0 -q \".*filter.*\"   # Quick run for filter benchmarks"
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
        -o|--output)
            OUTPUT_FILE="$2"
            shift 2
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
        -*)
            echo "Unknown option: $1"
            print_usage
            exit 1
            ;;
        *)
            BENCHMARK_PATTERN="$1"
            shift
            ;;
    esac
done

echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}Performance Benchmark Runner${NC}"
echo -e "${BLUE}================================${NC}"
echo ""

# Determine output file early for display
if [ -z "$OUTPUT_FILE" ]; then
    if [ "$GC_PROFILER" = true ]; then
        OUTPUT_FILE="target/jmh-memory-result.json"
    else
        OUTPUT_FILE="target/jmh-result.json"
    fi
fi

# Print configuration
echo -e "${YELLOW}Configuration:${NC}"
echo "  Warmup iterations: $WARMUP_ITERATIONS"
echo "  Measurement iterations: $MEASUREMENT_ITERATIONS"
echo "  Forks: $FORKS"
echo "  Threads: $THREADS"
echo "  GC Profiler: $GC_PROFILER"
echo "  Quick mode: $QUICK_MODE"
echo "  Pattern: $BENCHMARK_PATTERN"
echo "  Output: $OUTPUT_FILE"
echo ""

# Compile project (clean to ensure annotation processor runs)
echo -e "${BLUE}Step 1: Compiling project...${NC}"
./mvnw -B clean compile test-compile -q
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation successful${NC}"
else
    echo -e "${RED}✗ Compilation failed${NC}"
    exit 1
fi

# Verify BenchmarkList was generated
BENCHMARK_LIST="target/test-classes/META-INF/BenchmarkList"
if [ ! -f "$BENCHMARK_LIST" ]; then
    echo -e "${RED}✗ BenchmarkList not found at $BENCHMARK_LIST${NC}"
    echo "The JMH annotation processor may not have run."
    echo "Contents of target/test-classes/META-INF/:"
    ls -la target/test-classes/META-INF/ 2>/dev/null || echo "  Directory does not exist"
    exit 1
fi
echo -e "${GREEN}✓ BenchmarkList generated${NC}"
echo ""

# Find JMH dependencies in local Maven repository
M2_REPO="${HOME}/.m2/repository"
JMH_VERSION="1.37"

JMH_CORE="${M2_REPO}/org/openjdk/jmh/jmh-core/${JMH_VERSION}/jmh-core-${JMH_VERSION}.jar"
JOPT="${M2_REPO}/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"
MATH3="${M2_REPO}/org/apache/commons/commons-math3/3.6.1/commons-math3-3.6.1.jar"

# Verify dependencies exist
for dep in "$JMH_CORE" "$JOPT" "$MATH3"; do
    if [ ! -f "$dep" ]; then
        echo -e "${RED}Missing dependency: $dep${NC}"
        echo "Running Maven dependency resolution..."
        ./mvnw -B dependency:resolve -q
    fi
done

# Build classpath
CLASSPATH="target/classes:target/test-classes:${JMH_CORE}:${JOPT}:${MATH3}"

# Build JMH arguments
JMH_ARGS="${BENCHMARK_PATTERN} -rf json -rff ${OUTPUT_FILE}"
JMH_ARGS="$JMH_ARGS -wi $WARMUP_ITERATIONS -i $MEASUREMENT_ITERATIONS -f $FORKS -t $THREADS"

if [ "$GC_PROFILER" = true ]; then
    JMH_ARGS="$JMH_ARGS -prof gc"
fi

# Run benchmarks
echo -e "${BLUE}Step 2: Running benchmarks...${NC}"
echo "This may take several minutes..."
echo ""

java -cp "$CLASSPATH" org.openjdk.jmh.Main $JMH_ARGS

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✓ Benchmarks completed successfully!${NC}"
    echo ""

    # Display results location
    echo -e "${BLUE}Results:${NC}"
    echo "  JSON output: $OUTPUT_FILE"

    if [ -f "$OUTPUT_FILE" ]; then
        echo ""
        echo -e "${BLUE}Quick Summary:${NC}"
        echo "  Use 'jq' to parse results, e.g.:"
        echo "    cat $OUTPUT_FILE | jq '.[] | {benchmark: .benchmark, score: .primaryMetric.score}'"
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
