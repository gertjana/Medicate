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

var settings = {
    color1: '#040442',
    color2: '#040422'
};

var APIKEY = "";
var BASE_URI = "";
var STOCK_URI = "";
var DOSAGES_URI = "";
var SUPPLIES_URI = "";

function loadPropertiesAndUris() {
    APIKEY = Ti.App.Properties.getString("key");
    BASE_URI = Ti.App.Properties.getString("url");
    DOSAGES_URI =   BASE_URI + "api/user/" + APIKEY + "/dosages";
    STOCK_URI = BASE_URI + "api/user/" +  APIKEY + "/stock";
    SUPPLIES_URI = BASE_URI + "api/user/" + APIKEY + "/supplies";
}

loadPropertiesAndUris();

function reloadPropertiesAndUris() {
    loadPropertiesAndUris();
}