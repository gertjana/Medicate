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
	medicate.model = {
		dbname:'medicate-db' //overwrite this before creating your first entity to change
	};
	
	//Create a persistent entity
	medicate.model.Entity = function(_class, _properties) {
		tt.mixin(this,_properties);

		this._className = _class;

		//Create a table for this entity type
		var db = Ti.Database.open(medicate.model.dbname);
		db.execute('CREATE TABLE IF NOT EXISTS '+_class+' (id INTEGER PRIMARY KEY, json TEXT)');
		db.close();

		//save this entity - returns the ID of this entity
		this.save = function() {
			db = Ti.Database.open(model.model.dbname);
			db.execute('INSERT INTO '+this._className+' (json) VALUES (?)',JSON.stringify(this));
			var id = db.lastInsertRowId;
			this.id = id;
			db.close();
			Ti.App.fireEvent('app:entity.saved',{
				className:this._className,
				id:id
			});
			return id;
		};
	};

	//helper function to hydrate a JSON graph with class functions
	function hydrate(_className, _json) {
		return (medicate.model[_className]) ? new medicate.model[_className](JSON.parse(_json)) : JSON.parse(_json);
	}

	//load an entity by the given ID
	medicate.model.load = function(_className, _id) {
		var obj = null,
		db = Ti.Database.open(medicate.model.dbname);

		//be tolerant of entities that don't exist - create a table for them
		db.execute('CREATE TABLE IF NOT EXISTS '+_className+' (id INTEGER PRIMARY KEY, json TEXT)');

		var rs = db.execute('SELECT * FROM '+_className+' WHERE id = ?', _id);

		if (rs.isValidRow()) {
			var json = rs.fieldByName('json');
			obj = hydrate(_className,json);
			obj.id = rs.fieldByName('id');
		}

		rs.close();
		db.close();
		return obj;
	};

	//get a list of all entities of the given class
	medicate.model.list = function(_className) {
		var results = [],
		db = Ti.Database.open(medicate.model.dbname);

		//be tolerant of entities that don't exist - create a table for them
		db.execute('CREATE TABLE IF NOT EXISTS '+_className+' (id INTEGER PRIMARY KEY, json TEXT)');

		var rs = db.execute('SELECT * FROM '+_className);

		while (rs.isValidRow()) {
			var json = rs.fieldByName('json');

			obj = hydrate(_className,json);
			obj.id = rs.fieldByName('id');

			results.push(obj);
			rs.next();
		}

		rs.close();
		db.close();
		return results;
	};
})();
