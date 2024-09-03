repositories {
  mavenCentral()
}

plugins {
  java
  // test-logger plugin displays test results in the terminal, and is not strictly necessary.
  id("com.adarshr.test-logger") version "3.1.0"
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

testlogger {
  showStandardStreams = true
}

dependencies {
  implementation("org.apache.commons:commons-lang3:3.12.0")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
  testImplementation(files("libs/CSGrader.jar"))
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

sourceSets {
  main {
    java {
      setSrcDirs(listOf("src/main"))
    }
  }
  test {
    java {
      setSrcDirs(listOf("src/test"))
    }
  }
}
