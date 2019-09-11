# Authentication API

## User Sign Up

+ Endpoint : ``/user/register``
+ HTTP Method : `POST`
+ Request Body :
```json
{
	"name": "Andrew",
	"username": "andrewdudu",
	"email": "andrew@gmail.com",
	"password": "admin123",
	"confirm-password": "admin123"
}
```
+ Request Header : 
	+ Accept: `application/json`
+ Response Body (Success) : 

```json
{
	"signup" : "true"
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 400,
    "status": "Bad Request",
    "message": "Invalid Request: Invalid request format",
    "path": "/user/register"
}
```

## Merchant Sign Up

+ Endpoint : ``/merchant/register``
+ HTTP Method : `POST`
+ Request Body :
```json
{
	"name": "Andrew",
	"username": "andrewdudu",
	"email": "andrew@gmail.com",
	"password": "admin123",
	"confirm-password": "admin123"
}
```
+ Request Header : 
	+ Accept: `application/json`
+ Response Body (Success) : 

```json
{
	"signup" : "true"
}
```

+ Response Body (Fail) :

```json
{
	"timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 400,
    "status": "Bad Request",
    "message": "Invalid Request: Invalid request format",
    "path": "/merchant/register"
}
```

## User/Merchant/Admin Login

+ Endpoint : ``/login``
+ HTTP Method : ``POST``
+ Request Body : 
```json
{
	"username": "andrewdudu",
	"password": "admin123"
}
```
```json
{
	"email": "example@gmail.com",
	"password": "admin123"
}
```
+ Request Header : 
	+ Accept : ``application/json``
+ Response Body (Success) :

```json
{
	"token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg"
}
```

+ Response Body (Fail) : 

```json
{
    "timestamp": "2019-08-23T04:22:26.690+0000",
    "code": 400,
    "status": "Bad Request",
    "message": "Invalid Request: Invalid user authentication or invalid request format",
    "path": "/login"
}
```

## User/Merchant/Admin Logout

+ Endpoint : ``/logout``
+ HTTP Method : ``POST``
+ Request Header : 
	+ Accept : ``application/json``
	+ Authorization : `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjYyNDAyMjAsImlhdCI6MTU2NjIyMjIyMH0.sYLqMuG2Zr7zDEdK4YIIYX7WfTcroxl7Edc_YLU0dWncPliHfbgEfMYLoorYA_d01hPFF_fZhAyxLTIJYBRHuw`
+ Response Body (Success) : 

```json
{
	"logout" : "true"
}
```

+ Response Body (Fail) : 

```json
{
	"code" : "400",
    "status" : "Bad Request",
    "message" : "Invalid Request: Invalid user authentication or invalid request format",
    "path" : "/logout"
}
```