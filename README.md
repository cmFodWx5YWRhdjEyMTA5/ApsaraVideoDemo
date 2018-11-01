# ApsaraVideoDemo
[![](https://jitpack.io/v/NikouKarter/ApsaraVideoDemo.svg)](https://jitpack.io/#NikouKarter/ApsaraVideoDemo)

aliyun player demo
Simple integrated aliVodPlayer SDK.
1. put an LivePusherView into xml layout file in your project, call push function with an available url to start pushing stream, 
   it's use on the anchor side.
2. put an LiveRoomView into xml layout file in your project, it's use on the audience side.
3. put an PlayerView into xml layout file in your project, it implements some simple player methods.

# Usage
Step 1 : Add it in your root build.gradle at the end of repositories:

~~~groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
~~~

Step 2 : Add the dependency.

~~~groovy
dependencies {
    implementation 'com.github.NikouKarter:ApsaraVideoDemo:Tag'
}
~~~
