#!/usr/bin/env bash
# Compiles and runs the unit tests (dependency-free harness, exits non-zero on failure).
set -euo pipefail
cd "$(dirname "$0")"

OUT="$(mktemp -d)"
trap 'rm -rf "$OUT"' EXIT

javac -d "$OUT" $(find src -name '*.java')
java -cp "$OUT" com.example.driving.AllTests
