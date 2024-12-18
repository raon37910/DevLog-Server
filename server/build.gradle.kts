plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"

    // Spring REST Docs 의 결과물을 OpenAPI 3 스펙으로 변환
    id("com.epages.restdocs-api-spec") version "0.17.1"
    // OpenAPI 3 Spec을 기반으로 SwaggerUI 생성(HTML, CSS, JS)
    id("org.hidetake.swagger.generator") version "2.18.2"

    id("checkstyle")
    jacoco
    id("org.sonarqube") version "6.0.1.5171"
}

group = "com.raon"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

checkstyle {
    toolVersion = "10.12.1"
    configFile = file("${project.rootDir}/checkstyle/naver-checkstyle-rules.xml")
    configProperties = mapOf("suppressionFile" to "${project.rootDir}/checkstyle/naver-checkstyle-suppressions.xml")
    // 규칙 위반 시 빌드 실패 처리
    isIgnoreFailures = false
}

sonar {
    properties {
        property("sonar.projectKey", "raon37910_DevLog-Server")
        property("sonar.organization", "raon37910")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

dependencies {
    // MVC
    implementation("org.springframework.boot:spring-boot-starter-web")
    // MyBatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
    // MySQL
    runtimeOnly("com.mysql:mysql-connector-j")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // API Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.17.1")
}

tasks.withType<Test> {
    // jacoco 추가시 JVM이 CDS 기능을 활성화 할 때 발생하는 에러 메세지 존재 with
    // -Xbootclasspath 옵션 제거하도록 옵션 추가
    // Xbootclasspath을 이용하면 부트스트랩 클래스 경로에서 로드해야 되는 클래스를 내가 지정한 위치에서 로드 하도록 함
    // Jacoco에서 런타임에 바이트코드 변경을 적용하고 에이전트의 동작을 반영하기 위해 추가한다고 함.
    jvmArgs = (jvmArgs ?: mutableListOf()).filterNot { it.contains("-Xbootclasspath") }
    useJUnitPlatform()
}

tasks.test {
    dependsOn("checkstyleMain", "checkstyleTest")
    mustRunAfter("checkstyleMain", "checkstyleTest")
    finalizedBy("jacocoTestReport")
}

openapi3 {
    this.setServer("https://localhost:8080")
    title = "Devlog"
    description = "Devlog API"
    version = "0.1.0"
    format = "yaml" // or json
}

tasks.register<Copy>("copyOasToSwagger") {
    delete("src/main/resources/static/swagger-ui/openapi3.yaml") // 기존 yaml 파일 삭제
    from("$buildDir/api-spec/openapi3.yaml") // 복제할 yaml 파일 타겟팅
    into("src/main/resources/static/swagger-ui/.") // 타겟 디렉토리로 파일 복제
    dependsOn("openapi3") // openapi3 task가 먼저 실행되도록 설정
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
    reports {
        // Jacoco 보고서 생성, 소나 큐브 연동용으로 xml, csv 생성
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(true)
    }

    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            // 'element'가 없으면 프로젝트의 전체 파일을 합친 값을 기준으로 한다.
            limit {
                // 'counter'를 지정하지 않으면 default는 'INSTRUCTION'
                // 'value'를 지정하지 않으면 default는 'COVEREDRATIO'
                minimum = "0.00".toBigDecimal()
            }
        }

        rule {
            // 룰을 간단히 켜고 끌 수 있다.
            enabled = true

            // 룰을 체크할 단위는 클래스 단위
            element = "CLASS"

            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }

            // 라인 커버리지를 최소한 30% 만족시켜야 한다.
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }

            // 빈 줄을 제외한 코드의 라인수를 최대 2000라인으로 제한한다.
            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "2000".toBigDecimal()
            }

            // 커버리지 측정 제외 클래스들
            excludes = listOf(
                "com.raon.devlog.config.*",
                "com.raon.devlog.mapper.*",
                "com.raon.devlog.support.*",
                "com.raon.devlog.controller.ApiControllerAdvice",
                "com.raon.devlog.ServerApplication"
            )
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(
        ":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification"
    )

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}