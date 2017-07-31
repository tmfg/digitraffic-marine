#!/bin/bash
ssh -L 18080:172.17.206.134:80 dttestdaemon
# localhost:18080 -> dttestdaemon -> 172.17.206.134:80