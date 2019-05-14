plugins {
  java
  jacoco
  application
}       

repositories {
	mavenCentral()
}

dependencies {
  testCompile("org.junit.jupiter:junit-jupiter-api:5.2.0")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
	testRuntime("org.junit.platform:junit-platform-console:1.2.0")
}
 
sourceSets {
  main {
    java.srcDirs("MineSweeper/src")
    resources.srcDirs("MineSweeper/src")   
  }
  test {
    java.srcDirs("MineSweeper/test")
  }
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform {}
}

tasks {
    getByName<JacocoReport>("jacocoTestReport") {
        afterEvaluate {
            setClassDirectories(files(classDirectories.files.map {
                fileTree(it) { exclude("**/ui/**") }
            }))
        }
    }
}

application {
  mainClassName = "game.ui.MineSweeperUI"
}
 
defaultTasks("clean", "test", "jacocoTestReport")
