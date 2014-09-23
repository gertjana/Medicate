[![Build Status](https://api.shippable.com/projects/54200f9c78826375c3f0bce1/badge?branchName=master)](https://app.shippable.com/projects/54200f9c78826375c3f0bce1/builds/latest)


manage your medication, amounts, schedules, alerts

The web project is maven based, to run it:
 - install scala 2.9.1
 - clone this repository
 - cd to the web/ directory
 - run mvn jetty:run

The webapplication can be run in vmware's cloudfoundry
 - create an account on cloudfoundry
 - install the client tools (vmc)
 - create your application with the client tools, binding a mysql service to it
 - update your .m2/settings.xml with the example provided
 - use mvn cf:push to push you app to the cloud the first time
 - use mvn cf:update to update your app after that
 - look at the Boot.scala file to see how the db connection is setup


The mobile project is built with titanium appcelerator
 - install titanium studio
 - open the project in the mobile/ directory
 - run the emulator's from within titanium studio

links:
http://liftweb.net
http://www.appcelerator.com
http://www.cloudfoundry.com
