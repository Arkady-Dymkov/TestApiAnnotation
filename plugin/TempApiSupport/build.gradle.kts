plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.dw.ar"
version = "1.1"


val spaceUsername: String by project
val spacePassword: String by project

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.jetbrains.team/maven/p/arkadiyprojects/temp-api-annotation")
        credentials {
            username = spaceUsername
            password = spacePassword
        }
    }
}

dependencies {
    implementation("com.dw.ar:TestApiAnnotation:1.0")
}

intellij {
    version.set("2024.3.1")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(
        "com.intellij.java",
        "org.jetbrains.kotlin"
    ))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
