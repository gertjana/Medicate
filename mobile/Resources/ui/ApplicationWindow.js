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

(function() {
	var platformWidth = Ti.Platform.displayCaps.platformWidth;
	var buttonSize = (platformWidth - 40)/2;
	//create the main application window
	medicate.ui.createApplicationWindow = function(_args) {
		var win = Ti.UI.createWindow(medicate.combine($$.Window,{
				exitOnClose:true,
				orientationModes:[Ti.UI.PORTRAIT]
			}));

		var headerView = Ti.UI.createView(medicate.combine($$.headerView,{top:0}));
		headerView.add(Ti.UI.createLabel(medicate.combine($$.headerText,{text:'Medicate'})));
		win.add(headerView);

		// create filmstrip
		var filmStrip = medicate.ui.createFilmStripView({
			top:0,
			left:0,
			right:0,
			bottom:0,
			views:[medicate.ui.createSupplyView()]
		});
		
		
		// supply button
		var supplyButton = Ti.UI.createButton(
				medicate.combine($$.Button,{
			top:60,
			left:10,
			width: buttonSize,
			height: buttonSize,
			title: 'Supply',
			borderColor: '#000',
			borderRadius: 20,
			borderWidth: 3,
			image:'/images/medication.png'
			
		}));
		win.add(supplyButton);
		supplyButton.addEventListener('click', function() {
			alert("fire");
			filmStrip.fireEvent('changeIndex', {idx:1});
		});
		
		
			
		var stockButton = Ti.UI.createButton(
				medicate.combine($$.Button,{
			top:60,
			left:platformWidth/2+10,
			width: buttonSize,
			height: buttonSize,
			title: 'Stock',
			borderColor: '#000',
			borderRadius: 20,
			borderWidth: 3,
			image:'/images/stock.png'
			
		}));
		win.add(stockButton);				
		var dosagesbutton = Ti.UI.createButton(
				medicate.combine($$.Button,{
			top:60+buttonSize+10,
			left:10,
			width: buttonSize,
			height: buttonSize,
			title: 'Dosage',
			borderColor: '#000',
			borderRadius: 20,
			borderWidth: 3,
			image:'/images/potion.png'

		}));
		win.add(dosagesbutton);	
					
		var settingsButton = Ti.UI.createButton(
				medicate.combine($$.Button,{
			top:60+buttonSize+10,
			left:platformWidth/2+10,
			width: buttonSize,
			height: buttonSize,
			title: 'Settings',
			borderColor: '#000',
			borderRadius: 20,
			borderWidth: 3,
			image:'/images/settings.png'
		}));
		win.add(settingsButton);				



		if (Ti.Network.online == false) {
//			Ti.UI.createAlertDialog({
//				title:'No Network Connection', 
//				message:'Sorry, but we couldn\'t detect a connection to the internet - new Medicine data will not be available.'
//			}).show();
		}

		return win;
	};
})();


