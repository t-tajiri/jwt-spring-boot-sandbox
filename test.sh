#!/bin/bash

curl -i -H "Content-Type: application/json" \
        -X POST -d '{ "username": "admin", "password": "password" }' \
        http://localhost:8090/signup

TOKEN=$(curl -is -H "Accept: application/json" \
                 -H "Content-Type: application/json" \
                 -X POST -d '{ "username": "admin", "password": "password" }' \
                 http://localhost:8090/login \
        | grep -Fi Authorization \
        | sed -e "s/Authorization: Bearer //")

curl -i -H "Accept: application/json" \
         -H "Authorization: Bearer ${TOKEN}" \
         -X GET \
         http://localhost:8090/example

