#!/bin/bash
pids=()
LecteursEcrivains()
{
javac TestMix.java
local nblecteurs=100
local nbexecutions=10
local increment=0
local val=1
for i in $(seq 1 $nbexecutions)
do
increment=$(($increment+$val))
java TestMix $nblecteurs $increment &
pids+=($!)
done
}

echo "===========Script d'exécution test mix avancé=============="
LecteursEcrivains

sleep 2
for i in $(seq 0 $nbexecutions) 
do
    kill -9 ${pids[i]}
done