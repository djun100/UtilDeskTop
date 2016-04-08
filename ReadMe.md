    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }



compile 'com.github.djun100:UtilDeskTop:9884ace4e499af9fddedad34aecc0dfd41d89e65'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'