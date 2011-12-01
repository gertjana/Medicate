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
	medicate.ui.createSettingsView = function(_args) {
		Ti.API.log("DEBUG","starting settings view");
		var settingsView = Ti.UI.createView($$.stretch);
		
		settingsView.add(Ti.UI.createLabel(medicate.combine($$.headerText,{text:L('settings')})));

		
		var url = Titanium.App.Properties.getString('url');
		var username = Titanium.App.Properties.getString('username');
		var password = Titanium.App.Properties.getString('password');
		var apikey = Titanium.App.Properties.getString('key');

		settingsView.add(Ti.UI.createLabel({
		    left:10,
		    top:$$.headerView.height,
		    height:30,
		    width:'auto',
		    text:'Webservice URL'
		}));

		settingsView.add(Ti.UI.createLabel({
		    left:10,
		    top:$$.headerView.height+60,
		    height:30,
		    width:'auto',
		    text:'Username'
		}));

		settingsView.add(Ti.UI.createLabel({
		    left:10,
		    top:$$.headerView.height+120,
		    height:30,
		    width:'auto',
		    text:'Password'
		}));

		settingsView.add(Ti.UI.createLabel({
		    left:10,
		    top:$$.headerView.height+180,
		    height:30,
		    width:'auto',
		    text:'Api key'
		}));

		var apikeyLabel = Ti.UI.createLabel({
		    font:{fontSize:12, fontFamily:'Arial'},
		    left:10,
		    top:$$.headerView.height+200,
		    height:30,
		    width:'auto',
		    text:apikey
		});
		settingsView.add(apikeyLabel);


		var urlTextField = Titanium.UI.createTextField({
			height:35,
			top:$$.headerView.height+25,
			left:10,
			width:250,
			keyboardType:Titanium.UI.KEYBOARD_URL,
			returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
			borderStyle:Titanium.UI.INPUT_BORDERSTYLE_ROUNDED,
		    value:url
		});
		settingsView.add(urlTextField);

		var usernameTextField = Titanium.UI.createTextField({
			height:35,
			top:$$.headerView.height+85,
			left:10,
			width:250,
			keyboardType:Titanium.UI.KEYBOARD_DEFAULT,
			returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
			borderStyle:Titanium.UI.INPUT_BORDERSTYLE_ROUNDED,
		    value:username
		});
		settingsView.add(usernameTextField);

		var passwordTextField = Titanium.UI.createTextField({
			height:35,
			top:$$.headerView.height+145,
			left:10,
			width:250,
			keyboardType:Titanium.UI.KEYBOARD_URL,
			returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
			borderStyle:Titanium.UI.INPUT_BORDERSTYLE_ROUNDED,
		    value:password,
		    passwordMask:true
		});
		settingsView.add(passwordTextField);

		var button = Titanium.UI.createButton({
		   height:35,
		   top:$$.headerView.height+235,
		   left:10,
		   width:150,
		   title:"Retrieve api key"
		});
		settingsView.add(button);
		
		var xhr = Titanium.Network.createHTTPClient();

		button.addEventListener('click', function() {
		   var username = Ti.App.Properties.getString('username');
		   var password = Ti.App.Properties.getString('password');
		   var authUri =  Ti.App.Properties.getString('url') + "api/auth/" + username + "/" + password;

		   Ti.API.info(authUri);
		   xhr.open('GET',authUri);
		   xhr.send();
		});

		xhr.onload = function() {
		    if (this.status == 200) {
		        var key = JSON.parse(this.responseText).key;
		        Ti.App.Properties.setString("key", key);
		        apikeyLabel.text = key;
		    } else {
		        Ti.App.Properties.setString("key", "");
		        apikeyLabel.text = "";
		        win.message.text(JSON.parse(this.responseText).error);
		    }
		};

		urlTextField.addEventListener('change', function()
		{
		    Titanium.App.Properties.setString("url", urlTextField.value);
		});

		usernameTextField.addEventListener('change', function()
		{
		    if (Ti.App.Properties.getString("username") != usernameTextField.value) {
		        Titanium.App.Properties.setString("username", usernameTextField.value);
		        Titanium.App.Properties.setString("apikey", "");
		        apikeyLabel.text = "";
		    }
		});
		passwordTextField.addEventListener('change', function()
		{
		    if (Ti.App.Properties.getString("password") != passwordTextField.value) {
		        Titanium.App.Properties.setString("password", passwordTextField.value);
		        Titanium.App.Properties.setString("apikey", "");
		        apikeyLabel.text = "";
		    }
		});


		return settingsView;
	}
})();		