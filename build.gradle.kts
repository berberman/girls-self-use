plugins {
    kotlin("jvm") version "1.3.61"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
}

group = "cn.berberman"
version = "0.1.0-dev"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}


val sources = tasks.register<Jar>("sourcesJar") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    description = "Creates sources jar"
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val fat = tasks.register<Jar>("fatJar") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    description = "Packs binary output with dependencies"
    archiveClassifier.set("all")
    from(sourceSets.main.get().output)
    from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) } })
}

tasks.register("allJars") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    description = "Assembles all jars in one task"
    dependsOn( sources, fat, tasks.jar)
}

val rename = tasks.register("renamePomFile") {
    dependsOn(tasks.publishToMavenLocal)
    doLast {
        val path = "${buildDir.absolutePath}/publications/maven/"
        val old = File(path + "pom-default.xml")
        val f = File("$path${project.name}-$version.pom")
        old.renameTo(f)
    }
}

tasks.bintrayUpload.configure {
    dependsOn(rename)
}

bintray {
    user = "berberman"
    key = System.getenv("BintrayToken")
    setConfigurations("archives")
    val v = version.toString()
    val url = "https://github.com/berberman/fp-utils"
    publish = true
    pkg.apply {
        name = project.name
        desc = "functional utilities"
        repo = "maven"
        userOrg = "berberman"
        githubRepo = "berberman/fp-utils"
        vcsUrl = "$url.git"
        issueTrackerUrl = "$url/issues"
        publicDownloadNumbers = true
        version.apply {
            name = v
            vcsTag = v
            websiteUrl = "$url/releases/tag/$v"
        }
    }
}

publishing {
    repositories {
        maven("$buildDir/repo")
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/berberman/fp-utils")
            credentials {
                username = "berberman"
                password = System.getenv("GitHubToken")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

artifacts {
    add("archives", tasks.jar)
    add("archives", fat)
    add("archives", sources)
    add("archives", File("${buildDir.absolutePath}/publications/maven/${project.name}-$version.pom"))
}
