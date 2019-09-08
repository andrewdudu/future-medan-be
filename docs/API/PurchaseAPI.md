# Purchases API

## Get Purchases (by Id) For Admin

+ Endpoint : ``/purchases/{id}``
+ HTTP Method : `GET`
+ Request param : 
    + id (optional)
+ Request Header : 
	+ Accept: `application/json`
    + Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": [{
        "id": 1,
        "user_id": 1,
        "price": 13000,
        "product_name": "Book 1",
        "product_description": "Description Example",
        "product_sku": "123-ABC",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }, {
        "id": 1,
        "user_id": 1,
        "price": 13000,
        "product_name": "Book 1",
        "product_description": "Description Example",
        "product_sku": "123-ABC",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }, {
        "id": 1,
        "user_id": 1,
        "price": 13000,
        "product_name": "Book 1",
        "product_description": "Description Example",
        "product_sku": "123-ABC",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }]
}
```
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": 1,
        "user_id": 1,
        "price": 13000,
        "product_name": "Book 1",
        "product_description": "Description Example",
        "product_sku": "123-ABC",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Purchase with that Id.",
    "path": "/purchases/{id}"
}
```

## Get Purchases (by user id) For Admin and Users

+ Endpoint : ``/purchases/{purchaseId}/users/{userId}`` (purchaseId is optional)
+ HTTP Method : `GET`
+ Request param : 
    + id (optional)
+ Request Header : 
	+ Accept: `application/json`
    + Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": [{
        "id": 1,
        "price": 13000,
        "product_name": "Book 1",
        "product_description": "Description Example",
        "product_sku": "123-ABC",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }, {
        "id": 2,
        "price": 14000,
        "product_name": "Book 2",
        "product_description": "Description Example",
        "product_sku": "123-ABD",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }, {
        "id": 3,
        "price": 15000,
        "product_name": "Book 3",
        "product_description": "Description Example",
        "product_sku": "123-ABE",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }]
}
```
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": 1,
        "price": 13000,
        "product_name": "Book 1",
        "product_description": "Description Example",
        "product_sku": "123-ABC",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Purchase with that Id.",
    "path": "/purchases/{purchaseId}/users/{id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Purchase with that User Id.",
    "path": "/purchases/users/{id}"
}
```

## Add New Purchase (Users)

+ Endpoint : ``/purchases``
+ HTTP Method : ``POST``
+ Request Body : 
	+ id
    + product_id
    + qty
+ Request Header : 
	+ Accept : ``application/json``
    + Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`
+ Response Body (Success) :

```json
{
	"code": 200,
    "status": "OK",
    "data": {
        "id": 1,
        "price": 13000,
        "product_name": "Book 1",
        "product_description": "Description Example",
        "product_sku": "123-ABC",
        "product_image": "google.com/image",
        "author_name": "Andrew",
        "qty": 2
    }
}
```

+ Response Body (Fail) : 
```json
{
    "timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 400,
    "status": "Bad Request",
    "message": "Invalid Request: Invalid request format",
    "path": "/purchases"
}
```

```json
{
    "timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 401,
    "status": "Unauthorized",
    "message": "Invalid Request: Invalid user authentication or Unauthorized",
    "path": "/purchases"
}
```

## Delete Purchase by Id

+ Endpoint : ``/purchases/{id}``
+ HTTP Method : ``DELETE``
+ Request param : 
    + id (user id)
+ Request Header : 
	+ Accept : ``application/json``
	+ Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjYyNDAyMjAsImlhdCI6MTU2NjIyMjIyMH0.sYLqMuG2Zr7zDEdK4YIIYX7WfTcroxl7Edc_YLU0dWncPliHfbgEfMYLoorYA_d01hPFF_fZhAyxLTIJYBRHuw`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK"
}
```

+ Response Body (Fail) : 

```json
{
	"code" : "400",
    "status" : "Bad Request",
    "message" : "Invalid Request: Invalid user authentication or invalid request format",
    "path" : "/purchases/{id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Purchase with that Id.",
    "path": "/purchases/{id}"
}
```
