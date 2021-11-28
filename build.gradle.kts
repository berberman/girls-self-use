plugins {
    kotlin("jvm") version "1.6.0"
    `maven-publish`
    signing
}

group = "cn.berberman"
version = "0.1.0"

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name
            from(components["java"])
            pom {
                name.set("Girls' Self Use")
                description.set("Some utilities")
                url.set("https://github.com/berberman/girls-self-use")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("berberman")
                        name.set("Potato Hatsue")
                        email.set("berberman@yandex.com")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/berberman/girls-self-use.git")
                    developerConnection.set("scm:git:ssh://github.com/berberman/girls-self-use.git")
                    url.set("https://github.com/berberman/girls-self-use")
                }

            }
        }
    }
    repositories {
        maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "Sonatype"
            credentials {
                username = properties.getOrDefault("sonatypeUsername", "")?.toString()
                password = properties.getOrDefault("sonatypePassword", "")?.toString()
            }
        }
    }
    signing {
        sign(publishing.publications["maven"])
    }
    tasks.javadoc {
        if (JavaVersion.current().isJava9Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }
}

