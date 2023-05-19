----------
1. Started the framework project by adding dependencies rest assured, testng
2. Have added static imports and sample tests to check the dependencies
----------
1. Have automated get http method by taking postman apis as examples
2. Have written test that validate get response body by using hamcrest matchers library as assertions
3. Have written test that extracts response body
4. Have validated single value from extracted response body using different methods like jsonpath class,
path methods
5. Have validated response body without extraction by using hamcrest matchers
---------
Logging
1. Have written test for both request and response in order to log all, headers, parameters, cookies, body
2. Have written test log only if error, log only if validation fails
3. have used config on 'enabling the log if validation fails' in order to avoid code duplicate
----------
Headers
1. Have written tests to request multiple headers using header, headers and hashmap
2. Have written tests to request multi value header using header, headers we cannot use hashmap coz of
key duplication
3. Have written test to assert response header using header, headers
4. Have written test to extract response headers and used getName. getValue methods to get respective
names and values, used enhance for loop to get all headers name and values
5. To get multi value response header, have used getValues method which has return type as list, 
used enhanced for loop to get all the multi values
---------
Request specification
1. we can declare common request as request specification and reuse it to avoid code duplication
2. other than common requests we can append related request according to the test cases
3. alternate way for request specification is using request spec builder
4. we can use rest assured default request specification and avoid using given()
5. we can query the request specification using querable request specification
--------
Response specification
1. common validation can be declared as response specification and reuse it in test cases
2. other than common validation we can append related validations according to the test cases
3. the only difference between request and response specification is we can use request specification
in given() method as argument but response specification can't be used in then()
--------
1. Automated post request such that we create new workspaces
2. Automated put request such that we update the workspace name
3. Automated delete request such that we delete the workspace
----------
payload multiple ways
1. we can send request payload as File it is preferred only you have one variation of payload if multiple
variations of payload it's not recommended
2. we send payload in requests in the form of maps,nested maps,lists
3. we can send request payload as map but rest assured needs to convert map to json before sending the
request. It uses jackson library for converting map to json it's called serialisatio all we need to do is add
jackson dependency
4. encoding format if not defined rest assured sends it by default so to disable that we use setConfig in
request spec builder
-----------
Request Parameters
1. we use query parameters for filter(example particular page number) and path parameters for 
specific resource(example id)
2. both parameters append to the url in specific formats. query params appends after question mark
and in between we use & for multiple query params
3. path parameters gets appended after slash
-----------
serialisation, de-serialisation, pojo/map/list
1. converting the java object which is in the form of either pojo/map/list to json we call as serialisation
2. usually jackson library takes care of serialisation all we need to do is add the jackson dependencies
3. if we want to serialise outside rest assured we use 'object mapper' class and 'write value as string'
method to convert the java objects to json strings and use it in the request
4. pojo classes are used usually when we want to validate entire response json body. we can do that using 
object mapper class, read tree method
5. de-serialisation on pojo classes, we extract the response as pojo class and use setters and getters to
validate
-----------------------
Framework design:
started by added naive tests and improve step by step by creating robust and scalable framework design
1. Using pojo classes removing the hard coded payload. and generated pojo classes faster with the help of
a site
2. Deserialized the response to pojo class and used hamcrest matchers assertions

Reusable methods:

3. removing spec builders from before class and created seperate class for spec builder. created request
and request spec methods which returns respective request/response specifications.

4. Created two layers of reusable methods one for specific api, one which is general to all apis:
    1. for each application api we create separate class which contains reusable methods like post, get, 
   update of that api only.
    2. we create common reusable request methods for all apis like get, post, put and update methods. 
   Token Manager:
    3. Have removed hard coded access token ad created static method it will create a token if it is expired
   and return as string
    4. created reusable common method for all the apis.
    5. created separate request spec builder for this as base uri is different. response same mostly for 
   all apis

EndPoints:
5. created class which maintains all the endpoints which are constants

Property Loaders:
7. global variables are the ones which once defined never changes we keep them in config.properties
    1. access token variables are global here so create util class and properties.
    2. for get and update we use unique user id for request and it wont change through out so we keep 
   in test user data and define as properties files
