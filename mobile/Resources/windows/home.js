/*
Copyright 2011 Addictive Software

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

Ti.include("constants.js");

var tableView;
var data = [];
var authors = [];
var section;

var win = Titanium.UI.currentWindow;
win.backgroundImage = '../images/gradientBackground.png';

var xhr = Titanium.Network.createHTTPClient();

xhr.onerror = function(e) {
	alert(e.error);
};

xhr.onload = function() {
    var supplies = JSON.parse(this.responseText).Supplies;    
    
    data = [];
	section = Ti.UI.createTableViewSection();
    
    for (var i=0;i<supplies.length;i++) {
        var supply = supplies[i].Supply;       
        var medicine = supply.Medicine;
        var daysLeft = supply.DaysLeft;
        
        var row = Ti.UI.createTableViewRow();
        
        var barColor = '#00FF00';
        if (daysLeft < 50) {barColor = '#FFFF00';}
        if (daysLeft < 35) {barColor = '#FF0000';}

        row.height = 60;
        row.className = 'datarow';
        row.backgroundColor = '#FFF';
        var icon = Ti.UI.createImageView({
            width: 48,
            height: 48,
            top:6,
            left:6,
            image:'../images/medication.png'

        });
       	row.add(icon);
       
	    var medicineLabel = Ti.UI.createLabel({
			font:{fontSize:16,fontWeight:'bold', fontFamily:'Arial'},
	 		color: settings.color1,
			left:70,
	        top:2,
	        height:30,
	        width:200,
	        text:medicine
	    });
	    row.add(medicineLabel);

	    var daysLeftLabel = Ti.UI.createLabel({
			color: settings.color2,
			font:{fontSize:12,fontFamily:'Arial'},
			left:70,
			top:36,
			height:20,
			width:200,
			text:daysLeft + " days left"
		});
	    row.add(daysLeftLabel);

		var bar = Ti.UI.createImageView({
            width: daysLeft,
            height: 10,
            top:26,
            left:70,
            borderColor: barColor,
            backgroundColor: barColor
		});

		row.add(bar);

	    section.add(row);
    
    }
   	data.push(section);
    tableView.setData(data, {});

};




Titanium.UI.currentWindow.addEventListener('focus', function (e) {
        reloadPropertiesAndUris();

		Ti.API.info("getting supplies");
        xhr.open('GET',SUPPLIES_URI);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.send();
        
        Ti.API.info("getting options");
        xhrOptions.open('GET', OPTIONS_URI);
		xhrOptions.setRequestHeader("Accept", "application/json");
		xhrOptions.send();
});

var picker = Ti.UI.createPicker({
  top:20
});


var optionsDialogOpts = {
	options:[],
	cancel:0,
	title:'Take dose for:'
};

var xhrOptions = Ti.Network.createHTTPClient();


xhrOptions.onerror = function(e) {
	alert(e.error);
};

var dialog;
var optionsLength;

xhrOptions.onload = function() {
	var options = JSON.parse(this.responseText).DosageOptions;
	Ti.API.info(this.responseText);
	Ti.API.info(JSON.parse(this.responseText));
	Ti.API.info(options);
	optionsLength = options.length;
	
	options.push('Cancel');
	dialog.options = options;
	dialog.cancel = options.length;	
};

dialog = Titanium.UI.createOptionDialog(optionsDialogOpts);
dialog.addEventListener('click',function(e){
	Ti.API.info('clicked' + e.index);
	if (e.index != optionsLength) { //cancel
		var xhr2 = Ti.Network.createHTTPClient();
		
		Ti.API.info(TAKEDOSE_URI + "/" + dialog.options[e.index]);
		
		xhr2.open("GET", TAKEDOSE_URI + "/" + dialog.options[e.index]);
	    xhr2.setRequestHeader("Accept", "application/json");
	    xhr2.send();
		
		xhr2.onerror = function() {
			alert("An error occured taking a dose");
		};
	
		xhr2.onload = function() {
			Titanium.UI.createAlertDialog({title:'Take dose', message:'your daily dose is taken'}).show();
			
			xhr.open('GET',SUPPLIES_URI);
	        xhr.setRequestHeader("Accept", "application/json");
	        xhr.send();	
		};				
	}
});

tableView = Titanium.UI.createTableView({
	data:data,
	filterAttribute:'filter',
	separatorColor: settings.color1,
	backgroundColor:'transparent',
	style:Titanium.UI.iPhone.TableViewStyle.GROUPED

});

var takeDose = Titanium.UI.createButton({
	title:'Take Dose'
});
takeDose.addEventListener('click', function()
{
	dialog.show();
});

var refresh = Ti.UI.createButton({
	systemButton:Ti.UI.iPhone.SystemButton.REFRESH
});

refresh.addEventListener('click', function(){
        xhr.open('GET',SUPPLIES_URI);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.send();	
});

win.leftNavButton = refresh;
win.rightNavButton = takeDose;

Titanium.UI.currentWindow.add(tableView);
Titanium.UI.currentWindow.fireEvent("focus", null);
