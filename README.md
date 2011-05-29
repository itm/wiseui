WiseUI
======

The WiseUI is a [GWT][gwt] based web client for the [Testbed Runtime][testbedruntime].
The project is carried out as a joint venture formed by R.A. Computer Technology Institute (RACTI, University of Patras)
and the Institute of Telematics (ITM, University of Lübeck).

The WiseUI project is related to the research projects [WISEBED][wisebed] and [SmartSantander][smartsantander].


What do I need?
---------------

   * Git
   * JDK >= 1.6
   * Maven 2.2 or 3.0

All library dependencies are downloaded by Maven.

Build and Start the WiseUI
--------------------------

On the command-line go to the WiseUI directory. Perform a clean build on the project to make sure, that all Maven artifacts are installed in your local Maven repository `~/.m2/repository`. If you are running Maven for the first time, it will take some time as Maven downloads all project dependencies from the internet.

    $ cd wiseui
    $ mvn clean install
    
In the current configuration all tests should be configured as ignored, but if want to be certain you can skip the tests by appending `-DskipTests` to your Maven command:

    $ mvn clean install -DskipTests    

To start the WiseUI in "hosted mode" change to the `client` directory (where the actual web application resides) and use the GWT Maven plugin goal `gwt:run`:

    $ cd wiseui/client
    $ mvn gwt:run
    
For debug mode use:

    $ mvn gwt:debug    

 
Configure Hibernate
-------------------

If you want to configure you own database connection instead of the default HSQL, you can do this by declaring the following properties in your local Maven settings file `~/.m2/settings.xml`:

	<settings>
	    ...
		<profiles>
            <profile>
                <id>inject-hibernate-details-wiseui</id>
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
			<activeProfile>inject-hibernate-details-wiseui</activeProfile>
		</activeProfiles>
		...
	</settings>   
	

Loading Initial Testbed Configurations
--------------------------
    
If you want to have an initial set of testbed configurations, you can load this dump into your local e.g. MySQL database.

    $ mysql -u dbuser -p dbpass somedb < wiseui/persistence/src/main/sql/initial_testbed_configs.sql   

This will create a table called `testbed_config` in the database `somedb` containing the intial [WISEBED][wisebed] testbed configurations.


Deploy to a Tomcat
------------------

Your `~/.m2/settings.xml` should include a server configuration:

    <settings>
        ...
        <servers>
            <server>
               <id>tomcat6</id>
               <username>tomcat</username>
               <password>yourpassword</password>
            </server>
        </servers>
        ...
        <profiles>
            <profile>
                <id>inject-tomcat-details-wiseui</id>
                <properties>
                    <tomcat.manager.url>http://localhost:8080/manager</tomcat.manager.url>
                </properties>
           </profile>
        </profiles>
        ...
        <activeProfiles>
           <activeProfile>inject-tomcat-details-wiseui</activeProfile>
        </activeProfiles>
        ...
    </settings>

This requires a user `tomcat` with the role `manager` in your Tomcat installation for the configure URL `${tomcat.manager.url}` ([click here for more][tomcatmanager]). Furthermore, it is important that you use the id `tomcat6` for the server, because this is how it is referenced in the WiseUI master POM.

You can deploy the WiseUI web application with the Tomcat Maven plugin:

    $ pwd
    > [...]/wiseui
    $ cd client
    $ mvn tomcat:deploy
    
If you already have the web application running, you have to use `redeploy`:

    $ mvn tomcat:redeploy
    
For more information about the Tomcat Maven plugin visit the [plugin website][tomcatmvnplugin].


More Documentation
==================

Take a look at our [wiki][].

[gwt]:http://code.google.com/webtoolkit/doc/2.2/DevGuide.html
[wiki]:https://github.com/itm/wiseui/wiki
[testbedruntime]:https://github.com/itm/testbed-runtime
[wisebed]:http://www.wisebed.eu
[smartsantander]:http://www.smartsantander.eu
[tomcatmvnplugin]:http://mojo.codehaus.org/tomcat-maven-plugin/
[tomcatmanager]:http://tomcat.apache.org/tomcat-6.0-doc/manager-howto.html#Configuring_Manager_Application_Access


Continuous Integration - Latest Stable build
============================================

The latest stable version is available from [Hudson](http://ru1.cti.gr/hudson/job/wiseui/), the continuous integration tool.

  * [client-0.3.war](http://ru1.cti.gr/hudson/job/wiseui/lastSuccessfulBuild/artifact/client/target/client-0.3.war)
