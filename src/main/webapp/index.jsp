<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Gallery Upload</title>
	</head>
	
	<body>
		<form action="api/pictures/upload" method="post" enctype="multipart/form-data">
			<input type="file" name="file">
			<input type="submit" value="Submit">
		</form>
	</body>
</html>