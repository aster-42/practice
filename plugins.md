### maven

[maven plugins](http://maven.apache.org/plugins/index.html '')

编译
maven-compiler-plugin

例如使用Java11
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <source>11</source>
        <target>11</target>
        <parameters>true</parameters>
        <compilerArgs>
            --enable-preview
        </compilerArgs>
    </configuration>
</plugin>
```

插件打包
maven-assembly-plugin

```xml
<repositories>
	<repository>
		<id>central</id>
		<url>http://maven.aliyun.com/nexus/content/groups/public</url>
	</repository>
</repositories>
<pluginRepositories>
	<pluginRepository>
		<id>central</id>
		<url>http://maven.aliyun.com/nexus/content/groups/public</url>
	</pluginRepository>
</pluginRepositories>
```


### gradle


### idea

maven helper