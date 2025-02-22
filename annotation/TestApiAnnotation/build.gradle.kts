plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
}

group = "com.dw.ar"
version = "1.0"

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

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = groupId
            artifactId = "TestApiAnnotation"
            version = version
        }
    }
    repositories {
        maven {
            url = uri("https://packages.jetbrains.team/maven/p/arkadiyprojects/temp-api-annotation")
            credentials {
                username = spaceUsername
                password = spacePassword
            }
        }
    }
}