keepass-maven-plugin
====================

Maven plugin to integrate KeePass into build process

[![Build Status](https://travis-ci.org/knowhowlab/keepass-maven-plugin.svg?branch=master)](https://travis-ci.org/knowhowlab/keepass-maven-plugin)

Features
========

- reads KeePass 2.x files
- sets properties from filtered KeePass entry
  
Installation
============

```xml
    <plugin>
        <groupId>org.knowhowlab.maven.plugins</groupId>
        <artifactId>keepass-maven-plugin</artifactId>
        <version>0.1</version>
    </plugin>
```

Example
=======

Read **Username**, **Password** and **URL** from **test.kdbx** file, group name **production**, entry title **HTTP Server**
and store in properties **http.username**, **http.password** and **http.url**

```xml
    <plugin>
        <groupId>org.knowhowlab.maven.plugins</groupId>
        <artifactId>keepass-maven-plugin</artifactId>
        <version>0.1</version>
        <configuration>
            <file>${project.basedir}/src/main/keepass/test.kdbx</file>
            <password>admin123</password>
        </configuration>
        <executions>
            <execution>
                <id>read-production</id>
                <phase>validate</phase>
                <goals>
                    <goal>read</goal>
                </goals>
                <configuration>
                    <records>
                        <record>
                            <prefix>http.</prefix>
                            <group>production</group>
                            <title>HTTP Server</title>
                        </record>
                    </records>
                </configuration>
            </execution>
        </executions>
    </plugin>
```
