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
		<div id="alertBox" onclick="this.style.display='none'" class="alertBox">
			${alert}
			<div onclick="document.getElementById('alertBox').style.display='none'" class="circle">X</div>
		</div>
		<h1>${deviceId}</h1>
		<!-- to edit register values we have hiddenFormBox -->
		<div id="hiddenFormBox">
			<input type="hidden" id="hiddenDeviceId" name="deviceId" value="${deviceId}"> <input type="hidden" name="registerKey" id="registerKey"> <span id="registerKeySpan"></span> <br> <input type="hidden" name="oldRegisterValue" id="oldRegisterValue"> <span id="oldRegisterValueSpan"></span> <br> <span>Register New Value: </span> <input type="text" id="newRegisterValue"
				name="newRegisterValue"> <br> <input type="submit" onclick="changeRegisterValue();" value="Kaydet"> <br> <input type="button" value="Vazgeç" onclick="document.getElementById('hiddenFormBox').style.display = 'none'">
		</div>
		<!-- end of hidden form box -->
		<!-- start of registerList -->
		<div id="registerList">
			<jsp:include page="monitorDevice.jsp"></jsp:include>
		</div>
		<img id="monitorDeviceInfo" src="images/${DEVICE_NAME}.png">
	</div>
	<%@include file="includes/footer.jsp"%>
</body>
<script>
	checkForAlert(document.getElementById('alertBox'));
	$("#registerList").load('monitorDevice?deviceId=' + getParameter("deviceId"), function() {
		setInterval(function() {
			$("#registerList").load("monitorDevice?deviceId=" + getParameter("deviceId"));
		}, 5000);
	});
</script>
</html>