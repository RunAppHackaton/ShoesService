#!/bin/bash
gcloud run deploy $DOCKER_NAME_CONTAINER \
  --image=$GCP_REGION-docker.pkg.dev/$GCP_PROJECT_ID/$GCP_APP/$DOCKER_NAME_CONTAINER:${{ github.sha }} \
  --region=$GCP_REGION \
  --project=$GCP_PROJECT_ID \
  --port=$SHOES_SERVICE_SERVER_PORT \
  --add-cloudsql-instances=$SQL_INSTANCE_NAME \
  --service-account=$GCP_SQL_ACCOUNT_SERVICE_NAME \
  --env-vars-file=./secrets.json \
  --allow-unauthenticated