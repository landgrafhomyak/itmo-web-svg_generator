plugins {
    kotlin("multiplatform") version "1.9.0"
}

group = "ru.landgrafhomyak.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js("browser") {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "main.js"
                outputPath = rootDir.resolve("out")
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val browserMain by getting {
            dependencies {
                implementation(project(":lib"))

            }
        }
        val browserTest by getting
    }
}