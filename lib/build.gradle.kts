plugins {
    kotlin("multiplatform") version "1.9.0"
    `maven-publish`
}

group = "ru.landgrafhomyak.itmo.web"
version = "2.2"

repositories {
    mavenCentral()
}

kotlin {
    jvm("backend") {
        jvmToolchain(8)
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js("frontend") {
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