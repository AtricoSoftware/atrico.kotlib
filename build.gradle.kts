import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    id("org.jetbrains.dokka") version "0.10.0"
    id("maven-publish")
}

// Publication name
val publicationName = "$group.$name"
// Modules that have no example code
val modulesWithNoExamples = listOf("testing")

configure(allprojects) {
    repositories {
        jcenter()
    }
}

configure(subprojects) {
    group = "atrico.kotlib"

    repositories {
        mavenCentral()
        mavenLocal()
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.dokka")

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        testImplementation(project(":testing"))
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.5.2")
        testImplementation(group = "com.natpryce", name = "hamkrest", version = "1.7.0.0")
    }

    tasks {
        withType<KotlinCompile>().configureEach {
            kotlinOptions.jvmTarget = "1.8"
        }

        test {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }

    val sourcesJar by tasks.creating(Jar::class) {
        description = "Creates source code jar"
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    val dokka by tasks.getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }

    val dokkaJar by tasks.creating(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembles Kotlin docs with Dokka"
        archiveClassifier.set("javadoc")
        from(dokka)
    }

    publishing {
        publications {
            create<MavenPublication>(publicationName) {
                from(components["java"])
                artifact(sourcesJar)
                artifact(dokkaJar)
            }
        }

        repositories {
            maven {
                name = "build"
                url = uri("file://$buildDir/repository")
            }
        }
    }
}

// Add example sources unless module has none
configure(subprojects.filterNot { it.name in modulesWithNoExamples }) {
    sourceSets {
        create("examples") {
            kotlin
        }
    }

    dependencies {
        add("examplesImplementation", kotlin("stdlib-jdk8"))
        add("examplesImplementation", project(":${name}"))
        add("examplesImplementation", project(":core"))
    }

    val examplesJar by tasks.creating(Jar::class) {
        description = "Creates examples source code jar"
        archiveClassifier.set("examples")
        from(sourceSets["examples"].allSource)
    }

    (publishing.publications[publicationName] as MavenPublication).artifact(examplesJar)
}

// Add core module to all others except core
configure(subprojects.filter { it.name != "core" }) {
    dependencies {
        implementation(project(":core"))
    }
}