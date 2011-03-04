WiseUI
======

The WiseUI is a [GWT][gwt] based web client for the [Testbed Runtime][testbedruntime].


What do I need?
---------------
   * Git
   * JDK >= 1.6
   * Maven 2.2 or 3.0

Git Workflow
------------

The Web UI is developed in a separate branch "gwt-web-ui" of the testbed-runtime project. To set up a working branch on your local machine, please follow the instructions below:

    $ git clone git@github.com:itm/wiseui.git
    $ cd wiseui
    $ (edit files)
    $ git add (files)
    $ git commit -a -m "Explain what I changed"
    $ git status
    $ git push origin master


Build and Start the WiseUI with Maven
-------------------------------------

On the command-line go to the place where your clone of the testbed-runtime resides. Perform a clean build on the project to make sure, that all Maven artifacts are installed in your local Maven repository (~/.m2/repository). If you are running Maven for the first time, this will take a while as Maven downloads all project dependencies from the internet.

    $ cd wiseui
    $ mvn clean install

To start the WiseUI in "hosted mode", do the following:

    $ cd wiseui/client
    $ mvn gwt:run # or mvn gwt:debug


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
    
This Maven command activates the profile in client/pom.xml. The profile contains a maven-exec plugin  configuration, which uploads the *.war file to the designated server's Tomcat webapps directory. If the Tomcat server is configured to auto-deploy applications, the new web application will be deployed and started immediately.


More Documentation
==================
Take a look at our [wiki][].


[gwt]:http://code.google.com/webtoolkit/doc/2.1/DevGuide.html
[wiki]:https://www.itm.uni-luebeck.de/projects/testbed-runtime/wiki/WisebedWebUiDesign
[quick]:https://www.itm.uni-luebeck.de/projects/testbed-runtime/wiki/WisebedWebUiDevQuickStart
[testbedruntime]:https://github.com/itm/testbed-runtime
