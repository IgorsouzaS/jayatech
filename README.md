<h1 id="converter">JayaTech (Currency converter)</h1>


[![Github branch checks state](https://img.shields.io/github/checks-status/IgorSouzaS/jayatech/master.svg)](     )
[![Swagger](https://img.shields.io/badge/swagger-valid-brightgreen.svg)](https://jayatech-test.heroku.com/swagger-ui)
[![Github Last commit](https://img.shields.io/github/last-commit/IgorsouzaS/jayatech.svg)](         )
[![Language](https://img.shields.io/github/languages/top/IgorsouzaS/jayatech.svg)](        )
[![Heroku](https://img.shields.io/badge/%E2%86%91_Deploy_to-Heroku-7056bf.svg)](https://jayatech-test.heroku.com/users)


**Index**

* [Currency converter](#converter)
    * [Description](#description)
    * [Technologies](#technologies)
    * [Requirements](#requirements)
    * [Running the project](#running)
        * [Clone](#clone)
        * [Test](#test)
        * [Run](#run)
    * [Swagger documentation](#swagger)
        * [Swagger UI](#swagger-ui)
        * [Swagger-docs](#swagger-docs)
   * [Endpoints](#endpoints)
        * [Users](#users)
        * [Transactions](#transactions)
   * [Logs](#logs)
   * [Layers](#layers)
   * [Linting](#linting)
   * [Heroku CI](#heroku-ci)

<br/>


<h2 id="description">Description</h2>

This project is about a Rest API that performs a conversion between two currencies using updated conversion rates from an external service.

<img src="https://cdn.icon-icons.com/icons2/618/PNG/512/Currency_Conversion_icon-icons.com_56682.png" height="25px" width="25px"/> **Transactions**

The API records each conversion transaction with all related information and also provides an endpoint for consulting the transactions carried out by a user.

<img src="https://cdn0.iconfinder.com/data/icons/currency-30/128/workspace_128-07-512.png" height="25px" width="25px"/> **Currencies**

This project provides currency and exchange rates by converting some currencies, including BRL, USD, EUR, JPY and others.
<br/><br/>


<h2 id="technologies">Requirements for running the project</h2>
<h4>The following technologies were used in this project:</h4>

  * [Kotlin](https://kotlinlang.org/)
  * [Javalin](https://javalin.io/)
  * [Koin](https://insert-koin.io/)
  * [Exposed](https://github.com/JetBrains/Exposed)
  * [H2 database](https://www.h2database.com/html/main.html)
  * [Kotlinter plugin](https://plugins.gradle.org/plugin/org.jmailen.kotlinter)
  * [Retrofit2](https://square.github.io/retrofit/)
  * [Swagger OpenApi](https://swagger.io/specification/)
  * [Log4j](https://logging.apache.org/log4j/2.x/)
  * [HerokuCI](https://www.heroku.com/continuous-integration)
	
<br/>

<h2 id="requirements">Requirements for running the project</h2>

You should have Git, JDK and Gradle
  * [Git](https://git-scm.com/downloads)
  * [JDK](https://openjdk.java.net/install/)
  * [Gradle](https://gradle.org/install/)

<br/>

<h2 id="running">Running the project</h2>

<h4 id="clone">Clone this repo</h4>

```
$ git clone https://github.com/IgorsouzaS/jayatech.git && cd jayatech
```

<h4 id="test">Test on local machine - Run local tests (it will download dependencies, compile sources and run tests)</h4>

```
$ gradle test
```

<h4 id="run">How to run - Start server (it will download dependencies, compile sources and start the server)</h4>

```
$ gradle run
```

<br/>

<h2 id="swagger">Swagger Documentation</h2>

#### A swagger endpoint will be provided for documenting the project endpoints
<h5 id="swagger-docs">Swagger JSON version of the API documentation</h5>

```
GET     /swagger-docs
```

<h5 id="swagger-ui">Swagger UI</h5>

```
GET     /swagger-ui
```

<img src="https://github.com/IgorsouzaS/jayatech/blob/master/src/main/resources/images/swagger.png" height="350px" width="800px"/>

<br/>


<h2 id="endpoints">Endpoints</h2>

This API provides some endpoints for operations:

#### Users
```
POST      /users - register a user in the database and return it
GET       /users - returns the list of all users
PUT       /users - update a user in the database and return it
GET       /users/{userId} - returns the data of a specific user
DELETE    /users/{userId} - delete the data of a specific user

GET       /{userId}/transactions - returns the list of all transactions made by a userId
```
#### Transactions
```
POST      /transactions - make a transaction
GET       /transactions - returns the list of all transactions made
GET       /transactions/{transactionId} - returns the data of a specific transaction
DELETE    /transactions/{transactionId} - delete the data of a specific transaction
```
* A transaction needs a user to be done. Therefore, register a user before attempting to perform it or an 
error message will be returned

<br/>
Body example to make a transaction:

```
{
	"userId": "1",
	"originCurrency": "USD",
	"originAmount": 5.00,
	"destinationCurrency": "BRL"
}
```

<br/><br/><br/>


<h2 id="logs">Logs</h2>

#### All logs for this application will be saved in the project's root directory in the file 'logs.log'

<img src="https://github.com/IgorsouzaS/jayatech/blob/master/src/main/resources/images/logs-example.png" height="350px" width="800px"/>


<br/>
<h2 id="layers">Layers</h2>
the application was divided into 3 layers (model, repository and controller) and its functionalities grouped in modules.
</br>
The modules were injected as dependencies using Koin.
<br/>



```kotlin

object ModulesConfig {

    private val configModule = module {
        single { AppConfig() }
    }

    private val userModule = module {
        single { UserRepository(DbConfig("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "")) }
        single { UserController(get()) }
        single { UserRouter(get()) }
    }

    private val transactionModule = module {
        single { TransactionRepository(DbConfig("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "")) }
        single { TransactionController(get()) }
        single { TransactionRouter(get()) }
    }

    internal val modules = listOf(configModule, userModule, transactionModule)
}
```

<br/>

<h2 id="linting">Linting</h2>
Linting is the process of checking the source code for programmatic and stylistic errors. This is very useful for identifying some common and unusual mistakes that are made during the following. Two Linters were used for this project. The SonarLint plugin was used in the Intellij IDE and the
Ktlint was used as a task executor for the same purpose but it can be run without IDE since it is in the project as one of its gradle dependencies.


<br/><br/>

<h2 id="heroku-ci">Heroku CI</h2>

This project is using a CI pipeline of Heroku, which means that the changes on master branch of this repository
will start a process of testing and deploying of a new project version.



<br/><br/>

#### <div align="center"> Use it on [here](https://jayatech-test.heroku.com/users) <img src="https://imagepng.org/wp-content/uploads/2017/10/coracao.png" height="10px" width="10px"></div>
