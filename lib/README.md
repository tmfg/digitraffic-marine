# Building Spring fox Swagger UI

clone https://github.com/springfox/springfox -project

Build

        cd springfox/springfox-swagger-ui
        
Add proper java home for JAva 8 in gradle.properties

        org.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
        
Run build

        ./gradlew clean build -x test
        
Install library to project local maven repository. Add the short git commit revision identifier as version suffix.

        cd ..
        cd digitraffic-marine
        mvn install:install-file \
            -Dfile=../springfox/springfox-swagger-ui/build/libs/springfox-swagger-ui-3.0.0-SNAPSHOT.jar \
            -DgroupId=io.springfox -DartifactId=springfox-swagger-ui \
            -Dversion=3.0.0-SNAPSHOT-{GIT_REV} \
            -Dpackaging=jar \
            -DgeneratePom=true \
            -DlocalRepositoryPath=lib/
        
Change version to pom.xml

Build marine project and test Swagger UI

Commit and push changes