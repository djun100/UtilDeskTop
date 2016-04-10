    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }


compile 'com.github.djun100:UtilDeskTop:a423c164b6f40d099d51dfc360d76ac8dbb81fff'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'