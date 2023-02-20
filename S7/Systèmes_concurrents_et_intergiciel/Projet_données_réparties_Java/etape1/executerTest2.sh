#!/bin/bash
pids=()
lecteurs()
{
javac TestLecteurs.java
local nblecteurs=100
local nbexecutions=10
for i in $(seq 1 $nbexecutions)
do
java TestLecteurs $nblecteurs &
pids+=($!)
done
}

echo "===========Script d'exécution lecteurs=============="
lecteurs

sleep 2
for i in $(seq 0 $nbexecutions) 
do
    kill -9 ${pids[i]}
done