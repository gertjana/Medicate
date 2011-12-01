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

xhr.onload = function()
{
    var stocks = JSON.parse(this.responseText).stocks;    
    Ti.API.info(stocks.length);
    data = [];
	section = Ti.UI.createTableViewSection();
    
    for (var i=0;i<stocks.length;i++) {
        var stock = stocks[i].stock;       
        var name = stock.medicine;
        var amount = stock.amount;
        var id = stock.id;
                
        Ti.API.info(name);        
                
        var row = Ti.UI.createTableViewRow();
        
        row.height = 60;
        row.className = 'datarow';
        row.backgroundColor = '#FFF';
        
        row.id = id;
               
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
	        text:name
	    });
	    row.add(medicineLabel);

	    var amountLabel = Ti.UI.createLabel({
			color: settings.color2,
			font:{fontSize:12,fontFamily:'Arial'},
			left:70,
			top:36,
			height:20,
			width:200,
			text:amount + " pills in stock"
		});
	    row.add(amountLabel);
	    
	    section.add(row);
    
    }
   	data.push(section);
    tableView.setData(data, {});

};
tableView = Titanium.UI.createTableView({
	data:data,
	filterAttribute:'filter',
	separatorColor: settings.color1,
	backgroundColor:'transparent',
	style:Titanium.UI.iPhone.TableViewStyle.GROUPED

});

Titanium.UI.currentWindow.add(tableView);

Titanium.UI.currentWindow.addEventListener('focus', function (e) {
        reloadPropertiesAndUris();

        Ti.API.info(STOCK_URI);

        xhr.open('GET',STOCK_URI);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.send();
    
});


var addStock = Titanium.UI.createButton({
	title:'Add Stock'
});
addStock.addEventListener('click', function()
{
	Titanium.UI.createAlertDialog({title:'System Button', message:'Add Stock'}).show();
});
win.rightNavButton = addStock;

