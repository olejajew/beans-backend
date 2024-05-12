import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("com.google.cloud.tools.jib") version "3.3.2"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven {
        name = "GitLab"
        url = uri("https://git.rrpay.online/api/v4/projects/4/packages/maven")
        credentials(HttpHeaderCredentials::class) {
            if (project.hasProperty("gitLabPrivateToken")) {
                name = "Private-Token"
                value = project.property("gitLabPrivateToken").toString()
            } else {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
}


dependencies {
    val exposedVersion = "0.40.1"

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

    //exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:42.3.1")
    implementation("org.xerial:sqlite-jdbc:3.30.1")

    //utils
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.slf4j:slf4j-api:1.7.25")

    //testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:2.1.214")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.named("bootJar", org.gradle.jvm.tasks.Jar::class) {
    archiveFileName.set("bean_logger.jar")
    destinationDirectory.set(project.rootDir)
}
