import java.math.BigDecimal

plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.6"
    jacoco
}

group = "com.odanylchuk"
version = "0.0.1-SNAPSHOT"
description = "CosmoCatsMarketplace"

repositories {
    mavenCentral()
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.18.0")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // enable Spring Test AOT so AOT artifacts are generated for tests
    systemProperty("spring.test.aot.enabled", "true")
}

// Ensure AOT-related tasks are enabled for test processing
tasks.matching { it.name == "processTestAot" || it.name == "generateTestAot" }.configureEach {
    enabled = true
}

// JaCoCo configuration
jacoco {
    toolVersion = "0.8.13"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

// enforce minimum coverage threshold: 80%
val existingCoverageTask = tasks.findByName("jacocoTestCoverageVerification") as? org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
if (existingCoverageTask != null) {
    existingCoverageTask.violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                minimum = BigDecimal.valueOf(0.8)
            }
        }
    }
} else {
    tasks.register<org.gradle.testing.jacoco.tasks.JacocoCoverageVerification>("jacocoTestCoverageVerification") {
        dependsOn(tasks.test)
        violationRules {
            rule {
                limit {
                    counter = "INSTRUCTION"
                    // 80% minimum
                    minimum = BigDecimal.valueOf(0.8)
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(tasks.named("jacocoTestCoverageVerification"))
}
