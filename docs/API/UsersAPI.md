# Users API (For Admin)

## Get Users (by Id)

+ Endpoint : ``/users``
+ HTTP Method : `GET`
+ Request Header : 
	+ Accept: `application/json`
    + Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": [{
        "id": 1,
        "name": "Andrew",
        "email": "example@email.com",
        "username": "exampleusername"
    }, {
        "id": 2,
        "name": "John Doe",
        "email": "johndoe@email.com",
        "username": "johndoe"
    }, {
        "id": 3,
        "name": "Johnny",
        "email": "jhonny@email.com",
        "username": "jhonny"
    }]
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid Request: You are not allowed to access.",
    "path": "/users"
}
```

## Get Users (by Id)

+ Endpoint : ``/users/{user-id}``
+ HTTP Method : `GET`
+ Path Variable : 
    + user-id
+ Request Header : 
	+ Accept: `application/json`
    + Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`
+ Response Body (Success) : 

```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": 1,
        "name": "Andrew",
        "email": "example@email.com",
        "username": "exampleusername"
    }
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid Request: You are not allowed to access.",
    "path": "/users/{user-id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "status": 404,
    "error": "Not Found",
    "message": "Invalid Request: Cannot find User with that Id.",
    "path": "/users/{user-id}"
}
```

## Add New User

+ Endpoint : ``/users``
+ HTTP Method : ``POST``
+ Request Body : 
```json
{
    "username": "andrewdudu",
    "email": "example@mail.com",
    "name": "Andrew Wijaya",
    "password": "admin123",
    "confirmPassword": "admin123"
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
        "name": "Andrew",
        "email": "example@email.com",
        "username": "exampleusername"
    }
}
```

+ Response Body (Fail) : 
```json
{
    "timestamp": "2019-08-23T04:22:26.690+0000",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid Request: Invalid request format",
    "path": "/users"
}
```

```json
{
    "timestamp": "2019-08-23T04:22:26.690+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid Request: Invalid user authentication or Unauthorized",
    "path": "/users"
}
```

## Edit Users by Id

+ Endpoint : ``/users/{user-id}``
+ HTTP Method : ``PUT``
+ Path Variable : 
    + user-id
+ Request Body : 
```json
{
    "username": "andrewdudu",
    "email": "example@mail.com",
    "name": "Andrew Wijaya",
    "password": "admin123",
    "confirmPassword": "admin123"
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
        "id": 1,
        "name": "Andrew",
        "email": "example@email.com",
        "username": "exampleusername"
    }
}
```

+ Response Body (Fail) : 

```json
{
	"status" : "400",
    "error" : "Bad Request",
    "message" : "Invalid Request: Invalid user authentication or invalid request format",
    "path" : "/users/{user-id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "status": 404,
    "error": "Not Found",
    "message": "Invalid Request: Cannot find User with that Id.",
    "path": "/users/{user-id}"
}
```

## Delete User by Id

+ Endpoint : ``/users/{user-id}``
+ HTTP Method : ``DELETE``
+ Path Variable : 
    + user-id
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
	"status" : "400",
    "error" : "Bad Request",
    "message" : "Invalid Request: Invalid user authentication or invalid request format",
    "path" : "/users/{user-id}"
}
```
```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "status": 404,
    "error": "Not Found",
    "message": "Invalid Request: Cannot find User with that Id.",
    "path": "/users/{user-id}"
}
```
