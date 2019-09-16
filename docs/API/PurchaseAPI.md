# Purchases API

## Get Purchases For Admin

+ Endpoint : ``/purchases``
+ HTTP Method : `GET`
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
        "userId": 1,
        "price": 13000,
        "productName": "Book 1",
        "productDescription": "Description Example",
        "productSku": "123-ABC",
        "productImage": "google.com/image",
        "authorName": "Andrew",
        "qty": 2
    }, {
        "id": 1,
        "userId": 1,
        "price": 13000,
        "productName": "Book 1",
        "productDescription": "Description Example",
        "productSku": "123-ABC",
        "productImage": "google.com/image",
        "authorName": "Andrew",
        "qty": 2
    }, {
        "id": 1,
        "userId": 1,
        "price": 13000,
        "productName": "Book 1",
        "productDescription": "Description Example",
        "productSku": "123-ABC",
        "productImage": "google.com/image",
        "authorName": "Andrew",
        "qty": 2
    }]
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Purchase with that Id.",
    "path": "/purchases"
}
```

## Get Purchases (by Id) For Admin

+ Endpoint : ``/purchases/{purchase-id}``
+ HTTP Method : `GET`
+ Path Variable : 
    + purchase-id
+ Request Header : 
	+ Accept: `application/json`
    + Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": 1,
        "userId": 1,
        "price": 13000,
        "productName": "Book 1",
        "productDescription": "Description Example",
        "productSku": "123-ABC",
        "productImage": "google.com/image",
        "authorName": "Andrew",
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
    "path": "/purchases/{purchase-id}"
}
```

## Get Purchases (by user id) For Admin and Users

+ Endpoint : ``/purchases/{purchaseId}/users/{userId}``
+ HTTP Method : `GET`
+ Path Variable : 
    + purchaseId (optional)
    + userId (optional)
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
        "productName": "Book 1",
        "productDescription": "Description Example",
        "productSku": "123-ABC",
        "productImage": "google.com/image",
        "authorName": "Andrew",
        "qty": 2
    }, {
        "id": 2,
        "price": 14000,
        "productName": "Book 2",
        "productDescription": "Description Example",
        "productSku": "123-ABD",
        "productImage": "google.com/image",
        "authorName": "Andrew",
        "qty": 2
    }, {
        "id": 3,
        "price": 15000,
        "productName": "Book 3",
        "productDescription": "Description Example",
        "productSku": "123-ABE",
        "productImage": "google.com/image",
        "authorName": "Andrew",
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
        "productName": "Book 1",
        "productDescription": "Description Example",
        "productSku": "123-ABC",
        "productImage": "google.com/image",
        "authorName": "Andrew",
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
```json
{
    "product-id": 1,
    "qty": 2
}
```
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
        "productName": "Book 1",
        "productDescription": "Description Example",
        "productSku": "123-ABC",
        "productImage": "google.com/image",
        "authorName": "Andrew",
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

+ Endpoint : ``/purchases/{purchase-id}``
+ HTTP Method : ``DELETE``
+ Path Variable : 
    + purchase-id
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
    "path" : "/purchases/{purchase-id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Purchase with that Id.",
    "path": "/purchases/{purchase-id}"
}
```
