# KeePass Maven Plugin

Maven plugin to integrate KeePass into build process

[![Build Status](https://travis-ci.org/knowhowlab/keepass-maven-plugin.svg?branch=master)](https://travis-ci.org/knowhowlab/keepass-maven-plugin)

[![Buy me a coffee](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/dimi)


## Features

- reads KeePass 2.x files
- supports both credentials: master password and key file
- sets properties from filtered KeePass entry
- filter Groups by UUID, path, name and name regex
- filter Entries by UUID, title and title regex
- read custom entry attributes
  
## Documentation
  
- Detailed reference is in the [User Manual](https://knowhowlab.gitbooks.io/keepass-maven-plugin/content/)
- Project info and reports are [here](http://knowhowlab.github.io/keepass-maven-plugin)
- Blog articles related to the plugin are [here](http://blog.knowhowlab.org/search?q=keepass)
- Mailing List is [here](https://groups.google.com/d/forum/keepass-maven-plugin)
  
## Installation

```xml
    <plugin>
        <groupId>org.knowhowlab.maven.plugins</groupId>
        <artifactId>keepass-maven-plugin</artifactId>
        <version>0.4.1</version>
    </plugin>
```

## Example

Read **Username**, **Password** and **URL** from **test.kdbx** file, group name **production**, entry title **HTTP Server**
and store in properties **http.username**, **http.password** and **http.url**

```xml
    <plugin>
        <groupId>org.knowhowlab.maven.plugins</groupId>
        <artifactId>keepass-maven-plugin</artifactId>
        <version>0.4</version>
        <configuration>
            <file>${project.basedir}/src/main/keepass/test.kdbx</file>
            <password>admin123</password>
        </configuration>
        <executions>
            <execution>
                <id>read-production</id>
                <goals>
                    <goal>read</goal>
                </goals>
                <configuration>
                    <records>
                        <record>
                            <prefix>http.</prefix>
                            <group>name:production</group>
                            <entry>title:HTTP Server</entry>
                        </record>
                    </records>
                </configuration>
            </execution>
        </executions>
    </plugin>
```

## Known issues

On Java 7 or 8 you can see an exception

```java
java.security.InvalidKeyException: Illegal key size or default parameters
```

This means that JCE (Java Cryptography Extension) should be installed: 
- [Download JCE for JDK 7](http://www.oracle.com/technetwork/java/embedded/embedded-se/downloads/jce-7-download-432124.html)
- [Download JCE for JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)

or use property that applies JCE workaround from here ([JCE Workaround](http://stackoverflow.com/a/20286961/328982))

```xml
    <jceWorkaround>true</jceWorkaround>
```
