Clue Ride Server
=========================================================================================================
Author: Jett Marks
Technologies: CDI, REST, JPA
Summary: Provides REST support for the Clue Ride Mobile Applications: Player, Location Editor and Invitation Editor
Target Product: WildFly

What is it?
-----------

This REST API server provides back-end services for the Clue Ride suite of Mobile Applications. All requests for
game domain objects are handled by this server. Those game domain objects are:

- Game Players (called Seekers), Course Editors, and Guides
- Courses over which the game is played
- Outings which are scheduled games
- Attractions which are destinations along the Courses
- Puzzles to be solved along the way
- Badges which are awarded
- Game State events which are propagated using a separate Server-Sent Event (SSE) server

This server is also responsible for authentication and authorization.

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The JPA implementation is dependent on PostGreSQL and its Geo-Spatial extensions.

The application this project produces is designed to be run on JBoss WildFly.

Since the mobile apps access maps, images and an event stream which are provided by other back-end servers, an Apache 
front-end maps URLs against the host domain to the separate servers which respond to requests.

The Apache front-end is responsible for handling the images used within the application.

The <a href="https://github.com/jettmarks/clueRide-sse-server">Server-Sent Event (SSE) server</a> provides propagation of events to Mobile apps.

Deploying the application
-------------------------

 
First you need to start the JBoss container. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat
    
Note: Adding "-b 0.0.0.0" to the above commands will allow external clients (phones, tablets, desktops, etc...) connect through your local network.

For example

    $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 

To deploy the application, you first need to produce the archive to deploy using the following Maven goal:

    mvn package

You can now deploy the artifact by executing the following command:

    mvn wildfly:deploy

The client application will be running at the following URL <http://localhost:8080/server/>.

To undeploy run this command:

    mvn wildfly:undeploy

You can also start the JBoss container and deploy the project using JBoss Tools. See the <a href="https://github.com/wildfly/quickstart/blob/master/guide/Introduction.asciidoc" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> for more information.

Minification
============================

By default, the project uses the [wro4j](http://code.google.com/p/wro4j/) plugin, which provides the ability to concatenate, validate and minify JavaScript and CSS files. These minified files, as well as their unmodified versions are deployed with the project.

With just a few quick changes to the project, you can link to the minified versions of your JavaScript and CSS files.

First, in the <project-root>/src/main/webapp/index.html file, search for references to minification and comment or uncomment the appropriate lines.

Finally, wro4j runs in the compile phase so any standard build command like package, install, etc. will trigger it. The plugin is in a profile with an id of "minify" so you will want to specify that profile in your maven build.

NOTE: You must either specify the default profile for no tests or the arquillian test profile to run tests when minifying to avoid test errors. For example:

    #No Tests
    mvn clean package wildfly:deploy -Pminify,default

OR

    #With Tests
    mvn clean package wildfly:deploy -Pminify,arq-wildfly-remote
 
Running the Arquillian tests
============================

By default, tests are configured to be skipped. The reason is that the sample test is an Arquillian test, which requires the use of a container. You can activate this test by selecting one of the container configuration provided  for JBoss.

To run the test in JBoss, first start the container instance. Then, run the test goal with the following profile activated:

    mvn clean test -Parq-wildfly-remote

Running the QUnit tests
============================

QUnit is a JavaScript unit testing framework used and built by jQuery. This application includes a set of QUnit tests in order to verify JavaScript that is core to this HTML5 application. Executing QUnit test cases is quite easy. First, make sure the server is running and the project has been deployed as some of the tests will be testing the functionality of the services. Then, simply load the following HTML in the browser you wish to test.

    <project-root>/src/test/qunit/index.html

For more information on QUnit tests see http://docs.jquery.com/QUnit

Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE (Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should already have an IDE project.

Detailed instructions for using Eclipse / JBoss Tools with are provided in the <a href="https://github.com/wildfly/quickstart/guide/Introduction/" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the command line using archetype:generate, then you need to import the project into your IDE. If you are using NetBeans 6.8 or IntelliJ IDEA 9, then all you have to do is open the project as an existing project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs of any library in the project, you can run either of the following two commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
