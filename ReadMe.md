    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }



compile 'com.github.djun100:UtilDeskTop:9c01f636be053a6fe234e02ac8b26c7d61033ac9'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'