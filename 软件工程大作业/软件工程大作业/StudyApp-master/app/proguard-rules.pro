# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Herve\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class curriculumName to the JavaScript inter
# class:
#-keepclassmembers class fqcn.of.javascript.inter.for.webview {
#   public *;
#}
#
# ========== 保留所有的本地native方法不被混淆 ============
-keepclasseswithmembernames class * {
    native <methods>;
}
# ================================


# ========== 保留了继承自Activity、Application这些类的子类 ============
# ========== 因为这些子类，都有可能被外部调用 ============
# ========== 比如说，第一行就保证了所有Activity的子类不要被混淆 ============
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
# ================================

# ========== 如果有引用android-support-v4.jar包，可以添加下面这行 ============
-keep public class com.xxxx.app.ui.fragment.** {*;}
# ================================

# ========== 保留在Activity中的方法参数是view的方法 ============
# ========== 从而我们在layout里面编写onClick就不会被影响 ============

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
# ================================

# ========== 把混淆类中的方法名也混淆了 ============
-useuniqueclassmembernames
# ================================

# ========== 枚举类不能被混淆 ============
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
# ================================


# ========== 保留自定义控件（继承自View）不被混淆 ============
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# ================================

# ========== 保留Parcelable序列化的类不被混淆 ============
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
# ================================

# ========== 枚举类不能被混淆 ============
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
# ================================

 # ========== 保留自定义控件（继承自View）不被混淆 ============
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# ================================

# ========== 自定义控件不参与混淆 ============
-keep class com.jph.android.view.** { *; }
# ================================

# ========== 保留Serializable序列化的类不被混淆 ============
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# ================================

# ========== 对于R（资源）下的所有类及其方法，都不能被混淆 ============
-keep class **.R$* {
    *;
}
# ================================

# ========== 对于带有回调函数onXXEvent的，不能被混淆 ============
-keepclassmembers class * {
    void *(**On*Event);
}
# ================================

# ==============  实体类不参与混淆  ==================
-keep class com.example.herve.Study.bean.** { *; }
# ================================

# ========== 避免混淆泛型，这在JSON实体映射时非常重要，比如fastJson ============
-keepattributes Signature
# ================================

# ========== 抛出异常时保留代码行号，在异常分析中可以方便定位 ============
-keepattributes SourceFile,LineNumberTable
# ================================

# ========== 保留JS方法不被混淆 ============
-keepclassmembers class com.example.xxx.MainActivity$JSInterface1 {
    <methods>;
}
# ================================

# ========== greenDAO 3 ============
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**
# ================================

# ========== RxJava ============
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# ================================

# ========== retrofit2.X ============
-dontwarn retrofit.
-keep class retrofit. { *; }
-keepattributes Signature
-keepattributes Exceptions
# ================================

# ========== retrofit使用的实体类不能混淆 ============
-keep class com.life.me.entity.postentity{*;}
-keep class com.life.me.entity.resultentity{*;}
# ================================

# ========== fastjson ============
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** {*;}
# ================================

# ========== 对alipay的混淆处理 ============
-dontwarn com.alipay.android.app.**
-keep public class com.alipay.**  { *; }
# ================================