<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE>
<html>
  <head>
    <title>Create password page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/static/css/register.css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery/jquery-2.0.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery/jquery-ui-1.10.3.min.js"></script>
  </head>
  
  <body>
  <div class="container">
    <form class="form-signin">
       <h3 class="form-signin-heading">Please set your password</h3>
	   <label class="message" id="message"></label>
	   
       <label for="inputPassword" class="sr-only">Password:</label>
       <input type="password" id="password" class="form-control" placeholder="Please input your password" required autofocus/>
	   
	   
       <label for="confirmPassword" class="sr-only">Confirm Password:</label>
       <input type="password" id="confirmPassword" class="form-control" placeholder="Please confirm your password" required/>
       
       <input type="text" id="email" class="sr-only" value="${email }"/>
       <input type="text" id="securityKey" class="sr-only" value="${securityKey }"/>
       <input type="text" id="deviceKey" class="sr-only" value="${deviceKey }"/>
       <input type="text" id="userId" class="sr-only" value="${userId }"/>
       <input type="text" id="time" class="sr-only" value="${time }"/>
       <input type="text" id="clientType" class="sr-only" value="${clientType }"/>
	   <input type="text" id="url" class="sr-only" value="${pageContext.request.contextPath }/users"/>
       <br/>
	   <button type="button" class="btn btn-primary" onclick="savePassword()">Submit</button>
    </form>
   </div>
   
   <script type="text/javascript">
   		function savePassword(){
   			var password = $('#password').val();
			var confirmPassword = $('#confirmPassword').val();
			
			var securityKey = $('#securityKey').val();
			var deviceKey = $('#deviceKey').val();
			var email = $('#email').val();
			var userId = $('#userId').val();
			var time = $('#time').val();
			var clientType = $('#clientType').val();
			
			var url = $('#url').val();
			
			if(password.trim().length == 0 || confirmPassword.trim().length == 0 || password.trim().length != 8 || confirmPassword.trim().length!= 8){
				$('#message').html('password must be 8 chars with uppercase/lowercase/special char!');
				return false;
			}
			if(password != confirmPassword){
				$('#message').html('The two passwords can not match');
				return false;
			}
			var userInfo = {'password':password,'confirmPassword':confirmPassword,'securityKey':securityKey,'email':email,'deviceKey':deviceKey,'userId':userId,'time':time};
			$.ajax({
        		type: "POST",
        		url: url,
        		contentType: "application/json; charset=utf-8",
        		data: JSON.stringify(userInfo),
        		dataType: "json",
        		beforeSend: function(request) {
                	request.setRequestHeader("clientType", clientType);
                	request.setRequestHeader("apiKey", "web");
                },
        		success: function (data) {
        			if(data.data){
        				$('#message').html("save password successful!");
        			} else {
        				$('#message').html(data.message);
        			}
        		},
        		error: function (data) {
            		$('#message')("Send request fail!");
        		}
   			 });
   		}
   </script>
  </body>
</html>