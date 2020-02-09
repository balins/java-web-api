# Java Web API

A simple **standalone web application** providing public interface.

The API gives access to database resources and returns data in **JSON**.

Built using **Java EE** and **Jetty**.

Implemented using **DAO** pattern.

Tested on **H2Database** v1.4.196.

## Configuration

* Build using Maven `mvn install`
* Create a new database
    * NOTE: you can find **ready-to-go** database and .properties file in folder `/example_db`.
    Use these to skip the next two steps.
* Initialize the database with provided script `/sakila-min.sql`
    * by default script provides admin user as _user: admin'_, _password: 'admin'_
* Create _.properties_ file including all necessary data:

    `example.properties`
    ```
    url=*url of your database*
    driver=*jdbc driver of your database*
    username=*login for your database*
    password=*password for your database*
    ```

* Run application with the following parameters:
    * `-port [1024..49151]` - port number that will be used by server
    * `-dbProps path` - **relative** path to _.properties_ file
* Now you are able to connect at localhost:_port_ and start sending requests!

## Authentication

Every user of API has to pass his username and access token along with every request:

    /route?user=username&token=user_token&key1=val1&key2=val2...

Of course, all parameters can occur in any order.

## User roles

Every user is created with
* role - _user_ (standard user) or _admin_ (administrator),
* name,
* access token,
* usage limit.

After _limit_ requests, every user has to get his limit renewed by an admin.

### Standard user

Can access all **public** routes of the API in terms of his usage limits.

### Administrator

Can access **all routes** of the API in terms of his usage limits and has ability to:
* access all users' data,
* add new users,
* delete users,
* renew users' usage limits.

## Public routes

API consists of 3 public routes:
* **/actors** - provides access to actors data
* **/films** - provides access to films data
* **/languages** - provides access to languages data

### GET Parameters

#### All routes

All of the routes mentioned above provide:
* obtaining all records under given route

        /actors?...

* using multiple values of given filtering parameter

        /films?id=2,3,5,7...

* obtaining a record by its id - by parameter _id_

        /actors?id=1...

* pagination - by parameters _page_ (counting from 0) and _perPage_

        /films?minLength=60&page=2&perPage=10...

* ordering - by parameter _order_ (_desc_, _asc_)

        /languages?order=asc...

#### Route-specific parameters

##### /actors

* firstName

        /actors?firstName=Woody...

* lastName

        /actors?lastName=Williams...

##### /films

You can mix title and language with both minLength and maxLength.

* title

        /films?title=twisted pirates...

* language

        /films?language=mandarin...

* minLength (with duration equal or greater than)

        /films?title=twisted pirates&minLength=99...

* maxLength (with duration equal or less than)

        /films?language=mandarin&maxLength=99...

##### /languages

* name

        /languages?name=english...

## Admin routes

Non-public (accessible only to application's admins) part of the API has 2 routes:
* **/users** - provides access to users data
* **/admin** - provides access to mechanisms of user management

Operations on these routes don't subtract from your usage limit.

### GET Parameters

##### /users

* as well as in public API, you can order and paginate the results as well as get multiple results
by listing many ids

        /users?page=1&perPage=5&order=asc

* filter
    * all (returns all users)

            /users?filter=all...

    * admin (returns all admins)

            /users?filter=admin...

    * user (returns all standard users)

            /users?filter=user...

    * noaccess (returns users that have exhausted their usage limit)

            /users?filter=noaccess...


##### /admin
* action
    * add

            /admin?action=add&role=user&name=foo&limit=16

    * renew

            /admin?action=renew&id=256&limit=4096

    * delete

            /admin?action=delete&id=65536


You can modify multiple records within one request, for example:

    /admin?action=renew&id=1,2,4,8&limit=16,32,64,128


## Credits
Thanks to [@math-g](https://github.com/math-g) for porting Sakila (sample MySQL database) to H2 dialect.


<sub>And yeah, this API is vulnerable to SQL-injection. Emm... **let's call it a feature...**</sub>
