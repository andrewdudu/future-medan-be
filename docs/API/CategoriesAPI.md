# Categories API

## Get Categories

+ Endpoint : ``/categories``
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
        "name": "Category 1",
        "description": "Example Description",
        "image": "google.com/image"
    }, {
        "id": 2,
        "name": "Category 2",
        "description": "Example Description",
        "image": "google.com/image"
    }, {
        "id": 3,
        "name": "Category 3",
        "description": "Example Description",
        "image": "google.com/image"
    }]
}
```

## Get Categories (by Id)

+ Endpoint : ``/categories/{category-id}``
+ HTTP Method : `GET`
+ Path Variable : 
    + category-id
+ Request Header : 
	+ Accept: `application/json`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": 2,
        "name": "Category 2",
        "description": "Example Description",
        "image": "google.com/image"
    }
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Category with that Id.",
    "path": "/categories/{category-id}"
}
```

## Add New Category (Admin)

+ Endpoint : ``/categories``
+ HTTP Method : ``POST``
+ Request Body : 
```json
{
    "name": "John Doe",
    "description": "Example Description",
    "image": "http://google.com/image1.jpeg"
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
        "id": 2,
        "name": "Category 2",
        "description": "Example Description",
        "image": "google.com/image"
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
    "path": "/categories"
}
```

```json
{
    "timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 401,
    "status": "Unauthorized",
    "message": "Invalid Request: Invalid user authentication or Unauthorized",
    "path": "/categories"
}
```

## Edit Categories by Id (Admin)

+ Endpoint : ``/categories/{category-id}``
+ HTTP Method : ``PUT``
+ Path Variable : 
    + category-id
+ Request Body : 
```json
{
    "name": "John Doe",
    "description": "Example Description",
    "image": "http://google.com/image.jpeg"
}
```
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
        "name": "Category 2",
        "description": "Example Description",
        "image": "google.com/image"
    }
}
```

+ Response Body (Fail) : 

```json
{
	"code" : "400",
    "status" : "Bad Request",
    "message" : "Invalid Request: Invalid user authentication or invalid request format",
    "path" : "/categories/{category-id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Category with that Id.",
    "path": "/categories/{category-id}"
}
```

## Delete Category by Id

+ Endpoint : ``/categories/{category-id}``
+ HTTP Method : ``DELETE``
+ Path Variable : 
    + category-id
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
    "path" : "/categories/{category-id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 404,
    "status": "Not Found",
    "message": "Invalid Request: Cannot find a Category with that Id.",
    "path": "/categories/{category-id}"
}
```
