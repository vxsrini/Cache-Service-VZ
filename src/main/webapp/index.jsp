<!DOCTYPE html>
<html>
<head>
    <title>Hello WebSocket</title>   
</head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<body>

<form name="firstIndexForm" id="firstIndexForm">
		Index Page - First name: <input type="text" name="IP_fname"	id="IP_fname"><br> <br>
		Index Page - Last name: <input	type="text" name="IP_lname" id="IP_lname"><br>
		<input type="submit" value="Submit">
</form>

<br></br>

<a href="/FirstFrame.html" target="iframe_a">CCP</a>
&nbsp;&nbsp;
<a href="/SecondFrame.html" target="iframe_a">vBuild</a>
<br></br>
<iframe src="" name="iframe_a">
<p>Your browser does not support iframes.</p>
</iframe>


<script src="/CacheManagement.js"></script>
</body>
</html>