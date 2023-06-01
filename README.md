# Rfid C72 SDK Plugin

Note:- This Plugin Have Android 11 Support.

## Steps to follow
1. flutter pub add Rfid_c72_plugin
2. create/add folder to android
 -  create android/libs folder
 -  download content from below link and copy in android/libs
    
    Note:- (build.gradle and DeviceAPI_ver20220518_release.arr add this files to android/libs) [Link](https://github.com/Justin-roy/Rfid_c72_plugin/tree/main/android/libs)

3. In setting.gradle add below lines.
 - include ':app',':libs' //libs is folder name
4. In build.gradle add below lines. // app level (android/app/build.gradle)
 - dependencies {
   implementation project(":libs",)
   }
   //libs is folder name
   
   see here -> [link](https://github.com/Justin-roy/Rfid_c72_plugin/blob/main/example/android/app/build.gradle)
   
5. minSdkVersion 19 or higher
 - Ready to use :D 
    
- [still confuse refer this link example app](https://github.com/Justin-roy/Rfid_c72_plugin/tree/main/example)
    

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
