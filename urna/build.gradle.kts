plugins {
    id("java")
}

group = "com.lumi2021"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // javalin 7.0 tem uma API muito cursed
    implementation("io.javalin:javalin:6.7.0")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.16")

}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("run") {
    group = "application"
    description = "Executa o programa principal"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.lumi2021.urna.Program")
}