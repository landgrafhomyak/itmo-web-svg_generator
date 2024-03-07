plugins {
    kotlin("multiplatform") version "1.9.0"
    `maven-publish`
}

group = "ru.landgrafhomyak.itmo.web"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm("backend")
    js("frontend"){
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "graph.js"
                outputPath = projectDir.resolve("test_svg")
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

    }
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    this.publication.artifactId = this.publication.artifactId.replace(Regex("""^${Regex.escape(project.name)}"""), "graph-meta")
}