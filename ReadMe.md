    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }



compile 'com.github.djun100:UtilDeskTop:9081e9773a472df6adc957367a9d55ba1f67a732'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'