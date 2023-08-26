plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testImplementation("org.junit.platform:junit-platform-suite:1+")
}
tasks.test {
    useJUnitPlatform()
    maxParallelForks = 8
    failFast = true
}
