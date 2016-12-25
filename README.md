# Chaining multiple sources with RxJava
Companion piece to blog post: https://medium.com/@murki/chaining-multiple-sources-with-rxjava-20eb6850e5d9

### Pre-requisites:
This code example uses the Flickr API which requires you to use your own API Key, which you can get here: https://www.flickr.com/services/api/misc.api_keys.html

Once you get the key, you can add it to your local `gradle.properties` file as *FLICKR_API_KEY="xxxxxxxxxxxxxxxxxx"* because it will be retrieved by gradle at build time via https://github.com/murki/chaining-rxjava/blob/master/app/build.gradle#L23


![Sequence Diagramn](/chaining-rxjava-2.png?raw=true)


![App Screenshot](/screenshot-2016-03-29_13.42.59.680.png?raw=true)
