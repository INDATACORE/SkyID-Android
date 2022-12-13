<p align="center">
  <img src="SkyIDDemo/src/main/res/drawable/readme/skyid_readme.png"/>
</p>

 Sky Identification is a KYC (Know Your Customer) solution based on artificial intelligence and machine learning to identify your prospects or your customers on digital channels.

 The Sky Identification SDK enables your app to ocerize identity-documents and verify users identities using biometrics based solutions. 
 
# <span style="color:#FF9C00">Table Of Content</span>

- [Prerequisite](#prerequisite)
- [Quick Start](#quick-start)
  - [Sky Document Analysis](#Sky-Document-Analysis)
  - [Sky Face Analysis](#Sky-Face-Analysis)
- [SkyID's Dependencies](#sdks-dependencies)
- [SkyID's Requirements](#sdks-requirements)
  - [SkyID's Required Permissions](#sdks-required-permissions)
  - [SkyID's Required Configurations](#sdks-required-configs)
- [Troubleshooting](#troubleshooting)
- [Help](#help)

## <span style="color:#FF9C00">Prerequisite</span>

SkyID needs a licence key to work. You can request a free trial licence key by contacting as at 'contact@indatacore.com'. 

Once you have the license key, follow the instructions below for a successful integration of SkyID inside your Android application.

## <span style="color:#FF9C00">Quick Start</span>  

The Sky Identification SDK is composed of two main components : 
- **Sky Document Analysis Component:** ocerize the identity-documents  and automatically extract personal information
- **Sky Face Recognition Component:**  verify the liveness and the identity of the person holding the identity-document. 

#### <span style="color:#FF9C00">Sky Document Analysis</span>   

The Sky Document Analysis component can be used simply by calling the DocumentAnalyzer activity throughout a StartActivityForResult() method, see the following code: 

```java
import com.indatacore.SkyAnalytics.SkyIdentification.DocumentAnalyzer;
String Token = “xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx”
Intent intent = new Intent(this, DocumentAnalyzer.class);
intent.putExtra("Token", Token);
intent.putExtra("Language", Language.ENGLISH.getISO());
intent.putExtra("Country", Country.MOROCCO.getISO());
intent.putExtra("requestCode", DocumentAnalyzer.RequestCode);
intent.putExtra("ServiceID", service_id); 
startActivityForResult(intent, DocumentAnalyzer.RequestCode);
```
        
The DocumentAnalyzer activity will open the camera, ocerize the document in real time and, finally, return the extracted data. To retrieve it, you may use the following code :

```java
String response;
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DocumentAnalyzer.RequestCode) {
            if(resultCode == DocumentAnalyzer.RESULT_OK){
                String StatusCode = data.getStringExtra("StatusCode");
                String StatusLabel = data.getStringExtra("StatusLabel");
                String RequestedInformations = data.getStringExtra("RequestedInformations");
                String RequestedFiles = data.getStringExtra("RequestedFiles");
            } else if (resultCode == DocumentAnalyzer.RESULT_Not_OK) {
                String StatusCode = data.getStringExtra("StatusCode");
                String StatusLabel = data.getStringExtra("StatusLabel");
            }
        }
    }
```

The form of the response data is as follows :
<pre>
StatusCode: “XXX”  
StatusLabel: “xxxxxxxxxxxxxxxxxxxxxxxxx”
RequestedInformations: {"cin_id":"xxx", "father_name":"xxx","mother_name":"xxx",
"address":"xxx","register_civil":"xxx","sexe":"X","first_name":"xxx","last_name":"xxx","birth_date":"xx.xx.xxxx","birth_place":"xxx","validity_date":"xx.xx.xxxx"}
RequestedFiles: {"DocumentFrontSideFile":"xxx", "DocumentBackSideFile":"xxx","FaceFile":"xxx"}
</pre>

#### <span style="color:#FF9C00">Sky Face Analysis</span>   

   Similarly to Sky Document Analysis, the Sky Face Recognition component can be used simply by calling the FacebasedAuthenticator activity throughout a StartActivityForResult() method, see the following code : 

```java
import com.indatacore.SkyAnalytics.SkyIdentification.FacebasedAuthenticator;
String Token = “xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx”;
String DocumentFile= “/path/path/xxxxxx_CINFront.jpg”;
Intent intent = new Intent(this, FacebasedAuthenticator.class);
intent.putExtra("Token", Token);
intent.putExtra("RequestCode", FacebasedAuthenticator.RequestCode);
intent.putExtra("DocumentFile", DocumentFile);
intent.putExtra("Language", Language.ENGLISH);
startActivityForResult(intent, FacebasedAuthenticator.RequestCode);

```

The FacebasedAuthenticator activity will open the camera, verify the liveness and the identity of the person holding the identity-document and return the verification result. In order to retrieve it, you may use the following code: 

```java

String response;
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FacebasedAuthenticator.RequestCode) {
            if(resultCode == FacebasedAuthenticator.RESULT_OK){
	     String StatusCode  = data.getStringExtra("StatusCode");
	     String StatusLabel  = data.getStringExtra("StatusLabel");
	     String SelfieFile = data.getStringExtra("SelfieFile");
	     String FaceAuthenticationResult  = data.getStringExtra("FaceAuthenticationResult");
            } else if (resultCode == FacebasedAuthenticator.RESULT_Not_OK) {
               String StatusCode  = data.getStringExtra("StatusCode");
               String StatusLabel  = data.getStringExtra("StatusLabel");
            }
        }
    }

```

The form of the response data is as follows:
<pre>
StatusCode: “XXX”  
StatusLabel: “xxxxxxxxxxxxxxxxxxxxxxxxx”
FaceAuthenticationResult: “1” or “0”
SelfieFile: “/path/path/SelfieFile.jpg”
</pre>

## <span style="color:#FF9C00">SkyID's Dependencies</span> 

In order to avoid any error that can arise from the conflict between the SDK dependencies and the ones of the APK, you should add the following dependencies to your app's build.gradle file:

<pre>
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
implementation 'androidx.appcompat:appcompat:1.1.0'
implementation('org.apache.httpcomponents:httpmime:4.3.6') { exclude module: 'httpclient' }
implementation 'org.apache.httpcomponents:httpclient-android:4.3.5'
implementation 'com.android.volley:volley:1.1.0'
</pre>

## <span style="color:#FF9C00">SkyID's Requirements</span> 

#### <span style="color:#FF9C00">SkyID's Required permissions</span> 

To ensure normal functioning of the SDK, make sure to ask the application users to grant the application the required permissions before calling the SDK activities. The three required permissions are :

<pre>
&lt;uses-permission android:name="android.permission.CAMERA"/&gt;
&lt;uses-permission android:name="android.permission.INTERNET" /&gt;
&lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/&gt;
&lt;uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /&gt;
</pre>

#### <span style="color:#FF9C00">SkyID's Required Configurations</span>

In order to minimize the size of the SDK, the current release supports only the "armeabi-v7a" CPU architecture which is the most widely supported ABI on Android (About 90+% of all phones made in the last years supports this). Hence, in order to enshure normal functioning of the SDK together with your application, you should mention to gradle to build and package your APK using only the "armeabi-v7a" architecture:

<pre>

android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a"
        }
    }
}

</pre>



## <span style="color:#FF9C00">Troubleshooting</span> 

#### Issues related to Google Play Services

If you have encountered undesirable behavior of the SDK, please proceed as follows:

 ###### Step 1: Clear cache & data from Google Play Services

 1. On your Android phone or tablet, open the Settings app <img src="https://lh3.googleusercontent.com/PzFeiQQaPASuntRuvWiXoqZjQqUj0s0q0w_jI4Nx9vL6x7rGmmS9f-xQr1Kj9S91WMlm=h18"/>.
 2. Tap **Apps & notifications** <img src="https://lh3.googleusercontent.com/QbWcYKta5vh_4-OgUeFmK-JOB0YgLLoGh69P478nE6mKdfpWQniiBabjF7FVoCVXI0g=h18"/> **See all apps**.
 3. Scroll down and tap **Google Play Services**.
 4. Tap **Storage** <img src="https://lh3.googleusercontent.com/QbWcYKta5vh_4-OgUeFmK-JOB0YgLLoGh69P478nE6mKdfpWQniiBabjF7FVoCVXI0g=h18"/> **Clear Cache**.
 5. Tap **Manage Space** <img src="https://lh3.googleusercontent.com/QbWcYKta5vh_4-OgUeFmK-JOB0YgLLoGh69P478nE6mKdfpWQniiBabjF7FVoCVXI0g=h18"/> **Clear all Data**.
 8. Wait for 5-10 minutes(depends on your internet connection), then try again.
 9. If the problem persists, please restart your device and try again.

