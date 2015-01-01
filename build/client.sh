#!/bin/bash
# Build the cfollow client
# Add API key as the first parameter
cd ..
mkdir -p client/schema
mkdir -p client/logs
\cp schema/configuration/*.json client/schema
\cp schema/communication/*.json client/schema
\cp schema/logging/*.json client/schema
sed -i "s/___API_KEY___/$1/g" client/config.json