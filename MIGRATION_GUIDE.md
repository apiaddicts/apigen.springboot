# Migration guide

All changes required to migrate generated Apigen projects to new versions will be documented in this file.

## From [0.0.3] to [unreleased]

In this version Apigen has been updated to be autoconfigured when imported as dependency rather than be the parent of the generated project.

- Modify the parent of you project to be the official Spring Boot parent
- Add the following properties in the `pom.xml`

    ```xml
    <mapstruct-binding.version>0.2.0</mapstruct-binding.version>
    <mapstruct.version>1.4.2.Final</mapstruct.version>
    ```

- Add the following dependencies in the `pom.xml`

    ```xml
    <dependency>
        <groupId>org.apiaddicts.apitools.apigen</groupId>
        <artifactId>boot-starter</artifactId>
        <version>0.1.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <artifactId>junit-vintage-engine</artifactId>
                <groupId>org.junit.vintage</groupId>
            </exclusion>
        </exclusions>
    </dependency>
    ```
- Add the following plugins in the `pom.xml`
    ```xml
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
    <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
            <source>${java.version}</source>
            <target>${java.version}</target>
            <annotationProcessorPaths>
                <path>
                    <groupId>org.mapstruct</groupId>
                    <artifactId>mapstruct-processor</artifactId>
                    <version>${mapstruct.version}</version>
                </path>
                <path>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok</artifactId>
                    <version>${lombok.version}</version>
                </path>
                <path>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok-mapstruct-binding</artifactId>
                    <version>${mapstruct-binding.version}</version>
                </path>
            </annotationProcessorPaths>
        </configuration>
    </plugin>
    ```

[unreleased]: https://github.com/olivierlacan/keep-a-changelog/compare/v0.0.3...HEAD
[0.0.3]: https://github.com/apiaddicts/apigen/releases/tag/v0.0.3