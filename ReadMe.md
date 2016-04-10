    allprojects {
    repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    }
    }


compile 'com.github.djun100:UtilDeskTop:f3c6f5fd84d4bbeac04e0aa68dce71fd66a87d5d'

if use regex,compile

    providedCompile 'ru.lanwen.verbalregex:java-verbal-expressions:1.4'