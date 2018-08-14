var http = require('http');

var handleRequest = function(request, response) {
  console.log('Received request for URL: ' + request.url);
  response.writeHead(200);
  response.end('<body style="color:blue;background-color:#e5e8e8;position: absolute;width: 200px; height: 50px;top: 50%;left: 50%; margin-left: -50px;margin-top: -25px;"><h1 >Hello From Frontend-1!</h1></body>');
};
var www = http.createServer(handleRequest);
www.listen(8082);