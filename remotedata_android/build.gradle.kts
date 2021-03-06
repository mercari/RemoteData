import com.android.build.gradle.internal.dsl.TestOptions

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")

    jacoco
    id("de.mannodermaus.android-junit5")
    id("maven-publish")
    id("com.jfrog.bintray")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin(Dependencies.kotlin, Version.kotlin))

    // assertion
    testImplementation(TestDependencies.kluent)

    // Junit5
    testImplementation(TestDependencies.junitJupiter)
    testRuntimeOnly(TestDependencies.junitEngine)

    // Robolectric
    testImplementation(TestDependencies.junit4)
    testImplementation(TestDependencies.roboletric)

    //spek2
    testImplementation(TestDependencies.spek)
    testRuntimeOnly(TestDependencies.spekRunner)
}

android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)

    defaultConfig {
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
    }

    testOptions {
        unitTests(delegateClosureOf<TestOptions.UnitTestOptions> {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        })
    }
}

jacoco {
    toolVersion = Version.jacoco
}

val artifactGroupId = Artifact.groupdId
val artifactPublishVersion = Artifact.version

afterEvaluate {
    publishing {
        publications {
            register(project.name, MavenPublication::class) {

                from(components["release"])

                groupId = artifactGroupId
                artifactId = project.name
                version = artifactPublishVersion
            }
        }
    }
}

bintray {
    user = findProperty("BINTRAY_USER") as? String
    key = findProperty("BINTRAY_KEY") as? String
    override = false
    publish = true
    setPublications(project.name)
    pkg.apply {
        repo = "maven"
        name = "remotedatak"
        desc = "Algebraic data type (ADT) to represent the state of data that is loading from/to remote sources/destinations"
        userOrg = "mercari-inc"
        websiteUrl = "https://github.com/mercari/RemoteDataK"
        vcsUrl = "https://github.com/mercari/RemoteDataK"
        setLicenses("MIT")
        version.apply {
            name = artifactPublishVersion
        }
    }
}
