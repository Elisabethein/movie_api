#!/bin/sh
#Wait for PostgreSQL to be ready
until pg_isready -h localhost -p 5432 -U postgres -d postgres -t 1; do
  echo "Waiting for PostgreSQL to be ready..."
  sleep 2
done

#Once PostgreSQL is ready, run the Java application
java -jar MovieApiApplication.jar