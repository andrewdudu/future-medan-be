# Products API

## Get Products (by Id)

+ Endpoint : ``/products/{id}``
+ HTTP Method : `GET`
+ Request param : 
    + id (optional)
+ Request Header : 
	+ Accept: `application/json`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": [{
        "id": 1,
        "name": "Book 1",
        "sku": "1AB-H4M",
        "description": "Example Description",
        "price": 13000,
        "image": "google.com/image",
        "author": 1
    }, {
        "id": 2,
        "name": "Book 2",
        "sku": "1AC-H4M",
        "description": "Example Description",
        "price": 55000,
        "image": "google.com/image",
        "author": 1
    }, {
        "id": 3,
        "name": "Book 3",
        "sku": "1AD-H4M",
        "description": "Example Description",
        "price": 105000,
        "image": "google.com/image",
        "author": 1
    }]
}
```
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": 2,
        "name": "Book 2",
        "sku": "1AC-H4M",
        "description": "Example Description",
        "price": 55000,
        "image": "google.com/image",
        "author": 1
    }
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Product with that Id.",
    "path": "/products/{id}"
}
```

## Get Bestseller Products

+ Endpoint : ``/products/best-seller``
+ HTTP Method : `GET`
+ Request Header : 
	+ Accept: `application/json`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": [{
        "id": 1,
        "name": "Book 1",
        "sku": "1AB-H4M",
        "description": "Example Description",
        "price": 13000,
        "image": "google.com/image",
        "author": 1
    }, {
        "id": 2,
        "name": "Book 2",
        "sku": "1AC-H4M",
        "description": "Example Description",
        "price": 55000,
        "image": "google.com/image",
        "author": 1
    }, {
        "id": 3,
        "name": "Book 3",
        "sku": "1AD-H4M",
        "description": "Example Description",
        "price": 105000,
        "image": "google.com/image",
        "author": 1
    }]
}
```

## Add New Product (Admin or Merchant)

+ Endpoint : ``/products``
+ HTTP Method : ``POST``
+ Request Body : 
	+ name
    + sku
    + description
	+ price
    + image
+ Request Header : 
	+ Accept : ``application/json``
    + Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`
+ Response Body (Success) :

```json
{
	"code": 200,
    "status": "OK",
    "data": {
        "id": 2,
        "name": "Book 2",
        "sku": "1AC-H4M",
        "description": "Example Description",
        "price": 55000,
        "image": "google.com/image",
        "author": 1
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
    "path": "/products"
}
```

```json
{
    "timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 401,
    "status": "Unauthorized",
    "message": "Invalid Request: Invalid user authentication or Unauthorized",
    "path": "/products"
}
```

## Edit Products by Id (Admin or Merchant)

+ Endpoint : ``/products/{id}``
+ HTTP Method : ``PUT``
+ Request param : 
    + id (user id)
+ Request Body : 
    + name
    + sku
    + description
    + price
    + image
+ Request Header : 
	+ Accept : ``application/json``
	+ Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjYyNDAyMjAsImlhdCI6MTU2NjIyMjIyMH0.sYLqMuG2Zr7zDEdK4YIIYX7WfTcroxl7Edc_YLU0dWncPliHfbgEfMYLoorYA_d01hPFF_fZhAyxLTIJYBRHuw`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": 2,
        "name": "Book 2",
        "sku": "1AC-H4M",
        "description": "Example Description",
        "price": 55000,
        "image": "google.com/image",
        "author": 1
    }
}
```

+ Response Body (Fail) : 

```json
{
	"code" : "400",
    "status" : "Bad Request",
    "message" : "Invalid Request: Invalid user authentication or invalid request format",
    "path" : "/products/{id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Product with that Id.",
    "path": "/products/{id}"
}
```

## Delete User by Id

+ Endpoint : ``/products/{id}``
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
    "path" : "/products/{id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Product with that Id.",
    "path": "/products/{id}"
}
```
