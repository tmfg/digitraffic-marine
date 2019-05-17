#!/bin/bash
ssh -L 4433:oag.liikennevirasto.fi:443 dttestdaemon
# localhost:4433 -> dttestdaemon -> oag.liikennevirasto.fi:443