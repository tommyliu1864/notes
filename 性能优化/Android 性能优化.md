# 内存优化
- 内存抖动
- 内存泄漏
- 内存溢出

## 学习步骤
1. 先对工具有一个大体的了解
2. 通过具体案例中，使用工具分析问题

## 为什么要分析应用的内存
- Android 提供了受管理的内存环境 - 当系统确定我们的应用不再使用某些对象时，垃圾回收器会将未使用的内存释放回堆中。
- 虽然 Android 查找未使用内存的方式在不断改进，但对于所有 Android 版本，系统都必须在某个时间点短暂地暂停我们的代码。
- 大多数情况下，这些暂停难以察觉。但是，如果我们的应用分配内存的速度比系统回收内存的速度快，当回收器要释放足够的内存以满足分配需要时，我们的应用可能会产生延迟。此延迟可能会导致您的应用跳帧，使其明显变慢。
- 即使我们的应用未表现出变慢，但如果存在内存泄漏，应用在转到后台运行时，仍可能保留相应内存。此行为会导致系统强制执行不必要的垃圾回收事件，因而拖慢系统其余部分的内存性能。最终，系统将被迫终止应用进程以回收内存。然后，当用户返回应用时，它必须完全重启。

## Android Profiler
- Android Profiler 工具可提供实时数据，帮助我们了解应用的 CPU、内存、网络和电池资源使用情况。
- Android Studio 3.0 及更高版本中的 Android Profiler 取代了 Android Monitor 工具。
- Android Profiler 与 Android 5.0（API 级别 21）及更高版本兼容。

### 会话
- Android Studio 会将相应数据作为单独的条目添加到当前会话。

  ![](.\pictures\内存优化\会话.png)

### 内存性能分析器
- 内存性能分析器是 Android Profiler 中的一个组件，可帮助我们识别可能会导致应用卡顿甚至崩溃的内存泄漏和内存抖动。它显示一个应用内存使用量的实时图表，让我们可以

    - 捕获堆转储
    - 强制执行垃圾回收
    - 跟踪内存分配

- 我们也可以从命令行使用 dumpsys 检查应用内存，还可以在 logcat 中查看 GC 事件
- https://developer.android.google.cn/studio/command-line/dumpsys

### 使用内存性能分析器
为帮助防止出现内存问题，应使用内存性能分析器执行以下操作：
- 在时间轴上查找可能会导致性能问题的不理想的内存分配模式。
- 转储 Java 堆以查看在任何给定时间有哪些对象在占用内存。在一个较长的时间段内进行多次堆转储有助于识别内存泄漏。
- 记录正常条件和极端条件下用户交互期间的内存分配情况，从而准确识别您的代码是否在短时间内分配了过多对象，或所分配的对象是否出现了泄漏。

### 内存性能分析器概览

![](.\pictures\内存优化\内存性能分析器概览.png)

如图所示，内存性能分析器的默认视图包括以下各项：
- 用于强制执行垃圾回收事件的按钮。
- 用于捕获堆转储的按钮。
- 用于指定性能分析器多久捕获一次内存分配的下拉菜单。选择适当的选项可帮助您在进行性能剖析时提高应用性能。
- 用于缩放时间轴的按钮。
- 用于跳转到实时内存数据的按钮。
- 事件时间轴，显示活动状态、用户输入事件和屏幕旋转事件。
- 内存使用量时间轴，它会显示以下内容：
    - 一个堆叠图表，显示每个内存类别当前使用多少内存，如左侧的 y 轴以及顶部的彩色键所示。
    - 一条虚线，表示分配的对象数，如右侧的 y 轴所示。
    - 每个垃圾回收事件的图标。

### 查看内存分配情况
内存分配情况图表为我们显示内存中每个 Java 对象和 JNI 引用的分配方式。具体而言，内存性能分析器可为我们显示有关对象分配情况的以下信息：
- 分配了哪些类型的对象以及它们使用多少空间。

- 每个分配的堆栈轨迹，包括在哪个线程中。

- 对象在何时被取消分配

  ![](.\pictures\内存优化\查看内存分配情况.png)

### 捕获堆转储
- 在计算机领域，dump一般译作转储，为什么要dump？因为程序在计算机中运行时，在内存、CPU、I/O等设备上的数据都是动态的，也就是说数据使用完或者发生异常就会丢掉。如果我想得到某些时刻的数据（有可能是调试程序Bug或者收集某些信息），就要把他转储（dump）为静态（如文件）的形式。
- 捕获堆转储后，可以查看以下信息：
    - 应用分配了哪些类型的对象，以及每种对象有多少。
    - 每个对象当前使用多少内存。
    - 在代码中的什么位置保持着对每个对象的引用。
    - 对象所分配到的调用堆栈。

![](.\pictures\内存优化\捕获堆转储.png)

- 在类列表中，可以查看以下信息：
    - Allocations：堆中的分配数。
    - Native Size：此对象类型使用的原生内存总量（以字节为单位）。只有在使用 Android 7.0 及更高版本时，才会看到此列。
    - 您会在此处看到采用 Java 分配的某些对象的内存，因为 Android 对某些框架类（如 Bitmap）使用原生内存。
    - Shallow Size：此对象类型使用的 Java 内存总量（以字节为单位）。
    - Retained Size：为此类的所有实例而保留的内存总大小（以字节为单位）。

**Shallow Size**

- Shallow Size是指实例自身占用的内存, 可以理解为保存该'数据结构'需要多少内存, 注意不包括它引用的其他实例。

**Retained Size**

- 实例A的Retained Size是指, 当实例A被回收时, 可以同时被回收的实例的Shallow Size之和。

  ![](.\pictures\内存优化\Retained Size.jpg)

### 将堆转储另存为 HPROF 文件
- 如果您要保存堆转储以供日后查看，请将其导出到 HPROF 文件。
- 如需使用其他 HPROF 分析器（如 MAT），您需要将 HPROF 文件从 Android 格式转换为 Java SE HPROF 格式。 您可以使用 android_sdk/platform-tools/ 目录中提供的 hprof-conv 工具执行此操作。
- hprof-conv heap-original.hprof heap-converted.hprof

### 内存性能分析器中的泄漏检测
- 在内存性能分析器中分析堆转储时，您可以过滤 Android Studio 认为可能表明应用中的 Activity 和 Fragment 实例存在内存泄漏的分析数据。

  ![](.\pictures\内存优化\内存性能分析器中的泄漏检测.png)

- 过滤器显示的数据类型包括：
    - 已销毁但仍被引用的 Activity 实例。
    - 没有有效的 FragmentManager 但仍被引用的 Fragment 实例。

- 在某些情况（如以下情况）下，过滤器可能会产生误报：
    - 已创建 Fragment，但尚未使用它。
    - 正在缓存 Fragment，但它不是 FragmentTransaction 的一部分。


## MAT（Memory Analyzer Tool）

### Histogram
这个功能主要是查看类和对象关系，对象和对象之间的关系。

![](.\pictures\内存优化\MAT-histogram.png)

选择一个对象，右键选择Path to GC Roots，通常在排查内存泄漏的时候，我们会选择exclude all phantom/weak/soft etc.references,
意思是查看排除虚引用/弱引用/软引用等的引用链，因为被虚引用/弱引用/软引用的对象可以直接被GC给回收，我们要看的就是某个对象否还存在强 引用链（在导出HeapDump之前要手动出发GC来保证），如果有，则说明存在内存泄漏，然后再去排查具体引用。

![](.\pictures\内存优化\MAT-histogram2.png)

### 内存快照对比
为了更有效率的找出内存泄露的对象，一般会获取两个堆转储文件（先dump一个，隔段时间再dump一个），通过对比后的结果可以很方便定位。

![](.\pictures\内存优化\MAT-快照对比.png)

## 内存抖动
- 内存抖动是由于短时间内有大量对象进出新生区导致的，内存忽高忽低，有短时间内快速上升和下落的趋势，内存呈锯齿状。
- 它伴随着频繁的GC，GC 会大量占用 UI 线程和CPU 资源，会导致APP 整体卡顿，甚至有 OOM 的可能。

![](.\pictures\内存优化\内存抖动.png)

## 内存泄漏
- 程序中己动态分配的堆内存由于某种原因程序未释放或无法释放，造成系统内存的浪费。
- 不少人认为JAVA程序，因为有垃圾回收机制，应该没有内存泄露。我们已经知道了，如果某个对象，从根节点可到达，也就是存在从根节点到该对象的引用链，那么该对象是不会被 GC 回收的。如果说这个对象已经不会再被使用到了，是无用的，我们依然持有他的引用的话，就会造成内存泄漏，例如 一个长期在后台运行的线程持有 Activity 的引用，这个时 候 Activity 执行了 onDestroy 方法，那么这个 Activity 就是从根节点可到达并且无用的对象， 这个 Activity 对象就是泄漏的对象，给这个对象分配的内存将无法被回收。如果我们的 JAVA 运行很久，而这种内存泄露不断的发生，最后就没内存可用了。
- 当然 JAVA 的内存泄漏和 C/C++ 是不一样的，如果 JAVA 程序完全结束后，它所有的对象就都不可达了，系统就可以对他们进行垃圾回收，它的内存泄露仅仅限于它本身，而不会影响整个系统的。C/C++ 的内存泄露就比较糟糕了，它的内存泄露是系统级，即使该 C/C++ 程序退出，它的泄露的内存也无法被系统回收，永远不可用了，除非重启机器。
- Android的一个应用程序的内存泄露对别的应用程序影响不大。为了能够使得Android应用程序安全且快速的运行，Android的每个应用程序都会使用一个专有的Dalvik虚拟机实例来运行，它是由Zygote服务进程孵化出来的，也就是说每个应用程序都是在属于自己的进程中运行的。Android为不同类型的进程分配了不同的内存使用上限，如果程序在运行过程中出现了内存泄漏的而造成应用进程使用的内存超过了这个上限，则会被系统视为内存泄漏，从而被kill掉，这使得仅仅自己的进程被kill掉，而不会影响其他进程（如果是system_process等系统进程出问题的话，则会引起系统重启）。

### 常见内存泄漏场景
#### 单例导致内存泄露
单例模式在Android开发中会经常用到，但是如果使用不当就会导致内存泄露。因为单例的静态特性使得它的生命周期同应用的生命周期一样长，如果一个对象已经没有用处了，但是单例还持有它的引用，那么在整个应用程序的生命周期它都不能正常被回收，从而导致内存泄露。

```java
public class AppSettings {

    private static volatile AppSettings singleton;
    private Context mContext;
    private AppSettings(Context context) {
        this.mContext = context;
    }

    public static AppSettings getInstance(Context context) {
        if (singleton == null) {
            synchronized (AppSettings.class) {
                if (singleton == null) {
                    singleton = new AppSettings(context);
                }
            }
        }
        return singleton;
    }
}
```

像上面代码中这样的单例，如果我们在调用getInstance(Context context)方法的时候传入的context参数是Activity、Service等上下文，就会导致内存泄露。 以Activity为例，当我们启动一个Activity，并调用getInstance(Context context)方法去获取AppSettings的单例，传入Activity.this作为context，这样AppSettings类的单例sInstance就持有了Activity的引用，当我们退出Activity时，该Activity就没有用了，但是因为sIntance作为静态单例（在应用程序的整个生命周期中存在）会继续持有这个Activity的引用，导致这个Activity对象无法被回收释放，这就造成了内存泄露。

为了避免这样单例导致内存泄露，我们可以将context参数改为全局的上下文：
```java
private AppSettings(Context context) {
        this.mContext = context.getApplicationContext();
}
```

#### 静态变量导致内存泄漏
静态变量存储在方法区，它的生命周期从类加载开始，到整个进程结束。一旦静态变量初始化后，它所持有的引用只有等到进程结束才会释放。 比如下面这样的情况，在Activity中为了避免重复的创建info，将sInfo作为静态变量：

```java
public class MainActivity2 extends AppCompatActivity {

    public static Info sInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInfo = new Info(this);
    }

    class Info {

        private Context mContext;

        public Info(Context context) {
            this.mContext = context;
        }
    }
}
```

Info作为Activity的静态成员，并且持有Activity的引用，但是sInfo作为静态变量，生命周期肯定比Activity长。所以当Activity退出后，sInfo仍然引用了Activity，Activity不能被回收，这就导致了内存泄露。

在Android开发中，静态持有很多时候都有可能因为其使用的生命周期不一致而导致内存泄露，所以我们在新建静态持有的变量的时候需要多考虑一下各个成员之间的引用关系，并且尽量少地使用静态持有的变量，以避免发生内存泄露。当然，我们也可以在适当的时候讲静态量重置为null，使其不再持有引用，这样也可以避免内存泄露。

#### 非静态内部类导致内存泄露
非静态内部类（包括匿名内部类）默认就会持有外部类的引用，当非静态内部类对象的生命周期比外部类对象的生命周期长时，就会导致内存泄露。非静态内部类导致的内存泄露在Android开发中有一种典型的场景就是使用Handler，很多开发者在使用Handler是这样写的：

```java
public class MainActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();
    }

    private void start() {
        Message message = Message.obtain();
        message.what = 1;
        mHandler.sendMessage(message);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //doNothing
            }
        }
    };
}

```
也许有人会说，mHandler并未作为静态变量持有Activity引用，生命周期可能不会比Activity长，应该不一定会导致内存泄露呢，显然不是这样的！ 熟悉Handler消息机制的都知道，mHandler会作为成员变量保存在发送的消息msg中，即msg持有mHandler的引用，而mHandler是Activity的非静态内部类实例，即mHandler持有Activity的引用，那么我们就可以理解为msg间接持有Activity的引用。msg被发送后先放到消息队列MessageQueue中，然后等待Looper的轮询处理（MessageQueue和Looper都是与线程相关联的，MessageQueue是Looper引用的成员变量，而Looper是保存在ThreadLocal中的）。那么当Activity退出后，msg可能仍然存在于消息对列MessageQueue中未处理或者正在处理，那么这样就会导致Activity无法被回收，以致发生Activity的内存泄露。

通常在Android开发中如果要使用内部类，但又要规避内存泄露，一般都会采用静态内部类+弱引用的方式。

```java
MyHandler mHandler;

public static class MyHandler extends Handler {

        private WeakReference<Activity> mActivityWeakReference;

        public MyHandler(Activity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
}
```

mHandler通过弱引用的方式持有Activity，当GC执行垃圾回收时，遇到Activity就会回收并释放所占据的内存单元。这样就不会发生内存泄露了。 上面的做法确实避免了Activity导致的内存泄露，发送的msg不再已经没有持有Activity的引用了，但是msg还是有可能存在消息队列MessageQueue中，所以更好的是在Activity销毁时就将mHandler的回调和发送的消息给移除掉。

```java
 @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
 }
```

非静态内部类造成内存泄露还有一种情况就是使用Thread或者AsyncTask。要避免内存泄露的话还是需要像上面Handler一样使用静态内部类+弱应用的方式（代码就不列了，参考上面Hanlder的正确写法）。

#### 未取消注册或回调导致内存泄露
比如我们在Activity中注册广播，如果在Activity销毁后不取消注册，那么这个刚播会一直存在系统中，同上面所说的非静态内部类一样持有Activity引用，导致内存泄露。因此注册广播后在Activity销毁后一定要取消注册。 在注册观察则模式的时候，如果不及时取消也会造成内存泄露。比如使用Retrofit+RxJava注册网络请求的观察者回调，同样作为匿名内部类持有外部引用，所以需要记得在不用或者销毁的时候取消注册。

#### Timer和TimerTask导致内存泄露
Timer和TimerTask在Android中通常会被用来做一些计时或循环任务，比如实现无限轮播的ViewPager：

```java
private void stopTimer(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if(mTimerTask!=null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
```

当我们Activity销毁的时，有可能Timer还在继续等待执行TimerTask，它持有Activity的引用不能被回收，因此当我们Activity销毁的时候要立即cancel掉Timer和TimerTask，以避免发生内存泄漏。

#### 集合中的对象未清理造成内存泄露
这个比较好理解，如果一个对象放入到ArrayList、HashMap等集合中，这个集合就会持有该对象的引用。当我们不再需要这个对象时，也并没有将它从集合中移除，这样只要集合还在使用（而此对象已经无用了），这个对象就造成了内存泄露。并且如果集合被静态引用的话，集合里面那些没有用的对象更会造成内存泄露了。所以在使用集合时要及时将不用的对象从集合remove，或者clear集合，以避免内存泄漏。

#### 资源未关闭或释放导致内存泄露
在使用IO、File流或者Sqlite、Cursor等资源时要及时关闭。这些资源在进行读写操作时通常都使用了缓冲，如果不及时关闭，这些缓冲对象就会一直被占用而得不到释放，以致发生内存泄露。因此我们在不需要使用它们的时候就及时关闭，以便缓冲能及时得到释放，从而避免内存泄露。

#### 属性动画造成内存泄露
动画同样是一个耗时任务，比如在Activity中启动了属性动画（ObjectAnimator），但是在销毁的时候，没有调用cancle方法，虽然我们看不到动画了，但是这个动画依然会不断地播放下去，动画引用所在的控件，所在的控件引用Activity，这就造成Activity无法正常释放。因此同样要在Activity销毁的时候cancel掉属性动画，避免发生内存泄漏。

#### WebView造成内存泄露
关于WebView的内存泄露，因为WebView在加载网页后会长期占用内存而不能被释放，因此我们在Activity销毁后要调用它的destory()方法来销毁它以释放内存。另外在查阅WebView内存泄露相关资料时看到这种情况： Webview下面的Callback持有Activity引用，造成Webview内存无法释放，即使是调用了Webview.destory()等方法都无法解决问题（Android5.1之后）。 最终的解决方案是：在销毁WebView之前需要先将WebView从父容器中移除，然后再销毁WebView。


### LeakCanary 内存泄漏检测
- LeakCanary 是 Square 公司的一个开源库。通过它可以在 App 运行过程中检测内存泄漏，当内存泄漏发生时会生成发生泄漏对象的引用链，并通知程序开发人员。
- 17世纪，英国矿井工人发现，金丝雀对瓦斯这种气体十分敏感。空气中哪怕有极其微量的瓦斯，金丝雀也会停止歌唱；而当瓦斯含量超过一定限度时，虽然鲁钝的人类毫无察觉，金丝雀却早已毒发身亡。当时在采矿设备相对简陋的条件下，工人们每次下井都会带上一只金丝雀作为“瓦斯检测指标”，以便在危险状况下紧急撤离。
- LeakCanary 就是能敏感发现内存泄漏的金丝雀，来帮助我们尽量避免 OOM 的危险，这一理念也在它的 Logo 设计中体现。

<img src=".\pictures\内存优化\LeakCanary logo.jpg" style="zoom: 50%;" />

#### 初始化
引入 LeakCanary 只需要在 app 的 build.gradle 文件增加以下代码
```gradle
dependencies {
  // debugImplementation because LeakCanary should only run in debug builds.
  debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.7'
}
```

#### 运行步骤
- 检测保留的对象
- 生成堆转储文件
- 分析堆转储文件
- 对泄漏进行分类

#### 核心原理
LeakCanary 通过 hook Android的生命周期来自动检测Activity和Fragment何时被销毁，何时应该被垃圾回收，这些被destroy的对象被传递给ObjectWatcher，ObjectWatcher持有对它们的弱引用。

https://square.github.io/leakcanary/fundamentals-how-leakcanary-works/

#### 检测对象类型
LeakCanary自动检测以下对象的泄漏：

- 已销毁的 Activity 实例
- 销毁的 Fragment 实例
- 销毁的 Fragment View 实例
- 已清除的 ViewModel 实例

#### ReferenceQueue 与 WeakReference
弱引用（WeakReference）可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收器回收，虚拟机就会把这个弱引用加入到与之关联的引用队列中，我们可以用此特性来检查一个对象是否被垃圾回收器回收成功。

```kotlin
/**
 * WeakReference和ReferenceQueue联合使用案例
 * 也是用来监控某个对象是否被gc回收的手段
 */
fun main() {
    val referenceQueue = ReferenceQueue<Any?>()
    var obj: Any? = Object()
    // 软引用引用obj，并和一个引用队列关联
    // 当obj被GC回收后，引用它的弱引用会被添加到与之关联的引用队列
    val weakReference = WeakReference(obj, referenceQueue);
    // 把obj置空，方便GC回收对象
    obj = null
    // 触发 GC 回收
    System.gc()
    thread {
        sleep(1000)
    }
    var ref: Any? = null
    do {
        //如果能从引用队列中取出弱引用，说明obj对象被回收了
        ref = referenceQueue.poll()
        println("ref: $ref , 地址比较: ${weakReference === ref}")
    } while (ref != null) //把引用队列中的所有弱引用取出来
}
```

![](.\pictures\内存优化\ReferenceQueue.png)


#### LeakCanary 核心流程

![](.\pictures\内存优化\LeakCanary流程.png)


![](.\pictures\内存优化\LeakCanary 监控列表与保留列表.png)

```kotlin
/**
 * 自定义一个弱引用，并加入一个key属性，作为该弱引用的一个标示，方便从Map容器中取出弱引用
 */
class KeyWeakReference<T> : WeakReference<T> {
    var key: String

    constructor(referent: T, key: String) : super(referent) {
        this.key = key
    }

    constructor(referent: T, q: ReferenceQueue<in T>?, key: String) : super(
        referent,
        q
    ) {
        this.key = key
    }

    override fun toString(): String {
        return "KeyWeakReference(key='$key')"
    }
}
```

```kotlin
class Watcher {
    //监控列表
    private val watchedReferences = HashMap<String, KeyWeakReference<Any?>>()

    //保留列表，保留列表中出现的引用，说明它是泄漏的对象
    private val retainedReferences = HashMap<String, KeyWeakReference<Any?>>()

    //当被监控的对象被GC回收后，对应的弱引用会被加入到引用队列
    private val queue = ReferenceQueue<Any?>()

    fun watch(obj: Any?) {
        // 生成一个UUID key，便于从列表中取出相应的引用
        val key = UUID.randomUUID().toString()

        // 建立弱引用关系，并关联引用队列
        val reference: KeyWeakReference<Any?> = KeyWeakReference(obj, queue, key)

        // 加入到监控列表做登记
        watchedReferences[key] = reference

        // 过2秒后去看是否还在监控列表，如果还在，则加入到保留列表
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            Thread.sleep(1000)
            moveToRetained(key)
        }
    }

    private fun moveToRetained(key: String) {
        var ref: KeyWeakReference<Any?>? = null
        do {
            // 从引用队列中取出来发现不为空，说明对象被GC回收了，那么不需要加入到监控列表和保留列表中
            queue.poll()?.also { ref = it as KeyWeakReference<Any?> }
            // -----A-------
            // 回收成功，没有发生内存泄漏的情况
            ref?.key.let {
                watchedReferences.remove(it)
                retainedReferences.remove(it)
            }
            ref = null
        } while (ref != null)

        // 如果仍然存在于监控列表中，说明 A 处代码没有将对应key的弱引用从监控列表中移除
        // 将弱引用转入保留列表中
        // 回收不成功，发生了内存泄漏的情况
        watchedReferences.remove(key)?.also {
            retainedReferences[key] = it
        }
    }

    fun getRetainedReferences(): HashMap<String, KeyWeakReference<Any?>> {
        return retainedReferences
    }

}
```

```kotlin
fun main() {
    val watcher = Watcher()
    var obj:Any? = Object()
    watcher.watch(obj)

//    obj = null
    System.gc()
    Thread.sleep(2000)

    watcher.getRetainedReferences().forEach { (key, reference) ->
        println(
            "key:$key, keyWeakReference: $reference, obj: ${reference.get()}"
        )
    }
}
```

### LeakCanary 核心源码分析

<img src=".\pictures\内存优化\LeakCanary工程目录.png" style="zoom: 50%;" />

上图便是 leakCanary 工程目录，而初始化代码位于 leakcanary-android-process 其清单文件如下：

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.squareup.leakcanary.objectwatcher" >

    <uses-sdk android:minSdkVersion="14" />

    <application>
    	<!--很巧妙的利用利用provider进行初始化-->
        <provider
            android:name="leakcanary.internal.AppWatcherInstaller$MainProcess"
            android:authorities="${applicationId}.leakcanary-installer"
            android:enabled="@bool/leak_canary_watcher_auto_install"
            android:exported="false" />
    </application>

</manifest>
```

AMS 初始一个 app 的时候先调用 Provider.onCreate  再调用 Application.onCreate。

```kotlin
//AppWatcherInstaller.kt
internal sealed class AppWatcherInstaller : ContentProvider() {
  //内部类巧妙的运用,可以瞬间构造两个不同的provider,可以拿来切换或者测试等	
  internal class MainProcess : AppWatcherInstaller()

  internal class LeakCanaryProcess : AppWatcherInstaller()

  override fun onCreate(): Boolean {
    val application = context!!.applicationContext as Application
    AppWatcher.manualInstall(application)
    return true
  }
```



```kotlin
//ObjectWatcher.kt
class ObjectWatcher constructor(
  private val clock: Clock,
  //注意下这个参数,Executor很明显就是jdk类,用于封装线程调度
  private val checkRetainedExecutor: Executor,
  private val isEnabled: () -> Boolean = { true }
)

//AppWatcher.kt
object AppWatcher {

//ObjectWatcher对象具体后面讲.我们首先观察一个属性checkRetainedExecutor
val objectWatcher = ObjectWatcher(
   clock = { SystemClock.uptimeMillis() },
   checkRetainedExecutor = {
    //internal val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    //这里拿的android主线的handler
    //为什么这里拿主线程是有一定原因的后文再讲
     mainHandler.postDelayed(it, retainedDelayMillis)
   },
   isEnabled = { true }
 )
 

   fun manualInstall(
    application: Application,
    retainedDelayMillis: Long = TimeUnit.SECONDS.toMillis(5),
    watchersToInstall: List<InstallableWatcher> = appDefaultWatchers(application)
  ) {
    //...略
    //注册一些监听这里先不深究,讲到时在做分析
    LeakCanaryDelegate.loadLeakCanary(application)

    //我们在看下这个集合
    watchersToInstall.forEach {
      it.install()
    }
  }
  
   fun appDefaultWatchers(
    application: Application,
    reachabilityWatcher: ReachabilityWatcher = objectWatcher
  ): List<InstallableWatcher> {
  	//每个集合对象表示可以监控内存泄露的类
    return listOf(
      //监控Activity	
      ActivityWatcher(application, reachabilityWatcher),
      //fragment和viewmodel
      FragmentAndViewModelWatcher(application, reachabilityWatcher),
      //监控view
      RootViewWatcher(reachabilityWatcher),
      //监控service
      ServiceWatcher(reachabilityWatcher)
    )
  }	
}  
```

上面将四个集合对象调用 install 便完成了初始化.

#### ActivityWatcher

```kotlin
//ActivityWatcher.kt
class ActivityWatcher(
  private val application: Application,
  private val reachabilityWatcher: ReachabilityWatcher
) : InstallableWatcher {

  private val lifecycleCallbacks =
    object : Application.ActivityLifecycleCallbacks by noOpDelegate() {
      override fun onActivityDestroyed(activity: Activity) {
    	//利用Application对象注册Activity的destory回调.
    	//reachabilityWatcher指向AppWatcher.objectWatcher
        reachabilityWatcher.expectWeaklyReachable(
          activity, "${activity::class.java.name} received Activity#onDestroy() callback"
        )
      }
    }
  //初始化代码会调用
  override fun install() {
    application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
  }

  override fun uninstall() {
    application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
  }
}
```



Java 版监听 Activity 生命周期的回调

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initActivityLifecycleCallbacks();
    }

    /**
     * 在application里监听所有activity生命周期的回调
     */
    private void initActivityLifecycleCallbacks(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() { //添加监听
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //activity创建生命周期
                if(activity instanceof MainActivity){ //判断创建的activity对应对象
                   
                }

            }

            @Override
            public void onActivityStarted(Activity activity) {
                //activity启动生命周期

            }

            @Override
            public void onActivityResumed(Activity activity) {
                //activity恢复生命周期

            }

            @Override
            public void onActivityPaused(Activity activity) {
                //activity暂停生命周期

            }

            @Override
            public void onActivityStopped(Activity activity) {
                //activity停止生命周期

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                //保存activity实例状态

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //activity销毁生命周期

            }
        });
    }
}
```





```kotlin
class ObjectWatcher constructor(
 private val clock: Clock,
  private val checkRetainedExecutor: Executor,//指向main线程哦
  private val isEnabled: () -> Boolean = { true }){

  //放入要检测泄露的对象,和queue配合检测泄露,如果queue.pool返回对象时需要删除watchedObjects引用
  private val watchedObjects = mutableMapOf<String, KeyedWeakReference>()
  //queue存储要检测的休闲,和watchedObjects合作检测泄露
  private val queue = ReferenceQueue<Any>()

  @Synchronized override fun expectWeaklyReachable(
    watchedObject: Any,
    description: String
  ) {
  
    if (!isEnabled()) {
      return
    }
    
    //循环queue.pool函数直到返回null,期间同时清理能返回数据的watchedObjects对象
    removeWeaklyReachableObjects()

    val key = UUID.randomUUID()
      .toString()
    val watchUptimeMillis = clock.uptimeMillis()
	//将当前Activity放入引用对象中,注意这里传入了queue
    val reference =
      KeyedWeakReference(watchedObject, key, description, watchUptimeMillis, queue)
    
	//放入一个数组中
    watchedObjects[key] = reference
    //checkRetainedExecutor是主线程哦.execute会将任务丢到队列中,那么会延迟执行,
    //为什么checkRetainedExecutor要是主线程?因为Activity调用destroy不会马上回收,
    //android会在destroy返回后会做一些清理在回收
    checkRetainedExecutor.execute {
    //往下分析
    moveToRetained(key)
    }
  }
  private fun removeWeaklyReachableObjects() {
    //比较简单,queue.pool返回了对象,那么证明对象被回收,watchedObjects也就没必要保存监控对象
    var ref: KeyedWeakReference?
    do {
      ref = queue.poll() as KeyedWeakReference?
      if (ref != null) {
        watchedObjects.remove(ref.key)
      }
    } while (ref != null)
  }	
}
```

上文会将一个事件丢入main线程然后等候下次调度`moveToRetained`,这时基本Activity已经被回收

```kotlin
   private fun moveToRetained(key: String) {
  	//移除被回收对象
    removeWeaklyReachableObjects()
    
    val retainedRef = watchedObjects[key]
    //如果removeWeaklyReachableObjects没有移除retainedRef 那么证明还没有回收
    if (retainedRef != null) {
      retainedRef.retainedUptimeMillis = clock.uptimeMillis()
      //onObjectRetainedListeners在LeakCanaryDelegate.loadLeakCanary(application)被设置
      onObjectRetainedListeners.forEach { it.onObjectRetained() }
    }
  }	
```



```kotlin
//InternalLeakCanary.kt
//这里继承两个接口:
//				(Application) -> Unit 
//				OnObjectRetainedListener
internal object InternalLeakCanary : (Application) -> Unit, OnObjectRetainedListener {
  //运行到这
  override fun onObjectRetained() = scheduleRetainedObjectCheck()

  fun scheduleRetainedObjectCheck() {
  	//视为true即可
    if (this::heapDumpTrigger.isInitialized) {
      //heapDumpTrigger为HeapDumpTrigger,再次严禁的确认对象是否没有被释放
      heapDumpTrigger.scheduleRetainedObjectCheck()
    }
  }
}
```



```kotlin
//HeapDumpTrigger.kt
 fun scheduleRetainedObjectCheck(
    delayMillis: Long = 0L
  ) {
    val checkCurrentlyScheduledAt = checkScheduledAt
    if (checkCurrentlyScheduledAt > 0) {
      return
    }
    checkScheduledAt = SystemClock.uptimeMillis() + delayMillis
   	//backgroundHandler是一个子线程
    backgroundHandler.postDelayed({
      checkScheduledAt = 0
      checkRetainedObjects()
    }, delayMillis)
  }
 private fun checkRetainedObjects() {
   		//再次严禁的确认是否泄漏,
   		//获取存活数量
        var retainedReferenceCount = objectWatcher.retainedObjectCount
		
        if (retainedReferenceCount > 0) {
          //触发gc后在获取一次存活数量
          gcTrigger.runGc()
          retainedReferenceCount = objectWatcher.retainedObjectCount
        }
        //...略
}        
```



```kotlin
 object Default : GcTrigger {
    override fun runGc() {
      // Code taken from AOSP FinalizationTest:
      // https://android.googlesource.com/platform/libcore/+/master/support/src/test/java/libcore/
      // java/lang/ref/FinalizationTester.java
      // System.gc() does not garbage collect every time. Runtime.gc() is
      // more likely to perform a gc.
      //这里英文注释解释的很好,System.gc() 并不会每次都会执行gc,Runtime.gc()更加可能执行
      Runtime.getRuntime()
        .gc()
      enqueueReferences()
      System.runFinalization()
    }

    private fun enqueueReferences() {
      // Hack. We don't have a programmatic way to wait for the reference queue daemon to move
      // references to the appropriate queues.
      try {
        Thread.sleep(100)
      } catch (e: InterruptedException) {
        throw AssertionError()
      }
    }
  }
```



在确认泄漏后我们不再分析，看看其它泄漏是怎么检测的。

#### FragmentAndViewModelWatcher

```kotlin
class FragmentAndViewModelWatcher(
  private val application: Application,
  private val reachabilityWatcher: ReachabilityWatcher
) : InstallableWatcher {

  private val fragmentDestroyWatchers: List<(Activity) -> Unit> = run {
    val fragmentDestroyWatchers = mutableListOf<(Activity) -> Unit>()

   //略这里 AndroidXFragmentDestroyWatcher放入fragmentDestroyWatchers
	
    fragmentDestroyWatchers
  }

  private val lifecycleCallbacks =
    object : Application.ActivityLifecycleCallbacks by noOpDelegate() {
      override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
      ) {
      //注意这里调用oncreate的时候放入一个观察
      //fragmentDestroyWatchers存放AndroidXFragmentDestroyWatcher
        for (watcher in fragmentDestroyWatchers) {
          //调用AndroidXFragmentDestroyWatcher.invoke.
          //	
          watcher(activity)
        }
      }
    }

  override fun install() {
    application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
  }

  override fun uninstall() {
    application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
  } 
}
```



```kotlin
//AndroidXFragmentDestroyWatcher.kt
internal class AndroidXFragmentDestroyWatcher(
  private val reachabilityWatcher: ReachabilityWatcher
) : (Activity) -> Unit {

  override fun invoke(activity: Activity) {
    if (activity is FragmentActivity) {
      val supportFragmentManager = activity.supportFragmentManager
      //registerFragmentLifecycleCallbacks可以监听到每个fragment的添加和实例
      supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true)
      //viewModel相关,先暂时放着	
      ViewModelClearedWatcher.install(activity, reachabilityWatcher)
    }
  }
  
  private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentCreated(
      fm: FragmentManager,
      fragment: Fragment,
      savedInstanceState: Bundle?
    ) {
      //viewmodel相关,先跳过
      ViewModelClearedWatcher.install(fragment, reachabilityWatcher)
    }

    override fun onFragmentViewDestroyed(
      fm: FragmentManager,
      fragment: Fragment
    ) {
      val view = fragment.view
      if (view != null) {
      	//检测view是否被持有,原理同Activity,这里不在重复讲解
        reachabilityWatcher.expectWeaklyReachable(
          view, "${fragment::class.java.name} received Fragment#onDestroyView() callback " +
          "(references to its views should be cleared to prevent leaks)"
        )
      }
    }

    override fun onFragmentDestroyed(
      fm: FragmentManager,
      fragment: Fragment
    ) {
      //检测fragment是否被持有,原理同Activity.这里不在重复讲解
      reachabilityWatcher.expectWeaklyReachable(
        fragment, "${fragment::class.java.name} received Fragment#onDestroy() callback"
      )
    }
  }
}
```



#### ViewModelClearedWatcher

上一小节我们看到在 fragment 的 create 函数调用  ViewModelClearedWatcher.install(fragment, reachabilityWatcher) 进行监控viewmodel 的泄露。

```kotlin
internal class ViewModelClearedWatcher(
  storeOwner: ViewModelStoreOwner,
  private val reachabilityWatcher: ReachabilityWatcher
) : ViewModel() {

  private val viewModelMap: Map<String, ViewModel>?

  init {
	  //ViewModelStore内部有一个mMap对象,mMap存放所有的viewmodel
      val mMapField = ViewModelStore::class.java.getDeclaredField("mMap")
      mMapField.isAccessible = true
	 //获取fragment的ViewModelStoreOwner实例,通过实例获得这个mMap对象
	 //从而得到所有的viewmodel
      mMapField[storeOwner.viewModelStore] as Map<String, ViewModel>
    } catch (ignored: Exception) {
      null
    }
  }

  override fun onCleared() {
  	//开始回收viewmodel
  	//遍历所有的viewmodel然后检查是否泄漏
    viewModelMap?.values?.forEach { viewModel ->
      reachabilityWatcher.expectWeaklyReachable(
        viewModel, "${viewModel::class.java.name} received ViewModel#onCleared() callback"
      )
    }
  }

  companion object {
    fun install(
      storeOwner: ViewModelStoreOwner,
      reachabilityWatcher: ReachabilityWatcher
    ) {
      //这里注意传入的factory,不过传入什么都是创建ViewModelClearedWatcher对象
      val provider = ViewModelProvider(storeOwner, object : Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
          ViewModelClearedWatcher(storeOwner, reachabilityWatcher) as T
      })
      //ViewModelClearedWatcher是一个间谍viewmodel,主要是为了监听oncleared回调
      provider.get(ViewModelClearedWatcher::class.java)
    }
  }
}
```

#### ServiceWatcher

监控 Service 的 Destroy 代码可能略复杂，涉及反射和 service 启动流程。

我们四大组件的创建和销毁都会从 AMS 通过 Binder (跨进程IPC方式)传到我们 App 的 ActivityThread.mh 处理。

而 mh 是我们的是一个 Handler，于是乎我们可以反射拿到 mh,然后给 Handler 设置一个 mCallback 对象即可拦截所有的 AMS 消息，mCallback 返回 false 会继续交给 handler 处理.

```java
public final class ActivityThread {
	final H mH = new H();
	private class H extends Handler {
	}
}

```

<img src=".\pictures\内存优化\AMS 发送消息.png" style="zoom:67%;" />

我们设置 mCallback 后

<img src=".\pictures\内存优化\AMS 发送消息执行 callback.png" style="zoom: 67%;" />

最后看下 LeakCanary 相关代码

```kotlin

private val activityThreadClass by lazy { Class.forName("android.app.ActivityThread") }

 //ActivityThread有一静态个属性currentActivityThread,保存自己的实例
 private val activityThreadInstance by lazy {	
    activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null)!!
}
  	
 private fun swapActivityThreadHandlerCallback(swap: (Handler.Callback?) -> Handler.Callback?) {
    //activityThreadClass为Class.forName("android.app.ActivityThread")
    val mHField =
      activityThreadClass.getDeclaredField("mH").apply { isAccessible = true }
    //获取属性,这里mHField[xxxx]是kotlin语法不比困惑  
    val mH = mHField[activityThreadInstance] as Handler
	//找到Handler.mCallback
    val mCallbackField =
      Handler::class.java.getDeclaredField("mCallback").apply { isAccessible = true }
    //获取mCallback实例
    val mCallback = mCallbackField[mH] as Handler.Callback?
    //替换成自己的Handler.Callback,内部会转发到原始mCallback
    mCallbackField[mH] = swap(mCallback)
}
```

service 还有一个相关的函数 ActivityManagerProxy.serviceDoneExecuting，当 service 创建完成(回调 onCreate 之后)后，回调ActivityManagerProxy.serviceDoneExecuting。当 service 被销毁后(AMS 下发消息,且回调 onDestroy 后)回调ActivityManagerProxy.serviceDoneExecuting。

所以我们有两个`hook`点可以用来检测被销毁。

```kotlin
//因为都是简单操作,读者可自行类别上文swapActivityThreadHandlerCallback
private fun swapActivityManager(swap: (Class<*>, Any) -> Any) {
    val singletonClass = Class.forName("android.util.Singleton")
    val mInstanceField =
      singletonClass.getDeclaredField("mInstance").apply { isAccessible = true }

    val singletonGetMethod = singletonClass.getDeclaredMethod("get")

    val (className, fieldName) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      "android.app.ActivityManager" to "IActivityManagerSingleton"
    } else {
      "android.app.ActivityManagerNative" to "gDefault"
    }

    val activityManagerClass = Class.forName(className)
    val activityManagerSingletonField =
      activityManagerClass.getDeclaredField(fieldName).apply { isAccessible = true }
    val activityManagerSingletonInstance = activityManagerSingletonField[activityManagerClass]

    // Calling get() instead of reading from the field directly to ensure the singleton is
    // created.
    val activityManagerInstance = singletonGetMethod.invoke(activityManagerSingletonInstance)

    val iActivityManagerInterface = Class.forName("android.app.IActivityManager")
    mInstanceField[activityManagerSingletonInstance] =
      swap(iActivityManagerInterface, activityManagerInstance!!)
}
```

另外最后补充一个小点 ActivityThread 的 mServices 保存了所有 app 的 service。

```java
//ActivityThread.java
class ActivityThread{
	final ArrayMap<IBinder, Service> mServices = new ArrayMap<IBinder, Service>();
}
```

我们结合以下在看 ServiceWatcher 就很简单。

```java
//ServiceWatcher.java
//存放要观察的service
private val servicesToBeDestroyed = WeakHashMap<IBinder, WeakReference<Service>>()

private val activityThreadClass by lazy { Class.forName("android.app.ActivityThread") }

private val activityThreadInstance by lazy {
    activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null)!!
}
//存放所有app的service	
private val activityThreadServices by lazy {
    val mServicesField =
        activityThreadClass.getDeclaredField("mServices").apply { isAccessible = true }

    @Suppress("UNCHECKED_CAST")
    mServicesField[activityThreadInstance] as Map<IBinder, Service>
}
  
override fun install() {
    checkMainThread()
    
    try {
    
      swapActivityThreadHandlerCallback { mCallback ->
        uninstallActivityThreadHandlerCallback = {
          swapActivityThreadHandlerCallback {
            mCallback
          }
        }
        Handler.Callback { msg ->
  		  //回调stopservice函数	
          if (msg.what == STOP_SERVICE) {
            val key = msg.obj as IBinder
            //activityThreadServices一定不为空
            //所以调用onServicePreDestroy,内部会将service放入servicesToBeDestroyed
            //后面会回调serviceDoneExecuting函数
            activityThreadServices[key]?.let {
              onServicePreDestroy(key, it)
            }
          }
          mCallback?.handleMessage(msg) ?: false
        }
      }
      //servicesToBeDestroyed相关hook
      swapActivityManager { activityManagerInterface, activityManagerInstance ->
        uninstallActivityManager = {
          swapActivityManager { _, _ ->
            activityManagerInstance
          }
        }
        Proxy.newProxyInstance(
          activityManagerInterface.classLoader, arrayOf(activityManagerInterface)
        ) { _, method, args ->
          if (METHOD_SERVICE_DONE_EXECUTING == method.name) {
            val token = args!![0] as IBinder
            //servicesToBeDestroyed会在创建的时候回调,也会在销毁的时候回调
            //servicesToBeDestroyed所在上面销毁的时候加入元素.
            //所以servicesToBeDestroyed包含要检测的service直接调用onServiceDestroyed确认是否泄漏
            if (servicesToBeDestroyed.containsKey(token)) {
              onServiceDestroyed(token)
            }
          }
          try {
            if (args == null) {
              method.invoke(activityManagerInstance)
            } else {
              method.invoke(activityManagerInstance, *args)
            }
          } catch (invocationException: InvocationTargetException) {
            throw invocationException.targetException
          }
        }
      }
    } catch (ignored: Throwable) {
      SharkLog.d(ignored) { "Could not watch destroyed services" }
    }
}
```



## OOM（OutOfMemoryError）

### 单个应用可用的最大内存
Android 设备出厂以后，Java 虚拟机对单个应用的最大内存分配就确定下来了，超出这个值就会OOM。

命令（需要 root 权限）：adb shell cat /system/build.prop

也可以通过命令（无需 root 权限）：adb shell getprop

<img src=".\pictures\内存优化\build.prop.png" style="zoom:80%;" />

- dalvik.vm.heapstartsize=8m， 它表示堆分配的初始大小，它会影响到整个系统对内存的使用程度，和第一次使用应用时的流畅程度。
它值越小，系统内存消耗越慢。它值越大，系统内存消耗越快，但是应用更流畅。
- dalvik.vm.heapgrowthlimit=64m ，它表示单个进程内存被限定在64m，即程序运行过程中实际只能使用64m内存，超出就会报OOM（仅仅针对dalvik堆，不包括native堆）。
- dalvik.vm.heapsize=384m，单个进程可用的最大内存，但如果存在heapgrowthlimit参数，则以heapgrowthlimit为准。在Android开发中，如果要使用大堆，需要在清单文件中指定android:largeHeap为true，这样dvm heap最大可达heapsize。

### 导致OOM的常见原因
- 加载大图片
    - 比如，Galaxy Nexus的照相机能够拍摄2592x1936 pixels (5 MB)的图片。 如果bitmap的图像配置是使用ARGB_8888 (从Android 2.3开始的默认配置) ，那么加载这张照片到内存大约需要19MB(2592 * 1936 * 4 bytes) 的空间，从而迅速消耗掉该应用的剩余内存空间。 
- 内存泄漏

## JVMTI
- JVM Tools Interface

- 在了解 JVMTI 之前，需要先了解下Java平台调试体系JPDA（Java PlatformDebugger Architecture）。它是Java虚拟机为调试和监控虚拟机专门提供的一套接口。如下图所示，JPDA被抽象为三层实现。其中JVMTI就是JVM对外暴露的接口。JDI是实现了JDWP通信协议的客户端，调试器通过它和JVM中被调试程序通信。

- JVMTI 本质上是在JVM内部的许多事件进行了埋点。通过这些埋点可以给外部提供当前上下文的一些信息。甚至可以接受外部的命令来改变下一步的动作。外部程序一般利用C/C++实现一个JVMTIAgent，在Agent里面注册一些JVM事件的回调。当事件发生时JVMTI调用这些回调方法。Agent可以在回调方法里面实现自己的逻辑。JVMTIAgent是以动态链接库的形式被虚拟机加载的。

  ![](.\pictures\内存优化\JVMTI.png)

- JVMTI的一些重要的功能包括：
    - 重新定义类。
    - 跟踪对象分配和垃圾回收过程。
    - 遵循对象的引用树，遍历堆中的所有对象。
    - 检查 Java 调用堆栈。
    - 暂停（和恢复）所有线程。

### ART TI
- 在 Android 8.0 及更高版本中，ART 工具接口 (ART TI) 可提供某些运行时的内部架构信息，并允许分析器和调试程序影响应用的运行时行为。这可用于实现最先进的性能工具，以便在其他平台上实现原生代理。

- 运行时内部架构信息会提供给已加载到运行时进程中的代理。它们通过直接调用和回调与 ART 通信。

- 提供代理接口 JVMTI 的代码作为运行时插件来实现。

  ![](.\pictures\内存优化\ART TI.png)

### 加载或连接代理
如需在运行时启动时连接代理，请使用以下命令加载 JVMTI 插件和指定的代理：

```
dalvikvm -Xplugin:libopenjdkjvmti.so -agentpath:/path/to/agent/libagent.so …
```

如需将代理连接到已在运行的应用，请使用以下命令：

```
adb shell cmd activity attach-agent [process] /path/to/agent/libagent.so[=agent-options]
```

### API
以下方法已添加到 android.os.Debug 中。

```java
/**
     * Attach a library as a jvmti agent to the current runtime, with the given classloader
     * determining the library search path.
     * Note: agents may only be attached to debuggable apps. Otherwise, this function will
     * throw a SecurityException.
     *
     * @param library the library containing the agent.
     * @param options the options passed to the agent.
     * @param classLoader the classloader determining the library search path.
     *
     * @throws IOException if the agent could not be attached.
     * @throws a SecurityException if the app is not debuggable.
     */
    public static void attachJvmtiAgent(@NonNull String library, @Nullable String options,
            @Nullable ClassLoader classLoader) throws IOException {
```

### 抖音内存监控框架（Kenzo）
<img src=".\pictures\内存优化\Kenzo 架构.jpg" style="zoom:80%;" />



<img src=".\pictures\内存优化\Kenzo 流程.jpg" style="zoom:80%;" />

### mmap 内存映射
Android中的Binder机制就是mmap来实现的，不仅如此，微信的MMKV key-value组件、美团的 Logan的日志组件 都是基于mmap来实现的。mmap强大的地方在于通过内存映射直接对文件进行读写，减少了对数据的拷贝次数，大大的提高了IO读写的效率。

#### Linux文件系统

- 虚拟文件系统层：作用是屏蔽下层具体文件系统操作的差异，为上层的操作提供一个统一的接口。

- 文件系统层 ：具体的文件系统层，一个文件系统一般使用块设备上一个独立的逻辑分区。

- Page Cache （层页高速缓存层）：引入 Cache 层的目的是为了提高 Linux 操作系统对磁盘访问的性能。

- 通用块层：作用是接收上层发出的磁盘请求，并最终发出 I/O 请求。

- I/O 调度层：作用是管理块设备的请求队列。

- 块设备驱动层 ：利用驱动程序，驱动具体的物理块设备。

- 物理块设备层：具体的物理磁盘块。

  <img src=".\pictures\内存优化\Linux文件系统.png" style="zoom:80%;" />

#### Cache Page与Read/Write操作
由于有了Cache Page的存在，read/write系统调用会有以下的操作，我们拿Read来进行说明：

- 用户进程向内核发起读取文件的请求，这涉及到用户态到内核态的转换。
- 内核读取磁盘文件中的对应数据，并把数据读取到Cache Page中。
- 由于Page Cache处在内核空间，不能被用户进程直接寻址 ，所以需要从Page Cache中拷贝数据到用户进程的堆空间中。

注意，这里涉及到了两次拷贝：第一次拷贝磁盘到Page Cache，第二次拷贝Page Cache到用户内存。最后物理内存的内容是这样的，同一个文件内容存在了两份拷贝，一份是页缓存，一份是用户进程的内存空间。

<img src=".\pictures\内存优化\Cache Page与ReadWrite操作.png" style="zoom:80%;" />

#### mmap内存映射原理

mmap是一种内存映射文件的方法，它将一个文件映射到进程的地址空间中，实现文件磁盘地址和进程虚拟地址空间中一段虚拟地址的一一对映关系。实现这样的映射关系后，进程就可以采用指针的方式读写操作这一段内存，而系统会自动回写脏页面到对应的文件磁盘上，即完成了对文件的操作而不必再调用read,write等系统调用函数。相反，内核空间对这段区域的修改也直接反映用户空间，从而可以实现不同进程间的文件共享。

![](.\pictures\内存优化\mmap内存映射原理.png)

这里我们可以看出mmap系统调用与read/write调用的区别在于：

- mmap只需要一次系统调用（一次拷贝），后续操作不需要系统调用。
- 访问的数据不需要在page cache和用户缓冲区之间拷贝。 

从上所述，当频繁对一个文件进行读取操作时，mmap会比read/write更高效。

# 图片优化
- 质量压缩
- 尺寸压缩
- Native 压缩

## Bitmap.compress()

```java
public boolean compress(Bitmap.CompressFormat format, int quality, OutputStream stream);
```

这个方法有三个参数：

- Bitmap.CompressFormat format 图像的压缩格式；
- int quality 图像压缩率，0-100。 0 压缩100%，100意味着不压缩；
- OutputStream stream 写入压缩数据的输出流；

返回值
- 如果成功地把压缩数据写入输出流，则返回true。


### inJustDecodeBounds
- Options中有个属性inJustDecodeBounds，我们可以充分利用它，来避免大图片的溢出问题。

- 如果该值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了。但是允许我们查询图片的信息，这其中就包括图片大小信息，options.outHeight (图片原始高度)和option.outWidth(图片原始宽度)。

- Options中有个属性inSampleSize，我们可以充分利用它，实现缩放，如果被设置为一个值，要求解码器解码出原始图像的一个子样本，返回一个较小的bitmap，以节省存储空间。例如，inSampleSize =  2，则取出的缩略图的宽和高都是原始图片的1/2，图片大小就为原始大小的1/4。

  <img src=".\pictures\图片优化\inJustDecodeBounds.jpg" style="zoom:80%;" />

## libjpeg
- libjpeg是一个完全用C语言编写的库，包含了被广泛使用的JPEG解码、JPEG编码和其他的JPEG功能的实现。

### libjpeg-turbo
- libjpeg-turbo图像编解码器，使用了SIMD指令（MMX，SSE2，NEON，AltiVec）来加速x86，x86-64，ARM和PowerPC系统上的JPEG压缩和解压缩。在这样的系统上，libjpeg-turbo的速度通常是libjpeg的2-6倍，其他条件相同。在其他类型的系统上，凭借其高度优化的霍夫曼编码，libjpeg-turbo仍然可以大大超过libjpeg。在许多情况下，libjpeg-turbo的性能可与专有的高速JPEG编解码器相媲美。

> 1995年 JPEG 图片处理引擎最初用于 PC 。 2005年，为便于浏览器的使用，基于 JPEG 引擎开发了 skia 引擎。 2007年安卓用的 skia 引擎，但去掉了哈夫曼编码算法，采用定长编码算法，但解码还是保留了哈夫曼算法，但会导致图片处理后文件变大了。
>
> 早期由于 CPU 和内存在手机上都非常有限，而哈夫曼算法非常耗 CPU 资源，所以谷歌被迫使用了其他算法。我们可以绕过安卓 Bitmap API层，来自己编码实现—-修复使用哈夫曼算法。

**微信为什么采用 libjpeg 压缩？**

- 兼容低版本
- 垮平台算法复用

### 图片压缩流程

![](.\pictures\图片优化\图片压缩流程.png)

**左移右移运算**

**左移操作（<<）**

规则：

右边空出的位用0填补

高位左移溢出则舍弃该高位。

即 3 在32位计算机中的存储为(前后两条黑色竖线人为添加以方便于识别)：

```
| 0000 0000 0000 0000 0000 0000 0000 0011 |
```

左移2位结果如下：

```
00 | 00 0000 0000 0000 0000 0000 0000 0011 XX |
```

左移两位高位溢出，舍弃，低位也就是XX的位置空余，则补0变为：

```
| 0000 0000 0000 0000 0000 0000 0000 1100 |
```

再转换为十进制数：输出即为：12。

**右移操作（>>）**

左边空出的位用0或者1填补。正数用0填补，负数用1填补。

例如：6>>1

```
| 0000 0000 0000 0000 0000 0000 0000 0110 |
```

我们进行右移操作

```
| 0000 0000 0000 0000 0000 0000 0000 0011 | 0
```

则结果为 6>>1 = 3

**“与”运算**

运算规则：0&0=0;0&1=0;1&0=0;1&1=1;

即：两位同时为“1”，结果才为“1”，否则为0


第一个输入 | 第二个输入 | 输出结果
---|---|---
1 | 1 | 1
1 | 0 | 0
0 | 1 | 0
0 | 0 | 0

### 颜色的二进制运算
<img src=".\pictures\图片优化\颜色的二进制运算.png" style="zoom: 80%;" />

### 内存上的像素读取
<img src=".\pictures\图片优化\内存上的像素读取.png" style="zoom:80%;" />


# 启动优化

## 应用启动流程
- 点击桌面App图标，Launcher进程采用Binder IPC向system_server进程发起startActivity请求；

- system_server进程接收到请求后，向zygote进程发送创建进程的请求；

- Zygote进程fork出新的子进程，即App进程；

- App进程，通过Binder IPC向sytem_server进程发起attachApplication请求；

- system_server进程在收到请求后，进行一系列准备工作后，再通过binder IPC向App进程发送scheduleLaunchActivity请求；

- App进程的binder线程（ApplicationThread）在收到请求后，通过handler向主线程发送LAUNCH_ACTIVITY消息；

- 主线程在收到Message后，通过反射机制创建目标Activity，并回调Activity.onCreate()等方法。

- 到此，App便正式启动，开始进入Activity生命周期，执行完onCreate/onStart/onResume方法，UI渲染结束后便可以看到App的主界面。

  <img src=".\pictures\启动优化\应用启动流程.png" style="zoom:80%;" />

## 启动状态
应用有三种启动状态，每种状态都会影响应用向用户显示所需的时间：冷启动、温启动与热启动。

- 冷启动：

冷启动是指应用从头开始启动：系统进程在冷启动后才创建应用进程。发生冷启动的情况包括应用自设备启动后或系统终止应用后首次启动。 

- 热启动：

在热启动中，系统的所有工作就是将 Activity 带到前台。只要应用的所有 Activity 仍驻留在内存中，应用就不必重复执行对象初始化、布局加载和绘制。

- 温启动：

温启动包含了在冷启动期间发生的部分操作；同时，它的开销要比热启动高。有许多潜在状态可视为温启动。例如：

- [x] 用户在退出应用后又重新启动应用。进程可能未被销毁，继续运行，但应用需要执行 onCreate() 从头开始重新创建 Activity。

- [x] 系统将应用从内存中释放，然后用户又重新启动它。进程和 Activity 需要重启，但传递到 onCreate() 的已保存的实例savedInstanceState对于完成此任务有一定助益。


## 启动耗时统计

### 系统日志统计

在 Android 4.4（API 级别 19）及更高版本中，logcat 包含一个输出行，其中包含名为 Displayed 的值。此值代表从启动进程到在屏幕上完成对应 Activity 的绘制所用的时间。 

![](.\pictures\启动优化\系统日志统计.png)

### adb命令统计

adb shell am start -S -W [packageName]/.[activityName]

例如：adb shell am start -S -W com.dongnaoedu.optimizingexample/.MainActivity

![](.\pictures\启动优化\adb命令统计.png)

- [x] WaitTime：包括前一个应用Activity pause的时间和新应用启动的时间；
- [x] ThisTime：表示一连串启动Activity的最后一个Activity的启动耗时；
- [x] TotalTime：表示新应用启动的耗时，包括新进程的启动和Activity的启动，但不包括前一个应用Activity pause的耗时。

<img src=".\pictures\启动优化\启动耗时统计.png" style="zoom:80%;" />

### 冷启动耗时统计

在性能测试中存在启动时间2-5-8原则：

- 当用户能够在2秒以内得到响应时，会感觉系统的响应很快；
- 当用户在2-5秒之间得到响应时，会感觉系统的响应速度还可以；
- 当用户在5-8秒以内得到响应时，会感觉系统的响应速度很慢，但是还可以接受；
- 而当用户在超过8秒后仍然无法得到响应时，会感觉系统糟透了，或者认为系统已经失去响应。

而Google也提出一项计划：Android Vitals 。该计划旨在改善 Android 设备的稳定性和性能。当选择启用了该计划的用户运行您的应用时，其 Android 设备会记录各种指标，包括应用稳定性、应用启动时间、电池使用情况、呈现时间和权限遭拒等方面的数据。Google Play 管理中心 会汇总这些数据，并将其显示在 Android Vitals 信息中心内。

当应用启动时间过长时，Android Vitals 可以通过 Play管理中心提醒您，从而帮助提升应用性能。Android Vitals 在您的应用出现以下情况时将其启动时间视为过长：
- 冷启动用了 5 秒或更长时间。
- 温启动用了 2 秒或更长时间。
- 热启动用了 1.5 秒或更长时间。

实际上不同的应用因为启动时需要初始化的数据不同，启动时间自然也会不同。相同的应用也会因为在不同的设
备，因为设备性能影响启动速度不同。所以实际上启动时间并没有绝对统一的标准，我们之所以需要进行启动耗时
的统计的，可能在于产品对我们应用启动时间提出具体的要求。

## CPU Profile

## 应用启动之后收集方法执行

- Run Configuration 配置

![](.\pictures\启动优化\CPU Profile配置.png)

- Profile app 运行

![](.\pictures\启动优化\Profile 运行.png)

类型 | 作用
---|---
Call Chart | 根据时间线查看调用栈，便于观察每次调用是何时发生的
Flame Chart | 根据耗时百分比查看调用栈，便于发现总耗时很长的调用链
Top Down Tree | 查看记录数据中所有方法调用栈，便于观察其中每一步所消耗的精确时间
Bottom Up Tree | 相对于Top Down Tree，能够更方便查看耗时方法如何被调用

## 通过应用插桩生成跟踪日志
如需生成应用执行的方法跟踪，您可以使用 Debug 类进行应用插桩。通过这种方式检测我们的应用，可让我们更精确地控制设备何时开始和停止记录跟踪信息。此外，设备还能使用我们指定的名称保存跟踪日志，便于我们日后轻松识别各个日志文件。我们随后可以使用 Android Studio 的 CPU Profile 查看各个跟踪日志。

- Debug.startMethodTracing("optimizing_example.trace");
- Debug.stopMethodTracing();

在调用中，可以指定 .trace 文件的名称，系统会将它保存到一个特定于软件包的目录中。该目录专门用于保存目标设备上的永久性应用数据，与 getExternalFilesDir() 返回的目录相同，在大多数设备上都位于 ~/sdcard/ 目录中。此文件包含二进制方法跟踪数据，以及一个包含线程和方法名称的映射表。如需停止跟踪，请调用 stopMethodTracing()。

## 优化布局加载
- 避免布局嵌套过深
- 把耗时的布局渲染操作放在子线程中，等inflate操作完成后再回调到主线程

### AsyncLayoutInflater

依赖：
```gradle
implementation "androidx.asynclayoutinflater:asynclayoutinflater:1.0.0"
```

Java 写法：
```java
new AsyncLayoutInflater(this).inflate(R.layout.activity_main, null,
    new AsyncLayoutInflater.OnInflateFinishedListener() {
        @Override
        public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
            setContentView(view);
        }
    });
```

Kotlin 写法：
```kotlin
AsyncLayoutInflater(this).inflate(
    R.layout.activity_main, null
) { view, resid, parent -> setContentView(view) }
```


## 黑白屏问题
### 问题由来
当系统加载并启动 App 时，需要耗费相应的时间，即使时间不到 1s,用户也会感觉到当点击 App 图标时会有 “延迟” 现象，为了解决这一问题，Google 的做法是在 App 创建的过程中，先展示一个空白页面，让用户体会到点击图标之后立马就有响应；而这个空白页面的颜色是根据我们在 AndroiMainfest 文件中配置的主题背景颜色来决定的，现在一般默认是白色。

### 解决方案
- 修改AppTheme

在应用默认的 AppTheme 中，设置系统 “取消预览（空白窗体）” 为 true，或者设置空白窗体为透明。

```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
         Customize your theme here. 
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        
        <!--设置系统取消预览（空白窗口）-->
        <item name="android:windowDisablePreview">true</item>
        
         <!--设置背景透明-->
        <item name="android:windowIsTranslucent">true</item>
</style>
```

- 自定义AppTheme
```xml
// styles文件中自定义启动页主题theme
 <style name="AppTheme.LaunchTheme">
       <item name="android:windowBackground">@drawable/launch_layout</item>
        <item name="windowNoTitle">true</item>
        <item name="android:windowFullscreen">true</item>
 </style>
```

将启动的 Activity 的 theme 设置为自定义主题 ：

```xml
// AndroidManifest.xml 文件中
 <activity android:name=".MainActivity" android:theme="@style/AppTheme.LaunchTheme">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

- Activity 中重新设置为系统主题

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置为系统主题
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //....
    }
    
}
```


## StrictMode
StrictMode是一个开发人员工具，它可以检测出我们可能无意中做的事情，并提醒我们注意，以便我们能够修复它们。

StrictMode最常用于捕获应用程序主线程上的意外磁盘或网络访问。帮助我们让磁盘和网络操作远离主线程，可以使应用程序更加平滑、响应更快。

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            //线程检测策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()   //读、写操作
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()   //Sqlite对象泄露
                    .detectLeakedClosableObjects()  //未关闭的Closable对象泄露
                    .penaltyLog()  //违规打印日志
                    .penaltyDeath() //违规崩溃
                    .build());
        }
    }
}
```

## IdleHandler
### 介绍
IdleHandler 是 MessageQueue 内定义的一个接口，一般可用于做性能优化。当消息队列内没有需要立即执行的 message 时，会主动触发 IdleHandler 的 queueIdle 方法。返回值为 false，即只会执行一次；返回值为 true，即每次当消息队列内没有需要立即执行的消息时，都会触发该方法。

简单总结，IdleHandler可用于监听主线程是否为空闲状态（无事可干）。

```java
public final class MessageQueue {
    public static interface IdleHandler {
        boolean queueIdle();
    }
}
```

### 使用方式
通过获取 looper 对应的 MessageQueue 队列注册监听。

```java
Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
    @Override
    public boolean queueIdle() {
        // doSomething()
        return false;
    }
});
```

### 源码解析

```java
Message next() {
    // 隐藏无关代码...
    int pendingIdleHandlerCount = -1; // -1 only during first iteration
    int nextPollTimeoutMillis = 0;
    for (; ; ) {
        // 隐藏无关代码...
        // If first time idle, then get the number of idlers to run.
        // Idle handles only run if the queue is empty or if the first message
        // in the queue (possibly a barrier) is due to be handled in the future.
        if (pendingIdleHandlerCount < 0
                && (mMessages == null || now < mMessages.when)) {
            pendingIdleHandlerCount = mIdleHandlers.size();
        }
        if (pendingIdleHandlerCount <= 0) {
            // No idle handlers to run.  Loop and wait some more.
            mBlocked = true;
            continue;
        }
        if (mPendingIdleHandlers == null) {
            mPendingIdleHandlers = new IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
        }
        mPendingIdleHandlers = mIdleHandlers.toArray(mPendingIdleHandlers);
    }
    // Run the idle handlers.
    // We only ever reach this code block during the first iteration.
    for (int i = 0; i < pendingIdleHandlerCount; i++) {
        final IdleHandler idler = mPendingIdleHandlers[i];
        mPendingIdleHandlers[i] = null; // release the reference to the handler
        boolean keep = false;
        try {
            keep = idler.queueIdle();
        } catch (Throwable t) {
            Log.wtf(TAG, "IdleHandler threw exception", t);
        }
        if (!keep) {
            synchronized (this) {
                mIdleHandlers.remove(idler);
            }
        }
    }
    // Reset the idle handler count to 0 so we do not run them again.
    pendingIdleHandlerCount = 0;
    // While calling an idle handler, a new message could have been delivered
    // so go back and look again for a pending message without waiting.
    nextPollTimeoutMillis = 0;
}
```

- 在 MessageQueue 里 next 方法的 for 死循环内，获取 mIdleHandlers 的数量 pendingIdleHandlerCount；
- 通过 mMessages == null || now < mMessages.when 判断当前消息队列为空或者目前没有需要执行的消息时，给 pendingIdleHandlerCount 赋值；
- 当数量大于 0，遍历取出数组内的 IdleHandler，执行 queueIdle() ；
- 返回值为 false 时，主动移除监听 mIdleHandlers.remove(idler) ；

### 使用场景
- 如果启动的 Activity、Fragment、Dialog 内含有大量数据和视图的加载，导致首次打开时动画切换卡顿或者一瞬间白屏，可将部分加载逻辑放到 queueIdle() 内处理。例如引导图的加载和弹窗提示等；
- 系统源码中 ActivityThread 的 GcIdler，在某些场景等待消息队列暂时空闲时会尝试执行 GC 操作；
- 系统源码中  ActivityThread 的 Idler，在 handleResumeActivity() 方法内会注册 Idler()，等待 handleResumeActivity 后视图绘制完成，消息队列暂时空闲时再调用 AMS 的 activityIdle 方法，检查页面的生命周期状态，触发 activity 的 stop 生命周期等。这也是为什么我们 BActivity 跳转 CActivity 时，BActivity 生命周期的 onStop() 会在 CActivity 的 onResume() 后。
- 一些第三方框架 Glide 和 LeakCanary 等也使用到 IdleHandler；

# 卡顿分析
## 刷新率

大多数用户感知到的卡顿等性能问题的最主要根源都是因为渲染性能。Android系统每隔大概16.6ms发出VSYNC信
号，触发对UI进行渲染，如果每次渲染都成功，这样就能够达到流畅的画面所需要的60fps，为了能够实现60fps，
这意味着程序的大多数操作都必须在16ms内完成。

我们通常都会提到60fps与16ms，可是知道为何会是以程序是否达到60fps来作为App性能的衡量标准吗？这
是因为人眼与大脑之间的协作无法感知超过60fps的画面更新。

12fps大概类似手动快速翻动书籍的帧率，这明显是可以感知到不够顺滑的。24fps使得人眼感知的是连续线
性的运动，这其实是归功于运动模糊的效果。24fps是电影胶圈通常使用的帧率，因为这个帧率已经足够支撑
大部分电影画面需要表达的内容，同时能够最大的减少费用支出。但是低于30fps是无法顺畅表现绚丽的画面
内容的，此时就需要用到60fps来达到想要的效果，当然超过60fps是没有必要的。

开发app的性能目标就是保持60fps，这意味着每一帧你只有16ms=1000/60的时间来处理所有的任务。

![](.\pictures\卡顿分析与渲染优化\刷新率.png)

如果某个操作花费时间是24ms，系统在得到VSYNC信号的时候就无法进行正常渲染，这样就发生了丢帧现象。那
么用户在32ms内看到的会是同一帧画面。

![](.\pictures\卡顿分析与渲染优化\丢帧现象.png)

有很多原因可以导致丢帧， 一般主线程过多的UI绘制、大量的IO操作或是大量的计算操作占用CPU，都会导致App
界面卡顿。

## Systrace

### Systrace 简介
Systrace 允许你收集和检查设备上运行的所有进程的计时信息。 它包括Androidkernel的一些数据（例如CPU调度程序，IO和APP Thread），并且会生成HTML报告，方便用户查看分析trace内容。

### Systrace 安装

#### Systrace 安装目录

Systrace 存在于 Platform tools 目录下

![](.\pictures\卡顿分析与渲染优化\Systrace 安装目录.png)

#### Python 部分安装
虽然 Android SDK 中已经存在 Systrace，但需要使用 Systrace，必须要安装 Python 才能运行，而且要安装 Python 2.x 版本，3.x版本无法支持运行，这里我们安装的是 Python 2.7.13 版本，下载链接：https://www.python.org/downloads/release/python-2713/

- 环境变量配置

如果你的电脑已经安装 Python （我电脑上还安装了 Python 3.8），请将 Python 2.7.13 的 环境变量配置前置，这样 Python 运行时才会使用 Python 2.7的运行环境

<img src=".\pictures\卡顿分析与渲染优化\Python 环境变量配置.png" style="zoom:80%;" />


- 提示安装 win32con

尝试通过 systrace.py -l 命令查看环境是否正常，提示需要安装 win32con模块

<img src=".\pictures\卡顿分析与渲染优化\提示安装 win32con.png" style="zoom:80%;" />

- 提示 unknown encoding

到 Python27\Scripts 目录下（注意：到这个目录），执行 pip install pypiwin32，安装 pypiwin32，提示：LookupError: unknown encoding: cp65001

<img src=".\pictures\卡顿分析与渲染优化\提示 unknown encoding.png" style="zoom:80%;" />


执行：set PYTHONIOENCODING=UTF-8 设置Python 编码为 UTF-8

<img src=".\pictures\卡顿分析与渲染优化\设置Python编码.png" style="zoom:80%;" />

继续安装：pip install pypiwin32，安装成功

<img src=".\pictures\卡顿分析与渲染优化\pypiwin32 安装成功.png" style="zoom:80%;" />

- 提示更新pip

执行：python -m pip install --upgrade pip，更新pip

<img src=".\pictures\卡顿分析与渲染优化\更新pip.png" style="zoom:80%;" />

提醒继续更新pip，这里就不再更新了，目前版本应该够用了。

- 提示安装 six

尝试通过 systrace.py -l 命令查看环境是否正常，提示需要安装 six 模块

执行：pip install six 

<img src=".\pictures\卡顿分析与渲染优化\安装six模块.png" style="zoom:80%;" />

- 安装成功

尝试通过 systrace.py -l 命令查看环境是否正常，提示没有启动ADB，启动一个AVD之后，再执行 systrace.py -l 命令，成功！

![](.\pictures\卡顿分析与渲染优化\安装成功.png)

### Systrace 使用

python systrace.py -t 10 -o E:\mynewtrace.html gfx input view am dalvik sched wm disk res -a com.dongnaoedu.arch_demo

操作应用 10秒后，systrace生成一个HTML报告

![](.\pictures\卡顿分析与渲染优化\Systrace 报告.png)

W键，放大；S键，缩小


## CPU Profile

使用CPU Profile 也具有 Systrace 类似的功能

![](.\pictures\卡顿分析与渲染优化\CPU Profile.png)



![](.\pictures\卡顿分析与渲染优化\CPU Profile2.png)


## App层面监控卡顿

systrace可以让我们了解应用所处的状态，了解应用因为什么原因导致的。若需要准确分析卡顿发生在什么函数，
资源占用情况如何，目前业界两种主流有效的app监控方式如下：

- 利用UI线程的Looper打印的日志匹配
- 使用Choreographer.FrameCallback


### Looper日志检测卡顿

Android主线程更新UI。如果界面1秒钟刷新少于60次，即FPS小于60，用户就会产生卡顿感觉。简单来说，
Android使用消息机制进行UI更新，UI线程有个Looper，在其loop方法中会不断取出message，调用其绑定的
Handler在UI线程执行。如果在handler的dispatchMesaage方法里有耗时操作，就会发生卡顿。

```java
public static void loop() {
	//...... 
	for (;;) {
	//...... 
		Printer logging = me.mLogging; 
		if (logging != null) { 
			logging.println(">>>>> Dispatching to " + msg.target + " " + msg.callback + ": " + msg.what); 
		}
		msg.target.dispatchMessage(msg); 

		if (logging != null) { 
			logging.println("<<<<< Finished to " + msg.target + " " + msg.callback); 
		}
	//...... 
	}
}
```

只要检测 msg.target.dispatchMessage(msg) 的执行时间，就能检测到部分UI线程是否有耗时的操作。注意到这行
执行代码的前后，有两个logging.println函数，如果设置了logging，会分别打印出>>>>> Dispatching to和
<<<<< Finished to 这样的日志，这样我们就可以通过两次log的时间差值，来计算dispatchMessage的执行时
间，从而设置阈值判断是否发生了卡顿。

```java
public final class Looper {
	private Printer mLogging;
	public void setMessageLogging(@Nullable Printer printer) {
		mLogging = printer; 
	}
}

public interface Printer {
	void println(String x);
}
```

Looper 提供了 setMessageLogging(@Nullable Printer printer) 方法，所以我们可以自己实现一个Printer，在
通过setMessageLogging()方法传入即可，其实这种方式也就是 BlockCanary 原理。


### Choreographer.FrameCallback

Android系统每隔16ms发出VSYNC信号，来通知界面进行重绘、渲染，每一次同步的周期约为16.6ms，代表一帧
的刷新频率。通过Choreographer类设置它的FrameCallback函数，当每一帧被渲染时会触发回调
FrameCallback.doFrame (long frameTimeNanos) 函数。frameTimeNanos是底层VSYNC信号到达的时间戳 。

```java
public class ChoreographerHelper {

    static long lastFrameTimeNanos = 0;

    public static void start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {

                // 此方法提供帧开始渲染时的时间（以纳秒为单位）。
                // 1 纳秒=0.000001 毫秒
                @Override
                public void doFrame(long frameTimeNanos) {
                    if (lastFrameTimeNanos == 0) {
                        lastFrameTimeNanos = frameTimeNanos;
                        Choreographer.getInstance().postFrameCallback(this);
                        return;
                    }
                    long diff = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000;
                    if (diff > 16.6f) {
                        //掉帧数
                        int droppedCount = (int) (diff / 16.6);
                    }
                    lastFrameTimeNanos = frameTimeNanos;
                    Choreographer.getInstance().postFrameCallback(this);
                }
            });
        }
    }
}
```

# UI渲染优化

## 层级优化
由Android 视图的绘制都会经过 measure、layout、draw 三个过程，它们都包含自上而下的View Tree遍历，如果视图层级太深自然需要更多的时间来完成整个绘测过程，从而造成启动速度慢、卡顿等问题。

### Layout Inspector

使用 Android Studio 中的布局检查器，我们可以将应用布局与设计模型进行比较、显示应用的放大视图或 3D 视图，以及在运行时检查应用布局的细节。如果布局是在运行时（而不是完全在 XML 中）构建的并且布局行为出现异常，该工具会非常有用。

使用布局验证，我们可以使用不同的设备和显示配置（包括可变字体大小或用户语言）同时预览布局，以便轻松测试各种常见的布局问题。

#### 打开布局检查器

要打开 Layout Inspector，请执行以下操作：

- 在连接的设备或模拟器上运行您的应用。
- 依次点击 Tools > Layout Inspector。

如图 1 所示，布局检查器将显示以下内容：

- Component Tree：布局中视图的层次结构。
- Layout Display：按照应用布局在设备或模拟器上的显示效果呈现布局，并显示每个视图的布局边界。
- 布局检查器工具栏：布局检查器的工具。
- Attributes：所选视图的布局属性。

![](.\pictures\卡顿分析与渲染优化\Layout Inspector.png)


#### 选择视图

如要选择某个视图，请在 Component Tree 或 Layout Display 中点击该视图。所选视图的所有布局属性都会显示在 Attributes 面板中。

如果布局包含重叠的视图，您可以选择不在最前面的视图，方法是在 Component Tree 中点击该视图，或者旋转布局并点击所需视图。

#### 隔离视图

如要使用复杂的布局，您可以隔离各个视图，以便只有布局的一部分显示在 Component Tree 中并呈现在 Layout Display 中。

如要隔离某个视图，请在 Component Tree 中右键点击该视图，然后选择 Show Only Subtree 或 Show Only Parent。

如需返回完整视图，请右键点击该视图，然后选择 Show All。

#### 隐藏布局边框和视图标签

如需隐藏布局元素的边界框或视图标签，请点击 Layout Display 顶部的 View Options 图标 实时布局检查器视图选项图标，然后切换 Show Borders 或 Show View Label。

#### 将应用布局与参考图像叠加层进行比较

如需将应用布局与参考图像（如界面模型）进行比较，您可以在布局检查器中加载位图图像叠加层。

- 如需加载叠加层，请点击布局检查器顶部的 Load Overlay 图标 。系统会缩放叠加层以适合布局。
- 如需调整叠加层的透明度，请使用 Overlay Alpha 滑块。
- 如需移除叠加层，请点击 Clear Overlay 图标 。

#### 实时布局检查器

实时布局检查器可以在应用被部署到搭载 API 级别 29 或更高版本的设备或模拟器时，提供应用界面的完整实时数据分析。

如需启用实时布局检查器，请依次转到 File > Settings > Experimental，勾选 Enable Live Layout Inspector 旁边的框，然后点击 Layout Display 上方 Live updates 旁边的复选框。

实时布局检查器包含动态布局层次结构，可随着设备上视图的变化更新 Component Tree 和 Layout Display。

<img src=".\pictures\卡顿分析与渲染优化\Live updates.png" style="zoom:80%;" />

最后，Layout Display 可在运行时对应用的视图层次结构进行高级 3D 可视化。如需使用该功能，只需在实时布局检查器窗口中点击相应布局，然后拖动鼠标旋转该布局即可。如需展开或收起布局的图层，请使用 Layer Spacing 滑块。

![](.\pictures\卡顿分析与渲染优化\3D 可视化.png)


#### 布局验证

“布局验证”是一款可视化工具，用于同时预览不同设备中及采用不同配置的布局，有助于您在此过程的早期发现布局存在的问题。如需使用该功能，请点击 IDE 窗口右上角的 Layout Validation 标签页：

注意：要双击打开一个Component Tree，才会出现 Layout Validation选项

![](.\pictures\卡顿分析与渲染优化\布局验证.png)

如需在可用的配置集之间切换，请从“Layout Validation”窗口顶部的下拉列表中选择以下某个配置：

- Pixel Devices
- 自定义
- 色盲
- 字体大小

![](.\pictures\卡顿分析与渲染优化\layout-validation-dropdown.gif)

##### Pixel Devices

预览布局在 Pixel 设备上的显示效果：

![](.\pictures\卡顿分析与渲染优化\layout-validation-pixel-devices.gif)

##### 自定义

如需自定义要预览的显示配置，请从各种设置（包括语言、设备或屏幕方向）中进行选择：

![](.\pictures\卡顿分析与渲染优化\layout-validation-custom.gif)

##### 色盲

为了方便色盲用户使用您的应用，请通过常见色盲类型的模拟验证布局：

![](.\pictures\卡顿分析与渲染优化\layout-validation-color-blind.png)

##### 字体大小

验证各种字体大小下的布局，并通过使用较大的字体测试布局，改进适用于视力障碍用户的无障碍功能：

![](.\pictures\卡顿分析与渲染优化\layout-validation-font-sizes.png)


### merge标签

#### Merge的作用

merge标签是用来帮助在视图树中减少重复布局的，当一个layout包含另外一个layout时。

#### 示例

- 不使用merge

layout1.xml

```xml
<FrameLayout>
   <include layout="@layout/layout2"/>
</FrameLayout>
```

layout2.xml

```xml
<FrameLayout>
   <TextView />
</FrameLayout>
```

实际效果：

```xml
<FrameLayout>
   <FrameLayout>
      <TextView />
   </FrameLayout>
</FrameLayout>
```

- 使用merge

layout1.xml

```xml
<FrameLayout>
   <include layout="@layout/layout2"/>
</FrameLayout>
```

layout2.xml

```xml
<merge>
   <TextView />
</merge>
```

实际效果

```xml
<FrameLayout>
   <TextView />
</FrameLayout>
```

#### 要点

- merge必须放在布局文件的根节点上。
- merge并不是一个ViewGroup，也不是一个View，它相当于声明了一些视图，等待被添加。
- merge标签被添加到A容器下，那么merge下的所有视图将被添加到A容器下。
- 因为merge标签并不是View，所以在通过LayoutInflate.inflate方法渲染的时候， 第二个参数必须指定一个父容器，且第三个参数必须为true，也就是必须为merge下的视图指定一个父亲节点。
- 因为merge不是View，所以对merge标签设置的所有属性都是无效的。
- 在AS中无法预览怎么办？使用parentTag指定被装在的parent的布局容器类型，例如 tools:parentTag="android.widget.FrameLayout"，那么就可以预览到当前布局被装在进FrameLayout时候的效果。


### ViewStub

#### 问题
在开发应用程序的时候，经常会遇到这样的情况，会在运行时动态根据某个条件来决定显示哪个View或者某个布局。
最常见的做法就是把可能用到的View都写在上面，先把它们的可见性都设为View.GONE，然后在代码中动态的更改它的可见性。
这样的做法的优点是逻辑简单而且控制起来比较灵活。但是它的缺点是耗费资源。
虽然把View的初始可见性设置为View.GONE，但是在Inflate布局的时候View仍然会被Inflate，也就是说仍然会创建对象，也就是说会耗费内存等资源。

#### 解决方案
推荐的做法是使用android.view.ViewStub，ViewStub是一个轻量级的View，它一个看不见的，不占布局位置、占用资源非常小的控件。

可以为ViewStub指定一个布局，在Inflate整个布局文件时，只有ViewStub会被初始化，然后当ViewStub被设置为可见的时候，或是调用了ViewStub.inflate()的时候，ViewStub所指向的布局才会被Inflate和实例化，并且ViewStub的布局属性都会传给它所指向的布局。

#### 示例

![](.\pictures\卡顿分析与渲染优化\ViewStub 示例.png)

ViewStubTestActivity

```kotlin
/**
 * ViewStub 的使用示例
 */
class ViewStubTestActivity : AppCompatActivity() {

    private val binding: ActivityViewStubTestBinding by lazy {
        ActivityViewStubTestBinding.inflate(layoutInflater)
    }
    private var hintText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            btnVsShow.setOnClickListener {
                // inflate 方法只能被调用一次，因为调用后viewStub对象就被移除了视图树；
                // 所以，如果此时再次点击显示按钮，就会崩溃，错误信息：ViewStub must have a non-null ViewGroup view Parent；
                // 所以使用try catch ,当此处发现exception 的时候，在catch中使用setVisibility()重新显示
                try {
                    val viewStubLayout: View = viewStub.inflate() //inflate 方法只能被调用一次，
                    hintText = viewStubLayout.findViewById(R.id.textView)
                    //hintText.setText("没有相关数据，请刷新");
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    viewStub.visibility = View.VISIBLE
                } finally {
                    hintText!!.text = "没有相关数据，请刷新"
                }
            }
            btnVsHide.setOnClickListener {
                viewStub.visibility = View.INVISIBLE
            }
            btnVsChangeHint.setOnClickListener {
                if (hintText != null) {
                    hintText!!.text = "网络异常，无法刷新，请检查网络"
                }
            }
        }
    }
}
```

activity_view_stub_test.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:id="@+id/btn_vs_show"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="显示ViewStub" />

        <Button
            android:id="@+id/btn_vs_changeHint"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="更改ViewStub" />

        <Button
            android:id="@+id/btn_vs_hide"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_weight="1"
            android:text="隐藏ViewStub" />
    </LinearLayout>


    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="正在加载。。。" />

    <!--
        android:inflatedId 指定了懒加载视图根节点的ID
    -->
    <ViewStub
        android:id="@+id/viewStub"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:inflatedId="@+id/viewStubTree"
        android:layout="@layout/layout_view_stub" />

</RelativeLayout>
```

layout_view_stub.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/beauty" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="30dp"
        android:gravity="center" />
</LinearLayout>

```

### 过度渲染

过度绘制就是在绘制界面时，对同一个像素重复绘制了多次，但是用户能够看到的也只有最顶层绘制的内容，GPU过度绘制或多或少对性能有些影响。

#### GPU 过度绘制检查

手机开发者选项中能够显示过度渲染检查功能，通过对界面进行彩色编码来帮我们识别过度绘制。开启步骤如下：
- 进入开发者选项 (Developer Options)。 
- 找到调试 GPU 过度绘制(Debug GPU overdraw)。 
- 在弹出的对话框中，选择显示过度绘制区域（Show overdraw areas）。

Android 将按如下方式为界面元素着色，以确定过度绘制的次数：

<img src=".\pictures\卡顿分析与渲染优化\过度绘制的次数.png" style="zoom:80%;" />

请注意，这些颜色是半透明的，因此我们在屏幕上看到的确切颜色取决于界面内容。
有些过度绘制是不可避免的。在优化应用的界面时，应尝试达到大部分显示真彩色或仅有 1 次过度绘制（蓝色）的视觉效果。


#### 解决过度绘制问题
可以采取以下几种策略来减少甚至消除过度绘制：

- 移除布局中不需要的背景

    - [x] 默认情况下，布局没有背景，这表示布局本身不会直接渲染任何内容。但是，当布局具有背景时，其有可能会导致过度绘制。
    - [x] 移除不必要的背景可以快速提高渲染性能。不必要的背景可能永远不可见，因为它会被应用在该视图上绘制的任何其他内容完全覆盖。例如，当系统在父视图上绘制子视图时，可能会完全覆盖父视图的背景。

- 使视图层次结构扁平化

    - [x] 可以通过优化视图层次结构来减少重叠界面对象的数量，从而提高性能。
    
- 降低透明度
    - [x] 对于不透明的 view ，只需要渲染一次即可把它显示出来。但是如果这个 view 设置了 alpha 值，则至少需要渲染两次。这是因为使用了 alpha 的 view 需要先知道混合 view 的下一层元素是什么，然后再结合上层的 view 进行Blend混色处理。透明动画、淡入淡出和阴影等效果都涉及到某种透明度，这就会造成了过度绘制。可以通过减少要渲染的透明对象的数量，来改善这些情况下的过度绘制。例如，如需获得灰色文本，可以在 TextView中绘制黑色文本，再为其设置半透明的透明度值。但是，简单地通过用灰色绘制文本也能获得同样的效果，而且能够大幅提升性能。


# ANR 问题

## 什么是ANR？
ANR(Application Not responding)，是指应用程序未响应，Android系统对于一些事件需要在一定的时间范围内完成，如果超过预定时间能未能得到有效响应或者响应时间过长，都会造成ANR。一般地，这时往往会弹出一个提示框，告知用户当前xxx未响应，用户可选择继续等待或者Force Close。

那么哪些场景会造成ANR呢？

- KeyDispatchTimeout（常见）
    - [x] input事件在5S内没有处理完成发生了ANR。
    - [x] logcat日志关键字：Input event dispatching timed out
    
- ServiceTimeout
    - [x] 前台Service：onCreate，onStart，onBind等生命周期在20s内没有处理完成发生ANR。
    - [x] 后台Service：onCreate，onStart，onBind等生命周期在200s内没有处理完成发生ANR
    - [x] logcat日志关键字：Timeout executing service

- BroadcastTimeout
    - [x] 前台Broadcast：onReceiver在10S内没有处理完成发生ANR。
    - [x] 后台Broadcast：onReceiver在60s内没有处理完成发生ANR。
    - [x] logcat日志关键字：Timeout of broadcast BroadcastRecord

- ContentProviderTimeout
    - [x] ContentProvider 在10S内没有处理完成发生ANR。 
    - [x] logcat日志关键字：timeout publishing content providers

## ANR原理

发生ANR时会调用AppNotRespondingDialog.show()方法弹出对话框提示用户，该对话框的依次调用关系：

```java
AppErrors.appNotResponding();  //ANR对话框的唯一入口
 
AMS.UiHandler.sendMessage(ActivityManagerService.SHOW_NOT_RESPONDING_UI_MSG);
 
AppErrors.handleShowAnrUi();
 
AppNotRespondingDialog.show();
```

根据造成ANR的场景，产生ANR的来源可以总结为两大类：**组件类ANR**和**Input ANR**。

对于Service、Broadcast、Provider组件类的 ANR 而言，ANR是一套监控 Android 应用响应是否及时的机制，可以把发生 ANR 比作是引爆炸弹，那么整个流程包含三部分组成：

- **埋炸弹**：中控系统(system_server进程)启动倒计时，在规定时间内如果目标(应用进程)没有干完所有的活，则中控系统会定向炸毁(杀进程)目标。
- **拆炸弹**：在规定的时间内干完工地的所有活，并及时向中控系统报告完成，请求解除定时炸弹，则幸免于难。
- **引爆炸弹**：中控系统立即封装现场，抓取快照，搜集目标执行慢的罪证(traces)，便于后续调试分析，最后是炸毁目标。

## Service 超时机制
下面来看看埋炸弹与拆炸弹在整个服务启动(startService)过程所处的环节。

<img src=".\pictures\ANR\Service 超时机制.png" style="zoom:80%;" />

图解1：

1. 客户端(App进程)向中控系统(system_server进程)发起启动服务的请求
2. 中控系统派出一名空闲的通信员(binder_1线程)接收该请求，紧接着向组件管家(ActivityManager线程)发送消息，埋下定时炸弹
3. 通讯员1号(binder_1)通知工地(service所在进程)的通信员准备开始干活
4. 通讯员3号(binder_3)收到任务后转交给包工头(main主线程)，加入包工头的任务队列(MessageQueue)
5. 包工头经过一番努力干完活(完成service启动的生命周期)，然后等待SharedPreferences(简称SP)的持久化；
6. 包工头在SP执行完成后，立刻向中控系统汇报工作已完成
7. 中控系统的通讯员2号(binder_2)收到包工头的完工汇报后，立刻拆除炸弹。如果在炸弹倒计时结束前拆除炸弹则相安无事，否则会引发爆炸(触发ANR)


## Broadcast 超时机制

Broadcast 跟 Service 超时机制大抵相同，对于静态注册的广播在超时检测过程需要检测SP，如下图所示。

<img src=".\pictures\ANR\Broadcast 超时机制.png" style="zoom:80%;" />

图解2： 

1. 客户端(App进程)向中控系统(system_server进程)发起发送广播的请求
2. 中控系统派出一名空闲的通信员(binder_1)接收该请求转交给组件管家(ActivityManager线程)
3. 组件管家执行任务(processNextBroadcast方法)的过程埋下定时炸弹
4. 组件管家通知工地(receiver所在进程)的通信员准备开始干活
5. 通讯员3号(binder_3)收到任务后转交给包工头(main主线程)，加入包工头的任务队列(MessageQueue)
6. 包工头经过一番努力干完活(完成receiver启动的生命周期)，发现当前进程还有SP正在执行写入文件的操作，便将向中控系统汇报的任务交给SP工人(queued-work-looper线程) 
7. SP工人历经艰辛终于完成SP数据的持久化工作，便可以向中控系统汇报工作完成
8. 中控系统的通讯员2号(binder_2)收到包工头的完工汇报后，立刻拆除炸弹。如果在倒计时结束前拆除炸弹则相安无事，否则会引发爆炸(触发ANR)

（说明：SP从8.0开始采用名叫“queued-work-looper”的handler线程，在老版本采用newSingleThreadExecutor创建的单线程的线程池）

如果是动态广播，或者静态广播没有正在执行持久化操作的SP任务，则不需要经过“queued-work-looper”线程中转，而是直接向中控系统汇报，流程更为简单，如下图所示：

<img src=".\pictures\ANR\Broadcast 超时机制2.png" style="zoom:80%;" />

可见，只有XML静态注册的广播超时检测过程会考虑是否有SP尚未完成，动态广播并不受其影响。SP的apply将修改的数据项更新到内存，然后再异步同步数据到磁盘文件，因此很多地方会推荐在主线程调用采用apply方式，避免阻塞主线程，但静态广播超时检测过程需要SP全部持久化到磁盘，如果过度使用apply会增大应用ANR的概率，Google这样设计的初衷是针对静态广播的场景下，保障进程被杀之前一定能完成SP的数据持久化。因为在向中控系统汇报广播接收者工作执行完成前，该进程的优先级为Foreground级别，高优先级下进程不但不会被杀，而且能分配到更多的CPU时间片，加速完成SP持久化。


## ContentProvider 超时机制

Provider 的超时是在 Provider 进程首次启动的时候才会检测，当 Provider 进程已启动的场景，再次请求 Provider 并不会触发 Provider 超时。

<img src=".\pictures\ANR\ContentProvider 超时机制.png" style="zoom:80%;" />

图解3： 
1. 客户端(App进程)向中控系统(system_server进程)发起获取内容提供者的请求
2. 中控系统派出一名空闲的通信员(binder_1)接收该请求，检测到内容提供者尚未启动，则先通过zygote孵化新进程
3. 新孵化的provider进程向中控系统注册自己的存在
4. 中控系统的通信员2号接收到该信息后，向组件管家(ActivityManager线程)发送消息，埋下炸弹
5. 通信员2号通知工地(provider进程)的通信员准备开始干活
6. 通讯员4号(binder_4)收到任务后转交给包工头(main主线程)，加入包工头的任务队列(MessageQueue)
7. 包工头经过一番努力干完活(完成provider的安装工作)后向中控系统汇报工作已完成
8. 中控系统的通讯员3号(binder_3)收到包工头的完工汇报后，立刻拆除炸弹。如果在倒计时结束前拆除炸弹则相安无事，否则会引发爆炸(触发ANR)


## Input 超时机制

Input 的超时检测机制跟 Service、Broadcast、Provider 截然不同，为了更好的理解 Input 过程先来介绍两个重要线程的相关工作：

- InputReader线程负责通过EventHub(监听目录/dev/input)读取输入事件，一旦监听到输入事件则放入到 InputDispatcher的mInBoundQueue队列，并通知其处理该事件；
- InputDispatcher线程负责将接收到的输入事件分发给目标应用窗口，分发过程使用到3个事件队列：
    - [x] mInBoundQueue用于记录InputReader发送过来的输入事件；
    - [x] outBoundQueue用于记录即将分发给目标应用窗口的输入事件；
    - [x] waitQueue用于记录已分发给目标应用，且应用尚未处理完成的输入事件；

Input 的超时机制并非时间到了一定就会爆炸，而是处理后续上报事件的过程才会去检测是否该爆炸，所以更像是扫雷的过程，具体如下图所示。

<img src=".\pictures\ANR\Input 超时机制.png" style="zoom:80%;" />

图解4： 

1. InputReader线程通过EventHub监听底层上报的输入事件，一旦收到输入事件则将其放至mInBoundQueue队列，并唤醒InputDispatcher线程
2. InputDispatcher开始分发输入事件，设置埋雷的起点时间。先检测是否有正在处理的事件(mPendingEvent)，如果没有则取出mInBoundQueue队头的事件，并将其赋值给mPendingEvent，且重置ANR的timeout；则不会从mInBoundQueue中取出事件，也不会重置timeout。然后检查窗口是否就绪(checkWindowReadyForMoreInputLocked)，满足以下任一情况，则会进入扫雷状态(检测前一个正在处理的事件是否超时)，终止本轮事件分发，否则继续执行步骤3。
    - [x] 对于按键类型的输入事件，则outboundQueue或者waitQueue不为空。
    - [x] 对于非按键的输入事件，则waitQueue不为空，且等待队头时间超时500ms。
3. 当应用窗口准备就绪，则将mPendingEvent转移到outBoundQueue队列
4. 当outBoundQueue不为空，且应用管道对端连接状态正常，则将数据从outboundQueue中取出事件，放入waitQueue队列
5. InputDispatcher通过socket告知目标应用所在进程可以准备开始干活
6. App在初始化时默认已创建跟中控系统双向通信的socketpair，此时App的包工头(main线程)收到输入事件后，会层层转发到目标窗口来处理
7. 包工头完成工作后，会通过socket向中控系统汇报工作完成，则中控系统会将该事件从waitQueue队列中移除。

Input 超时机制为什么是扫雷，而非定时爆炸呢？是由于对于 Input 来说即便某次事件执行时间超过timeout时长，只要用户后续在没有再生成输入事件，则不会触发ANR。这里的扫雷是指当前输入系统中正在处理着某个耗时事件的前提下，后续的每一次 Input 事件都会检测前一个正在处理的事件是否超时（进入扫雷状态），检测当前的时间距离上次输入事件分发时间点是否超过timeout时长。如果前一个输入事件，则会重置ANR的timeout，从而不会爆炸。

## ANR 超时阈值

不同组件的超时阈值各有不同，关于 Service、Broadcast、ContentProvider 以及 Input 的超时阈值如下表：

<img src=".\pictures\ANR\ANR 超时阈值.png" style="zoom:80%;" />


## 前台与后台服务的区别

系统对前台服务启动的超时为20s，而后台服务超时为200s，那么系统是如何区别前台还是后台服务呢？来看看ActiveServices的核心逻辑：

```java
ComponentName startServiceLocked(...) {	
    final boolean callerFg;	
    if (caller != null) {	
        final ProcessRecord callerApp = mAm.getRecordForAppLocked(caller);	
        callerFg = callerApp.setSchedGroup != ProcessList.SCHED_GROUP_BACKGROUND;	
    } else {	
        callerFg = true;	
    }	
    ...	
    ComponentName cmp = startServiceInnerLocked(smap, service, r, callerFg, addToStarting);	
    return cmp;	
}
```

在startService过程根据发起方进程callerApp所属的进程调度组来决定被启动的服务是属于前台还是后台。当发起方进程不等于ProcessList.SCHEDGROUPBACKGROUND(后台进程组)则认为是前台服务，否则为后台服务，并标记在ServiceRecord的成员变量createdFromFg。

什么进程属于 SCHEDGROUPBACKGROUND 调度组呢？进程调度组大体可分为TOP、前台、后台，进程优先级(Adj)和进程调度组(SCHED_GROUP)算法较为复杂，其对应关系可粗略理解为Adj等于0的进程属于Top进程组，Adj等于100或者200的进程属于前台进程组，Adj大于200的进程属于后台进程组。关于Adj的含义见下表，简单来说就是Adj>200的进程对用户来说基本是无感知，主要是做一些后台工作，故后台服务拥有更长的超时阈值，同时后台服务属于后台进程调度组，相比前台服务属于前台进程调度组，分配更少的CPU时间片。

<img src=".\pictures\ANR\Android进程优先级ADJ.png" style="zoom:80%;" />

**前台服务准确来说，是指由处于前台进程调度组的进程发起的服务。**这跟常说的fg-service服务有所不同，fg-service是指挂有前台通知的服务。

## 前台与后台广播超时

前台广播超时为10s，后台广播超时为60s，那么如何区分前台和后台广播呢？来看看AMS的核心逻辑：

```java
BroadcastQueue broadcastQueueForIntent(Intent intent) {	
    final boolean isFg = (intent.getFlags() & Intent.FLAG_RECEIVER_FOREGROUND) != 0;	
    return (isFg) ?mFgBroadcastQueue :mBgBroadcastQueue;	
}	
mFgBroadcastQueue = new BroadcastQueue(this, mHandler,	"foreground", BROADCAST_FG_TIMEOUT, false);	
mBgBroadcastQueue = new BroadcastQueue(this, mHandler,	"background", BROADCAST_BG_TIMEOUT, true);
```

根据发送广播sendBroadcast(Intent intent)中的intent的flags是否包含FLAGRECEIVERFOREGROUND来决定把该广播是放入前台广播队列或者后台广播队列，前台广播队列的超时为10s，后台广播队列的超时为60s，默认情况下广播是放入后台广播队列，除非指明加上FLAGRECEIVERFOREGROUND标识。

后台广播比前台广播拥有更长的超时阈值，同时在广播分发过程遇到后台service的启动(mDelayBehindServices)会延迟分发广播，等待service的完成，因为等待service而导致的广播ANR会被忽略掉；后台广播属于后台进程调度组，而前台广播属于前台进程调度组。简而言之，后台广播更不容易发生ANR，同时执行的速度也会更慢。

另外，只有串行处理的广播才有超时机制，因为接收者是串行处理的，前一个receiver处理慢，会影响后一个receiver；并行广播通过一个循环一次性向所有的receiver分发广播事件，所以不存在彼此影响的问题，则没有广播超时。

**前台广播准确来说，是指位于前台广播队列的广播。**

## 前台与后台ANR

除了前台服务，前台广播，还有前台ANR可能会让你云里雾里的，来看看其中核心逻辑：

```java
final void appNotResponding(...) {	
    ...	
    synchronized (mService) {	
        isSilentANR = !showBackground && !isInterestingForBackgroundTraces(app);	
        ...	
    }	
    ...	
    File tracesFile = ActivityManagerService.dumpStackTraces(	
            true, firstPids,	
            (isSilentANR) ?null :processCpuTracker,	
            (isSilentANR) ?null :lastPids,	
            nativePids);	
    synchronized (mService) {	
        if (isSilentANR) {	
            app.kill("bg anr", true);	
            return;	
        }	
        ...	
        //弹出ANR选择的对话框	
        Message msg = Message.obtain();	
        msg.what = ActivityManagerService.SHOW_NOT_RESPONDING_UI_MSG;	
        msg.obj = new AppNotRespondingDialog.Data(app, activity, aboveSystem);	
        mService.mUiHandler.sendMessage(msg);	
    }	
}
```

决定是前台或者后台ANR取决于该应用发生ANR时对用户是否可感知，比如拥有当前前台可见的activity的进程，或者拥有前台通知的fg-service的进程，这些是用户可感知的场景，发生ANR对用户体验影响比较大，故需要弹框让用户决定是否退出还是等待，如果直接杀掉这类应用会给用户造成莫名其妙的闪退。

后台ANR相比前台ANR，只抓取发生无响应进程的trace，也不会收集CPU信息，并且会在后台直接杀掉该无响应的进程，不会弹框提示用户。

**前台ANR准确来说，是指对用户可感知的进程发生的ANR。**

## ANR 分析

对于Service、Broadcast、Provider、Input发生ANR后，中控系统会马上去抓取现场的信息，用于调试分析。收集的信息包括如下：

- 将amanr信息输出到EventLog，也就是说ANR触发的时间点最接近的就是EventLog中输出的amanr信息
- 收集以下重要进程的各个线程调用栈trace信息，保存在data/anr/traces.txt文件
    - [x] 当前发生ANR的进程，system_server进程以及所有persistent进程
    - [x] audioserver, cameraserver, mediaserver, surfaceflinger等重要的native进程
    - [x] CPU使用率排名前5的进程
- 将发生ANR的reason以及CPU使用情况信息输出到main log
- 将traces文件和CPU使用情况信息保存到dropbox，即data/system/dropbox目录
- 对用户可感知的进程则弹出ANR对话框告知用户，对用户不可感知的进程发生ANR则直接杀掉

整个ANR信息收集过程比较耗时，其中抓取进程的trace信息，每抓取一个等待200ms，可见persistent越多，等待时间越长。关于抓取trace命令，对于Java进程可通过在adb shell环境下执行kill -3 [pid]可抓取相应pid的调用栈；对于Native进程在adb shell环境下执行debuggerd -b [pid]可抓取相应pid的调用栈。对于ANR问题发生后的蛛丝马迹(trace)在traces.txt和dropbox目录中保存记录。

有了现场信息，可以调试分析，先定位发生ANR时间点，然后查看trace信息，接着分析是否有耗时的message、binder调用，锁的竞争，CPU资源的抢占，以及结合具体场景的上下文来分析，调试手段就需要针对前面说到的message、binder、锁等资源从系统角度细化更多debug信息。

- 作为应用开发者应让主线程尽量只做UI相关的操作，避免耗时操作，比如过度复杂的UI绘制，网络操作，文件IO操作；
- 避免主线程跟工作线程发生锁的竞争，减少系统耗时binder的调用，谨慎使用sharePreference，注意主线程执行provider query操作。
- 简而言之，尽可能减少主线程的负载，让其空闲待命，以期可随时响应用户的操作。


### 什么情况下会出现ANR

- 主线程阻塞
- 无法建立 binder 通信
- 系统资源不足（CPU、内存）


## 线上监控方案

### WatchDog
WatchDog 创建一个监测线程，该线程不断往UI线程post一个任务，然后睡眠固定时间，等该线程重新起来后检测之前post的任务是否执行了，如果任务未被执行，则判断出现 ANR 问题。

<img src=".\pictures\ANR\WatchDog ANR监控.png" style="zoom:80%;" />

```kotlin
package com.dongnaoedu.anr

import android.os.*
import android.util.Log
import java.lang.StringBuilder

/**
 * 一个监听 ANR 的线程
 */
object ANRWatchDog : Thread("ANR-WatchDog-Thread") {
    // 超时即为ANR
    private const val mTimeout = 5000L
    private const val mIgnoreDebugger = true
    private val mHandler = Handler(Looper.getMainLooper())

    // ANR 发生时执行的回调函数，没有指定时会使用默认实现
    private lateinit var mOnANRHappened: (stackTraceInfo: String) -> Unit
    private val mBlockChecker = BlockChecker(this)

    // Kotlin的超类为Any，不再提供 wait、notify等线程通信方法，把一个 Object 对象当做锁来用
    private val lock: Object = Object()

    /**
     * 一个检查主线程是否被阻塞的线程任务
     */
    private class BlockChecker(val mANRWatchDog: ANRWatchDog) : Runnable {
        private var mCompleted = false
        private var mStartTime = 0L
        private var mExecuteTime = 0L
        override fun run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            // 执行这里代表消息被 MainLooper 从消息队列中取出，并执行 Runnable 任务
            mCompleted = true
            // 任务执行的时间
            mExecuteTime = SystemClock.uptimeMillis()
            Log.d("ning","BlockChecker run mCompleted is:$mCompleted")
        }

        /**
         * 把任务发送给MainLooper，准备执行任务，但是任务真正执行要等到主线程没有被阻塞
         */
        fun schedule() {
            mCompleted = false
            Log.d("ning","BlockChecker schedule mCompleted is:$mCompleted")
            mStartTime = SystemClock.uptimeMillis()
            // 立即发送Message到队列，而且是放在队列的最前面
            mHandler.postAtFrontOfQueue(this)
        }

        // 如果任务被执行，并且等待时间没有超过timeout时间，说明主线程没有被阻塞
        val notBlocked: Boolean
            get() {
                Log.d("ning","BlockChecker notBlocked get completed:$mCompleted, duration:${mExecuteTime - mStartTime}")
                return mCompleted && (mExecuteTime - mStartTime) in 0 until mANRWatchDog.mTimeout
            }
    }

    override fun run() {
        // 设置当前线程为后台线程，执行优先级低于主线程，这样主线程会分配更多执行资源
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
        loop@ while (!isInterrupted) {
            synchronized(lock) {
                // mBlockChecker 发送消息给主线程
                mBlockChecker.schedule()
                try {
                    // 等待 mTimeout 毫秒时间
                    lock.wait(mTimeout)
                    Log.d("ning","ANRWatchDog wait over")
                } catch (e: InterruptedException) {
                    Log.w("ning", e.toString())
                }
            }
            // 检查主线程是否被阻塞，没有则重新开始倒计时
            // 看 mBlockChecker 任务是否被执行，并且没有超时
            if (mBlockChecker.notBlocked) {
                Log.d("ning","notBlocked, ANRWatchDog count again")
                continue
            }
            // 执行到这里，说明已经超时
            // 由于调试模式比较耗时，会拖慢主线程执行速度，所以当处于调试模式下，就有可能会超时
            // 如果忽略调试带来的影响，则重新开始倒计时
            if (Debug.isDebuggerConnected() && !mIgnoreDebugger) {
                continue
            }
            // 执行回调
            mOnANRHappened(stackTraceInfo)
        }
    }

    /**
     * 开始监听
     * @param onANRHappened 指定 ANR 发生时执行的回调函数
     */
    fun start(onANRHappened: (stackTraceInfo: String) -> Unit = {}) {
        this.mOnANRHappened = onANRHappened
        // 启动线程
        start()
    }

    // 获取主线程的堆栈信息
    private val stackTraceInfo: String
        get() {
            val sb = StringBuilder()
            for (stackTraceElement in Looper.getMainLooper().thread.stackTrace) {
                sb.append("$stackTraceElement \r\n")
            }
            return sb.toString()
        }
}
```

```kotlin
// 开启 ANR 监听
ANRWatchDog.start{ stackTraceInfo ->
    Log.d("ning","应用程序没有响应...")
    Log.d("ning",stackTraceInfo.toString())
}

// 关闭 ANR 监听
ANRWatchDog.interrupt()
```


### FileObserver
当ANR发生的时候，通过监听文件文件夹“data/anr/”的写入情况,来判断是否发生了ANR，如果监听到文件写入，说明有此时有ANR异常发生。


# 电量优化

电池续航时间是移动用户体验中最重要的一个方面。没电的设备完全无法使用。因此，对于应用来说，尽可能地考虑电池续航时间是至关重要的。在我们开发时对于单个APP应该注意能够：

- **减少操作**：我们的应用是否存在可删减的多余操作？例如，是否可以缓存已下载的数据，而不是每次重新下载数据？
- **推迟操作**：应用是否需要立即执行某项操作？例如，是否可以等到设备充电后或者Wifi连接时（通常情况下使用移动网络要比WIFI更耗电 ）再将数据备份到云端？
- **合并操作**：工作是否可以批处理，而不是多次将设备置于活动状态？比如和服务器请求不同的接口获取数据，部分接口是否可以合并为一个？


## Doze低电耗模式和StandBy待机模式

从 Android 6.0（API 级别 23）开始，Android 引入了两项省电功能，通过管理应用在设备未连接至电源时的行为方式，帮助用户延长电池寿命。当用户长时间未使用设备时，低电耗模式会延迟应用的后台 CPU 和网络活动，从而降低耗电量。应用待机模式会延迟用户近期未与之交互的应用的后台网络活动。

低电耗模式和应用待机模式管理在 Android 6.0 或更高版本上运行的所有应用的行为，无论它们是否专用于 API 级 别 23。

### Doze低电耗模式
如果设备 未充电、屏幕熄灭、让设备在一段时间内保持不活动状态 ，那么设备就会进入Doze模式。在Doze模式下，系统会尝试通过限制应用访问占用大量网络和 CPU 资源的服务来节省电量。它还会阻止应用访问网络，并延迟其作业、同步和标准闹钟。

Doze中文是打盹，所以系统会定期退出打盹一小段时间，让应用完成其延迟的活动。在此维护期内，系统会运行所有待处理的同步、作业和闹钟，并允许应用访问网络。

![](.\pictures\电量优化\Doze低电耗模式.png)

随着时间的推移，系统安排维护期的次数越来越少，这有助于在设备未连接至充电器的情况下长期处于不活动状态时降低耗电量。

一旦用户通过移动设备、打开屏幕或连接至充电器唤醒设备，系统就会立即退出低电耗模式，并且所有应用都会恢复正常活动。

在低电耗模式下，您的应用会受到以下限制：
- 暂停访问网络。
- 系统忽略PowerManager.WakeLock唤醒锁定。
- 标准 AlarmManager 闹钟（包括 setExact() 和 setWindow() ）推迟到下一个维护期。
    - [x] 如果需要设置在设备处于低电耗模式时触发的闹钟，请使用API 23(6.0)提供的setAndAllowWhileIdle() (一次性闹钟，同set方法)或 setExactAndAllowWhileIdle() （比set方法设置的精度更高，同setExact）。
    - [x] 使用 setAlarmClock() 设置的闹钟将继续正常触发，系统会在这些闹钟触发之前不久退出低电耗模式。
- 系统不执行 WLAN 扫描。
- 系统不允许运行同步适配器AbstractThreadedSyncAdapter (账号同步拉活)。
- 系统不允许运行 JobScheduler 。

setAndAllowWhileIdle() 及 setExactAndAllowWhileIdle() 为每个应用触发闹钟的频率都不能超过每 9分钟一次。

如果应用需要与网络建立持久性连接来接收消息，应尽可能使用 Firebase 云消息传递 (FCM)。如果是国内无法使用Google服务，需要实现如IM功能，需要与手机厂商合作。

### Standby待机模式

应用待机模式允许系统判定应用在用户未主动使用它时是否处于待机状态。当用户有一段时间未触摸应用并且应用没有以下表现，则Android系统就会使应用进入空闲状态

- 应用当前有一个进程在前台运行（作为活动或前台服务，或者正在由其他活动或前台服务使用）。
- 应用生成用户可在锁定屏幕或通知栏中看到的通知。

当用户将设备插入电源时，系统会从待机状态释放应用，允许它们自由访问网络并执行任何待处理的作业和同步。

如果设备长时间处于闲置状态，系统将允许闲置应用访问网络，频率大约每天一次。


## 白名单

系统提供了一个可配置的白名单，将部分免除低电耗模式和应用待机模式优化的应用列入其中。在低电耗模式和应用待机模式期间，列入白名单的应用可以使用网络并保留部分唤醒锁定。不过，列入白名单的应用仍会受到其他限制，就像其他应用一样。例如，列入白名单的应用的作业和同步会延迟（在6.0及以下的设备上），并且其常规 AlarmManager 闹钟不会触发。应用可以调用PowerManager.isIgnoringBatteryOptimizations() 来检查应用当前是否在豁免白名单中。

可以在设置 中的 电池优化手动配置白名单（Apps & notifications -> Special app access -> Battery optimization） 。另外，系统也提供了一些方法，让应用要求用户将其列入白名单。

- 应用可以触发 ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS Intent，让用户直接转到电池优化，以便他们在其中添加该应用。

```java
startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
```

- 具有 REQUEST_IGNORE_BATTERY_OPTIMIZATIONS 权限的应用可以触发一个系统对话框，让用户直接将该应用添加到白名单，而无需转到“设置”。此类应用将通过触发 ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS Intent 来触发该对话框。

```java
Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);

intent.setData(Uri.parse("package:"+getPackageName())); 
startActivity(intent);
```

```xml
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
```

## 在低电耗模式下测试

```
#启用 Doze 
adb shell dumpsys deviceidle enable 
#强制进入doze模式 (同时还需要关闭屏幕) 
adb shell dumpsys deviceidle force-idle 
#退出doze模式 
adb shell dumpsys deviceidle unforce 
#关闭doze 
adb shell dumpsys deviceidle disable 
#重置设备 
adb shell dumpsys battery reset 
#查看doze白名单 
adb shell dumpsys deviceidle whitelist
```

## 在应用待机模式下测试

```
#设置断开充电 
adb shell dumpsys battery unplug 
#进入standby 
adb shell am set-inactive <packageName> true 
#退出standby 
adb shell am set-inactive <packageName> false 
#查看是否处于standby 
adb shell am get-inactive <packageName> 
#重置 
adb shell dumpsys battery reset
```

## 监控电池电量和充电状态

为了减少电池续航被我们软件的影响，我们可以通过检查电池状态以及电量来判断是否进行某些操作。比如我们可以在充电时才进行一些数据上报之类的操作。

### 获取充电状态

```java
IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
Intent batteryStatus = registerReceiver(null, ifilter); 
// 是否正在充电 
int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1); 
boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL; 
// 什么方式充电？ 
int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1); 
//usb 
boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB; 
//充电器 
boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC; 
Log.e(TAG, "isCharging: " + isCharging + " usbCharge: " + usbCharge + " acCharge:" + acCharge);
```

### 监控充电状态变化

```java
//注册广播
IntentFilter ifilter = new IntentFilter();
//充电状态
ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//电量显著变化
ifilter.addAction(Intent.ACTION_BATTERY_LOW); //电量不足
ifilter.addAction(Intent.ACTION_BATTERY_OKAY); //电量从低变回高
powerConnectionReceiver = new PowerConnectionReceiver();
registerReceiver(powerConnectionReceiver, ifilter);
```

```java
public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "充电状态：CONNECTED", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Toast.makeText(context, "充电状态：DISCONNECTED", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            Toast.makeText(context, "电量过低", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {
            Toast.makeText(context, "电量从低变回高", Toast.LENGTH_SHORT).show();
        }
    }
}
```

## WorkManager

WorkManager API 是一个针对原有的 Android 后台调度 API 整合的建议替换组件。

<img src=".\pictures\电量优化\WorkManager.png"  />

如果设备在 API 级别 23 或更高级别上运行，系统会使用 JobScheduler。在 API 级别 14-22 上，系统会使用 GcmNetworkManager（如果可用），否则会使用自定义 AlarmManager 和 BroadcastReciever 实现作为备用。

```java
Constraints constraints = new Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED) //Wi-Fi
        .setRequiresCharging(true) //在设备充电时运行
        .setRequiresBatteryNotLow(true) //电量不足不会运行
        .build();
OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
        .setConstraints(constraints)
        .build();
WorkManager.getInstance(this).enqueueUniqueWork("upload", ExistingWorkPolicy.KEEP, uploadWorkRequest);
```

## BatteryHistorian

Battery Historian是一个可以了解设备随时间的耗电情况的工具。在系统级别，该工具以 HTML 的形式可视化来自系统日志的电源相关事件。在具体应用级别，该工具可提供各种数据，帮助您识别耗电的应用行为。

Battery Historian可以帮助我们查看应用是否具有以下耗电行为：

- 过于频繁地触发唤醒提醒（至少每10秒钟一次）。持续保留 GPS 锁定。
- 至少每 30 秒调度一次作业。
- 至少每 30 秒调度一次同步。
- 使用手机无线装置的频率高于预期。
- 项目地址：https://github.com/google/battery-historian


### 收集数据

- 重置电池数据收集
```
adb shell dumpsys batterystats --reset 
```
- 开启wakeLock唤醒锁信息记录（可选）
```
adb shell dumpsys batterystats --enable full-wake-history 
```
- 断开设备与计算机的连接，以便仅消耗设备电池的电量。
- 使用您的应用并执行您想要获取数据的操作；例如，断开 WLAN 连接并将数据发送到云端。
- 重新连接手机。
- 生成报告
    - 对于搭载 Android 7.0 及更高版本的设备：
    ```
    adb bugreport > E://bugreport.zip
    ```
    - 对于搭载 Android 6.0 及更低版本的设备：
    ```
    adb bugreport > E://bugreport.txt
    ```
- 关闭wakeLock统计：
    ```
    adb shell dumpsys batterystats --disable full-wake-history
    ```
### 报告分析

#### 使用系统级视图

Battery Historian 工具提供各种应用和系统行为的系统级可视化结果，以及它们随时间推移与耗电量的相关性。如图 1 所示，此视图可帮助您诊断和识别应用存在的耗电问题。

![](.\pictures\电量优化\Battery Historian.png)

图 1. Battery Historian 显示了影响功耗的系统级事件。

该图中特别值得注意的是表示电池电量的黑色水平下降趋势线（在 y 轴上进行测量）。例如，在电池电量线的最开始处，大约早上 6:50 时，该图表显示电量出现较为急剧的下降。

![](.\pictures\电量优化\Battery Historian 时间轴特写图.png)

图 2. Battery Historian 时间轴（大约从早上 6:50 到 7:20）的特写图。

在电池电量线的最开始处，随着电池电量的急剧下降，显示画面上显示有三件事正在发生：CPU 正在运行，应用已获取唤醒锁定，且屏幕已打开。通过这种方式，Battery Historian 可帮助您了解耗电量高时正在发生什么事件。然后，您可以针对应用中的这些行为，研究是否可以进行相关优化。


#### 查看具体应用的数据

表格提供了关于您的应用的两个数据维度。首先，您可以查找应用的耗电量与其他应用相比的排名位置。为此，请点击“Tables”下的“Device Power Estimates”表格。

![](.\pictures\电量优化\研究哪些应用的耗电量最大.png)

图 3. 研究哪些应用的耗电量最大。

图 3 中的表格显示，Pug Power 在此设备上的耗电量排名第九，在不属于操作系统的应用中排名第三。这些数据表明此应用需要更深入的研究。

要查找特定应用的数据，在可视化图表左侧下方，在“App Selection”下方的第二个下拉菜单中输入应用的软件包名称。

![](.\pictures\电量优化\输入特定的应用以查看其数据.png)

图 4. 输入特定的应用以查看其数据。

![](.\pictures\电量优化\虚构应用 Pug Power 的数据可视化图表.png)

图 5. 虚构应用 Pug Power 的数据可视化图表。

![](.\pictures\电量优化\虚构应用 Pugle Power 的表格数据.png)

图 6. 虚构应用 Pugle Power 的表格数据。


查看可视化图表并没有发现任何显而易见的问题。JobScheduler 行显示应用没有调度作业。SyncManager 行显示应用尚未执行任何同步操作。

不过，查看表格数据的“唤醒锁定”部分发现，Pug Power 获取了 1 个小时内的唤醒锁定总次数。这种代价高昂的异常行为可能是应用耗电量较高的原因。这条信息有助于开发者专攻优化后可能会大大获益的方面。在这种情况下，我们需要结合代码考虑为什么应用会获得如此多的唤醒锁定时间？是否可以优化？


#### 安装注意

Battery Historian 安装：https://www.jianshu.com/p/378cf678bdeb

Docker与操作系统、Battery Historian 的关系：

<img src=".\pictures\电量优化\Docker与操作系统、Battery Historian 的关系.png" style="zoom:80%;" />


Windows Docker 下载路径：https://hub.docker.com/editions/community/docker-ce-desktop-windows

Docker 对Windows的要求：
- Windows 10 专业版
- 版本不能太旧，版本太旧要先更新 Windows，这个软件更新-wsl_update_x64.msi
- 激活

Windows 10 激活码：https://mswin.njbykjc.cn/

Docker 安装：https://www.runoob.com/docker/windows-docker-install.html

Docker 配置阿里镜像，下载更快
```
"registry-mirrors": [
    "https://tcjput04.mirror.aliyuncs.com"
  ]
```

拉取battery镜像到本地
- docker pull blystad/battery-historian-2

运行battery-historian
- docker run --name=battery -d -p 9999:9999 blystad/battery-historian-2

## WakeLock
### 什么是WakeLock

- WakeLock是一种机制，表示应用需要设备保持运行（stay on）。
- WakeLock默认是引用计数，如果WakeLock是引用计数的话，则调用了几次acquire()方法，就需要调用对应次数的release()方法才能真正释放掉WakeLock。
- 如果WakeLock不是引用计数的话，则调用一次release()函数，就可以释放前面调用多次acquire()获取的WakeLock锁。

### 使用WakeLock

使用WakeLock之前，需要在AndroidManifest.xml中声明android.permission.WAKE_LOCK权限。一般是通过使用PowerManager.newWakeLock（int,String）来获取，并指定一个WakeLock类型：

- FULL_WAKE_LOCK    保持屏幕全亮、键盘背光灯点亮和CPU运行。
- SCREEN_BRIGHT_WAKE_LOCK   保持屏幕全亮和CPU运行。
- SCREEN_DIM_WAKE_LOCK   保持屏幕开启（但是让它变暗）和CPU运行。
- PARTIAL_WAKE_LOCK   保持CPU运行。

```java
PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);  
WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
```

通过调用acquire()方法获取Wake Lock对象，并可以指定一个超时值timeout，在该timeout内保持使用该WakeLock。

```java
PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);  
WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");  
wakeLock.acquire(10000);//timeout = 10s
// do something that require the CPU is active    
wakeLock.release();  
```

### WakeLock的作用

为了延长电池的使用寿命，Android设备会在一段时间后使屏幕变暗，然后关闭屏幕显示，最后停止CPU。WakeLock是一个电源管理系统服务功能，应用程序可以使用它来控制设备的电源状态。

WakeLock可以用来保持CPU运行，避免屏幕变暗和关闭，以及避免键盘背光灯熄灭。

### WakeLock的使用场景

屏幕Wake Lock通常用于在用户观看屏幕但是很少与屏幕进行交互期间（例如，播放视频）防止屏幕变暗。

CPU Wake Lock用于防止设备进入休眠状态，直至执行了一个操作。当服务从意图接收器内启动时常常出现这种情况，因为意图接收器可能在设备休眠期间接收意图。值得注意的是，在这种情况下，系统将在广播接收器的整个onReceive处理程序中使用CPU Wake Lock。


## Energy Profiler

使用 Android 8.0 及以上版本的设备时，使用Energy Profiler 可以了解应用在哪里耗用了不必要的电量。 Energy Profiler 会监控 CPU、网络无线装置和 GPS 传感器的使用情况，并直观地显示其中每个组件消耗的电量。还会显示可能会影响耗电量的系统事件（唤醒锁定、闹钟、作业和位置信息请求）的发生次数。


# 网络优化

正常一条网络请求需要经过的流程是这样：

- DNS 解析，请求DNS服务器，获取域名对应的 IP 地址；
- 与服务端建立连接，包括 tcp 三次握手，安全协议同步流程；
- 连接建立完成，发送和接收数据，解码数据。

这里有明显的三个优化点：
- 直接使用 IP 地址，去除 DNS 解析步骤；
- 不要每次请求都重新建立连接，复用连接或一直使用同一条连接(长连接)；
- 压缩数据，减小传输的数据大小。

## DNS优化

DNS（Domain Name System），它的作用是根据域名查出IP地址，它是HTTP协议的前提，只有将域名正确的解析成IP地址后，后面的HTTP流程才能进行。

DNS 完整的解析流程很长，会先从本地系统缓存取，若没有就到最近的 DNS 服务器取，若没有再到主域名服务器取，每一层都有缓存，但为了域名解析的实时性，每一层缓存都有过期时间。

### DNS 解析的优点
为什么不直接通过IP访问，而是通过域名请求，DNS解析之后再获取IP访问呢？

![](.\pictures\网络优化\DNS解析的好处.png)

### 传统的DNS解析机制的缺点
- 缓存时间设置得长，域名更新不及时，设置得短，大量 DNS 解析请求影响请求速度；
- 域名劫持，容易被中间人攻击，或被运营商劫持，把域名解析到第三方 IP 地址，据统计劫持率会达到7%；
- DNS 解析过程不受控制，无法保证解析到最快的IP；
- 一次请求只能解析一个域名。

<img src=".\pictures\网络优化\传统DNS解析.png" style="zoom:80%;" />


为了解决这些问题，就有了 HTTPDNS，原理很简单，就是自己做域名解析的工作，通过 HTTP 请求后台去拿到域名对应的 IP 地址，直接解决上述所有问题。

HTTPDNS的好处总结就是：
- Local DNS 劫持：由于 HttpDns 是通过 IP 直接请求 HTTP 获取服务器 A 记录地址，不存在向本地运营商询问 domain 解析过程，所以从根本避免了劫持问题。
- DNS 解析由自己控制，可以确保根据用户所在地返回就近的 IP 地址，或根据客户端测速结果使用速度最快的IP；
- 一次请求可以解析多个域名。
- ......

HTTPDNS 几乎成为中大型 APP 的标配。解决了第一个问题， DNS 解析耗时的问题，顺便把DNS 劫持也解决了。

### 阿里云 HttpDNS
https://help.aliyun.com/document_detail/30103.html?spm=a2c4g.11186623.6.543.7eee78bc3kDYhO

## 连接优化

第二个问题，连接建立耗时的问题，这里主要的优化思路是复用连接，不用每次请求都重新建立连接，如何更有效率地复用连接，可以说是网络请求速度优化里最主要的点了。

**keep-alive**： HTTP 协议里有个 keep-alive，HTTP1.1默认开启，一定程度上缓解了每次请求都要进行TCP三次握手建立连接的耗时。原理是请求完成后不立即释放连接，而是放入连接池中，若这时有另一个请求要发出，请求的域名和端口是一样的，就直接拿出连接池中的连接进行发送和接收数据，少了建立连接的耗时。 实际上现在无论是客户端还是浏览器都默认开启了keep-alive，对同个域名不会再有每发一个请求就进行一次建连的情况，纯短连接已经不存在了。

但有 keep-alive 的连接一次只能发送接收一个请求，在上一个请求处理完成之前，无法接受新的请求。若同时发起多个请求，就有两种情况：

- 若串行发送请求，可以一直复用一个连接，但速度很慢，每个请求都要等待上个请求完成再进行发送。
- 若并行发送请求，那么只能每个请求都要进行tcp三次握手建立新的连接。

对并行请求的问题，新一代协议 HTTP2 提出了多路复用去解决。 HTTP2 的多路复用机制一样是复用连接，但它复用的这条连接支持同时处理多条请求，所有请求都可以并发在这条连接上进行，也就解决了上面说的并发请求需要建立多条连接带来的问题。


![](.\pictures\网络优化\HTTP2.png)

多路复用把在连接里传输的数据都封装成一个个stream，每个stream都有标识，stream的发送和接收可以是乱序的，不依赖顺序，也就不会有阻塞的问题，接收端可以根据stream的标识去区分属于哪个请求，再进行数据拼接，得到最终数据。

Android 的开源网络库OKhttp默认就会开启 keep-alive ，并且在Okhttp3以上版本也支持了 HTTP2。

Tomcat 配置：

server.xml

```xml
<Connector port="9999" protocol="org.apache.coyote.http11.Http11NioProtocol"
       maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
       clientAuth="true" sslProtocol="TLS"
       keystoreFile="D:\dev\apache-tomcat-9.0.54\conf\server.keystore" keystorePass="123456"
       truststoreFile="D:\dev\apache-tomcat-9.0.54\conf\server.keystore" truststorePass="123456">
	   <UpgradeProtocol className="org.apache.coyote.http2.Http2Protocol" />
</Connector>
```

证书生成：

生成客户端keystore
- keytool -genkeypair -alias client -keyalg RSA -validity 3650 -keypass 123456 -storepass 123456 -keystore client.jks

生成服务端keystore
- keytool -genkeypair -alias server -keyalg RSA -validity 3650 -keypass 123456 -storepass 123456 -keystore server.keystore

导出客户端证书
- keytool -export -alias client -file client.cer -keystore client.jks -storepass 123456

导出服务端证书
- keytool -export -alias server -file server.cer -keystore server.keystore -storepass 123456

证书交换

生成客户端信任证书库(由服务端证书生成的证书库)：
- keytool -import -v -alias server -file server.cer -keystore truststore.jks -storepass 123456 

将客户端证书导入到服务器证书库(使得服务器信任客户端证书)：
- keytool -import -v -alias client -file client.cer -keystore server.keystore -storepass 123456

用Google浏览器访问，导入证书 client.jks


## 数据压缩

第三个问题，传输数据大小的问题。数据对请求速度的影响分两方面，一是压缩率，二是解压序列化反序列化的速度。目前最流行的两种数据格式是 json 和 protobuf，json 是字符串，protobuf 是二进制，即使用各种压缩算法压缩后，protobuf 仍会比 json 小，数据量上 protobuf 有优势，序列化速度 protobuf 也有一些优势 。

https://github.com/protocolbuffers/protobuf/blob/master/java/lite.md

除了选择不同的序列化方式（数据格式）之外，Http可以对内容（也就是body部分）进行编码，可以采用gzip这样的编码，从而达到压缩的目的。

在OKhttp的 BridgeInterceptor 中会自动为我们开启gzip解压的支持。

```java
boolean transparentGzip = false;
if (userRequest.header("Accept-Encoding") == null && userRequest.header("Range") == null) {
    transparentGzip = true;
    requestBuilder.header("Accept-Encoding", "gzip");
}
```

如果服务器响应头存在： Content-Encodin:gzip

```java
//服务器响应 Content-Encodin:gzip 并且有响应body数据
if (transparentGzip
        && "gzip".equalsIgnoreCase(networkResponse.header("Content-Encoding"))
        && HttpHeaders.hasBody(networkResponse)) {
    GzipSource responseBody = new GzipSource(networkResponse.body().source());
    Headers strippedHeaders = networkResponse.headers()
            .newBuilder()
            .removeAll("Content-Encoding")
            .removeAll("Content-Length").build();
    responseBuilder.headers(strippedHeaders);
    String contentType = networkResponse.header("Content-Type");
    responseBuilder.body(new RealResponseBody(contentType, -1L, Okio.buffer(responseBody)));
}
```

客户端也可以发送压缩数据给服务端，通过代码将请求数据压缩，并在请求中加入 Content-Encodin:gzip 即可。

```java
private RequestBody gzip(final RequestBody body) {
    return new RequestBody() {
        @Override
        public MediaType contentType() {
            return body.contentType();
        }

        @Override
        public long contentLength() {
            return -1;
            // We don't know the compressed length in advance! }
            @Override public void writeTo (BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        }
    }
}

public RequestBody getGzipRequest(String body) {
    RequestBody request = null;
    try {
        request = RequestBody.create(MediaType.parse("application/octet-stream"), compress(body));
    } catch (IOException e) {

        e.printStackTrace();
    }
    return request;
}
```

### Ptotobuf
Protobuf是一种平台无关、语言无关、可扩展且轻便高效的序列化数据结构的协议，可以用于网络通信和数据存储。 可简单类比于 XML ，其具有以下特点：
- 语言无关、平台无关。即 ProtoBuf 支持 Java、C++、Python 等多种语言，支持多个平台
- 高效，即比 XML 更小（3 ~ 10倍）、更快（20 ~ 100倍）、更为简单
- 扩展性、兼容性好。你可以更新数据结构，而不影响和破坏原有的旧程序

#### 引入插件

```gradle
plugins {
    //...
    id "com.google.protobuf" version "0.8.17"
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }

    generateProtoTasks {
        all().each { task ->
            task.builtins {
                java {
                    option 'lite'
                }
            }
        }
    }
}

dependencies {
    implementation  "com.google.protobuf:protobuf-javalite:3.18.0"
}
```

#### 安装IDEA插件并编写proto文件

<img src=".\pictures\网络优化\proto 插件.png" style="zoom:80%;" />

在Java同级目录下创建proto目录

![](.\pictures\网络优化\proto 目录.png)


编写settings.proto文件

```protobuf
syntax = "proto3";

option java_package = "com.dongnaoedu.protoexamples.proto";
option java_multiple_files = true;

message Settings {
    string language = 1;
    int32 code = 404;
}
```


#### 执行编译

Run Generate Sources Gradle Tasks

#### 开始使用

```kotlin

fun main() {
    // json 格式
    println("{language:'English',code:404}".toByteArray().size)
    val settings = Settings.newBuilder().setLanguage("English").setCode(404).build()
    val byteArray = settings.toByteArray()
    // Protobuf 与 json 格式对比尺寸大小
    println("size:${byteArray.size}")
    // 序列化
    serialize(byteArray)

    // 反序列化
    println("反序列化")
    val settings2: Settings = Settings.parseFrom(deserialize())
    println(settings2)
}


/**
 * 序列化
 */
fun serialize(byteArray: ByteArray) {
    var os: OutputStream? = null
    try {
        os = BufferedOutputStream(FileOutputStream(File("E://settings.pb")))
        os.write(byteArray)
    } finally {
        os?.close()
    }
}

/**
 * 反序列化
 */
fun deserialize(): ByteArray {
    var bis: InputStream? = null
    return try {
        bis = BufferedInputStream(FileInputStream(File("E://settings.pb")))
        bis.readAllBytes()
    } finally {
        bis?.close()
    }
}
```

#### 混淆配置
```gradle
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
```

#### 数据结构

<img src=".\pictures\网络优化\protobuf 数据结构.png" style="zoom:80%;" />


### 其他
- 使用webp代替png/jpg
- 不同网络的不同图片下发，如（对于原图是300x300的图片）：
    - 2/3G使用低清晰度图片：使用100X100的图片;
    - 4G再判断信号强度为强则使用使用300X300的图片，为中等则使用200x200，信号弱则使用100x100图片;
    - WiFi网络：直接下发300X300的图片
- http开启缓存 / 首页数据加入缓存


# Crash监控

Crash（应用崩溃）是由于代码异常而导致 App 非正常退出，导致应用程序无法继续使用，所有工作都停止的现象。发生 Crash 后需要重新启动应用（有些情况会自动重启），而且不管应用在开发阶段做得
多么优秀，也无法避免 Crash 发生，特别是在 Android 系统中，系统碎片化严重、各 ROM 之间的差异，甚至系统Bug，都可能会导致Crash的发生。

在 Android 应用中发生的 Crash 有两种类型，Java 层的 Crash 和 Native 层 Crash。这两种Crash 的监控和获取堆栈信息有所不同。

## Java Crash

Java的Crash监控非常简单，Java中的Thread定义了一个接口： UncaughtExceptionHandler ；用于处理未捕获的异常导致线程的终止（注意：catch了的是捕获不到的），当我们的应用crash的时候，就
会走 UncaughtExceptionHandler.uncaughtException ，在该方法中可以获取到异常的信息，我们通过 Thread.setDefaultUncaughtExceptionHandler 该方法来设置线程的默认异常处理器，我们可以将异常信息保存到本地或者是上传到服务器，方便我们快速的定位问题。

```java
package com.dongnaoedu.crashreporter

import android.annotation.SuppressLint
import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

@SuppressLint("StaticFieldLeak")
object JavaCrashHandler : Thread.UncaughtExceptionHandler {
    private lateinit var defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler
    private lateinit var mContext: Context

    /**
     * 发生 Java 层 Crash 会进入此方法
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        val dir = File(mContext.filesDir, "crash_info")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        // 将异常信息打印到日志文件
        val file = File(dir, "${System.currentTimeMillis()}.log")
        try {
            val pw = PrintWriter(FileWriter(file))
            pw.println("thread: " + t.name)
            e.printStackTrace(pw)
            pw.flush()
            pw.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            defaultUncaughtExceptionHandler.uncaughtException(t, e)
        }
    }

    /**
     * 初始化
     */
    fun init(applicationContext: Context) {
        mContext = applicationContext
        Thread.getDefaultUncaughtExceptionHandler().also { defaultUncaughtExceptionHandler = it }
        Thread.setDefaultUncaughtExceptionHandler(this)
    }
}
```

## NDK Crash

相对于Java的Crash，NDK的错误无疑更加让人头疼，特别是对初学NDK的同学，不说监控，就算是错误堆栈都不知道怎么看。

### Linux信号机制
信号机制是Linux进程间通信的一种重要方式，Linux信号一方面用于正常的进程间通信和同步，另一方面它还负责监控系统异常及中断。当应用程序运行异常时，Linux内核将产生错误信号并通知当前进程。当前进程在接收到该错误信号后，可以有三种不同的处理方式。

- 忽略该信号；
- 捕捉该信号并执行对应的信号处理函数（信号处理程序）；
- 执行该信号的缺省操作（如终止进程）；

当Linux应用程序在执行时发生严重错误，一般会导致程序崩溃。其中，Linux专门提供了一类crash信号，在程序接收到此类信号时，缺省操作是将崩溃的现场信息记录到核心文件，然后终止进程。

常见崩溃信号列表：

<img src=".\pictures\Crash监控\常见崩溃信号列表.png" style="zoom:80%;" />

一般的出现崩溃信号，Android系统默认缺省操作是直接退出我们的程序。但是系统允许我们给某一个进程的某一个特定信号注册一个相应的处理函数（signal），即对该信号的默认处理动作进行修改。因此NDK Crash的监控可以采用这种信号机制，捕获崩溃信号执行我们自己的信号处理函数从而捕获NDK Crash。

### 墓碑

Android本机程序本质上就是一个Linux程序，当它在执行时发生严重错误，也会导致程序崩溃，然后产生一个记录崩溃的现场信息的文件，而这个文件在Android系统中就是 tombstones 墓碑文件。


### BreakPad

Google breakpad是一个跨平台的崩溃转储和分析框架和工具集合，其开源地址是：https://github.com/google/breakpad

breakpad在Linux中的实现就是借助了Linux信号捕获机制实现的。因为其实现为C++，因此在Android中使用，必须借助NDK工具。

如果出现NDK Crash，会在我们指定的目录： /data/data/[packageName]/files/native_crash 下生成NDK Crash信息文件。

### Crash解析

采集到的Crash信息记录在minidump文件中。minidump是由微软开发的用于崩溃上传的文件格式。我们可以将此文件上传到服务器完成上报，但是此文件没有可读性可言，要将文件解析为可读的崩溃堆栈
需要按照breakpad文档编译 minidump_stackwalk 工具，而Windows系统编译个人不会。不过好在，无论你是 Mac、windows还是ubuntu在 Android Studio 的安装目录下的 bin\lldb\bin 里面就存在一
个对应平台的 minidump_stackwalk 。

```
minidump_stackwalk xxxx.dump > crash.txt
```

接下来使用 Android NDK 里面提供的 addr2line 工具将寄存器地址转换为对应符号。addr2line 要用和自己 so 的 ABI 匹配的目录，同时需要使用有符号信息的so(一般debug的就有)。

因为我使用的是模拟器x86架构，因此addr2line位于：

Android\Sdk\ndk\21.3.6528147\toolchains\x86-4.9\prebuilt\windows-x86_64\bin\i686-linuxandroid-addr2line.exe

```
i686-linux-android-addr2line.exe -f -C -e libcrashreporter.so 0x1feab
```

libcrashreporter.so 目录：

PerformanceOptimizing\code\CrashReporter\app\build\intermediates\cxx\Debug\701p5a2z\obj\x86


# APK 瘦身

随着业务迭代，apk体积逐渐变大。项目中积累的无用资源，未压缩的图片资源等，都为apk带来了不必要的体积增加。而APK 的大小会影响应用加载速度、使用的内存量以及消耗的电量。

## 了解 APK 结构

在讨论如何缩减应用的大小之前，有必要了解下应用 APK 的结构。APK 文件由一个 Zip 压缩文件组成，其中包含构成应用的所有文件。这些文件包括 Java 类文件、资源文件和包含已编译资源的文件。

APK 包含以下目录：
- META-INF/ ：包含 CERT.SF 和 CERT.RSA 签名文件，以及 MANIFEST.MF 清单文件。
- assets/ ：包含应用的资源；应用可以使用 AssetManager 对象检索这些资源。
- res/ ：包含未编译到 resources.arsc 中的资源（图片、音视频等）。
- lib/ ：包含特定于处理器软件层的已编译代码。此目录包含每种平台类型的子目录，如 armeabi 、 armeabi-v7a 、 arm64-v8a 、 x86 、 x86_64 和 mips 。


APK 还包含以下文件。在这些文件中，只有 AndroidManifest.xml 是必需的。

- resources.arsc ：包含已编译的资源。此文件包含 res/values/ 文件夹的所有配置中的 XML 内容。打包工具会提取此 XML内容，将其编译为二进制文件形式，并压缩内容。此内容包括语言字符串和样式，以及未直接包含在 resources.arsc 文件中的内容（例如布局文件和图片）的路径。
- classes.dex ：包含以 Dalvik/ART 虚拟机可理解的 DEX 文件格式编译的类。
- AndroidManifest.xml ：包含核心 Android 清单文件。此文件列出了应用的名称、版本、访问权限和引用的库文件。该文件使用 Android 的二进制 XML 格式。


## Android Size Analyzer

Android Size Analyzer 工具可轻松地发现和实施多种缩减应用大小的策略。

https://github.com/android/size-analyzer

编译：
```
gradlew :analyzer:executableJar
```

使用：
```
java -jar analyzer/build/libs/analyzer.jar check-project <path-to-project-directory>
```

显示可以优化的文件，并指定文件：
```
java -jar analyzer/build/libs/analyzer.jar check-project <path-to-project-directory> -d --show-fixes
```

![](.\pictures\APK瘦身\Android size analyzer.png)



## 移除未使用资源

APK瘦身关键就在一个字：删！没用的就删了。

## 启用资源缩减 （不打包）

如果在应用的 build.gradle 文件中启用了资源缩减： shrinkResources ，则 Gradle 在打包APK时可以自动忽略未使用资源。 资源缩减只有在与代码缩减： minifyEnabled 配合使用时才能发挥作用。在代码缩减器移除所有不使用的代码后，资源缩减器便可确定应用仍要使用的资源 。

```gradle
android {
    // Other settings
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
```

## 使用Lint分析器（物理删除）

lint 工具是 Android Studio 中附带的静态代码分析器，可检测到 res/ 文件夹中未被代码引用的资源。从菜单栏中依次选择 Analyze > Run Inspection By Name

![](.\pictures\APK瘦身\使用Lint分析器.png)

分析完成后会弹出：

![](.\pictures\APK瘦身\Lint分析结果.png)

lint 工具不会扫描 assets/ 文件夹、通过反射引用的资源或已链接至应用的库文件。此外，它也不会移除资源，只会提醒您它们的存在。 与资源缩减不同，这里点击删除，那就是把文件删了。

反射引用资源：getResources().getIdentifier("layout_main","layout",getPackageName());


## 自定义要保留的资源

如果有想要特别声明需要保留或舍弃的特定资源，在项目中创建一个包含 <resources> 标记的 XML 文件，并在 tools:keep 属性中指定每个要保留的资源，在 tools:discard 属性中指定每个要舍弃的资源。这两个属性都接受以逗号分隔的资源名称列表。还可以将星号字符用作通配符。

```xml
<?xml version="1.0" encoding="utf-8"?> 
<resources xmlns:tools="http://schemas.android.com/tools" 
    tools:keep="@layout/l_used*_c,@layout/l_used_a,@layout/l_used_b*" 
    tools:discard="@layout/unused2" />
```

将该文件保存在项目资源中，例如，保存在 res/raw/keep.xml 中。构建系统不会将此文件打包到 APK 中。


## 一键删除无用资源

Android Studio给我们提供了一键移除所有无用的资源。从菜单栏中依次选择 Refactor > Remove Unused Resources，但是这种方式不建议使用，因为如果某资源仅存在动态获取资源id 的方式，那么这个资源会被认为没有使用过，从而会直接被删除。


## 移除未使用的备用资源

Gradle 资源缩减器只会移除未由应用代码引用的资源，这意味着，它不会移除用于不同设备配置的备用资源。可以使用 Android Gradle 插件的 resConfigs 属性移除应用不需要的备用资源文件。
例如，如果使用的是包含语言资源的库（如 AppCompat ），那么 APK 中将包含这些库中所有已翻译语言的字符串。如果只想保留应用正式支持的语言，则可以使用 resConfig 属性指定这些语言。系统会移除未指定语言的所有资源。


![](.\pictures\APK瘦身\移除未使用的备用资源.png)

配置resConfigs 只打包默认与简体中文资源

```gradle
android { 
    defaultConfig { 
        resConfigs "zh-rCN" 
    } 
}
```

- 中文（中國）：values-zh-rCN
- 中文（台灣）：values-zh-rTW
- 中文（香港）：values-zh-rHK

## 动态库打包配置

so文件是由ndk编译出来的动态库，是 c/c++ 写的，所以不是跨平台的。ABI 是应用程序二进制接口简称（Application Binary Interface），定义了二进制文件（尤其是.so文件）如何运行在相应的系统平台上，从使用的指令集，内存对齐到可用的系统函数库。在Android 系统中，每一个CPU架构对应一个ABI，目前支持的有：
armeabi-v7a，arm64- v8a，x86，x86_64。目前市面上手机设备基本上都是arm架构， armeabi-v7a 几乎能兼容所有设备。因此可以配置：

```gradle
android{ 
    defaultConfig{ 
        ndk{
            abiFilters "armeabi-v7a" 
        } 
    } 
}
```

对于第三方服务，如百度地图、Bugly等会提供全平台的cpu架构。进行了上面的配置之后，表示只会把armeabi-v7a打包进入Apk。从而减少APK大小。

对于arm64架构的设备，如果使用armeabi-v7a也能够兼容，但是不如使用arm64的so性能。因此现在部分应用市场会根据设备提供不同架构的Apk安装。此时我们需要打包出针对arm64的apk与armv7a的apk，可以使用productFlavor 。

```gradle
flavorDimensions "default"
productFlavors{ 
    arm32{
        dimension "default"
        ndk{
            abiFilters "armeabi-v7a" 
        }
    }
    arm64{
        dimension "default" 
        ndk{
            abiFilters "arm64-v8a" 
        } 
    }    
}
```

也可以使用：

```gradle
splits {
    abi {
        enable true 
        reset() 
        include 'arm64-v8a','armeabi-v7a' 
        // exclude 'armeabi' 
        universalApk true //是否打包一个包含所有so的apk 
    } 
}
```

## 使用矢量图

Apk中图片应该算是占用空间最多的资源。我们可以使用webp减少png、jpg图片占用空间的大小。对于小图标也可以使用矢量图。

矢量图可以创建与分辨率无关的图标和其他可伸缩媒体。使用这些图形可以极大地减少 APK 占用的空间。 矢量图片在 Android 中以 VectorDrawable 对象的形式表示。借助 VectorDrawable 对象，100 字节的文件可以生成与屏幕大小相同的清晰图片。

不过，系统渲染每个 VectorDrawable 对象需要花费大量时间，而较大的图片则需要更长的时间才能显示在屏幕上。因此，建议仅在显示小图片时使用这些矢量图。

![](.\pictures\APK瘦身\矢量图1.png)



![](.\pictures\APK瘦身\矢量图2.png)


## 重复使用资源

现在我们有一个矢量图：

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android" 
    android:width="24dp" 
    android:height="24dp" 
    android:viewportWidth="24" 
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal"> 
    <path
        android:fillColor="@android:color/white" 
        android:pathData="M10,20v-6h4v6h5v-8h3L12,3 2,12h3v8z"/> 
</vector>
```

它的显示效果为：

![](.\pictures\APK瘦身\矢量图3.png)

如果我们需要让矢量图显示红色怎么办？这种情况，我们不需要再去创建一个新的矢量图。可以直接给 ImageView 设置 android:tint 属性 来完成颜色的修改。

```xml
<ImageView 
    android:layout_width="50dp" 
    android:layout_height="50dp" 
    android:tint="@color/colorAccent" 
    android:src="@drawable/tabbar_home_vector" />
```

![](.\pictures\APK瘦身\矢量图4.png)


## 选择器
如果需要让矢量图实现触摸变色。只需要创建selector，设置给tint即可

```xml
<!-- tabbar_home_tint_selector --> 
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android"> 
    <item android:color="@color/colorPrimary" android:state_pressed="true" /> 
    <item android:color="@color/colorAccent" /> 
</selector>

<ImageView 
    android:clickable="true" 
    android:layout_width="50dp" 
    android:layout_height="50dp" 
    android:src="@drawable/tabbar_home_vector" 
    android:tint="@color/tabbar_home_tint_selector" />
```

阿里矢量图库：

https://www.iconfont.cn/help/detail?spm=a313x.7781069.1998910419.d8d11a391&helptype=code

## 其他
- 使用精简版本的依赖：如protobuf-lite版本；
- 对于分模块的库按需引入：如netty分模块引入；
- 主动移除无用代码（开启R8/Progurad自动移除）
- 避免使用枚举，使用 @IntDef 代替。
- 开启资源混淆：https://github.com/shwenzhang/AndResGuard
- 支付宝删除Dex debugItem https://juejin.im/post/6844903712201277448
- 对于发布Google paly的应用选择使用：AAB https://developer.android.google.cn/guide/app-bundle


# 轻量级存储优化

对于 Android 轻量级存储方案，有大多数人都很熟悉的 SharedPreferences；也有基于 mmap 的高性能组件 MMKV，底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强；还有 Jetpack DataStore 是一种数据存储解决方案，允许您使用协议缓冲区存储键值对或类型化对象。DataStore 使用 Kotlin 协程和 Flow 以异步、一致的事务方式存储数据。（下面代码基于Android 29 源码）

## SharedPreferences

SharedPreferences 是 Android 中简单易用的轻量级存储方案，用来保存 App 的相关信息，其本质是一个键值对（key-value）的方式保存数据的 xml 文件，文件路径为 /data/data/应用程序包名/shared_prefs，文件内容如下：

```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="pref.device.id">8207e635-bd88-4220-9fc6-59c5e367ad82</string>
    <string name="pref.contact.chat">Let's chat(test)</string>
    <boolean name="pref.user.birthday.modifiable" value="true" />
    <int name="pref.user.birthday.day" value="0" />
    <string name="pref.contact.phone">123-456-8888</string>
    <string name="pref.user.phone"></string>
    <int name="pref.user.birthday.year" value="0" />
    <boolean name="pref.is.login" value="true" />
    <boolean name="pref.first_launch" value="false" />
</map>
```

每次读取数据时，通过解析 xml 文件，得到指定 key 对应的 value。

SharedPreferences 的设计初衷是轻量级存储，如果我们存储了大量的数据，那会对内存造成什么影响？

### 源码初步分析

我们先来看看 SharedPreferences 的源码设计，首先从我们的常规调用 Context.getSharedPreferences(name, mode)开始，最终都会调用到

```java
//ContextImpl.java
@Override
public SharedPreferences getSharedPreferences(String name, int mode) {
            ···
    File file;
    synchronized (ContextImpl.class) {
        if (mSharedPrefsPaths == null) { //用于记录所有的SP文件，文件名为key，file为value
            mSharedPrefsPaths = new ArrayMap<>();
        }
        file = mSharedPrefsPaths.get(name);
        if (file == null) {
            file = getSharedPreferencesPath(name);
            mSharedPrefsPaths.put(name, file);
        }
    }
    return getSharedPreferences(file, mode);
}
```

再来看看方法 getSharedPreferences 方法

```java
//ContextImpl.getSharedPreferences方法
@Override
public SharedPreferences getSharedPreferences(File file, int mode) {
    SharedPreferencesImpl sp;
    synchronized (ContextImpl.class) {
        final ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
        sp = cache.get(file);
        if (sp == null) {
            checkMode(mode);
                            ···
            //SharedPreferences的真正实现类是SharedPreferencesImpl
            sp = new SharedPreferencesImpl(file, mode);
            cache.put(file, sp);
            return sp;
        }
    }
            ···
    return sp;
}
```

SharedPreferences 是个接口，其真正实现类是 SharedPreferencesImpl

```java
final class SharedPreferencesImpl implements SharedPreferences {
    @UnsupportedAppUsage
    private final File mFile; //对应的xml文件
    private final File mBackupFile;
    private Map<String, Object> mMap; //map中缓存了xml文件中所有键值对
    ···
     @UnsupportedAppUsage
    SharedPreferencesImpl(File file, int mode) {
        mFile = file;
        mBackupFile = makeBackupFile(file);
        mMode = mode;
        mLoaded = false;
        mMap = null;
        mThrowable = null;
        startLoadFromDisk(); //开启一个线程加载xml文件内容
    }  
}
```

每当调用 SharedPreferencesImpl 的构造器的时候，都会开始调用 startLoadFromDisk 方法，然后在该方法中开启一个子线程加载 xml 文件中的内容，最后将 xml 中的内容全部加载到 mMap中

```java
map = (Map<String, Object>) XmlUtils.readMapXml(str);
```

### 内存占用

从上面的分析可以看出当 xml 中数据过大时，肯定会导致内存占用过高，虽然 Context.getSharedPreferences(name, mode)调用时会将 xml 中的数据一股脑加载到 mMap 中导致内存占用过大，也就是空间换时间，同时 ContextImpl.getSharedPreferencesCacheLocked

```java
private static ArrayMap<String, ArrayMap<File, SharedPreferencesImpl>> sSharedPrefsCache;   //静态
@GuardedBy("ContextImpl.class")
private ArrayMap<File, SharedPreferencesImpl> getSharedPreferencesCacheLocked() {
    if (sSharedPrefsCache == null) {  
        sSharedPrefsCache = new ArrayMap<>();
    }

    final String packageName = getPackageName();
    ArrayMap<File, SharedPreferencesImpl> packagePrefs = sSharedPrefsCache.get(packageName);
    if (packagePrefs == null) {
        packagePrefs = new ArrayMap<>();
        sSharedPrefsCache.put(packageName, packagePrefs);
    }

    return packagePrefs;
}
```

可以看到这个静态的 sSharedPrefsCache 保存了所有的 sp，然后 sSharedPrefsCache 的 value 值保存了所有键值对，也就是说用过的 sp 永远存在于内存中。

同时开发者也需要注意对每个 sp（xml）大小进行控制，毕竟对读写操作也会有一定的影响，具体的区分可以根据相应的业务进行区分。

但是 SharedPreferences 的设计初衷就是面向轻量级的数据存储，所以该设计没毛病，设计者应该自己注意使用场景，毕竟再好的设计也不能面对所有场景。

### 首次调用可能阻塞主线程

我们再来看看 SharedPreferencesImpl.getString() 方法

```java
@Override
@Nullable
public String getString(String key, @Nullable String defValue) {
    synchronized (mLock) {
        awaitLoadedLocked();
        String v = (String)mMap.get(key);
        return v != null ? v : defValue;
    }
}

@GuardedBy("mLock")
private void awaitLoadedLocked() {
            ···
    while (!mLoaded) {
        try {
            mLock.wait();
        } catch (InterruptedException unused) {
        }
    }
            ···
}
```

可以看到，当 sp 还没加载完毕主线程会一直阻塞在那里，直到加载 sp 的子线程加载完成。对于上面的问题，我们可以提前调用 getSharedPreferences 方法让子线程提前加载 sp 的内容。

防止连续多次edit/commit/apply

```java
SharedPreferences sp = getSharedPreferences("jackie", MODE_PRIVATE);
sp.edit().putString("a", "ljc").commit();
sp.edit().putString("b", "cxy").commit();
sp.edit().putString("c", "lsm").apply();
sp.edit().putString("c", "dmn").apply();
```

每次调用 edit 方法都会创建一个 Editor 对象，造成额外的内存占用。很多设计者会对 SharedPreferences 进行封装，隐藏掉 edit()和commit/apply()调用流程，但往往同时也忽略了Editor.commit/apply()的设计理念和使用场景。如果是复杂的场景，用户可以在多次 putXxx 方法之后再统一进行 commit/apply()，也就是一次更新多个键值对，只进行一次 IO 操作。

### commit/apply 引起的 ANR 问题

commit 是同步地提交到硬件磁盘，有返回值表明修改是否成功，如果在主线程中提交会阻塞线程，影响后续的操作，可能导致 ANR；而 apply 是将修改数据提交到内存，而后异步真正提交到硬件磁盘，没有返回值。我们着重研究一下 apply 为什么会导致 ANR 问题，先来看看 apply 的源码：

```java
@Override
public void apply() {
    final long startTime = System.currentTimeMillis();

    final MemoryCommitResult mcr = commitToMemory();
    final Runnable awaitCommit = new Runnable() {
            @Override
            public void run() {
                try {
                    mcr.writtenToDiskLatch.await(); //等待
                } catch (InterruptedException ignored) {
                }
                                ···
            }
        };

    QueuedWork.addFinisher(awaitCommit); //加入队列

    Runnable postWriteRunnable = new Runnable() {
            @Override
            public void run() {
                awaitCommit.run();
                QueuedWork.removeFinisher(awaitCommit);
            }
        };

    SharedPreferencesImpl.this.enqueueDiskWrite(mcr, postWriteRunnable);
    notifyListeners(mcr);
}
```

首先把带有 await 的 runnable 添加到 QueuedWork 队列，然后把这个写入任务 postWriteRunnable 通过 enqueueDiskWrite 交给 HandlerThread（Handler + Thread） 进行执行，待处理的任务排队进行执行。然后我们进入 ActivityThread 的 handleStopActivity 方法中，可以看到如下代码

```java
// Make sure any pending writes are now committed.
if (!r.isPreHoneycomb()) {
    QueuedWork.waitToFinish();
}
```

我们再来看看 waitToFinish 中的一段源码

```java
Is called from the Activity base class's onPause(), after BroadcastReceiver's onReceive,
     * after Service command handling, etc. (so async work is never lost) 
     */  //这个注释很重要
public static void waitToFinish() { 
      ··· 
        try {
        while (true) {
            Runnable finisher;

            synchronized (sLock) {
                finisher = sFinishers.poll();
            }

            if (finisher == null) {
                break;
            }

            finisher.run(); //关键,相当于调用 `mcr.writtenToDiskLatch.await()`
        }
    } finally {
        sCanDelay = true;
    }
 }       
```

还记得之前的 QueuedWork.addFinisher(awaitCommit)吗，里面的 awaitCommit 在等待写入线程，如果用户使用了太多的 apply，也就是说写入队列中会有很多写入任务。而只有一个线程在写入，一旦涉及到大量的读写很容易造成ANR（android 8.0 之前，android 8.0 之前的实现 QueuedWork.waitToFinish 是有缺陷的。在多个生命周期方法中，在主线程等待任务队列去执行完毕，而由于cpu调度的关系任务队列所在的线程并不一定是处于执行状态的，而且当apply提交的任务比较多时，等待全部任务执行完成，会消耗不少时间，这就有可能出现 ANR），因为本文的源码时基于 android 29 的，所以该版本或者说是 android 8.0之后并不存在 ANR 问题，因为 8.0之后做了很大的优化，会主动触发processPendingWork取出写任务列表中依次执行，而不是只在在等待。还有一个更重要的优化：

我们知道在调用 apply 方法时，会将改动同步提交到内存中 map 中，然后将写入磁盘的任务加入的队列中，在工作线程中从队列中取出写入任务，依次执行写入。注意，不管是内存的写入还是磁盘的写入，对于一个 xml 格式的 sp 文件来说，都是全量写入的。 这里就存在优化的空间，比如对于同一个 sp 文件，连续调用 n 次apply,就会有 n 次写入磁盘任务执行，实际上只需要最后执行最后那次就可以了，最后那次提交对应内存的 map 是持有最新的数据，所以就可以省掉前面 n-1 次的执行，这个就是android 8.0中做的优化，是使用版本来进行控制的。


#### 解决方案


解决方案可以参考今日头条的解决方案，通过反射 ActivityThread 中的 H(Handler) 变量，给 Handler 设置一个 callback，Handler 的 dispatchMessage 中先处理 callback。队列清理需要反射调用 QueuedWork。Google 之所以在Activity/Service 的 onStop 之前调用该方法是为了尽量保证 sp 的数据持久化，该文章中也对比了清理队列和未清理情况下的失败率（相差不大）。

还有一个解决方案，因为 SharedPreferences 是个接口，所以可以自己实现 apply (异步调用系统 commit，这样并不会导致类似系统 apply 那样的阻塞)，同时重写 Activity 和 Application 的 getSharedPreference 方法，直接返回自己的实现。但是这个方案带来的副作用比清理等待锁更加明显：系统apply是先同步更新缓存再异步写文件，调用方在同一线程内读写缓存是同步的，无需关心上下文数据读写同步问题；commit 异步化之后直接在子线程中更新缓存再写文件，调用方需要关注上下文线程切换，异步有可能引发读写数据不一致问题。所以还是推荐使用第一种方案。


### 安全机制

安全机制我们可以分为线程安全，进程安全，文件备份机制。

SharedPreferences 通过锁来保证线程安全，这里就不赘述了。而如何保证进程安全呢，我们再来看看 SharedPreferences 类的注释，可以看到不支持进程安全。

```
 *
 * <p><em>Note: This class does not support use across multiple processes.</em>
 *
```

SharedPreferences 提供了 MODE_MULTI_PROCESS 这个 Flag 来支持跨进程，保证了在 API 11 以前的系统上，如果 sp 已经被读取进内存，再次获取这个 sp 的时候，如果有这个 flag，会重新读一遍文件，仅此而已！

```java
@Override
public SharedPreferences getSharedPreferences(File file, int mode) {
    SharedPreferencesImpl sp;
    ···
    if ((mode & Context.MODE_MULTI_PROCESS) != 0 ||
        getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
        // If somebody else (some other process) changed the prefs
        // file behind our back, we reload it.  This has been the
        // historical (if undocumented) behavior.
        sp.startReloadIfChangedUnexpectedly();
    }
    return sp;
}
```

所以说 SharedPreferences 的跨进程通信压根就不可靠！对于如何保证进程安全，可以使用 ContentProvider 进行统一访问，或者使用文件锁的方式。

最后我们再来看看文件备份机制，我们在运行程序的时候，可能会遇到手机死机或者断电等突发状况，这个时候如何保证文件的正常和安全就至关重要了。Android 系统本身的文件系统虽然有保护机制，但还会有数据丢失或者文件损坏的情况，所以对文件的备份就至关重要了。从 SharedPreferencesImpl 的 commit() -> enqueueDiskWrite() -> writeToFile()

```java
@GuardedBy("mWritingToDiskLock")
private void writeToFile(MemoryCommitResult mcr, boolean isFromSyncCommit) {

                ···
                    //尝试写入文件
        if (!backupFileExists) {
            if (!mFile.renameTo(mBackupFile)) { //直接把原有的文件命名成备份文件
                Log.e(TAG, "Couldn't rename file " + mFile
                      + " to backup file " + mBackupFile);
                mcr.setDiskWriteResult(false, false);
                return;
            }
        } else {
            mFile.delete();
        }
        // Writing was successful, delete the backup file if there is one.
        // 写入成功，删除备份文件
        mBackupFile.delete();

                    ···
}
```

备份的时候是直接将原有的文件重命名为备份文件，写入成功后再删除备份文件。再来看看前面的 loadFromDisk 方法

```java
private void loadFromDisk() {
    synchronized (mLock) {
        if (mLoaded) {
            return;
        }
        if (mBackupFile.exists()) {
            mFile.delete();
            mBackupFile.renameTo(mFile);
        }
        ···
    }
```

如果因为异常情况（比如进程被 kill）导致写入失败，下次启动的时候若发现存在备份文件，则将备份文件重新命名为源文件，原本未完成写入的文件就直接丢弃。

### 小结
到此我们先做个小结，我们提到了 SharedPreferences 的内存占用问题以及可能阻塞主线程，正确的应用场景和合适的代码调用方式，还提到了可能导致的 ANR 问题，最后我们分析了它的安全机制，线程安全，进程安全（无），文件备份机制。

在正确使用 SharedPreferences 的情况下，可以大概总结一下 SharedPreferences 的问题，可能导致内存占用高，ANR，无法保证进程安全。


## MMKV
MMKV 腾讯开发的基于 mmap 内存映射的 key-value 组件，底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强，支持多进程。从 2015 年中至今在微信上使用，其性能和稳定性经过了时间的验证。后续也已移植到 Android / macOS / Win32 / POSIX 平台，一并开源。

MMKV 原本是要解决微信上特殊文字引起系统的 crash，解决过程中有一些计数器需要保存（因为闪退随时可能发生），这时就需要一个性能非常高的通用 key-value组件，SharedPreferences、NSUserDefaults、SQLite 等常见组件这些都不满足，考虑到这个防 crash 方案最主要的诉求还是实时写入，而 mmap 内存映射文件刚好满足这种需求。

### 使用方式
首先导入依赖

```gradle
dependencies {
    implementation 'com.tencent:mmkv-static:1.2.7'
    // replace "1.2.7" with any available version
}
```

MMKV 的使用非常简单，所有变更立马生效，无需调用 sync、apply。 在 App 启动时初始化 MMKV，设定 MMKV 的根目录（files/mmkv/），例如在 Application 里：

```java
public void onCreate() {
    super.onCreate();

    String rootDir = MMKV.initialize(this);
    System.out.println("mmkv root: " + rootDir);
    //……
}
```

如果不同的业务需要区别存储，也可以单独创建自己的实例

```java
MMKV kv = MMKV.mmkvWithID("MyID");
kv.encode("bool", true);
```

如果业务需要多进程访问，那么在初始化的时候加上标志位 MMKV.MULTI_PROCESS_MODE：

```java
MMKV kv = MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);
kv.encode("bool", true);
```

MMKV 提供一个全局的实例，可以直接使用：

```java
import com.tencent.mmkv.MMKV;
//……

MMKV kv = MMKV.defaultMMKV();

kv.encode("bool", true);
boolean bValue = kv.decodeBool("bool");

kv.encode("int", Integer.MIN_VALUE);
int iValue = kv.decodeInt("int");

kv.encode("string", "Hello from mmkv");
String str = kv.decodeString("string");
```

### 支持的数据类型

- 支持以下 Java 语言基础类型：
    - boolean、int、long、float、double、byte[]

- 支持以下 Java 类和容器：
    - String、Set<String>
    - 任何实现了Parcelable的类型


### SharedPreferences 迁移
MMKV 提供了 importFromSharedPreferences() 函数，可以比较方便地迁移数据过来。

MMKV 还额外实现了一遍 SharedPreferences、SharedPreferences.Editor 这两个 interface，在迁移的时候只需两三行代码即可，其他 CRUD 操作代码都不用改。

```java
private void testImportSharedPreferences() {
    //SharedPreferences preferences = getSharedPreferences("myData", MODE_PRIVATE);
    MMKV preferences = MMKV.mmkvWithID("myData");
    // 迁移旧数据
    {
        SharedPreferences old_man = getSharedPreferences("myData", MODE_PRIVATE);
        preferences.importFromSharedPreferences(old_man);
        old_man.edit().clear().commit();
    }
    // 跟以前用法一样
    SharedPreferences.Editor editor = preferences.edit(); //注意 preferences.edit();
    editor.putBoolean("bool", true);
    editor.putInt("int", Integer.MIN_VALUE);
    editor.putLong("long", Long.MAX_VALUE);
    editor.putFloat("float", -3.14f);
    editor.putString("string", "hello, imported");
    HashSet<String> set = new HashSet<String>();
    set.add("W"); set.add("e"); set.add("C"); set.add("h"); set.add("a"); set.add("t");
    editor.putStringSet("string-set", set);
    // 无需调用 commit()
    //editor.commit();
}
```

可以看到使用preferences.edit();可以让迁移后的用法和之前一样，MMKV 已经为我们考虑的很周到了，迁移的成本非常低，不迁移过来还等什么呢？

### mmap 原理
mmap 是一种内存映射文件的方法，即将一个文件或者其它对象映射到进程的地址空间，实现文件磁盘地址和进程虚拟地址空间中一段虚拟地址的一一对应关系。实现这样的映射关系后，进程就可以采用指针的方式读写操作这一段内存，而系统会自动回写脏页面到对应的文件磁盘上，即完成了对文件的操作而不必再调用 read，write等系统调用函数。相反，内核空间对这段区域的修改也直接反映用户空间，从而可以实现不同进程间的文件共享。

<img src=".\pictures\轻量级存储优化\mmap内存映射.jpg" style="zoom: 50%;" />

关于虚拟(地址)空间和虚拟内存：请放弃虚拟内存这个概念，那个是广告性的概念，在开发中没有意义。开发中只有虚拟空间的概念，进程看到的所有地址组成的空间，就是虚拟空间。虚拟空间是某个进程对分配给它的所有物理地址（已经分配的和将会分配的）的重新映射。 mmap的作用，在应用这一层，是让你把文件的某一段，当作内存一样来访问。

通过 mmap 内存映射文件，提供一段可供随时写入的内存块，App 只管往里面写数据，由操作系统负责将内存回写到文件，不必担心 crash 导致数据丢失。


### 为什么选择 Protobuf
数据序列化方面我们选用 Protobuf 协议，pb 在性能和空间占用上都有不错的表现。Protocol buffers 通常称为 Protobuf，是 Google 开发的一种协议，允许对结构化数据进行序列化和反序列化，不仅仅是一种消息格式，它还是一组用于定义和交换这些消息的规则和工具。 谷歌开发它的目的是提供一种比 XML更好的方式来进行系统间通信。该协议甚至超越了JSON，具有更好的性能，更好的可维护性和更小的尺寸。

但是它也有一些缺点，二进制格式可读性差，维护成本高等。

#### 增量更新机制
标准 protobuf 不提供增量更新的能力，每次写入都必须全量写入。考虑到主要使用场景是频繁地进行写入更新，我们需要有增量更新的能力：将增量 kv 对象序列化后，直接 append 到内存末尾；这样同一个 key 会有新旧若干份数据，最新的数据在最后；那么只需在程序启动第一次打开 mmkv 时，不断用后读入的 value 替换之前的值，就可以保证数据是最新有效的。

使用 append 实现增量更新带来了一个新的问题，就是不断 append 的话，文件大小会增长得不可控。例如同一个 key 不断更新的话，是可能耗尽几百 M 甚至上 G 空间，而事实上整个 kv 文件就这一个 key，不到 1k 空间就存得下。这明显是不可取的。我们需要在性能和空间上做个折中：以内存 pagesize 为单位申请空间，在空间用尽之前都是 append 模式；当 append 到文件末尾时，进行文件重整、key 重排，尝试序列化保存重排结果；重排后空间还是不够用的话，将文件扩大一倍，直到空间足够。

#### 多进程设计与实现
我们先来看MMKV的设计初衷是要解决什么问题，最主要的诉求还是实时写入，而且要求速度够快，性能高。当要求跨进程通信的时候，我们先看看我们有什么，C/S 架构中有 ContentProvider，但是问题很明显，启动慢访问也慢，这个可以说是 Android 下基于Binder 的 C/S 架构组件的痛点，socket、PIPE、message queue，因为要至少 2 次的内存拷贝，就更加慢了。

MMKV 追求的是极致的访问速度，我们要尽可能的避免进程间通信，C/S架构是不可取的。再考虑到 MMKV 底层使用 mmap 实现，采用去中心化的架构是很自然的选择。我们只需要将文件 mmap 到每个访问进程的内存空间，加上合适的进程锁，再处理好数据的同步，就能够实现多进程并发访问。


### 性能对比
单进程性能，可以看到 MMKV 在写入性能上远远超过 SharedPreferences 和 SQlite，在读取性能上也有相近或超越的表现。

<img src=".\pictures\轻量级存储优化\MMKV单进程性能对比.jpg" style="zoom:80%;" />

多进程性能，MMKV 无论是在写入性能还是在读取性能，都远远超越 MultiProcessSharedPreferences & SQLite & SQLite， MMKV 在 Android 多进程 key-value 存储组件上是不二之选。

<img src=".\pictures\轻量级存储优化\MMKV多进程性能对比.jpg" style="zoom:80%;" />

（测试机器是 华为 Mate 20 Pro 128G，Android 10，每组操作重复 1k 次，时间单位是 ms。）

### 小结
MMKV 可以解决 SharedPreferences 不能直接跨进程通信的问题，但 SharedPreferences 也可以通过 ContentProvider 或者文件锁等方式解决该问题，个人感觉 MMKV 的主要优势有两点，SharedPreferences 可能导致 Activity/Service 等生命周期去做 waitToFinish() 导致ANR 问题，而 MMKV 不存在这个问题，另一个优势是实时写入，性能高，速度快（设计初衷）。

虽然 SharedPreferences 的跨进程、ANR 问题也可以用技术方案进行解决，但是 MMKV 天然不存在这两个问题，而且该组件也支持从 SharedPreferences 迁移到 MMKV，迁移也及其简单，成本很小。所以 MMKV 的确是一个更好的轻量级存储方案。


## DataStore
DataStore 是 Android Jetpack 的一部分。Jetpack DataStore 是一种数据存储解决方案，允许您使用协议缓冲区存储键值对或类型化对象。DataStore 使用 Kotlin 协程和流程（Flow）以异步、一致的事务方式存储数据。官方建议如果当前在使用 SharedPreferences 存储数据，请考虑迁移到 DataStore。

DataStore 提供两种不同的实现：Preferences DataStore 和 Proto DataStore。
- Preferences DataStore 以键值对的形式存储在本地和 SharedPreferences 类似，此实现不需要预定义的架构，也不确保类型安全。
- Proto DataStore 将数据作为自定义数据类型的实例进行存储。此实现要求您使用协议缓冲区来定义架构，但可以确保类型安全。

### Preferences DataStore 使用方式
先导入依赖

```gradle
dependencies {
  // Preferences DataStore (SharedPreferences like APIs)  
  implementation "androidx.datastore:datastore-preferences:1.0.0-alpha06"
  // Typed DataStore (Typed API surface, such as Proto)
  implementation "androidx.datastore:datastore-core:1.0.0-alpha06"
}  
```

Preferences DataStore 的使用方式如下

```kotlin
//1.构建 DataStore
val dataStore: DataStore<Preferences> = context.createDataStore(name = PREFERENCE_NAME)

//2.Preferences DataStore 以键值对的形式存在本地，需要定义一个 key(比如：KEY_JACKIE)
//Preferences DataStore 中的 key 是 Preferences.Key<T> 类型
val KEY_JACKIE = stringPreferencesKey("username")
GlobalScope.launch {
    //3.存储数据
    dataStore.edit {
        it[KEY_JACKIE] = "jackie"
    }
    //4.获取数据
    val getName = dataStore.data.map {
        it[KEY_JACKIE]
    }.collect{ //flow 调用collect 开始消费数据
        Log.i(TAG, "onCreate: $it")  //打印出 jackie
    }
}
```

需要注意的是读取、写入数据都要在协程中进行，因为 DataStore 是基于 Flow 实现的。也可以看到没有 commit/apply() 方法，同时可以监听到操作成功或者失败结果。

Preferences DataStore 只支持 Int , Long , Boolean , Float , String 键值对数据，适合存储简单、小型的数据，并且不支持局部更新，如果修改了其中一个值，整个文件内容将会被重新序列化。

### SharedPreferences 迁移到 Preferences DataStore

接下来我们来看看 SharedPreferences 迁移到 DataStore，在构建 DataStore 的时候传入 SharedPreferencesMigration，当 DataStore 构建完了之后，需要执行一次读取或者写入操作，即可完成迁移，迁移成功后，会自动删除 SharedPreferences 文件

```kotlin
val dataStoreFromPref = this.createDataStore(name = PREFERENCE_NAME_PREF ,migrations = listOf(SharedPreferencesMigration(this,OLD_PREF_NANE)))
```

我们原本的 SharedPreferences 数据如下

```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="name">lsm</string>
    <boolean name="male" value="false" />
    <int name="age" value="30" />
    <float name="height" value="175.0" />
</map>
```

原本文件目录如下：

![](.\pictures\轻量级存储优化\SharedPreferences原目录.jpg)

迁移后的文件目录如下：

![](.\pictures\轻量级存储优化\迁移后的文件目录.jpg)

可以看到迁移后原本的 SharedPreferences 被删除了，同时也可以看到 DataStore 的文件更小一些，在迁移的过程中发现一个有趣的情况，如果直接迁移后并不进行任意值的读取，在对应的目录上找不到迁移后的文件，只有当进行任意值的读取后，才会在对应的目录上找到文件。完整代码如下：

```kotlin
val dataStoreFromPref = this.createDataStore(name = PREFERENCE_NAME_PREF
                    , migrations = listOf(SharedPreferencesMigration(this, OLD_PREF_NANE)))
//迁移后需要手动读取一次，才可以找到迁移的文件            
val KEY_NAME = stringPreferencesKey("name")
GlobalScope.launch { 
    dataStoreFromPref.data.map { 
        it[KEY_NAME]
    }.collect {
        Log.i(TAG, "onCreate: ===============$it")
    }
}
```

下面我们继续来看 Proto DataStore，Proto DataStore 比 Preference DataStore 更加灵活，支持更多的类型

- Preference DataStore 只支持 Int 、 Long 、 Boolean 、 Float 、 String，而 protocol buffers 支持的类型，Proto DataStore 都支持
- Proto DataStore 使用了二进制编码压缩，体积更小，速度比 XML 更快

### Proto DataStore 使用方式

因为 Proto DataStore 是存储类的对象（typed objects ），通过 protocol buffers 将对象序列化存储在本地。

数据序列化常用的方式有 JSON、Protocol Buffers、FlatBuffers。Protocol Buffers 简称 Protobuf，共两个版本 proto2 和 proto3，大多数项目使用的 proto2，两者语法不一致，proto3 简化了 proto2 的语法，提高了开发效率。Proto DataStore 对着两者都支持，我们这里使用 proto 3。

新建Person.proto文件，添加一下内容：

```protobuf
syntax = "proto3";

option java_package = "com.hi.dhl.datastore.protobuf";
option java_outer_classname = "PersonProtos";

message Person {
    // 格式：字段类型 + 字段名称 + 字段编号
    string name = 1;
}
```

syntax ：指定 protobuf 的版本，如果没有指定默认使用 proto2，必须是.proto文件的除空行和注释内容之外的第一行

option ：表示一个可选字段

message 中包含了一个 string 类型的字段(name)。注意 ：= 号后面都跟着一个字段编号

每个字段由三部分组成：字段类型 + 字段名称 + 字段编号，在 Java 中每个字段会被编译成 Java 对象。

这些是简单的语法介绍，然后进行 Build 就可以看到生成的文件。

然后我们再来看具体的使用方式：

```kotlin
//1.构建Proto DataStore
val protoDataStore: DataStore<PersonProtos.Person> = this
    .createDataStore(fileName = "protos_file",serializer = PersonSerializer)

GlobalScope.launch(Dispatchers.IO) {
    protoDataStore.updateData { person ->
        //2.写入数据
        person.toBuilder().setName("jackie").build()
    }

    //3.读取数据
    protoDataStore.data.collect {
        Log.i(TAG, "onCreate: ============"+it.name)
    }

}
```

PersonSerializer 类实现如下：

```kotlin
object PersonSerializer: Serializer<PersonProtos.Person> {
    override val defaultValue: PersonProtos.Person
        get() {
            return PersonProtos.Person.getDefaultInstance()
        }

    override fun readFrom(input: InputStream): PersonProtos.Person {
        try {
            return PersonProtos.Person.parseFrom(input) // 是编译器自动生成的，用于读取并解析 input 的消息
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: PersonProtos.Person, output: OutputStream) =
        t.writeTo(output) // t.writeTo(output) 是编译器自动生成的，用于写入序列化消息
}
```

读取和写入也是都在协程当中，创建的文件在该目录下：

![](.\pictures\轻量级存储优化\Proto Datastore文件.jpg)

#### SharedPreferences 迁移到 Proto DataStore

接下来我们来看看 SharedPreferences 如何迁移到 Proto DataStore 当中

```kotin
//1.创建映射关系
val sharedPrefsMigration =
    androidx.datastore.migrations.SharedPreferencesMigration<PersonProtos.Person>(this,OLD_PREF_NANE){
        sharedPreferencesView,person ->

        //获取SharedPreferences 数据
        val follow = sharedPreferencesView.getString(NAME,"")
        //写入数据，也就是说将数据映射到对应的类的属性中
        person.toBuilder().setName(follow).build()
    }
//2.构建 Protos DataStore 并传入 sharedPrefsMigration
val protoDataStoreFromPref = this.createDataStore(fileName = "protoDataStoreFile"
    ,serializer = PersonSerializer,migrations = listOf(sharedPrefsMigration))

GlobalScope.launch(Dispatchers.IO) {
    protoDataStoreFromPref.data.map {
        it.name
    }.collect{

    }
}
```

可以看到迁移首先需要创建映射关系，然后构建 Protos DataStore 并传入 sharedPrefsMigration，最后迁移完的 SharedPreferences 会被删除，就算你只迁移了一个数据，整个SharedPreferences 也会被删除，所以迁移是一定要把所有需要的数据都搬过去。最后是迁移后的目录

![](.\pictures\轻量级存储优化\Proto Datastore 迁移后的目录.jpg)

SharedPreferences vs DataStore 功能对比

![](.\pictures\轻量级存储优化\SharedPreferences vs DataStore 功能对比.jpg)

#### 应用场景
- Preferences DataStore 以键值对的形式存储在本地和 SharedPreferences 类似，存取一些简单的字段等。
- Proto DataStore 将数据作为自定义数据类型的实例进行存储。可以存取一些复杂的对象，适合保存一些重要对象的保存。

## 总结
SharedPreferences 的 Api 使用很友好，数据改变时可以进行监听。但是它在 8.0 之前可能造成ANR（8.0之后优化了），而且不能跨进程。而 DataStore 存在 Preferences DataStore 和 Proto DataStore 这两种方式，前者适合存储键值对的数据但是效率并不如 SharedPreferences（耗时是两倍左右），后者适合存储一些自定义的数据类型，DataStore 也可以在当数据改变可以进行监听，使用 Flow 以异步一致性方式存储数据，功能强大很多，但还是不能跨进程。

Proto DataStore 感觉在复杂数据的存储上可能会很有优势，当本地需要一些缓存数据对象，如果使用 Proto DataStore 能够快速获取整个对象（比如首页的缓存数据），然后进行数据加载这是很有优势的。但是其速度我也还没和其他方式进行对比，有兴趣的读者可以自己尝试一波。

而 MMKV 虽然不是官方出品的，但是在性能，速率，跨进程上面秒杀官方的两个数据存储方式。如果只是很简单的数据存储而且需要跨进程，MMKV 是首选。
