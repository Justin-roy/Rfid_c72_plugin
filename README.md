# Rfid C72 SDK Plugin

Note:- This Plugin Support Android 11.

## Steps to follow
1. Run command to add package:
   ```javascript
   flutter pub add rfid_c72_plugin
   ```
3. Create android/libs folder
 -  Download both [build.gradle](https://github.com/Justin-roy/Rfid_c72_plugin/blob/main/example/android/libs/build.gradle) and [DeviceAPI_ver20220518_release.aar](https://github.com/Justin-roy/Rfid_c72_plugin/blob/main/example/android/libs/DeviceAPI_ver20220518_release.aar) and copy to android/libs
4. In android/setting.gradle add the following line to the top of the file:
   ```javascript
   include ':app',':libs' //libs is folder name
   ```
5. In android/app/build.gradle add a reference to the libs folder in the dependencies section. [Example](https://github.com/Justin-roy/Rfid_c72_plugin/blob/main/example/android/app/build.gradle)
   ```javascript
   dependencies {
   implementation project(":libs",)
   }
   //libs is folder name
   ```
   
6. In android/app/build.gradle set minSdkVersion 19 or higher
 - Ready to use :D 
    
- [Still confused? Refer to this example app link](https://github.com/Justin-roy/Rfid_c72_plugin/tree/main/example)
    

## Examples

```javascript
import 'package:flutter/material.dart';
import 'package:rfid_c72_plugin_example/rfid_scanner.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: RfidScanner(),
    );
  }
}
```

## Tested on Rfid C72 Device

<img width="300" src="https://firebasestorage.googleapis.com/v0/b/instagram-clone-cf306.appspot.com/o/github_ss%2Frfid_c72.jpg?alt=media&token=e1a8d8f0-a615-482f-805c-f474434a8792&_gl=1*8sr3gl*_ga*NTMyNDY1MDA5LjE2NTgyOTQxNDg.*_ga_CW55HF8NVT*MTY4NTYxNzk4My4yMS4xLjE2ODU2MTgwOTAuMC4wLjA."> 

## Authors

- [@Justin Roy](https://www.linkedin.com/in/justin-roy-4817551ba/)

## Badges

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](http://www.gnu.org/licenses/agpl-3.0)

## Support

For support, give a star ⭐ to repo.
