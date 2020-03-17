# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontoptimize
-ignorewarnings

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class org.apache.http.**

-keep class com.google.android.gms.internal.** { *; }
-keepattributes **

-dontwarn com.mixpanel.android.mpmetrics.**
-dontwarn javax.annotation.**
-dontwarn com.google.common.**
-dontwarn org.apache.http.**
-dontwarn org.scribe.services.**
-dontwarn android.net.http.AndroidHttpClient

-dontnote org.apache.http.**
-dontnote android.net.http.**
-dontnote libcore.icu.ICU
-dontnote sun.misc.Unsafe
-dontnote com.google.vending.licensing.ILicensingService
-dontwarn android.security.**
-dontwarn com.google.android.gms.**
-dontwarn okio.**
-dontwarn com.mixpanel.**

#Retrofit related
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#End of retrofit related

-keepclassmembers class io.gupshup.smsapp.** { *; }
-keepclassmembers class io.gupshup.crypto.** { *; }
-keep class io.gupshup.crypto.** { *; }
-keep class io.gupshup.soip.** {*;}
-keepclassmembers class io.gupshup.soip.** {*;}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclassmembers public class org.apache.http.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**

-dontwarn org.mockito.**
-dontwarn sun.reflect.**
-dontwarn android.test.**

-dontwarn java.lang.management.**

# ez-vcard
-dontwarn ezvcard.io.json.**            # JSON serializer (for jCards) not used
-dontwarn freemarker.**                 # freemarker templating library (for creating hCards) not used
-dontwarn org.jsoup.**                  # jsoup library (for hCard parsing) not used
-dontwarn sun.misc.Perf
-keep,includedescriptorclasses class ezvcard.property.** { *; }  # keep all VCard properties (created at runtime)


-keep class com.gupshup.registration.** {*;}
-keepnames class com.gupshup.registration.enums.** {*;}
-keepnames enum com.gupshup.registration.enums.** {*;}
-keepnames interface com.gupshup.registration.enums.** {*;}
-keep class com.gupshup.registration.enums.** { *; }
-keep class com.google.gson.annotations.** {*;}
-keep class java.lang.annotation.** {*;}
-keepnames interface com.google.gson.annotations.** {*;}
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keepclassmembers class com.gupshup.registration.enums.** {
    <fields>;
}
-keep class com.address_package.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-dontwarn okhttp3.**



