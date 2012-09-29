These two jars must be installed into a local repository using these commands

these are from http://m2.neo4j.org/content/repositories/snapshots/org/neo4j/app/neo4j-server/1.8-SNAPSHOT/

they are listed at this location and had their names simplified.

mvn install:install-file -Dfile=/home/dhenton/Downloads/neo/neo4j-server-1.8-tests.jar -DgroupId=org.neo4j.app -DartifactId=neo4j-server-tests -Dversion=1.8 -Dpackaging=jar

mvn install:install-file -Dfile=/home/dhenton/Downloads/neo/neo4j-server-static-web.jar -DgroupId=org.neo4j.app -DartifactId=neo4j-server-static-web -Dversion=1.8 -Dpackaging=jar


==============

In netbeans:

1) In Maven project open "Add dependency" dialog
2) Make up some groupId, artifactId and version and fill them, OK.
3) Dependency will be added to the pom.xml and will appear under "Libraries" node of maven project
4) Right-click jar node that appears in the dependency section and "manually install artifact", fill the path to the jar
5) Jar should be installed to local Maven repo with coordinates entered in step 2) 