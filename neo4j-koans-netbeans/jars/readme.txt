These two jars must be installed into a local repository using these commands

these are from http://m2.neo4j.org/content/repositories/snapshots/org/neo4j/app/neo4j-server/1.8-SNAPSHOT/

they are listed at this location and had their names simplified.

mvn install:install-file -Dfile=/home/dhenton/Downloads/neo/neo4j-server-1.8-tests.jar -DgroupId=org.neo4j.app -DartifactId=neo4j-server-tests -Dversion=1.8-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=/home/dhenton/Downloads/neo/neo4j-server-static-web.jar -DgroupId=org.neo4j.app -DartifactId=neo4j-server-static-web -Dversion=1.8-SNAPSHOT -Dpackaging=jar
