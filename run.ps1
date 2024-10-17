# build
$MAVEN_CLI_OPTS = "--batch-mode"
./mvnw $MAVEN_CLI_OPTS compile

# package
$env:MAVEN_OPTS = "-Dmaven.container.image.build=true -Dmaven.container.image.push=true"
./mvnw $MAVEN_CLI_OPTS install

# run
./mvnw $MAVEN_CLI_OPTS spring-boot:run --quiet