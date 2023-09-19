solution with AngularJS and Restfull services

- you should be using Jboss EAP 7

- you have to add in your project buld path
EAP-7.0.0/modules/system/layers/base/com/fasterxml/jackson/core/jackson-annotations/main/jackson-annotations-2.5.4.redhat-1.jar
EAP-7.0.0/modules/system/layers/base/javax/ws/rs/api/main/jboss-jaxrs-api_2.0_spec-1.0.0.Final-redhat-1.jar

- import in your project
	- the Java sources in a pack package
	- persistence.xml in a src/META-INF folder
	- the index.html page in the WebContent folder
	- web.xml in the WebContent/WEB-INF folder

- deploy it in JBoss as before

