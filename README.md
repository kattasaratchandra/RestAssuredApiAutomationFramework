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