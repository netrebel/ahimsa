# Sample project

The code in this project is taken from:

[http://www.infoq.com/presentations/demo-java-javascript-mongodb/]

Technologies used:

* HTML5
* Angular.js
* Java
* MongoDB (mongojack)

# Run

This project can be run in two ways:

1. Deploy as a WAR file in Tomcat

## Test URLs

* GET http://localhost:8080/api/coffeeshop

* POST http://localhost:8080/api/coffeeshop/1/order

```
{
    "coffeeShopId" : 1,
    "type" : {
        "name" : "latte",
        "family": "coffee"
    },
    "size" : "large",
    "drinker": "Miguel"
}
```