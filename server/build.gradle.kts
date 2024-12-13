plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"

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
    // JDBC
//    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    // MySQL
//    runtimeOnly("com.mysql:mysql-connector-j")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
                minimum = "0.30".toBigDecimal()
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
                minimum = "0.30".toBigDecimal()
            }

            // 라인 커버리지를 최소한 30% 만족시켜야 한다.
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.30".toBigDecimal()
            }

            // 빈 줄을 제외한 코드의 라인수를 최대 2000라인으로 제한한다.
            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "2000".toBigDecimal()
            }

            // 커버리지 체크를 제외할 클래스들
            excludes = listOf(
//                    "*.test.*",
//                    "*.Kotlin*"
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