# TRAPCOUNT REPORT
## RIAC
### Documentation
Trapcount Report is a service to generate a pdf report for Trapcount system and send by email the pdf report

* [Design](https://riac.atlassian.net/wiki/spaces/TC/pages/275152939/Reports+and+delivery+emails)

### Pre Installation
This project should be work with **java 11**, **maven 3.6** and run by default on **8080** port. To connect with the database the IP should be add into the **whitelist** on the _Azure database instance_

### Installation
Clone the Bitbucket repository and use maven to compile and download the dependencies

[repository](https://riac-ricardo@bitbucket.org/riacdev/trapcount-report.git)
```
$ git clone https://riac-ricardo@bitbucket.org/riacdev/trapcount-report.git
$ cd trapcount-report
$ mvn clean package
$ mvn spring-boot:run
```

* ricardo.zaragoza@riac.dev
* carlos.zaragoza@riac.dev

## DEPLOY DEV
````
mvn clean package -Pdev azure-webapp:deploy
````

## LOGS DEVELOP ENVIRONMENT
````
az webapp log tail --name trapcount-report-1615176426823 --resource-group trapcount-report-1615176426823-rg
````