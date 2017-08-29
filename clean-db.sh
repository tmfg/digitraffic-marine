#!/bin/bash
set -e
(
  cd ../digitraffic-ci-db
  ./clean-migrate-ais-vagrant.sh
)
