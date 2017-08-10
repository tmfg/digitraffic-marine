#!/bin/bash
ssh -L 6180:webgis-test.vally.local:6180 dttestdaemon
# localhost:6180 -> dttestdaemon -> webgis-test.vally.local:6180