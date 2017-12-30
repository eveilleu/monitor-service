# monitor-service
Fans Entertainment - Coding Test for Architect Candidates


This service is bundled with SpringBoot.

To execute

./gradlew build
./gradlew bootRun

Sample calls:
to monitor the host api.test.paysafe.com

Start monitoring:
POST http://localhost:8080/monitor/start
{
	"hostname" : "api.test.paysafe.com",
	"interval" : 5000
}

Stop monitoring:
POST http://localhost:8080/monitor/stop
{
	"hostname" : "api.test.paysafe.com"
}

View monitoring data:
GET http://localhost:8080/monitor/monitoring_data?hostname=api.test.paysafe.com

