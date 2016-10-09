Trade validation service
========================

Service which checks trade details 

Service is based on Gradle, Spring 4.3.3 and Spring Boot 1.4.1

Service built from modules which implement separated functionality.
 
 - trade-validator-model : model classes for trade, validation errors 
  
 - trade-validator-api : API interfaces to add new things into validation service
 
 - trade-validator-core : core services for trade validation
  
 - trade-validator-app : REST interface for validating trades
 
 - trade-validators : implementation of trade validation business rules


Each validation rule should implement `TradeValidator` interface and be a spring managed bean, during startup Spring will inject available validators into `ValidationCore`.
REST API controllers calls `ValidationCore` for validate trodes and return results.

Validators from `trade-validators` have pre-defined, hardcoded  settings, can load configurations from spring configurations and also expose JMX managed beans which allow settings change on runtime. 



API endpoints
=============

`/api/validate` - validate single trade, as result is returned trade and list of invalid fields

`/api/validateBulk` - bulk validate of a trade list, as result is returned list of trades and each trade have list of errors

`/api/shutdown` - shutdown endpoint, `GET` request to fetch status, `POST` to shutdown service. Shutdown status is also available through default `/info` endpoint. When shutdown status is set, service no more accept trades for validation.

`/metrics` - fetch  application metrics 

`/swagger-ui.html#/validation-controller` - swagger Rest API documentation



Example API calls
=================

**Validate single trade**

Request

`curl -v   -H "Content-Type: application/json"   -X POST --data "@./raw_trades/single_trade.json" localhost:8080/api/validate`

Response

```
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /api/validate HTTP/1.1
> User-Agent: curl/7.35.0
> Host: localhost:8080
> Accept: */*
> Content-Type: application/json
> Content-Length: 232
> 
* upload completely sent off: 232 out of 232 bytes
< HTTP/1.1 200 
< X-Application-Context: application
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Date: Sun, 09 Oct 2016 09:40:17 GMT
< 
* Connection #0 to host localhost left intact
{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-15","customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On spot trades valueDate should be +2 days from today date"]},"haveErrors":true}
```


**Bulk trades validation**

Request 

`curl -v   -H "Content-Type: application/json"   -X POST --data "@./raw_trades/bulk_trades.json" localhost:8080/api/validateBulk`

Response

```
binary256@binary256-host /r/p/u/p/j/n/trade-validator> curl -v   -H "Content-Type: application/json"   -X POST --data "@./raw_trades/bulk_trades.json" localhost:8080/api/validateBulk
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /api/validateBulk HTTP/1.1
> User-Agent: curl/7.35.0
> Host: localhost:8080
> Accept: */*
> Content-Type: application/json
> Content-Length: 5089
> Expect: 100-continue
> 
< HTTP/1.1 100 Continue
< HTTP/1.1 200 
< X-Application-Context: application
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Date: Sun, 09 Oct 2016 09:43:29 GMT
< 
[{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-15","customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On spot trades valueDate should be +2 days from today date"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-22","customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On spot trades valueDate should be +2 days from today date"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-22","customer":"PLUTO2","ccyPair":"EURUSD","type":"Forward","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On forward trades valueDate should be more than 2 days"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-21","customer":"PLUTO2","ccyPair":"EURUSD","type":"Forward","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On forward trades valueDate should be more than 2 days","valueDate is holiday date"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-08","customer":"PLUTO2","ccyPair":"EURUSD","type":"Forward","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On forward trades valueDate should be more than 2 days","Value date cannot be before Trade date"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-08","customer":"PLUT02","ccyPair":"EURUSD","type":"Forward","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On forward trades valueDate should be more than 2 days","Value date cannot be before Trade date"],"customer":["Customer is not in approved list"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":"2016-08-22","customer":"PLUTO3","ccyPair":"EURUSD","type":"Forward","style":null,"excerciseStartDate":null,"expiryDate":null,"premiumDate":null,"deliveryDate":null,"legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["On forward trades valueDate should be more than 2 days"],"customer":["Customer is not in approved list"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","style":"EUROPEAN","excerciseStartDate":null,"expiryDate":"2016-08-19","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["valueDate is missing"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO2","ccyPair":"EURUSD","type":"VanillaOption","style":"EUROPEAN","excerciseStartDate":null,"expiryDate":"2016-08-21","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["valueDate is missing"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","style":"EUROPEAN","excerciseStartDate":null,"expiryDate":"2016-08-25","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{"expiryDate":["expiry date should be before delivery date"],"valueDate":["valueDate is missing"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","style":"AMERICAN","excerciseStartDate":"2016-08-12","expiryDate":"2016-08-19","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["valueDate is missing"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO2","ccyPair":"EURUSD","type":"VanillaOption","style":"AMERICAN","excerciseStartDate":"2016-08-12","expiryDate":"2016-08-21","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{"valueDate":["valueDate is missing"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","style":"AMERICAN","excerciseStartDate":"2016-08-12","expiryDate":"2016-08-25","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{"expiryDate":["expiry date should be before delivery date"],"valueDate":["valueDate is missing"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","style":"AMERICAN","excerciseStartDate":"2016-08-10","expiryDate":"2016-08-19","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{* Connection #0 to host localhost left intact
"excerciseStartDate":["excerciseStartDate should be after trade date"],"valueDate":["valueDate is missing"]},"haveErrors":true},{"trade":{"tradeDate":"2016-08-11","valueDate":null,"customer":"PLUTO3","ccyPair":"EURUSD","type":"VanillaOption","style":"AMERICAN","excerciseStartDate":"2016-08-10","expiryDate":"2016-08-19","premiumDate":"2016-08-12","deliveryDate":"2016-08-22","legalEntity":"CS Zurich"},"invalidFields":{"excerciseStartDate":["excerciseStartDate should be after trade date"],"valueDate":["valueDate is missing"],"customer":["Customer is not in approved list"]},"haveErrors":true}]
```


**Fetch shutdown status**

Request 
`curl http://localhost:8080/api/shutdown`

Response
```
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /api/shutdown HTTP/1.1
> User-Agent: curl/7.35.0
> Host: localhost:8080
> Accept: */*
> 
< HTTP/1.1 200 
< X-Application-Context: application
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Date: Sun, 09 Oct 2016 09:45:08 GMT
< 
* Connection #0 to host localhost left intact
false⏎                                            
```

**Set shutdown flag**
 
 Request
 `curl -v -X POST http://localhost:8080/api/shutdown`
 
 Response
```
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /api/shutdown HTTP/1.1
> User-Agent: curl/7.35.0
> Host: localhost:8080
> Accept: */*
> 
< HTTP/1.1 200 
< X-Application-Context: application
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Date: Sun, 09 Oct 2016 09:46:29 GMT
< 
* Connection #0 to host localhost left intact
true⏎ 
```
