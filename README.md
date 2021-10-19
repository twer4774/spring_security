# Study Spring Security

- FastCampus Spring Security
- https://github.com/jongwon/sp-fastcampus-spring-sec

## gralde 초기 설정

```
#github에서 clonse을 받는다.
#터미널에서
gradle init
1: basic
1: Groovy
```

### settings.gradle

```groovy
rootProject.name = 'spring_security'

["comp", "web", "server"].each {

    def compDir = new File(rootDir, it)
    if(!compDir.exists()){
        compDir.mkdirs()
    }

    compDir.eachDir {subDir ->

        def gradleFile = new File(subDir.absolutePath, "build.gradle")
        if(!gradleFile.exists()){
            gradleFile.text =
                    """
                    
                    dependencies {
                
                    }
                
                    """.stripIndent(20)
        }
        [
                "src/main/java/com/walter/fastcampus",
                "src/main/resources",
                "src/test/java/walter/fastcampus",
                "src/test/resources"
        ].each {srcDir->
            def srcFolder = new File(subDir.absolutePath, srcDir)
            if(!srcFolder.exists()){
                srcFolder.mkdirs()
            }
        }

        def projectName = ":${it}-${subDir.name}";
        include projectName
        project(projectName).projectDir = subDir
    }
}
```

- gradle sync 실행 시 comp, web, server 폴더 생성(서브 프로젝트)
- 각 서브 프로젝트에 basic-test 같이 폴더를 하나 생성한 뒤 다시 gralde sync를 실행하면, main, test를 생성해준다.