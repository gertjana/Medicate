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
//(function() {
	//medicate.network = {};

	var httpClient = Titanium.Network.createHTTPClient();

	httpClient.setRequestHeader("Accept: application/json");

	httpClient.onerror = function(e) {
		Titanium.UI.createAlertDialog({title:'Network', message:e.error}).show();
	}

	function getJsonFromUrl(u,c) {
		
	};

/*	medicate.network.getJsonFromUrl = function (url, callback) {
		httpClient.onload = callback;
		httpClient.open('GET',url);
        httpClient.send();	
	};*/
//})();

