WiseUI
======

The WiseUI is a [GWT][gwt] based web client for the [Testbed Runtime][testbedruntime].
The project is carried out as a joint venture formed by R.A. Computer Technology Institute (CTI, University of Patras)
and the Institute of Telemactics (ITM, University of Lübeck).

The WiseUI project is related to the research projects [WISEBED][wisebed] and [SmartSantander][smartsantander].


What do I need?
---------------

   * Git
   * JDK >= 1.6
   * Maven 2.2 or 3.0

All library dependencies are downloaded by Maven.

Git Workflow
------------

The following commands show how to clone the WiseUI repository and how to make first changes.

    $ git clone git@github.com:itm/wiseui.git
    $ cd wiseui
    $ (edit files)
    $ git add (files)
    $ git commit -a -m "Explain what I changed"
    $ git status
    $ git push origin master

Configure Hibernate
-------------------

In order to have Hibernate properly configured, make sure you declare the following required properties in your local Maven settings file (~/.m2/settings.xml):

	<settings>
		<profiles>
            <profile>
                <id>inject-hibernate-details</id>
                <properties>
                    <hibernate.dialect>org.hibernate.dialect.MySQLDialect</hibernate.dialect>
                    <jdbc.connection.driver_class>com.mysql.jdbc.Driver</jdbc.connection.driver_class>
                    <jdbc.connection.url>jdbc:mysql://localhost/wiseuidb</jdbc.connection.url>
                    <jdbc.connection.username>root</jdbc.connection.username>
                    <jdbc.connection.password/>
                    <jdbc.connection.pool_size>10</jdbc.connection.pool_size>
                </properties>
            </profile>
		</profiles>
		<activeProfiles>
			<activeProfile>inject-hibernate-details</activeProfile>
		</activeProfiles>
	</settings>


Build and Start the WiseUI
--------------------------

On the command-line go to the WiseUI directory. Perform a clean build on the project to make sure, that all Maven artifacts are installed in your local Maven repository (~/.m2/repository). If you are running Maven for the first time, it will take some time as Maven downloads all project dependencies from the internet.

    $ cd wiseui
    $ mvn clean install
    
Or, if you don't want to execute the JUnit tests, try:

    $ mvn clean install -DskipTests    

To start the WiseUI in "hosted mode", do the following:

    $ cd wiseui/client
    $ mvn gwt:run
    
For debug mode type:

    $ mvn gwt:debug    


Deploy to a Remote Tomcat
-------------------------

To use this profile, make sure you settings.xml (~.m2/settings.xml) looks like the example below. Replace the property values with your specific credentials, hostname, etc.

> Also, it is highly recommended that you SSH client is configured to log in on you remote server (deployment target) without password. This requires a special SSH setup.
> You have to create a private key with ssh-keygen and copy it into the file ~.ssh/authorized_keys on your remote server.

This is how your settings.xml should look like:

    <settings>
        <profiles>
            <profile>
                <id>inject-deployment-details</id>
                <properties>
                    <deployment.artifact.name>wiseui</deployment.artifact.name>
                    <deployment.user>root</deployment.user>
                    <deployment.host>10.0.0.1</deployment.host>
                    <deployment.directory>/usr/local/tomcat/webapps/</deployment.directory>
                </properties>
            </profile>
        </profiles>
        <activeProfiles>
            <activeProfile>inject-deployment-details</activeProfile>
        </activeProfiles>
    </settings>

You can deploy the resulting war-file with:

    $ pwd
    > [...]/wiseui
    $ cd client
    $ mvn install -Ddeploy=remote
    
This Maven command activates the profile in client/pom.xml. The profile contains a maven-exec plugin  configuration, which uploads the `*.war` file to the designated server's Tomcat webapps directory. If the Tomcat server is configured to auto-deploy applications, the new web application will be deployed and started immediately.


More Documentation
==================

Take a look at our [wiki][].

[gwt]:http://code.google.com/webtoolkit/doc/2.2/DevGuide.html
[wiki]:https://github.com/itm/wiseui/wiki
[testbedruntime]:https://github.com/itm/testbed-runtime
[wisebed]:http://www.wisebed.eu
[smartsantander]:http://www.smartsantander.eu


Continuous Integration - Latest Stable build
============================================

The latest stable version is available from [Hudson](http://ru1.cti.gr/hudson/job/wiseui/), the continuous integration tool.

  * [client-0.3.war](http://ru1.cti.gr/hudson/job/wiseui/lastSuccessfulBuild/artifact/client/target/client-0.3.war)
