    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }


compile 'com.github.djun100:UtilDeskTop:7047c9ecec39aaa621deb9a77c800400c6289aea'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'