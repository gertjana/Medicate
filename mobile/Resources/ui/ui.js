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
	medicate.ui = {};
	
	//create a film strip like view 
	medicate.ui.createFilmStripView = function(_args) {
		var root = Ti.UI.createView(medicate.combine($$.stretch,_args)),
			views = _args.views,
			container = Ti.UI.createView({
				top:0,
				left:0,
				bottom:0,
				width:$$.platformWidth*_args.views.length
			});

		for (var i = 0, l = views.length; i<l; i++) {
			var newView = Ti.UI.createView({
				top:0,
				bottom:0,
				left:$$.platformWidth*i,
				width:$$.platformWidth
			});
			newView.add(views[i]);
			container.add(newView);
		}
		root.add(container);

		//set the currently visible index
		root.addEventListener('changeIndex', function(e) {
			var leftValue = $$.platformWidth*e.idx*-1;
			container.animate({
				duration:$$.animationDuration,
				left:leftValue
			});
		});

		return root;
	};	
})();


Ti.include(
	'/ui/ApplicationWindow.js',
	'/ui/utils.js'
);

