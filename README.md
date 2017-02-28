# factory puzzle


##  What is it?

 	Paint factory is a API used for solving the paint puzzle: Satisfy all customers
 	with limit paint types. 
 	
 	For the details of puzzle, please check file /resource/PaintShop.pdf

##  Documentation

	For the puzzle, there are pre-fix constrains:
	* One customer at most prefer one matte paint
	* One customer can be satisfied by one paint
	* One paint batch contain unlimit amount of paint
	* One color can only be either glossy/matte
	* Minimize Matte paint batch
	
	with these constrains,to resolve this issue can be simplified as:
	1. Satisfy maximun customers with glossy paint
	2. Satisfy the rest with matte paint
	3. if needed, satisfy glossy-prefered customers with another paint
	
	And then transfer them into a do-able solution:
	1. assume all batches are glossy, each batch would satisfy some customers without repeat
	2. queue maximum customers under each batch without repeat, the rest would be who only prefer matte paint
	3. change batch to matte from glossy, to satisfy matte-only customer
		3.1. dismiss the customer queue under this batch, and map them into another batch
		3.2. if needed, repeat step 3
		
	4. when all customer have mapped into the queue under each batch, then job done.
  
  
## Usage

 	set the initialize parameter in "config.properties"
 	run "src/main/java/ding/demo/main/Application.java" as Java Application.

### API 

	The API is self-describing and can be explored from the base URL by default:

	POST: http://localhost:8080/paintfactory/api/v1/paint-requests
	Parameters
	
		| Name | Required | Type | Description |
		| ---- | ---- | ---- | ---- |
		| file | required | file | File contains paint request in the defined format, check PaintShop.pdf for details  |
	
	Every JSON response includes a `links` object which allows you to navigate the result of you uploaded request.
	
	Example response:
	{
	  "name": "paint-request1",
	  "created": 1488272723374,
	  "links": [
	    {
	      "name": "self",
	      "uri": "http://localhost:8080/paintfactory/api/v1/paint-requests/paint-request1/paint-request1"
	    },
	    {
	      "name": "result",
	      "uri": "http://localhost:8080/paintfactory/api/v1/paint-requests/paint-request1/paint-request1.result"
	    }
	  ]
	}

	GET:  http://localhost:8080/paintfactory/api/v1/paint-requests/:fildfolder/:filename
	Successful response is the requested file
  
  	Check /resource/test-data/ for testing files
##  Contacts