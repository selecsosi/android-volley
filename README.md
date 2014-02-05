Android Volley - With Responses That Keep Your Headers
----------

Standard volley library with some additional helpers that return a response model with a GSON parsed POJO and the response headers from the server

Use exactly like volley, but include a different response listener

    new ResponseWithHeaders.Listener<MyPOJO>() {
                @Override public void onResponse(ResponseModel<MyPOJO> response) {
                        // response.getData() with have your response object
                        // response.getHeaders() will return a Map<String, String> with your response headers
                    }
                }
            }
            
Due to the nature of Java reflections and the loss of type info on compilation, a ResponseWithHeadersRequest must have the type info set ala,

    ResponseHeadersRequest<MyPOJO> request = new ResponseHeadersRequest<MyPOJO>(Method.GET, "http://myrestendpoint.com/MyPojo/1", /**listeners**/)
    request.setResponseType(new TypeToken<MyPOJO>() {}.getType());
    

