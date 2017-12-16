/**
 * 
 * 
 */

var fieldsToPush = [];


$(document).ready(function(){
	console.log(" ------- Begin ready function for " + location.href + " --------");

	try {
		$.ajax({ url: "/getDataFromCache",
			context: document.body,
			success: function(response){
				console.log("getDataFromCache Response = " + JSON.stringify(response));
				populateData(response);
			}
		});
	
    
        $.ajax({
                url : '/getIdsToPush',
                type : 'GET',
                context: document.body,
                success : function(response) {
                	console.log("getIdsToPush Response = " + JSON.stringify(response));
                	parseResponse(response);
                }
        });
    } catch (exception) {
        console.log("Exception occured during cache processing - cache functionality may not work");
        alert("Exception occured during cache processing - cache functionality may not work")
    }
	
});



$(window).on('beforeunload', function(data){
    
	console.log("-------- Begin beforeunload function for " + location.href + " --------");
	
	var fromObj = [];

    for (var index = 0; index < fieldsToPush.length; index++) {
        //var tempObj = {"name" : fieldsToPush[index], "value" : $("#" + fieldsToPush[index]).val() } ;
        var tempObj = {} ;
        tempObj['name'] = fieldsToPush[index];
        tempObj['value'] = $("#" + fieldsToPush[index]).val();
        fromObj.push(tempObj);

    }

    var finalObj = {};
    finalObj.formData = fromObj;

    var strJson = JSON.stringify(finalObj);
    console.log("Json to be sent as payload for pushDataToCache [" + strJson + "]");
    try {
            $.ajax({
                    url : '/pushDataToCache',
                    type : 'POST',
                    context: document.body,
                    contentType:"application/json",
                    dataType : 'json',
                    data : strJson,
                    success : function(result) {
                            alert('SUCCESS');
                    }
            });
    } catch (exception) {
            alert("Request failed");
    }

});


function parseResponse(response){
	var formData = response.formData;
	
	for ( var i in formData) {
		fieldsToPush.push(formData[i].value);
	}
	console.log("ParseResponse successful, populated fieldsToPush array with = " + fieldsToPush.length);
}

function populateData(response){
	var formData = response.formData;
	
	for ( var i in formData) {
		$("#" + formData[i].name).val(formData[i].value);
	}
	console.log("PopulateData successful, populated fields successfully = " + formData.length);
}