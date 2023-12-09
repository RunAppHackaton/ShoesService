#!/bin/bash
docker buildx create --use
docker buildx build \
  --file ./.github/workflows/Dockerfile \
  --tag $DOCKER_USERNAME/$DOCKER_NAME_CONTAINER:$DOCKER_CONTAINER_TAG \
  --push .
