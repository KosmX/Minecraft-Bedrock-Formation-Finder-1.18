# injars = your shadowed jar
-injars  build/libs/bedrockformation.jar
# outjars = the name of the new obfuscated/minified jar
-outjars build/libs/bedrockformation-min.jar

# important! you need to add this "rt" file from your JDK as libraryjars!
-libraryjars  <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)
# add here the jar to any compileOnly dependency you might have (or add "dontwarn" to make proguard ignore the missing classes)
# -libraryjars "PathTo\YourProject\SomeLocalLibraries\SomeLocalLibrary.jar"

-dontwarn com.google.**
-dontwarn javax.crypto.**
#-keepnames class kotlin.coroutines.** { *; }
-dontwarn **kotlinx.coroutines.**
-dontwarn **org.apache.commons.codec**

#-dontshrink
#-dontobfuscate
#-dontoptimize

# Keep your main class
 -keep public class dev.kosmx.bedrockfinder.Main {
    public static void main(java.lang.String[]);
  }
 -keep public class com.mike.Main {
    public static void main(java.lang.String[]);
  }

# Keep event handlers
#-keep,allowobfuscation,allowoptimization class * extends org.bukkit.event.Listener {
#    @org.bukkit.event.EventHandler <methods>;
#}

# Keep main package name
# -keeppackagenames "your.package"

# Keep public enum names
-keepclassmembers public enum ** {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep all ProtocolLib packet listeners (this was rough to get working, don't turn on optimization, it ALWAYS breaks the sensible ProtocolLib)

# Remove dependencies obsfuscation to remove bugs factor
#-keep,allowshrinking class yourpackage.dependencies.** { *; }

# If your goal is obfuscating and making things harder to read, repackage your classes with this rule
-optimizationpasses 5
-overloadaggressively
-allowaccessmodification
-mergeinterfacesaggressively
# You can parse any resource files that might contain a reference of your classes here (so they are updated according to the modifications made by Proguard)
-adaptresourcefilecontents **.yml,META-INF/MANIFEST.MF

# Some attributes that you'll need to keep (warning: removing *Annotation* might break some stuff)
-keepattributes Exceptions,Signature,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
#-keepattributes Exceptions,Signature,Deprecated,LineNumberTable,*Annotation*,EnclosingMethod
#-keepattributes LocalVariableTable,LocalVariableTypeTable,Exceptions,InnerClasses,Signature,Deprecated,LineNumberTable,*Annotation*,EnclosingMethod