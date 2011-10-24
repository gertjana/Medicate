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

var win = Titanium.UI.currentWindow;
//win.backgroundImage = '../images/gradientBackground.png';

var url = Titanium.App.Properties.getString('url');
var username = Titanium.App.Properties.getString('username');
var password = Titanium.App.Properties.getString('password');
var apikey = Titanium.App.Properties.getString('apikey');

win.add(Ti.UI.createLabel({
    color:settings.color1,
    font:{fontSize:16,fontWeight:'bold', fontFamily:'Arial'},
    left:10,
    top:10,
    height:30,
    width:'auto',
    text:'Webservice URL'
}));

win.add(Ti.UI.createLabel({
    color:settings.color1,
    font:{fontSize:16,fontWeight:'bold', fontFamily:'Arial'},
    left:10,
    top:70,
    height:30,
    width:'auto',
    text:'Username'
}));

win.add(Ti.UI.createLabel({
    color:settings.color1,
    font:{fontSize:16,fontWeight:'bold', fontFamily:'Arial'},
    left:10,
    top:130,
    height:30,
    width:'auto',
    text:'Password'
}));

win.add(Ti.UI.createLabel({
    color:settings.color1,
    font:{fontSize:16,fontWeight:'bold', fontFamily:'Arial'},
    left:10,
    top:190,
    height:30,
    width:'auto',
    text:'Api key'
}));

var apikeyLabel = Ti.UI.createLabel({
   color:settings.color2,
    font:{fontSize:12, fontFamily:'Arial'},
    left:10,
    top:210,
    height:30,
    width:'auto',
    text:apikey
});

win.add(apikeyLabel);


var urlTextField = Titanium.UI.createTextField({
	color: settings.color2,
	height:35,
	top:35,
	left:10,
	width:250,
	keyboardType:Titanium.UI.KEYBOARD_URL,
	returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
	borderStyle:Titanium.UI.INPUT_BORDERSTYLE_ROUNDED,
    value:url
});

var usernameTextField = Titanium.UI.createTextField({
	color: settings.color2,
	height:35,
	top:95,
	left:10,
	width:250,
	keyboardType:Titanium.UI.KEYBOARD_DEFAULT,
	returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
	borderStyle:Titanium.UI.INPUT_BORDERSTYLE_ROUNDED,
    value:username
});

var passwordTextField = Titanium.UI.createTextField({
	color: settings.color2,
	height:35,
	top:155,
	left:10,
	width:250,
	keyboardType:Titanium.UI.KEYBOARD_URL,
	returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
	borderStyle:Titanium.UI.INPUT_BORDERSTYLE_ROUNDED,
    value:password,
    passwordMask:true
});

var button = Titanium.UI.createButton({
   height:35,
   top:245,
   left:10,
   width:150,
   color:settings.color1,
   title:"Retrieve api key"
});
win.add(button);

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

win.add(urlTextField);
win.add(usernameTextField);
win.add(passwordTextField);


var fbButton = Titanium.Facebook.createLoginButton({
	'style':'wide',
	'appid':'bla',
	'apikey':'9494e611f2a93b8d7bfcdfa8cefdaf9f',
	'sessionProxy':'http://api.appcelerator.net/p/fbconnect/',
    top:100,
    height:100
});
//win.add(fbButton);

