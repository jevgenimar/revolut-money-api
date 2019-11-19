### Running locally ###
Execute the command to run unit tests, API tests and build the application ` ./gradlew build`

Execute the command to run application `./gradlew run`

### API Endpoints ###

#### Create account ####

Endpoint creates account.

##### Request #####

`POST /accounts`

###### Request fields ######


| Field  | Description | Format |
| ------------- | ------------- | ------------- |
| iban  | international account number  | 34-character text  |
| owner  | account owner name  | 200-character text  |

###### Sample request #######
```json
{
"iban" : "iban",
"owner" : "test user"
}
```

##### Response #####

| Field  | Description | Format |
| ------------- | ------------- | ------------- |
| iban  | international account number  | 34-character text  |
| owner  | account owner  | 200 character text  |
| publicId  | the ID of account  | UUID  |
| balance  | balance on the account | Decimal  |

###### Sample response #######
```json
{
    "publicId": "5616d169-52c9-40ca-a305-7bf428722c1f",
    "iban": "iban",
    "balance": 0,
    "owner": "test user"
}
```

#### Retrieve Account ####

Endpoint returns account.

##### Request #####

`GET /accounts/:publicId`

###### Request fields ######


| Field  | Description | Format |
| ------------- | ------------- | ------------- |
| publicId  | the ID of account  | UUID  |

```
GET /accounts/22cefb51-08a3-4386-ab34-34756fa3f5cf
```

##### Response #####

| Field  | Description | Format |
| ------------- | ------------- | ------------- |
| iban  | international account number  | 34-character text  |
| owner  | account owner  | 200 character text  |
| publicId  | account owner  | UUID  |
| balance  | balance on the account | Decimal  |

###### Sample response #######
```json
{
    "publicId": "2cefb51-08a3-4386-ab34-34756fa3f5cf",
    "iban": "iban",
    "balance": 0,
    "owner": "test user"
}
```


#### Transfer ####

This endpoint processes transfers between accounts 

##### Request #####

`POST /accounts/transfer/:sourceAccountId/:targetAccountId`

###### Request fields ######


| Field  | Description | Format |
| ------------- | ------------- | ------------- |
| sourceAccountId  | the ID of account  | UUID  |
| targetAccountId  | the ID of account  | UUID  |
| amount  | the transaction amount  | Decimal  |


###### Sample request #######
```json
POST /accounts/transfer/2cefb51-08a3-4386-ab34-34756fa3f5cf/2cefb51-08a3-4386-ab34-34756fa3f333
{
"amount" : 50.00,
}
```

##### Response #####
Empty Reponse with status `200`. See Errors for possible validation exceptions.

#### Deposit ####

This endpoint deposits money to account

##### Request #####

`POST /accounts/deposit/:targetAccountID`

###### Request fields ######


| Field  | Description | Format |
| ------------- | ------------- | ------------- |
| targetAccountID  | the ID of account  | UUID  |
| amount  | the transaction amount  | Decimal  |

###### Sample request #######
```json
POST /accounts/deposits/2cefb51-08a3-4386-ab34-34756fa3f5cf
{
"amount" : 50.00,
}
```

##### Response #####
Empty Reponse with status `200`. See Errors for possible validation exceptions.

#### Withdraw ####

This endpoint withdraws money from account

##### Request #####

`POST /accounts/withdraw/:targetAccountID`

###### Request fields ######


| Field  | Description | Format |
| ------------- | ------------- | ------------- |
| targetAccountID  | the ID of account  | UUID  |
| amount  | the transaction amount  | Decimal  |

###### Sample request #######
```json
POST /accounts/withdraw/2cefb51-08a3-4386-ab34-34756fa3f5cf

{
"amount" : 50.00,
}
```

##### Response #####
Empty Reponse with status `200`. See Errors for possible validation exceptions.


### Errors ###
The API uses the following error codes:

| Code  | HTTP Status | Meaning |
| ------------- | ------------- | ------------- |
| `ACCOUNT_ALREADY_LOCKED`  | `400`  | Bad Request - account is already locked by another operation  |
| `NON_UNIQUE_RESULT`  | `500`  | We have a problem with server. Try again later.  |
| `NOT_ENOUGH_BALANCE`  | `400`  | Bad Request - account does not have enough balance to perform the operation  |
| `RESOURCE_NOT_FOUND`  | `404`  | Not Found -- The requested resource could not be found.  |
| `REQUEST_VALIDATION_FAILED`  | `400`  | Bad Request - Validation of request failed  |


### Dependencies ###
[A micro framework for creating web applications](http://sparkjava.com/) (`com.sparkjava:spark-core`)

[Database Query Library](https://www.sql2o.org/) (`org.sql2o:sql2o`)

[A relational database management system (support in-memory)](http://hsqldb.org/) (`org.hsqldb:hsqld`)

[A Java serialization/deserialization library to convert Java Objects into JSON ](https://github.com/google/gson) (`com.google.code.gson:gson`)

[Provides extra methods for Java Core classes](https://commons.apache.org/proper/commons-lang/) (`org.apache.commons:commons-lang3`)

[Console logging](http://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html) (`org.slf4j:slf4j-simple`)

### Testing Dependencies ###

[A simple framework to write repeatable tests](https://junit.org/junit4/) (`junit:junit`)

[Library for testing and validating REST services](http://rest-assured.io/) (`com.jayway.restassured:rest-assured`)

[A mocking framework for unit tests](http://rest-assured.io/) (`org.mockito:mockito-core`)


### Next Steps ###
- scheduler for clearing the locks in case one instance of bank goes down (e.g infrastructure crash) and the lock will not be removed
- add history of operations, so we can track transactions
- it may not be the most efficient way to perform the transaction immediately (potentially the stream of events could be used for that)
- exchange currency could be added as between banks (e.g. its EURO in Estonia and point sterling in UK) should be done some kind of money conversion

