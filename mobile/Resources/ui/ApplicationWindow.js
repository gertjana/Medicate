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

	//create the main application window
	medicate.ui.createApplicationWindow = function(_args) {
		var win = Ti.UI.createWindow(medicate.combine($$.Window,{
			exitOnClose:true,
			orientationModes:[Ti.UI.PORTRAIT]
		})),
		headerView = Ti.UI.createView(medicate.combine($$.headerView,{top:0})),
			tabHeight = 60,
			tabWidth = platformWidth/5,
			tabView = Ti.UI.createView({
				bottom:0,
				height:tabHeight,
				//backgroundImage:'images/gray_top_BG.png', //TODO
				width:platformWidth
			}),
			tabs = [];
		
		//Add the main app 'filmstrip'	
		var appFilmStrip = medicate.ui.createFilmStripView({
			top:0,
			left:0,
			right:0,
			bottom:tabHeight-10,
			views: [
				//medicate.ui.createTimelineView(),
				//medicate.ui.createMentionsView(),
				//medicate.ui.createDMView(),
				//medicate.ui.createAccountView(),
				//medicate.ui.createAboutView()
			]
		});
		
		//create the 'tab' view, which we will animate back and forth along the tab bar
		var tab = Ti.UI.createView({
			left:0,
			top:15,
			height:45,
			width:tabWidth,
			bottom:0
		});

		tab.add(Ti.UI.createImageView({
			top:0,
			left:0,
			height:10, //might not want to hard-code these, should scale more smarter-er ;)
			width:10,
			image:'images/blk_button_BG.png' //TODO
		}));

		tab.add(Ti.UI.createView({
			right:10,
			left:10,
			backgroundColor:medicate.ui.theme.darkBlue
		}));

		tab.add(Ti.UI.createImageView({
			top:0,
			right:0,
			height:10, //might not want to hard-code these, should scale more smarter-er ;)
			width:10,
			image:'images/tab_r.png'
		}));

		tabView.add(tab);
		
		//create clickable tab images
		function createTab(_icon,_cb,_on) {
			var view = Ti.UI.createView({
				width:tabWidth
			}),
			off_path = 'images/'+_icon+'.png',
			on_path = 'images/'+_icon+'_on.png',
			dimension = 40,
			image = Ti.UI.createImageView({
				height:dimension,
				width:dimension,
				image:(_on) ? on_path : off_path,
				bottom:2
			});

			view.on = _on||false; //ivar for 'on' state

			//assemble view
			view.add(image);
			view.addEventListener('click',_cb);

			//'instance' method
			view.toggle = function() {
				view.on = !view.on;
				image.image = (view.on) ? on_path : off_path;
			};

			return view;
		}

		//toggle view state of application to the relevant tab
		function selectIndex(_idx) {
			for (var i = 0, l = tabs.length; i<l; i++) {
				//select the tab and move the tab 'cursor'
				if (_idx === i) {
					//if the tab is already selected, do nothing
					if (!tabs[i].on) {
						Ti.API.info('selecting tab index: '+_idx);
						//animate the tab
						tab.animate({
							duration:$$.animationDuration,
							left:tabWidth*i,
							bottom:0
						},function(idx) { //use closure to retain value of i in idx
							return function() {
								if (!tabs[idx].on) {
									tabs[idx].toggle();
								}
							};
						}(i));

						//set the current film strip index
						appFilmStrip.fireEvent('changeIndex',{idx:i});
					}
				}
				else if (tabs[i].on && (_idx !== i)) {
					tabs[i].toggle();
				}
			}
		}

		//assemble main app tabs
		// HACK: need to use annonymous functions to wrap selectIndex as a view event handler
		tabs.push(createTab('home', function() {
			selectIndex(0);
		},true));
		tabs.push(createTab('medicines', function() {
			selectIndex(1);
		}));
		tabs.push(createTab('dosages', function() {
			selectIndex(2);
		}));
		tabs.push(createTab('stock', function() {
			selectIndex(3);
		}));
		tabs.push(createTab('settings', function() {
			selectIndex(4);
		}));

		//add tabs to layout
		for (var i = 0, l = tabs.length; i<l; i++) {
			tabs[i].left = tabWidth*i;
			tabView.add(tabs[i]);
		}

		//App app-level event listener to change tabs
		Ti.App.addEventListener('app:change.tab', function(e) {
			selectIndex(e.tabIndex);
		});

		//create 'drawer' view to show drill-down data
		//var drawer = tt.ui.createDrawerView();

		//create a loading view which we can show on long data loads
		//var loader = tt.ui.createLoadingView();

		//assemble main app window
		win.add(headerView);
		win.add(tabView);
		win.add(appFilmStrip);
		//win.add(drawer);
		//win.add(loader);


		//initialize Twitter goodness and let folks know most of the awesomeness will not be available offline
		if (Ti.Network.online == false) {
			Ti.UI.createAlertDialog({
				title:'No Network Connection', 
				message:'Sorry, but we couldn\'t detect a connection to the internet - new Twitter data will not be available.'
			}).show();
		}

		//one-time switch used if no account is present
		function switchit() {
			//selectIndex(3);
			//Ti.App.removeEventListener('app:drawer.opened', switchit);
		}

		//get the current account
		if (!Ti.App.Properties.hasProperty('currentAccountID')) {
			//If we don't have an account, that means none has been entered.  Switch tabs and create one
			//Ti.App.fireEvent('app:show.drawer', {showing:'createAccount'});
			//switch tabs after the drawer opens
			//Ti.App.addEventListener('app:drawer.opened', switchit);
		} else {
			//tt.app.currentAccount = tt.model.load('Account',Ti.App.Properties.getString('currentAccountID'));
			//Ti.App.fireEvent('app:account.selected');
		}

		return win;
	};
})();
