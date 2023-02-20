#!/bin/bash
pids=()
ecrivains()
{
javac TestEcrivain.java
local nbecrivain=1000
local nbexecutions=10
for i in $(seq 1 $nbexecutions)
do
java TestEcrivain $nbecrivain &
pids+=($!)
done
}

echo "===========Script d'exécution écrivains=============="
ecrivains

sleep 2
for i in $(seq 0 $nbexecutions) 
do
    kill -9 ${pids[i]}
done