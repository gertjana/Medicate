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

var win = Titanium.UI.currentWindow;
win.backgroundImage = '../images/gradientBackground.png';

var xhr = Titanium.Network.createHTTPClient();

xhr.onload = function()
{
    var supplies = JSON.parse(this.responseText).Supplies;    
    Ti.API.info('found ' + supplies.length + ' supplies');
    
    data = [];
    
    for (var i=0;i<supplies.length;i++) {
        var supply = supplies[i].Supply;
        
        var medicine = supply.Medicine;
        var daysLeft = supply.DaysLeft;
        
        Ti.API.info(medicine + " " + daysLeft);
        
        var row = Ti.UI.createTableViewRow();
        
        row.height = 60;
        row.className = 'datarow';
       
	    var medicineLabel = Ti.UI.createLabel({
			font:{fontSize:16,fontWeight:'bold', fontFamily:'Arial'},
	 		color: settings.color1,
			left:10,
	        top:2,
	        height:30,
	        width:200,
	        text:medicine
	    });
	    row.add(medicineLabel);

	    var daysLeftLabel = Ti.UI.createLabel({
			color: settings.color2,
			font:{fontSize:12,fontFamily:'Arial'},
			left:10,
			top:36,
			height:20,
			width:200,
			text:daysLeft + " days left"
		});
	    row.add(daysLeftLabel);

	    data.push(row);
    
    }
    tableView.setData(data, {});

};


Titanium.UI.currentWindow.addEventListener('focus', function (e) {
        reloadPropertiesAndUris();

        Ti.API.info(SUPPLIES_URI);

        xhr.open('GET',SUPPLIES_URI);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.send();
    
});

tableView = Titanium.UI.createTableView({
	data:data,
	filterAttribute:'filter',
	separatorColor: settings.color1,
	backgroundColor:'transparent'
});


Titanium.UI.currentWindow.add(tableView);
