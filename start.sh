#!/usr/bin/env bash
# Builds and launches the driving simulation.
# Requires only a JDK (javac + java), Java 8 or newer. No other dependencies.
set -euo pipefail
cd "$(dirname "$0")"

OUT="$(mktemp -d)"
trap 'rm -rf "$OUT"' EXIT

javac -d "$OUT" $(find src/main -name '*.java')
java -cp "$OUT" com.example.driving.Main
