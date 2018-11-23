import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    kotlin("jvm")

    jacoco
    id("org.junit.platform.gradle.plugin")

    id("maven-publish")
    id("com.jfrog.bintray")
}

repositories {
    mavenCentral()
}

dependencies {
    val kotlinVersion = extra.get("kotlinVersion") as String
    implementation(kotlin("stdlib", kotlinVersion))

    // assertion
    testImplementation("org.amshove.kluent:kluent-android:${extra.get("kluentVersion") as String}")

    //spek2
    val spekVersion = extra.get("spekVersion") as String
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
}

jacoco {
    toolVersion = "0.8.1"

    val junitPlatformTest: JavaExec by tasks
    applyTo(junitPlatformTest)
}

task<JacocoReport>("codeCoverageReport") {
    group = "reporting"

    val junitPlatformTest: JavaExec by tasks

    reports {
        xml.isEnabled = true
        xml.destination = file("${project.buildDir}/reports/jacoco/codeCoverageReport/report.xml")
        html.isEnabled = true
    }

    val tree = fileTree("${project.buildDir}/classes")

    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories = files(mainSrc)
    classDirectories = files(tree)

    executionData = fileTree(project.buildDir) {
        include("jacoco/*.exec")
    }

    dependsOn(junitPlatformTest)
}

val artifactGroupId = extra.get("artifactGroupId") as String
val artifactPublishVersion = extra.get("artifactPublishVersion") as String

group = artifactGroupId
version = artifactPublishVersion

// publishing
val sourceSets = project.the<SourceSetContainer>()

val sourcesJar by tasks.registering(Jar::class) {
    from(sourceSets["main"].allSource)
    classifier = "sources"
}

val doc by tasks.creating(Javadoc::class) {
    isFailOnError = false
    source = sourceSets["main"].allJava
}
val javadocJar by tasks.creating(Jar::class) {
    dependsOn(doc)
    from(doc)

    classifier = "javadoc"
}

publishing {
    publications {
        register(project.name, MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar)
            groupId = artifactGroupId
            artifactId = project.name
            version = artifactPublishVersion
        }
    }
}

// bintray
configure<BintrayExtension> {
    user = findProperty("BINTRAY_USERNAME") as? String
    key = findProperty("BINTRAY_KEY") as? String
    setPublications(project.name)
    pkg.apply {
        repo = "maven"
        name = "remotdatak"
        desc = "Abstract Data Type (ADT) to represent data that is fetching from the remote sources"
        userOrg = "mercari-inc"
        websiteUrl = "https://github.com/mercari/RemoteData"
        vcsUrl = "https://github.com/mercari/RemoteData"
        setLicenses("MIT")
        version.apply {
            name = artifactPublishVersion
        }
    }
}
