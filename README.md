# Mobile Parallel Execution using Appium on OpenSTF


# What is OpenSTF?
The [OpenSTF](http://openstf.io/) is the open source mobile android **S**martphone **T**est **F**arm. I was searching for the device farm setup which I can setup without any hassle and came across this project. This project is written in **NodeJs** and uses **RethinkDB**, so easy to setup. Follow their [Github Page](https://github.com/openstf/stf) for the setup instructions.

# Pre-requisites
- [OpenSTF](http://openstf.io/) (> v2.0.0)
- [OpenSTF](http://openstf.io/) (> v2.0.0)

- [Appium](http://appium.io/) 
- [Gradle](https://gradle.org/) 

# Installation
Run this command in terminal:
```
git clone https://github.com/tatarynov/stf-appium-example.git
```

# Running test
1. Make sure your `stf` is running.
2. Generate the Access Token from your `stf` settings page and copy it to your clipboard. (Settings > Keys > + > Give some title > Generate New Token).
3. Open `/src/test/java/io/openstf/AndroidTest.java` and change the following:
    1. `STF_SERVICE_URL` to your actual STF URL.
    2. `ACCESS_TOKEN` to the copied access token from the step #2.
    3. Update `parallelDp` data provider method to the list of device serial ids connected to your machine.
4. From terminal, `cd` to the cloned directory and run `gradle clean test`.