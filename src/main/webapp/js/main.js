function allowDrop(ev) {
	ev.preventDefault();
}
function getParameter ( val ) {
    var result = "Not found", tmp = [];
    location.search
        .replace ( "?", "" )
        // or 
        //.susubstr ( 1 )
        .split ( "&" )
        .forEach ( function ( item ) {
            tmp = item.split ( "=" );
            if ( tmp[0] === val ) result = decodeURIComponent ( tmp[1] );
        });
    return result;
}

function drag(ev) {
	ev.dataTransfer.setData("DATA", ev.target.id);
}

function drop(ev) {
	ev.preventDefault();
	var data = ev.dataTransfer.getData("DATA");
	var elementCount = document.getElementById(data).childElementCount;

	for (var i = 0; i < elementCount; i++) {
		document.getElementById(data).children[i].style.display = "block";
	}

	ev.target.appendChild(document.getElementById(data));

	disableDropableItems();
}

function disableDropableItems() {
	var dropableItems = document.getElementsByClassName("leftItem");

	Array.prototype.forEach.call(dropableItems, function(el) {
		el.setAttribute('draggable', false);
	});
}

function getRegisterValues(deviceId) {
	window.location = "/editDevice?deviceId=" + deviceId;
}

function getDevices() {
	$.getJSON("device_list", function(data) {
		$.each(data, function(key, deviceName) {
			generateConnectionParametersForUnrecognizedDevice(deviceName);
		});
	});

	$.getJSON("configured_device_list", function(data) {
		$.each(data, function(deviceId, deviceName) {
			generateConnectionParametersForRecognizedDevice(deviceId, deviceName);
		});
	});

}

function generateConnectionParametersForUnrecognizedDevice(deviceName) {
	var connectionString = "connection_parameter?deviceType=" + deviceName;

	$.getJSON(connectionString, function(data) {
		var items = [];
		items.push("<p class='deviceType'>" + deviceName + "</p>");
		items.push("<input type='hidden' id='deviceId' name='DEVICE_NAME' value=" + deviceName + ">");
		$.each(data, function(i, item) {
			items.push("<div name='" + item[0] + "' class='innerItem'>");
			items.push("<h3 class='submitKey'> " + item[0] + "</h3>");
			if (item[1] == "COMBO") {
				if (item[2] <= 0) {
					items.push("<span style='color: white; background-color: black;' class='selectBox'>Not Found</span>");
				} else {
					items.push("<select class='selectBox' name='" + item[0] + "'><option value=''>SELECT</option>");
					$.each(item[2], function(ii, iitem) {
						items.push("<option value='" + iitem.key/*iitem.value*/ + "'>" + iitem.key + "</option>");
					});
					items.push("</select>");
				}
			} else if (item[1] == "NUMBER") {
				items.push("<input type='text' class='selectBox' name='" + item[0] + "' value='" + item[2] + "' />");
			} else {
				items.push("<input type='text' class='selectBox' name='" + item[0] + "'   />");
			}
			items.push("<br></div><br>");
		});
		items.push("<input class='hidden submit' type='submit' value='Kaydet'>");
		items.push("<br><input style='float:right;' class='hidden submit' type='button' onclick='testConnectionData();' value='Test Connection'>");

		$("<div/>", {
			"id" : deviceName,
			"class" : "leftItem deviceOrientation",
			"draggable" : "true",
			"ondragstart" : "drag(event)",
			html : items.join("")
		}).css({
			"background" : "url('../images/" + deviceName + ".png') right no-repeat"
		}).appendTo(".leftTop");
	});
}

function generateConnectionParametersForRecognizedDevice(deviceId, deviceName) {
	var connectionString = "connection_parameter?deviceType=" + deviceName;

	$.getJSON(connectionString, function(data) {
		var items = [];
		items.push("<p class='deviceType'>" + deviceName + " id : " + deviceId + "</p>");
		items.push("<input type='hidden' id='deviceId' name='DEVICE_NAME' value=" + deviceName + ">");
		items.push("<div id= " + deviceId + " onclick='getRegisterValues(this.id)' class='play_border'><div class='play_button'></div></div>");

		$("<div/>", {
			"id" : deviceName,
			"class" : "leftItem",
			"draggable" : "false",
			"ondragstart" : "drag(event)",
			html : items.join("")
		}).appendTo(".leftBottom");
	});
}

function rangeShower(inputId, labelId) {
	$(inputId).mousemove(function(e) {
		$(labelId).html($(this).val());
	});
}

function editRegisterValue(key, value) {
	var form = document.getElementById('hiddenFormBox');

	form.style.display = "block";

	document.getElementById('registerKey').value = key;
	document.getElementById('registerKeySpan').innerHTML = "Register Key: " + key;
	document.getElementById('oldRegisterValue').value = value;
	document.getElementById('oldRegisterValueSpan').innerHTML = 'Register Old Value: ' + value;
	document.getElementById('newRegisterValue').value = null;

	return false;
}

function checkForAlert(alertBox) {
	var minLength = 106; // inner div length
	if (alertBox.innerHTML.length > minLength) {
		alertBox.style.display = 'block';
		window.setTimeout(function() {
			alertBox.style.display = 'none';
		}, 4000);
	} else {
		alertBox.style.display = 'none';
	}
	return false;
}

function changeRegisterValue() {
	// get values
	var deviceId = document.getElementById('hiddenDeviceId').value;
	var registerKey = document.getElementById('registerKey').value;
	var oldVal = document.getElementById('oldRegisterValue').value;
	var newVal = document.getElementById('newRegisterValue').value;

	$.post('updateRegister', {
		deviceId : deviceId,
		registerKey : registerKey,
		oldRegisterValue : oldVal,
		newRegisterValue : newVal
	}, function(data) {
		// alert(data);
	});

	// clear form
	deviceType = null;
	registerKey = null;
	oldVal = null;
	newVal = null;

	document.getElementById('hiddenFormBox').style.display = 'none';
	return false;

}

function testConnectionData() {
	var formArray = $('#connectionParams').serializeArray();
	var formData = {};
	$.each(formArray, function(i, data) {
		formData[data.name] = data.value;
	});
	$.post('testConnectionParameters', formData, function(data) {
		alert(data.error);
	});
	return false;
}
