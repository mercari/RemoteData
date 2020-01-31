import org.gradle.kotlin.dsl.extra

object Artifact {
  const val groupdId = "com.mercari.remotedata"
  const val version = "1.0.0"
}

object MavenUrl {
  const val spekDev = "https://dl.bintray.com/spekframework/spek-dev"
}

object Version {
  const val bintray = "1.8.4"
  const val kotlin = "1.3.10"
  const val kluent = "1.49"
  const val spek = "2.0.1"
  const val junitPlatform = "1.2.0"
  const val jacoco = "0.8.3"
}

object Classpath {
  const val kotlin = "gradle-plugin"
  const val junitPlatform = "org.junit.platform:junit-platform-gradle-plugin:${Version.junitPlatform}"
  const val bintray = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Version.bintray}"
}

object Dependencies {
  const val kotlin = "stdlib"
}

object TestDependencies {
  const val kluent = "org.amshove.kluent:kluent-android:${Version.kluent}"
  const val spek = "org.spekframework.spek2:spek-dsl-jvm:${Version.spek}"
  const val spekRunner = "org.spekframework.spek2:spek-runner-junit5:${Version.spek}"
}