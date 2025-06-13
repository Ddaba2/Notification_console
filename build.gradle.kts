plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("jakarta.mail:jakarta.mail-api:2.1.0")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.eclipse.angus:jakarta.mail:2.0.3")
}

tasks.test {
    useJUnitPlatform()
}