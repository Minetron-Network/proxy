import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.bundling.AbstractArchiveTask

plugins {
    java
    `java-library`
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.freefair.lombok") version "8.4"
}

group = "dev.waterdog.waterdogpe"
version = "2.0.4-SNAPSHOT"
description = "bamboo-proxy"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo.waterdog.dev/main")
}

dependencies {
    val nettyVersion = "4.2.9.Final"
    val log4jVersion = "2.25.3"
    val jlineVersion = "3.30.6"
    val protocolVersion = "3.0.0.Beta11-SNAPSHOT"

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("com.bugsnag:bugsnag:3.8.0")
    implementation("net.cubespace:Yamler-Core:2.4.1-SNAPSHOT")
    implementation("org.yaml:snakeyaml:1.32")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("org.apache.commons:commons-lang3:3.18.0")

    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("com.lmax:disruptor:3.4.4")

    implementation("org.jline:jline:$jlineVersion")
    implementation("org.jline:jline-terminal:$jlineVersion")
    implementation("org.jline:jline-terminal-jna:$jlineVersion")
    implementation("org.jline:jline-reader:$jlineVersion")
    implementation("net.minecrell:terminalconsoleappender:1.3.0")

    implementation("org.cloudburstmc.protocol:bedrock-codec:$protocolVersion")
    implementation("org.cloudburstmc.protocol:bedrock-connection:$protocolVersion")
    implementation("org.cloudburstmc.netty:netty-transport-raknet:1.0.0.CR3-SNAPSHOT")
    implementation("io.netty:netty-transport-native-kqueue:$nettyVersion")
    implementation("io.netty:netty-transport-native-epoll:$nettyVersion")
    implementation("com.nimbusds:nimbus-jose-jwt:9.37.4")
}

tasks.named<ShadowJar>("shadowJar") {
    manifest {
        attributes(
            "Main-Class" to "dev.waterdog.waterdogpe.WaterdogPE",
            "Implementation-Version" to project.version,
            "Implementation-Title" to "WaterdogPE",
            "Multi-Release" to "true"
        )
    }

    transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)

    exclude(
        "META-INF/*.SF",
        "META-INF/*.DSA",
        "META-INF/*.RSA",
        "META-INF/DEPENDENCIES",
        "META-INF/LICENSE*",
        "META-INF/NOTICE*",
        "META-INF/maven/**",
        "about.html"
    )

    mergeServiceFiles()

    archiveFileName.set("${project.description}.jar")

    isZip64 = true
}

tasks.withType<AbstractArchiveTask> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
}

tasks.build {
    dependsOn("shadowJar")
}
