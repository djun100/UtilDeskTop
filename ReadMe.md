    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }



compile 'com.github.djun100:UtilDeskTop:9910274217dcfec2dd7f01dab7efd76d421e299b'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'