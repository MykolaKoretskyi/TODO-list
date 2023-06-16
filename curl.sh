#!/bin/bash

curl --location --request GET 'http://springbootapplication-env.eba-hg4ebfam.eu-central-1.elasticbeanstalk.com/task' \
--header 'Authorization: Basic YWRtaW46MjAw'




curl --location --request GET 'http://springbootapplication-env.eba-hg4ebfam.eu-central-1.elasticbeanstalk.com/task/4' \
--header 'Authorization: Basic YWRtaW46MjAw'




curl --location --request POST 'http://springbootapplication-env.eba-hg4ebfam.eu-central-1.elasticbeanstalk.com/task' \
--header 'Authorization: Basic YWRtaW46MjAw' \
--header 'Content-Type: application/json' \
--data '{
        
        "what": "Finish all work",
        "executionDate": "2023-05-07",
        "status": "PLANNED"
    }'
	
	
	
curl --location --request PUT 'http://springbootapplication-env.eba-hg4ebfam.eu-central-1.elasticbeanstalk.com/task/2' \
--header 'Authorization: Basic YWRtaW46MjAw' \
--header 'Content-Type: application/json' \
--data '{
        
        "what": "Finish all work",
        "executionDate": "2023-05-07",
        "status": "PLANNED"
    }'
	
	
	
	
curl --location --request PUT 'http://springbootapplication-env.eba-hg4ebfam.eu-central-1.elasticbeanstalk.com/task/2/status/next' \
--header 'Authorization: Basic YWRtaW46MjAw' \
--header 'Content-Type: application/json'
	
	
	
	
curl --location --request DELETE 'http://springbootapplication-env.eba-hg4ebfam.eu-central-1.elasticbeanstalk.com/task/3' \
--header 'Authorization: Basic YWRtaW46MjAw'
	
	
	
	
curl --location --request DELETE 'http://springbootapplication-env.eba-hg4ebfam.eu-central-1.elasticbeanstalk.com/task/delete/all' \
--header 'Authorization: Basic YWRtaW46MjAw'