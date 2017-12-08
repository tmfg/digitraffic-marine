#!/bin/bash
ssh -L 6180:webgis-ais.vally.local:6180 dtprodhotdaemon
# localhost:6180 -> dtprodhotdaemon -> webgis-ais.vally.local:6180