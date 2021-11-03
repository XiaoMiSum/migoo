mvn -f ../pom.xml clean source:jar -Dmaven.test.skip=true deploy gpg:sign
