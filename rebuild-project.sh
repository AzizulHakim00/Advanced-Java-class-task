#!/usr/bin/env bash
set -euo pipefail
ROOT="${1:-Maze-Bank-Management-System}"
export ROOT
rm -rf "$ROOT"
for part in "$(dirname "$0")"/project-parts/part-*.sh; do
  bash "$part"
done
printf 'Project created at %s\n' "$ROOT"
