    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }


compile 'com.github.djun100:UtilDeskTop:f4eddba8d3bf138ffa22104a562ede56f4a452f6'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'