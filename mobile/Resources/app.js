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

Ti.include("windows/constants.js");

Titanium.API.info("starting application");

var winHome = Titanium.UI.createWindow({
    url:'windows/home.js',
    title:'Home',
    barColor: settings.color1,
    color1: settings.color1,
    color2: settings.color2
});
var tabHome = Titanium.UI.createTab({
    icon:'images/home.png',
    title:'Home',
    window:winHome
});
var winStock = Titanium.UI.createWindow({
    url:'windows/stock.js',
    title:'Stock',
    barColor: settings.color1,
    color1: settings.color1,
    color2: settings.color2
});
var tabStock = Titanium.UI.createTab({
    icon:'images/stock.png',
    title:'Stock',
    window:winStock
});
var winDosages = Titanium.UI.createWindow({
    url:'windows/dosages.js',
    title:'Dosages',
    barColor: settings.color1,
    color1: settings.color1,
    color2: settings.color2
});
var tabDosages = Titanium.UI.createTab({
    icon:'images/dosages.png',
    title:'Dosages',
    window:winDosages
});


var winSettings = Titanium.UI.createWindow({
    url:'windows/settings.js',
    title:'Settings',
    barColor: settings.color1,
    color1: settings.color1,
    color2: settings.color2
});
var tabSettings = Titanium.UI.createTab({
    icon:'images/settings.png',
    title:'Settings',
    window:winSettings
});

var winMedication = Titanium.UI.createWindow({
    url:'windows/medication.js',
    title:'Medication',
    barColor: settings.color1,
    color1: settings.color1,
    color2: settings.color2
});
var tabMedication = Titanium.UI.createTab({
    icon:'images/medication.png',
    title:'Medication',
    window:winMedication
});

var tabGroup = Titanium.UI.createTabGroup();

tabGroup.addTab(tabHome);
tabGroup.addTab(tabMedication);
tabGroup.addTab(tabStock);
tabGroup.addTab(tabDosages);
tabGroup.addTab(tabSettings);

tabGroup.open({
	transition:Titanium.UI.iPhone.AnimationStyle.CURL_UP
});


