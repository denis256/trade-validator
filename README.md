Trade validation service
========================


Example API calls
=================

curl -X POST localhost:8080/api/validate

curl -H "Content-Type: application/json" -X POST -d '{"customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-15","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"}' localhost:8080/api/validate


curl http://localhost:8080/api/shutdown

curl -X POST http://localhost:8080/api/shutdown

http://localhost:8080/info