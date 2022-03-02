#!/bin/sh

[[ "$PWD" =~ scripts ]] && echo "PWD ends with scripts, going back a step, to repo root." && cd ..
echo "Current directory: $PWD (This should be the repo root)."
[ $(id -u) = 0 ] && echo "Do NOT run this as root. Run this without sudo please." && exit 1
[ ! -d ".git" ] && git init
git config core.hooksPath .githooks
git config core.editor :
