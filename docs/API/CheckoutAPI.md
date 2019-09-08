# Checkout API

## Checkout (For Users)

+ Endpoint: `/checkout`
+ HTTP Method: `POST`
+ Request Body: 
    + cartId
+ Request Header: 
    + Accept: `application/json`
+ Response Body (Success):

```json
{
    "code": 200,
    "status": "OK",
}
```