import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jmailen.kotlinter") version "3.3.0"
    application
}

group = "me.igors"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

apply(plugin = "org.jmailen.kotlinter")

dependencies {
    implementation("io.javalin:javalin:3.12.0")
    implementation("io.javalin:javalin-openapi:3.12.0")
    implementation("org.jetbrains.exposed:exposed-core:0.18.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.18.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.18.1")
    implementation("com.h2database:h2:1.4.200")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")
    implementation("org.koin:koin-core:2.2.2")
    implementation("org.slf4j:slf4j-log4j12:1.8.0-beta4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("io.swagger.core.v3:swagger-core:2.0.9")
    implementation("org.webjars:swagger-ui:3.24.3")

    implementation("org.jmailen.gradle:kotlinter-gradle:3.3.0")

    testImplementation("org.koin:koin-test:2.2.2")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}