# Building Springfox Swagger UI and adding to project local m2 repository

clone https://github.com/springfox/springfox -project

## Build Springfox project

Go to springfox -project directory
        
Add proper java home for Java 8 in gradle.properties ie.

        org.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
        
Run build

        ./gradlew clean build
 
       
## Install library to project local maven repository.

Add the short git commit revision identifier as version suffix {GIT_REV}.

        # in digitraffic-marine -project
        mvn install:install-file \
            -Dfile=../springfox/springfox-swagger-ui/build/libs/springfox-swagger-ui-3.0.0-SNAPSHOT.jar \
            -DgroupId=io.springfox -DartifactId=springfox-swagger-ui \
            -Dpackaging=jar \
            -DgeneratePom=true \
            -DlocalRepositoryPath=local-m2 \
            -Dversion=3.0.0-SNAPSHOT-{GIT_REV}
        
Update version to root project pom.xml

            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>3.0.0-SNAPSHOT-{GIT_REV}</version>
            </dependency>

Build marine project and test Swagger UI

Commit and push changes