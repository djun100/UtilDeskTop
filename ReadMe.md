    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }



compile 'com.github.djun100:UtilDeskTop:439c1f41909a6a2d2b882cbfee61e6e7aa0f41e1'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'