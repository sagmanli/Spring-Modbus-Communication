<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>DEVICES</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css">
<script src="js/jquery.js"></script>
<script src="js/main.js"></script>
</head>
<body>
	<%@include file="includes/header.jsp"%>
	<div id="container">
		<div id="alertBox" class="alertBox">
			${alert}
			<div
				onclick="document.getElementById('alertBox').style.display='none'"
				class="circle">X</div>
		</div>
		<div class="adminLeft">
			<div class="leftTop">
				<h3>Modeller</h3>
			</div>
			<div class="leftBottom">
				<h3>Aktif Cihazlar</h3>
			</div>
			<!-- auto generated area -->
		</div>
		<form onsubmit="$('#connectionParams').serialize();"
			action="updateConnectionParameters" method="POST"
			id="connectionParams" name="connectionParams" class="adminRight"
			ondrop="drop(event)" ondragover="allowDrop(event)"></form>
	</div>
	<%@include file="includes/footer.jsp"%>
	<script>
		checkForAlert(document.getElementById('alertBox'));
		getDevices();
	</script>
</body>
</html>


