[TOC]

# 运行时数据区（01）

## 课程内容概览	

**该部分包含：**

- 前置知识了解
- 程序计数器
- 虚拟机栈
- 本地方法栈

> 部分图源自作者[youthlql](https://github.com/youthlql)，懒得重新看视频截图了，就在这位作者的笔记基础上和自己原ipad的goodnotes笔记结合做了一定修改



# 前置知识-类的加载简单了解



### 类的加载过程

- 加载（Loading）：查找编译后该类对应的 .class文件，并对常量进行默认初始化赋值0
- 链接（Linking）：
  - 验证（Verify）：验证class文件是否符合当前虚拟机的要求
  - 准备（Prepare）：为类变量分配内存并设置默认初始值0，并为常量进行显式赋值
  - 解析（Resolve）：将常量池内的符号引用转换为直接引用的过程

- 初始化（Initialization）：执行类构造器 clinit()方法的过程，对类变量初始化赋值



### 类的加载器

- 引导类/启动类 加载器Bootstrap ClassLoader：用c/c++编写，不继承于Classloader类，用于加载Java核心类库等，若要输出该类的对象，将会输出NULL
- 扩展类加载器Extension ClassLoader：用Java编写，继承于Classloader类
- 系统类加载器Application ClassLoader：用Java编写，继承于Classloader类，是默认的类加载器
- 特殊的：自定义类加载器自己重写loadClass()或findClass()方法的加载器

<font color='cornflowerblue'>自定义类加载器的父类是系统类加载器</font>



### 获取ClassLoader类对象的途径

方式一：获取当前类的ClassLoader
clazz.getClassLoader()
方式二：获取当前线程上下文的ClassLoader
Thread.currentThread().getContextClassLoader()
方式三：获取系统的ClassLoader
ClassLoader.getsystemClassLoader()
方式四：获取调用者的ClassLoader
DriverManager.getCallerClassLoader()



### 双亲委派机制	

![image-20230703163721280](image/JVMRuntimeDataArea1.assets/image-20230703163721280.webp)

**工作原理**

- 1)如果一个类加载器收到了类加载请求，它并不会自已先去加载，而是把这个请求委托给父类的加载器去执行：
- 2)如果父类加载器还存在其父类加载器则进一步向上委托，依次递归请求最终将到达顶层的启动类加载器
- 3)如果父类加载器可以完成类加载任务，就成功返回，倘若父类加载器无法完成此加载任务，子加载器才会尝试自已去加载，这就是双亲委派模式。



**优势**

- 避免类的重复加载
- 保护程序安全，防止核心API被随意篡改
  - 自定义类：java.lang.String
  - 自定义类：java.lang.Shkstart

举例：在自己的项目中写一个目录为Java.lang下的类String，当new这个自己写的String类时，逐层向上委托过后，最高层的启动类加载器可以执行该请求，故new的String类时核心类库中的String类，自己写的没有办法被new到，保护了API不被篡改（<font color='cornflowerblue'>自己写的类都由Application ClassLoader加载</font>）



### 类的主动使用和被动使用

Java程序对类的使用方式分为：主动使用和被动使用。

<font color='cornflowerblue'>主动使用即会造成类的初始化（Initialization）</font>

**主动使用**，又分为八种情况：

1. 创建类的实例，包括new、反射、克隆、反序列化
2. 访问某个类或接口的静态变量，或者对该静态变量赋值
3. 调用类的静态方法
4. 反射中使用Class.forName("com.atguigu.Test")加载类时，会进行初始化2
5. 初始化一个类的子类，但是不会初始化其实现的接口
6. 若是一个接口定义了default方法，那么直接实现或者间接实现该接口的类的初始化，该接口要在其之前被初始化
7. Java虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的那个类，虚拟机会先初始化这个主类）
8. JDK7开始提供的动态语言支持：

     - java.lang.invoke.MethodHandle实例的解析结果，当初次调用MethodHandle实例时，初始化该MethodHandle指向的方法所在的类。


     - REF_getstatic、REF_putStatic、REF_invokeStatic方法句柄对应的类没有初始化，则初始化


<font color='red'>只有类的主动使用，才会进行类的初始化，执行clinit()方法进行初始化赋值</font>



除了以上八种情况，其他使用Java类的方式都被看作是对<font color='red'>类的被动使用，都不会导致类的初始化</font>（<font color='cornflowerblue'>即类的加载过程中的Initialization</font>）

**被动使用**

关于类的被动使用，即不会进行类的初始化操作，即不会调用clinit.>()

1. 当访问一个静态字段时，只有真正声明这个字段的类才会被初始化。
   - 当通过子类引用父类的静态变量，不会导致子类初始化
2. 通过数组定义类引用，不会触发此类的初始化
3. 引用常量不会触发此类或接口的初始化。因为常量在链接阶段就己经被显式赋值了。
4. 调用ClassLoader类的了loadcLass()方法加载一个类，并不是对类的主动使用，不会导致类的初始化。
   - ClassLoader.getSystemClassLoader().loadClass("com.spongehah.java.Person")，该种反射加载类的方式并不会造成类的初始化



# 基础知识了解



## HotSpot虚拟机结构

![image-20230703170639531](image/JVMRuntimeDataArea1.assets/image-20230703170639531.webp)

![image-20230703162327913](image/JVMRuntimeDataArea1.assets/image-20230703162327913.webp)

【HotSpot虚拟机结构图】

当我们通过前面的：类的加载 --> 验证 --> 准备 --> 解析 --> 初始化，这几个阶段完成后，就会用到执行引擎对我们的类进行使用，同时执行引擎将会使用到我们运行时数据区

Java虚拟机定义了若千种程序运行期间会使用到的运行时数据区，其中有一些会随着虚拟机启动而创建，随着虚拟机退出而销毁。另外一些则是与线程一一对应的，这些与线程对应的数据区域会随着线程开始和结束而创建和销毁。

灰色的为单独线程私有的，红色的为多个线程共享的。即：

- 每个线程：独立包括程序计数器、栈、本地栈
- 线程间共享：堆、堆外内存（永久代或元空间、代码缓存）

![image-20230703162256930](image/JVMRuntimeDataArea1.assets/image-20230703162256930.webp)



## 线程

**JVM 线程**

- 线程是一个程序里的运行单元。JVM允许一个应用有多个线程并行的执行
- **在Hotspot JVM里，每个线程都与操作系统的本地线程直接映射**
  - 当一个Java线程准备好执行以后，此时一个操作系统的本地线程也同时创建。Java线程执行终止后，本地线程也会回收
- 操作系统负责将线程安排调度到任何一个可用的CPU上。一旦本地线程初始化成功，它就会调用Java线程中的run()方法



**JVM系统线程**

- 如果你使用jconsole或者是任何一个调试工具，都能看到在后台有许多线程在运行。这些后台线程不包括调用`public static void main(String[])`的main线程以及所有这个main线程自己创建的线程。

- 这些主要的后台系统线程在Hotspot JVM里主要是以下几个：

  - **虚拟机线程**：这种线程的操作是需要JVM达到安全点才会出现。这些操作必须在不同的线程中发生的原因是他们都需要JVM达到安全点，这样堆才不会变化。这种线程的执行类型包括"stop-the-world"的垃圾收集，线程栈收集，线程挂起以及偏向锁撤销。

  - **周期任务线程**：这种线程是时间周期事件的体现（比如中断），他们一般用于周期性操作的调度执行。

  - **GC线程**：这种线程对在JVM里不同种类的垃圾收集行为提供了支持。

  - **编译线程**：这种线程在运行时会将字节码编译成到本地代码。

  - **信号调度线程**：这种线程接收信号并发送给JVM,在它内部通过调用适当的方法进行处理。



# 程序计数器PC Register

![image-20230703163236435](image/JVMRuntimeDataArea1.assets/image-20230703163236435.webp)

【运行时数据区结构图】

## PC寄存器介绍

JVM中的程序计数寄存器(Program Counter Register)中，Reqister的命名源于CPU的寄存器，**寄存器存储指令相关的现场信息**，CPU只有把数据存储到寄存器才能够运行。

这里，并非是广义上所指的物理寄存器，或许将其翻译为PC计数器（或指令计数器）会更加贴切（也称为程序钩子），并且也不容易引起一些不必要的误会。<font color='red'>JVM中的PC寄存器是对物理PC寄存器的一种抽象模拟</font>

- 它是一块很小的内存空间，几乎可以忽略不记。也是运行速度最快的存储区域。
- 在JVM规范中，每个线程都有它自己的程序计数器，是线程私有的，生命周期与线程的生命周期保持一致。
- 任何时间一个线程都只有一个方法在执行，也就是所调的<font color='red'>当前方法</font>。程序计数器会存储当前线程正在执行的Java方法的JVM指令地址；或者，如果是在执行native方法，则是未指定值(undefined)，本地方法的指令地址存储在本地方法栈中
- 它是程序控制流的指示器，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。
- 字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令。
- 它是**唯一一个**在Java虚拟机规范中没有规定任何OutotMemoryError情况的区域。
- <font color='red'>PC Register没有GC和OOM</font>



## PC寄存器作用

PC寄存器用来存储指向下一条指令的地址，也即将要执行的指令代码。由执行引擎读取下一条指令，并执行该指令。

![image-20230703171158679](image/JVMRuntimeDataArea1.assets/image-20230703171158679.webp)



## 举例

```java
public class PCRegisterTest {

    public static void main(String[] args) {
        int i = 10;
        int j = 20;
        int k = i + j;

        String s = "abc";
        System.out.println(i);
        System.out.println(k);

    }
}
```

查看字节码

> 看字节码的方法：https://blog.csdn.net/21aspnet/article/details/88351875

```java
Classfile /F:/IDEAWorkSpaceSourceCode/JVMDemo/out/production/chapter04/com/atguigu/java/PCRegisterTest.class
  Last modified 2020-11-2; size 675 bytes
  MD5 checksum 53b3ef104479ec9e9b7ce5319e5881d3
  Compiled from "PCRegisterTest.java"
public class com.atguigu.java.PCRegisterTest
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #6.#26         // java/lang/Object."<init>":()V
   #2 = String             #27            // abc
   #3 = Fieldref           #28.#29        // java/lang/System.out:Ljava/io/PrintStream;
   #4 = Methodref          #30.#31        // java/io/PrintStream.println:(I)V
   #5 = Class              #32            // com/atguigu/java/PCRegisterTest
   #6 = Class              #33            // java/lang/Object
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               LocalVariableTable
  #12 = Utf8               this
  #13 = Utf8               Lcom/atguigu/java/PCRegisterTest;
  #14 = Utf8               main
  #15 = Utf8               ([Ljava/lang/String;)V
  #16 = Utf8               args
  #17 = Utf8               [Ljava/lang/String;
  #18 = Utf8               i
  #19 = Utf8               I
  #20 = Utf8               j
  #21 = Utf8               k
  #22 = Utf8               s
  #23 = Utf8               Ljava/lang/String;
  #24 = Utf8               SourceFile
  #25 = Utf8               PCRegisterTest.java
  #26 = NameAndType        #7:#8          // "<init>":()V
  #27 = Utf8               abc
  #28 = Class              #34            // java/lang/System
  #29 = NameAndType        #35:#36        // out:Ljava/io/PrintStream;
  #30 = Class              #37            // java/io/PrintStream
  #31 = NameAndType        #38:#39        // println:(I)V
  #32 = Utf8               com/atguigu/java/PCRegisterTest
  #33 = Utf8               java/lang/Object
  #34 = Utf8               java/lang/System
  #35 = Utf8               out
  #36 = Utf8               Ljava/io/PrintStream;
  #37 = Utf8               java/io/PrintStream
  #38 = Utf8               println
  #39 = Utf8               (I)V
{
  public com.atguigu.java.PCRegisterTest();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 7: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/atguigu/java/PCRegisterTest;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=5, args_size=1
         0: bipush        10
         2: istore_1
         3: bipush        20
         5: istore_2
         6: iload_1
         7: iload_2
         8: iadd
         9: istore_3
        10: ldc           #2                  // String abc
        12: astore        4
        14: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
        17: iload_1
        18: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
        21: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
        24: iload_3
        25: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
        28: return
      LineNumberTable:
        line 10: 0
        line 11: 3
        line 12: 6
        line 14: 10
        line 15: 14
        line 16: 21
        line 18: 28
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      29     0  args   [Ljava/lang/String;
            3      26     1     i   I
            6      23     2     j   I
           10      19     3     k   I
           14      15     4     s   Ljava/lang/String;
}
SourceFile: "PCRegisterTest.java"
```



*   左边的数字代表**指令地址（指令偏移）**，即 PC 寄存器中可能存储的值，然后执行引擎读取 PC 寄存器中的值，并执行该指令

![0009](image/JVMRuntimeDataArea1.assets/0009.webp)



## 两个面试题

**使用PC寄存器存储字节码指令地址有什么用呢？**或者问**为什么使用 PC 寄存器来记录当前线程的执行地址呢？**

1.  因为CPU需要不停的切换各个线程，这时候切换回来以后，就得知道接着从哪开始继续执行
  
2.  JVM的字节码解释器就需要通过改变PC寄存器的值来明确下一条应该执行什么样的字节码指令

![0010](image/JVMRuntimeDataArea1.assets/0010.webp)



**PC寄存器为什么被设定为私有的？**

1.  我们都知道所谓的多线程在一个特定的时间段内只会执行其中某一个线程的方法，CPU会不停地做任务切换，这样必然导致经常中断或恢复，如何保证分毫无差呢？**<font color='red'>为了能够准确地记录各个线程正在执行的当前字节码指令地址，最好的办法自然是为每一个线程都分配一个PC寄存器</font>**，这样一来各个线程之间便可以进行独立计算，从而不会出现相互干扰的情况。
2.  由于CPU时间片轮限制，众多线程在并发执行过程中，任何一个确定的时刻，一个处理器或者多核处理器中的一个内核，只会执行某个线程中的一条指令。
3.  这样必然导致经常中断或恢复，如何保证分毫无差呢？每个线程在创建后，都会产生自己的程序计数器和栈帧，程序计数器在各个线程之间互不影响。

> 注意并行和并发的区别



## CPU 时间片

1.  CPU时间片即CPU分配给各个程序的时间，每个线程被分配一个时间段，称作它的时间片。
  
2.  在宏观上：我们可以同时打开多个应用程序，每个程序并行不悖，同时运行。
  
3.  但在微观上：由于只有一个CPU，一次只能处理程序要求的一部分，如何处理公平，一种方法就是引入时间片，**每个程序轮流执行**。

![0011](image/JVMRuntimeDataArea1.assets/0011.webp)







# 虚拟机栈



## 虚拟机栈基本内容

<font color='red'>首先栈是运行时的单位，而堆是存储的单位。</font>

即：栈解决程序的运行问题，即程序如何执行，或者说如何处理数据。堆解决的是数据存储的问题，即数据怎么放，放哪里

![0001](image/JVMRuntimeDataArea1.assets/0001.webp)





- Java虚拟机栈是什么？
  - Java虚拟机栈（Java Virtual Machine Stack），早期也叫Java栈。**每个线程在创建时都会创建一个虚拟机栈**，其内部**保存一个个的栈帧**（Stack Frame），**对应着一次次的Java方法调用**，栈是线程私有的
- 虚拟机栈的生命周期
  - **生命周期和线程一致**，也就是线程结束了，该虚拟机栈也销毁了

- 虚拟机栈的作用

  - 主管Java程序的运行，它保**存方法的局部变量**（8 种基本数据类型、对象的引用地址）、**部分结果**，并**参与方法的调用和返回**。

  - 局部变量，它是相比于成员变量来说的（或属性）
  - 基本数据类型变量 VS 引用类型变量（类、数组、接口）

举例：

```java
public class StackTest {

    public static void main(String[] args) {
        StackTest test = new StackTest();
        test.methodA();
    }

    public void methodA() {
        int i = 10;
        int j = 20;

        methodB();
    }

    public void methodB(){
        int k = 30;
        int m = 40;
    }
}
```

![0002](image/JVMRuntimeDataArea1.assets/0002.webp)



**虚拟机栈的特点**

- 栈是一种快速有效的分配存储方式，访问速度仅次于程序计数器。

- JVM直接对Java栈的操作只有两个：
  - 每个方法执行，伴随着**进栈**（入栈、压栈）
  - 执行结束后的**出栈**工作

- 对于栈来说不存在垃圾回收问题
  - <font color='red'>栈不需要GC，但是可能存在OOM</font>
  
  

**虚拟机栈的异常**

**面试题：栈中可能出现的异常？**

- Java 虚拟机规范允许Java栈的大小是<font color='cornflowerblue'>动态</font>的或者是<font color='cornflowerblue'>固定不变</font>的。

  - 如果采用固定大小的Java虚拟机栈，那每一个线程的Java虚拟机栈容量可以在线程创建的时候独立选定。如果线程请求分配的栈容量超过Java虚拟机栈允许的最大容量，Java虚拟机将会抛出一个**StackoverflowError** 异常。

  - 如果Java虚拟机栈可以动态扩展，并且在尝试扩展的时候无法申请到足够的内存，或者在创建新的线程时没有足够的内存去创建对应的虚拟机栈，那Java虚拟机将会抛出一个 **OutofMemoryError** 异常。



## 设置栈内存大小

**概念**

> 多去官方文档看看：https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE
>
> 地址经常变

我们可以使用参数 **<font color='red'>-Xss</font>** 选项来设置线程的**最大栈空间**，栈的大小直接决定了函数调用的最大可达深度。

> Sets the thread stack size (in bytes). Append the letter `k` or `K` to indicate KB, `m` or `M` to indicate MB, and `g` or `G` to indicate GB. The default value depends on the platform:
>
> * Linux/x64 (64-bit): 1024 KB
> * macOS (64-bit): 1024 KB
> * Oracle Solaris/x64 (64-bit): 1024 KB
> * Windows: The default value depends on virtual memory



The following examples set the thread stack size to 1024 KB in different units:

```java
-Xss1m
-Xss1024k
-Xss1048576
```

**举例**

![0004](image/JVMRuntimeDataArea1.assets/0004-16890796567761.webp)

```java
public class StackErrorTest {
    private static int count = 1;
    public static void main(String[] args) {
        System.out.println(count);
        count++;
        main(args);
    }
}
```

**没设置参数前**

部分输出结果：

```java
11404
11405
11406
Exception in thread "main" java.lang.StackOverflowError
	at sun.nio.cs.UTF_8$Encoder.encodeLoop(UTF_8.java:691)
```

说明栈在11406这个深度溢出了



**设置栈参数之后**



部分输出结果

```java
2474
2475
2476
Exception in thread "main" java.lang.StackOverflowError
	at sun.nio.cs.UTF_8.updatePositions(UTF_8.java:77)
```

说明参数起作用了



## 栈的存储单位-栈帧

1.  每个线程都有自己的栈，栈中的数据都是以**<font color='red'>栈帧</font>**（Stack Frame）的格式存在
2.  在这个线程上正在执行的<font color='red'>每个方法都各自对应一个栈帧</font>（Stack Frame）。
3.  栈帧是一个内存区块，是一个数据集，维系着方法执行过程中的各种数据信息。



### 栈运行原理

1.  JVM直接对Java栈的操作只有两个，就是对栈帧的**压栈和出栈**，遵循先进后出（后进先出）原则
  
2.  在一条活动线程中，一个时间点上，只会有一个活动的栈帧。即**只有当前正在执行的方法的栈帧（栈顶栈帧）是有效的**。这个栈帧被称为**<font color='cornflowerblue'>当前栈帧</font>（Current Frame）**，与当前栈帧相对应的方法就是**<font color='cornflowerblue'>当前方法</font>（Current Method）**，定义这个方法的类就是**<font color='cornflowerblue'>当前类<（Current Class）**

3.  执行引擎运行的所有字节码指令只针对当前栈帧进行操作。
  
4.  如果在该方法中调用了其他方法，对应的新的栈帧会被创建出来，放在栈的顶端，成为新的当前帧。

![0005](image/JVMRuntimeDataArea1.assets/0005.webp)

1.  **不同线程中所包含的栈帧是不允许存在相互引用的**，即不可能在一个栈帧之中引用另外一个线程的栈帧。
2.  如果当前方法调用了其他方法，方法返回之际，当前栈帧会传回此方法的执行结果给前一个栈帧，接着，虚拟机会丢弃当前栈帧，使得前一个栈帧重新成为当前栈帧。
3.  Java方法有两种返回函数的方式。
    - 一种是正常的函数返回，使用return指令。
    - 另一种是方法执行中出现未捕获处理的异常，以抛出异常的方式结束。
    - 但不管使用哪种方式，都会导致栈帧被弹出。






### 栈帧的内部结构

每个栈帧中存储着：

- <font color='red'>局部变量表</font>（Local Variables）

- <font color='red'>操作数栈</font>（Operand Stack）（或<font color='red'>表达式栈</font>）

- 动态链接（Dynamic Linking）（或指向运行时常量池的方法引用）

- 方法返回地址（Return Address）（或方法正常退出或者异常退出的定义）

- 一些附加信息

![0006](image/JVMRuntimeDataArea1.assets/0006.webp)

并行每个线程下的栈都是私有的，因此每个线程都有自己各自的栈，并且每个栈里面都有很多栈帧，栈帧的大小主要由局部变量表 和 操作数栈决定的



## 局部变量表



**概念**

1.  局部变量表也被称之为局部变量数组或本地变量表
2.  **定义为一个<font color='red'>数字数组</font>，主要用于<font color='red'>存储方法参数和定义在方法体内的局部变量</font>**，这些数据类型包括各类基本数据类型、对象引用（reference），以及returnAddress返回值类型。
3.  由于局部变量表是建立在线程的栈上，是线程的私有数据，因此**<font color='red'>不存在数据安全问题</font>**
4.  **局部变量表所需的容量<font color='red'>大小是在编译期确定下来</font>的**，并保存在方法的Code属性的**maximum local variables**数据项中。在方法<font color='red'>运行期间是不会改变局部变量表的大小</font>的。
5.  方法嵌套调用的次数由栈的大小决定。一般来说，<font color='cornflowerblue'>栈越大，方法嵌套调用次数越多</font>。（主要取决于局部变量表的大小）
    *   对一个函数而言，它的参数和局部变量越多，使得局部变量表膨胀，它的栈帧就越大，以满足方法调用所需传递的信息增大的需求。
    *   进而函数调用就会占用更多的栈空间，导致其嵌套调用次数就会减少。
6.  <font color='cornflowerblue'>局部变量表中的变量只在当前方法调用中有效</font>。
    *   在方法执行时，虚拟机通过使用局部变量表完成参数值到参数变量列表的传递过程。
    *   当方法调用结束后，<font color='cornflowerblue'>随着方法栈帧的销毁，局部变量表也会随之销毁</font>。

**举例**

```java
public class LocalVariablesTest {
    private int count = 0;

    public static void main(String[] args) {
        LocalVariablesTest test = new LocalVariablesTest();
        int num = 10;
        test.test1();
    }

    //练习：
    public static void testStatic(){
        LocalVariablesTest test = new LocalVariablesTest();
        Date date = new Date();
        int count = 10;
        System.out.println(count);
        //因为this变量不存在于当前方法的局部变量表中！！
//        System.out.println(this.count);
    }

    //关于Slot的使用的理解
    public LocalVariablesTest(){
        this.count = 1;
    }

    public void test1() {
        Date date = new Date();
        String name1 = "atguigu.com";
        test2(date, name1);
        System.out.println(date + name1);
    }

    public String test2(Date dateP, String name2) {
        dateP = null;
        name2 = "songhongkang";
        double weight = 130.5;//占据两个slot
        char gender = '男';
        return dateP + name2;
    }

    public void test3() {
        this.count++;
    }

    public void test4() {
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        //变量c使用之前已经销毁的变量b占据的slot的位置
        int c = a + 1;
    }
}
```



看完字节码后，可得结论：所以局部变量表所需的容量大小是在编译期确定下来的。



### Slot槽

<font color='red'>即局部变量表数组的每一个小格</font>

1.  参数值的存放总是从局部变量数组索引 0 的位置开始，到数组长度-1的索引结束。
2.  局部变量表，**<font color='red'>最基本的存储单元是Slot（变量槽）</font>**，局部变量表中存放编译期可知的各种<font color='cornflowerblue'>基本数据类型（8种），引用类型（reference），returnAddress类型的变量</font>。
3.  在局部变量表里，**<font color='red'>32位以内的类型只占用一个slot</font>**（包括returnAddress类型），**<font color='red'>64位的类型占用两个slot</font>**（long和double）。
    - byte、short、char在储存前被转换为int，boolean也被转换为int，0表示false，非0表示true
    - long和double则占据两个slot
4.  JVM会为局部变量表中的每一个Slot都<font color='cornflowerblue'>分配一个访问索引</font>，通过这个索引即可成功访问到局部变量表中指定的局部变量值
5.  当一个实例方法被调用的时候，它的方法参数和方法体内部定义的局部变量将会**<font color='red'>按照顺序被复制</font>**到局部变量表中的每一个slot上
6.  如果需要访问局部变量表中一个64bit的局部变量值时，<font color='red'>只需要使用前一个索引即可</font>。（比如：访问long或double类型变量）
7.  如果当前帧是由构造方法或者实例方法创建的，那么**<font color='red'>该对象引用this将会存放在index为0的slot处</font>**，其余的参数按照参数表顺序继续排列。（this也相当于一个变量），<font color='red'>**即非static方法，this将会被放在index0处**</font>

![](image/JVMRuntimeDataArea1.assets/0014.webp)

**Slot代码示例**

**this 存放在 index = 0 的位置：**

代码

```java
	public void test3() {
        this.count++;
    }
```



局部变量表：this 存放在 index = 0 的位置

![](image/JVMRuntimeDataArea1.assets/0015.webp)



**64位的类型（1ong和double）占用两个slot**

代码

```java
 	public String test2(Date dateP, String name2) {
        dateP = null;
        name2 = "songhongkang";
        double weight = 130.5;//占据两个slot
        char gender = '男';
        return dateP + name2;
    }
```

weight 为 double 类型，index 直接从 3 蹦到了 5

![](image/JVMRuntimeDataArea1.assets/0016.webp)



**static 无法调用 this**

this 不存在与 static 方法的局部变量表中，所以无法调用

```java
    public static void testStatic(){
        LocalVariablesTest test = new LocalVariablesTest();
        Date date = new Date();
        int count = 10;
        System.out.println(count);
        //因为this变量不存在于当前方法的局部变量表中！！
//        System.out.println(this.count);
    }
```



### Slot的重复利用

栈帧中的局部变量表中的槽位是可以重用的，如果一个局部变量过了其作用域，那么在其作用域之后申明新的局部变量变就很有可能会复用过期局部变量的槽位，从而达到节省资源的目的。



代码

```java
    public void test4() {
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        //变量c使用之前已经出了作用域的变量b占据的slot的位置
        int c = a + 1;
    }
```

局部变量 c 重用了局部变量 b 的 slot 位置



**举例：静态变量与局部变量的对比**



```java
变量的分类：
1、按照数据类型分：① 基本数据类型  ② 引用数据类型
2、按照在类中声明的位置分：
  2-1、成员变量：在使用前，都经历过默认初始化赋值
       2-1-1、类变量: linking的prepare阶段：给类变量默认赋值
              ---> initial阶段：给类变量显式赋值即静态代码块赋值
       2-1-2、实例变量：随着对象的创建，会在堆空间中分配实例变量空间，并进行默认赋值
  2-2、局部变量：在使用前，必须要进行显式赋值的！否则，编译不通过。
```



1.  参数表分配完毕之后，再根据方法体内定义的变量的顺序和作用域分配。
2.  我们知道<font color='cornflowerblue'>类变量</font>有两次初始化的机会**，**第一次是在“<font color='red'>准备阶段</font>”，执行系统初始化，对类变量设置零值，另一次则是在“<font color='red'>初始化</font>”阶段，赋予程序员在代码中定义的初始值。
3.  和类变量初始化不同的是，**局部变量表不存在系统初始化的过程**，这意味着一旦定义了<font color='red'>局部变量则必须人为的初始化</font>，否则无法使用。

```java
public void test()	{
	int i;		//报错，因为局部变量必须显式赋值
	System.out.println(i);
}
```



### 补充说明

1.  在栈帧中，<font color='cornflowerblue'>与性能调优关系最为密切的部分就是前面提到的局部变量表</font>。在方法执行时，虚拟机使用局部变量表完成方法的传递。
  
2.  局部变量表中的变量也是重要的垃圾回收<font color='cornflowerblue'>根节点</font>，<font color='red'>只要被局部变量表中直接或间接引用的对象都不会被回收</font>。




## 操作数栈

### 操作数栈的特点

1.  每一个独立的栈帧除了包含局部变量表以外，还包含一个<font color='red'>后进先出</font>（Last - In - First -Out）的 操作数栈，也可以称之为**<font color='red'>表达式栈</font>**（Expression Stack）
  
2.  操作数栈，在方法执行过程中，**根据字节码指令，往栈中写入数据或提取数据**，即入栈（push）和 出栈（pop）
  - 某些字节码指令将值压入操作数栈，其余的字节码指令将操作数取出栈。使用它们后再把结果压入栈，
  - 比如：执行复制、交换、求和等操作

![](image/JVMRuntimeDataArea1.assets/0018.webp)





![](image/JVMRuntimeDataArea1.assets/0019.webp)



### 操作数栈的作用



1.  操作数栈，**<font color='red'>主要用于保存计算过程的中间结果，同时作为计算过程中变量临时的存储空间</font>**。
  
2.  操作数栈就是JVM执行引擎的一个工作区，当一个方法刚开始执行的时候，一个新的栈帧也会随之被创建出来，这时方法的操作数栈是空的。
  
3.  每一个操作数栈都会拥有一个明确的栈深度用于存储数值，其所需的<font color='red'>最大深度在编译期就定义好了</font>，保存在方法的Code属性中，为**maxstack**的值。
  
4.  栈中的任何一个元素都是可以任意的Java数据类型
  
    *   32bit的类型占用一个栈单位深度
    *   64bit的类型占用两个栈单位深度
5.  操作数栈<font color='red'>并非采用访问索引的方式来进行数据访问的</font>，而是只能通过标准的入栈和出栈操作来完成一次数据访问。
  
6.  <font color='red'>如果被调用的方法带有返回值的话，其返回值将会被压入当前栈帧的操作数栈中</font>，并更新PC寄存器中下一条需要执行的字节码指令。
  
7.  操作数栈中元素的数据类型必须与字节码指令的序列严格匹配，这由编译器在编译器期间进行验证，同时在类加载过程中的类检验阶段的数据流分析阶段要再次验证。
  
8.  另外，**我们说Java虚拟机的解释引擎是基于栈的执行引擎，其中的栈指的就是操作数栈**。





### 操作数栈代码追踪

```java
	public void testAddOperation() {
        //byte、short、char、boolean：都以int型来保存
        byte i = 15;
        int j = 8;
        int k = i + j;

       // int m = 800;

    }
```

对应字节码指令

```java
 0 bipush 15
 2 istore_1
 3 bipush 8
 5 istore_2
 6 iload_1
 7 iload_2
 8 iadd
 9 istore_3
10 return
```

![](image/JVMRuntimeDataArea1.assets/0021.webp)

**一步一步看流程**

1、首先执行第一条语句，PC寄存器指向的是0，也就是指令地址为0，然后使用bipush让操作数15入操作数栈。

![](image/JVMRuntimeDataArea1.assets/0022.webp)

2、执行完后，PC寄存器往下移，指向下一行代码，下一行代码就是将操作数栈的元素存储到局部变量表1的位置（istore_1），我们可以看到局部变量表的已经增加了一个元素。并且操作数栈为空了

*   解释为什么局部变量表索引从 1 开始，因为该方法为实例方法，局部变量表索引为 0 的位置存放的是 this

![](image/JVMRuntimeDataArea1.assets/0023.webp)

3、然后PC下移，指向的是下一行。让操作数8也入栈，同时执行store操作，存入局部变量表中

![](image/JVMRuntimeDataArea1.assets/0024.webp)



4、然后从局部变量表中，依次将数据放在操作数栈中，等待执行 add 操作

iload_1：取出局部变量表中索引为1的数据入操作数栈

![](image/JVMRuntimeDataArea1.assets/0025.webp)

5、然后将操作数栈中的两个元素执行相加操作，并存储在局部变量表3的位置

![](image/JVMRuntimeDataArea1.assets/0026.webp)



**小问题**

**关于类型转换的说明**

![](image/JVMRuntimeDataArea1.assets/0027.webp)



*   因为 8 可以存放在 byte 类型中，所以压入操作数栈的类型为 byte ，而不是 int ，所以执行的字节码指令为 bipush 8
*   但是存储在局部变量的时候，会转成 int 类型的变量：istore_4



![](image/JVMRuntimeDataArea1.assets/0028.webp)

- m改成800之后，byte存储不了，就成了short型，sipush 800





**如果被调用的方法带有返回值，返回值入操作数栈**

```java
  public int getSum(){
        int m = 10;
        int n = 20;
        int k = m + n;
        return k;
    }

    public void testGetSum(){
        //获取上一个栈桢返回的结果，并保存在操作数栈中
        int i = getSum();
        int j = 10;
    }
```



getSum() 方法字节码指令：最后带着个 ireturn

![](image/JVMRuntimeDataArea1.assets/0029.webp)

testGetSum() 方法字节码指令：一上来就加载 getSum() 方法的返回值()

![](image/JVMRuntimeDataArea1.assets/0030.webp)





### 栈顶缓存技术

**栈顶缓存技术：Top Of Stack Cashing**

1.  前面提过，基于栈式架构的虚拟机所使用的零地址指令更加紧凑，但完成一项操作的时候必然需要使用更多的入栈和出栈指令，这同时也就意味着将需要更多的指令分派（instruction dispatch）次数（也就是你会发现指令很多）和导致内存读/写次数多，效率不高。
  
2.  由于操作数是存储在内存中的，因此频繁地执行内存读/写操作必然会影响执行速度。为了解决这个问题，HotSpot JVM的设计者们提出了栈顶缓存（Tos，Top-of-Stack Cashing）技术，**将栈顶元素全部缓存在物理CPU的寄存器中，以此降低对内存的读/写次数，提升执行引擎的执行效率。**
  
3.  寄存器的主要优点：指令更少，执行速度快，但是指令集（也就是指令种类）很多




## 动态链接

**动态链接（或指向运行时常量池的方法引用）**

1.  每一个栈帧内部都包含**<font color='red'>一个指向运行时常量池中该栈帧所属方法的引用</font>**。包含这个引用的目的就是**为了支持当前方法的代码能够实现<font color='red'>动态链接</font>**（Dynamic Linking），比如：invokedynamic指令
  
3.  在Java源文件被编译到字节码文件中时，所有的变量和方法引用都作为符号引用（Symbolic Reference）保存在class文件的常量池里。比如：描述一个方法调用了另外的其他方法时，就是通过常量池中指向方法的符号引用来表示的，那么**<font color='red'>动态链接的作用就是为了将这些符号引用转换为调用方法的直接引用</font>**



![image-20230711214051104](image/JVMRuntimeDataArea1.assets/image-20230711214051104.webp)



**为什么要用常量池呢？**

1.  因为在不同的方法，都可能调用常量或者方法，所以只需要存储一份即可，然后记录其引用即可，节省了空间。
  
2.  常量池的作用：就是为了提供一些符号和常量，便于指令的识别



### 静态链接与动态链接

在JVM中，将符号引用转换为调用方法的直接引用与方法的绑定机制相关

- **静态链接**：

当一个字节码文件被装载进JVM内部时，如果被调用的<font color='cornflowerblue'>目标方法在编译期确定，且运行期保持不变时</font>，这种情况下将调用方法的符号引用转换为直接引用的过程称之为静态链接

- **动态链接**：

如果<font color='cornflowerblue'>被调用的方法在编译期无法被确定下</font>来，也就是说，只能够在程序运行期将调用的方法的符号转换为直接引用，由于这种引用转换过程具备动态性，因此也被称之为动态链接。（<font color='red'>如多态的使用和lamda表达式</font>）



### 早期绑定与晚期绑定

> 静态链接与动态链接针对的是方法。早期绑定和晚期绑定范围更广。早期绑定涵盖了静态链接，晚期绑定涵盖了动态链接。

静态链接和动态链接对应的方法的绑定机制为：早期绑定（Early Binding）和晚期绑定（Late Binding）。**绑定是一个字段、方法或者类在符号引用被替换为直接引用的过程**，这仅仅发生一次。

- **早期绑定**

早期绑定就是指被调用的目标方法如果在<font color='cornflowerblue'>编译期可知，且运行期保持不变时</font>，即可将这个方法与所属的类型进行绑定，这样一来，由于明确了被调用的目标方法究竟是哪一个，因此也就**可以使用静态链接的方式将符号引用转换为直接引用**。

- **晚期绑定**

如果被调用的方法<font color='cornflowerblue'>在编译期无法被确定下来</font>，**只能够在程序运行期根据实际的类型绑定相关的方法**，这种绑定方式也就被称之为晚期绑定。



```java
class Animal {

    public void eat() {
        System.out.println("动物进食");
    }
}

interface Huntable {
    void hunt();
}

class Dog extends Animal implements Huntable {
    @Override
    public void eat() {
        System.out.println("狗吃骨头");
    }

    @Override
    public void hunt() {
        System.out.println("捕食耗子，多管闲事");
    }
}

class Cat extends Animal implements Huntable {

    public Cat() {
        super();//表现为：早期绑定
    }

    public Cat(String name) {
        this();//表现为：早期绑定
    }

    @Override
    public void eat() {
        super.eat();//表现为：早期绑定
        System.out.println("猫吃鱼");
    }

    @Override
    public void hunt() {
        System.out.println("捕食耗子，天经地义");
    }
}

public class AnimalTest {
    public void showAnimal(Animal animal) {
        animal.eat();//表现为：晚期绑定
    }

    public void showHunt(Huntable h) {
        h.hunt();//表现为：晚期绑定
    }
}

```

部分字节码

```java
{
  public com.atguigu.java2.AnimalTest();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 54: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/atguigu/java2/AnimalTest;

  public void showAnimal(com.atguigu.java2.Animal);
    descriptor: (Lcom/atguigu/java2/Animal;)V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=2, args_size=2
         0: aload_1
         1: invokevirtual #2                  // Method com/atguigu/java2/Animal.eat:()V
         4: return
      LineNumberTable:
        line 56: 0
        line 57: 4
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/atguigu/java2/AnimalTest;
            0       5     1 animal   Lcom/atguigu/java2/Animal;

  public void showHunt(com.atguigu.java2.Huntable);
    descriptor: (Lcom/atguigu/java2/Huntable;)V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=2, args_size=2
         0: aload_1
         1: invokeinterface #3,  1            // InterfaceMethod com/atguigu/java2/Huntable.hunt:()V
         6: return
      LineNumberTable:
        line 60: 0
        line 61: 6
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       7     0  this   Lcom/atguigu/java2/AnimalTest;
            0       7     1     h   Lcom/atguigu/java2/Huntable;
}
SourceFile: "AnimalTest.java"
```



invokevirtual 体现为晚期绑定

invokeinterface 也体现为晚期绑定

invokespecial 体现为早期绑定




### 虚方法与非虚方法

**虚方法与非虚方法的区别**

1.  如果方法在编译期就确定了具体的调用版本，这个版本在运行时是不可变的。这样的方法称为非虚方法。
2.  <font color='red'>静态方法、私有方法、final方法、实例构造器、父类方法</font>都是非虚方法。
3.  其他方法称为虚方法。



**子类对象的多态的使用前提：**

1.  类的继承关系
2.  方法的重写



**虚拟机中调用方法的指令**

- **普通指令：**

1.  invokestatic：调用静态方法，解析阶段确定唯一方法版本
2.  invokespecial：调用<init>方法、私有及父类方法，解析阶段确定唯一方法版本             <font color='cornflowerblue'>1和2是非虚方法</font>
3.  invokevirtual：调用所有虚方法
4.  invokeinterface：调用接口方法            <font color='cornflowerblue'> 3和4除final修饰的方法，都是虚方法</font>

- **动态调用指令**

invokedynamic：动态解析出需要调用的方法，然后执行            <font color='cornflowerblue'>虚方法</font>



前四条指令固化在虚拟机内部，方法的调用执行不可人为干预。而invokedynamic指令则支持由用户确定方法版本。<font color='red'>其中invokestatic指令和invokespecial指令调用的方法称为非虚方法，其余的（final修饰的除外）称为虚方法。</font>



**举例**

```java
class Father {
    public Father() {
        System.out.println("father的构造器");
    }

    public static void showStatic(String str) {
        System.out.println("father " + str);
    }

    public final void showFinal() {
        System.out.println("father show final");
    }

    public void showCommon() {
        System.out.println("father 普通方法");
    }
}

public class Son extends Father {
    public Son() {
        //invokespecial
        super();
    }

    public Son(int age) {
        //invokespecial
        this();
    }

    //不是重写的父类的静态方法，因为静态方法不能被重写！
    public static void showStatic(String str) {
        System.out.println("son " + str);
    }

    private void showPrivate(String str) {
        System.out.println("son private" + str);
    }

    public void show() {
        //invokestatic
        showStatic("atguigu.com");
        //invokestatic
        super.showStatic("good!");
        //invokespecial
        showPrivate("hello!");
        //invokespecial
        super.showCommon();

        //invokevirtual
        showFinal();//因为此方法声明有final，不能被子类重写，所以也认为此方法是非虚方法。
        //虚方法如下：
        
        /*
        invokevirtual  你没有显示的加super.，编译器认为你可能调用子类的showCommon(即使son子类没有重写，也会认为)，所以编译期间确定不下来，就是虚方法。
        */
        showCommon();
        info();

        MethodInterface in = null;
        //invokeinterface
        in.methodA();
    }

    public void info() {

    }

    public void display(Father f) {
        f.showCommon();
    }

    public static void main(String[] args) {
        Son so = new Son();
        so.show();
    }
}

interface MethodInterface {
    void methodA();
}
```

Son 类中 show() 方法的字节码指令如下

![](image/JVMRuntimeDataArea1.assets/0032.webp)



**关于 invokedynamic 指令**

1.  JVM字节码指令集一直比较稳定，一直到Java7中才增加了一个invokedynamic指令，这是<font color='red'>Java为了实现【动态类型语言】支持而做的一种改进</font>。
  
2.  但是在Java7中并没有提供直接生成invokedynamic指令的方法，需要借助ASM这种底层字节码工具来产生invokedynamic指令。直到<font color='red'>Java8的Lambda表达式的出现，invokedynamic指令的生成</font>，在Java中才有了直接的生成方式。
  
3.  Java7中增加的动态语言类型支持的本质是对Java虚拟机规范的修改，而不是对Java语言规则的修改，这一块相对来讲比较复杂，增加了虚拟机中的方法调用，最直接的受益者就是运行在Java平台的动态语言的编译器。


```java
@FunctionalInterface
interface Func {
    public boolean func(String str);
}

public class Lambda {
    public void lambda(Func func) {
        return;
    }

    public static void main(String[] args) {
        Lambda lambda = new Lambda();

        Func func = s -> {
            return true;
        };

        lambda.lambda(func);

        lambda.lambda(s -> {
            return true;
        });
    }
}
```



**动态语言和静态语言**

1.  动态类型语言和静态类型语言两者的区别就在于**对类型的检查是在编译期还是在运行期**，满足前者就是静态类型语言，反之是动态类型语言。
  
2.  说的再直白一点就是，静态类型语言是判断变量自身的类型信息；动态类型语言是判断变量值的类型信息，变量没有类型信息，变量值才有类型信息，这是动态语言的一个重要特征。

Java：String info = "mogu blog";     		(Java是静态类型语言的，会先编译就进行类型检查)
    JS：var name = "shkstart";    var name = 10;	（运行时才进行检查）
    

    Python: info = 130.5 (运行时才检查)



### 虚方法表



1.  在面向对象的编程中，会很频繁的使用到**动态分派**，如果在每次动态分派的过程中都要重新在类的方法元数据中搜索合适的目标的话就可能影响到执行效率。因此，<font color='cornflowerblue'>为了提高性能</font>，**JVM采用在类的<font color='red'>方法区建立一个虚方法表</font>（virtual method table）来实现**，<font color='red'>非虚方法不会出现在表中</font>。<font color='red'>使用索引表来代替查找</font>。【上面动态分派的过程，我们可以看到如果子类找不到，还要从下往上找其父类，非常耗时】
  
3.  <font color='cornflowerblue'>每个类中都有一个虚方法表</font>，表中存放着各个方法的实际入口。
  
4.  虚方法表是什么时候被创建的呢？虚方法表会在<font color='cornflowerblue'>类加载的链接阶段被创建并开始初始化</font>，类的变量初始值准备完成之后，JVM会把该类的虚方法表也初始化完毕。（<font color='cornflowerblue'>类的准备阶段完成后成表</font>）

  

**例子**

如图所示：如果类中重写了方法，那么调用的时候，就会直接在该类的虚方法表中查找

![](image/JVMRuntimeDataArea1.assets/0034.webp)

1、比如说son在调用toString的时候，Son没有重写过，Son的父类Father也没有重写过，那就直接调用Object类的toString。那么就直接在虚方法表里指明toString直接指向Object类。

2、下次Son对象再调用toString就直接去找Object，不用先找Son-->再找Father-->最后才到Object的这样的一个过程。



## 方法返回地址

![](image/JVMRuntimeDataArea1.assets/0039.webp)

> 在一些帖子里，方法返回地址、动态链接、一些附加信息  也叫做帧数据区

1.  存放<font color='red'>调用该方法的pc寄存器的值</font>。一个方法的结束，有两种方式：
  
    *   正常执行完成
    *   出现未处理的异常，非正常退出
2.  无论通过哪种方式退出，在方法退出后都返回到该方法被调用的位置。方法正常退出时，**<font color='red'>调用者（即外层方法）的pc计数器的值作为返回地址，即调用该方法的指令的下一条指令的地址</font>**。而通过异常退出的，返回地址是要通过异常表来确定，栈帧中一般不会保存这部分信息。
3.  本质上，方法的退出就是当前<font color='cornflowerblue'>栈帧出栈</font>的过程。此时，需要<font color='red'>恢复上层方法的局部变量表、操作数栈、将返回值压入调用者栈帧的操作数栈、设置PC寄存器值</font>等，让**调用者方法继续执行下去**。
4.  正常完成出口和异常完成出口的区别在于：<font color='red'>通过异常完成出口退出的不会给他的上层调用者产生任何的返回值。</font>




**方法退出的两种方式**

当一个方法开始执行后，只有两种方式可以退出这个方法，

**正常退出：**

1.  执行引擎遇到任意一个方法返回的字节码指令（return），会有返回值传递给上层的方法调用者，简称**正常完成出口**；
2.  一个方法在正常调用完成之后，究竟需要使用哪一个返回指令，还需要根据方法返回值的实际数据类型而定。
3. 在字节码指令中，返回指令包含：
    - ireturn：当返回值是boolean，byte，char，short和int类型时使用

    - lreturn：Long类型

    - freturn：Float类型

    - dreturn：Double类型

    - areturn：引用类型

    - return：返回值类型为void的方法、实例初始化方法、类和接口的初始化方法



**异常退出：**

1.  在方法执行过程中遇到异常（Exception），并且这个异常没有在方法内进行处理，也就是只要在本方法的异常表中没有搜索到匹配的异常处理器，就会导致方法退出，简称**异常完成出口**。
  
2.  方法执行过程中，抛出异常时的异常处理，存储在一个异常处理表，方便在发生异常的时候找到处理异常的代码


![](image/JVMRuntimeDataArea1.assets/0040.webp)



异常处理表：

*   反编译字节码文件，可得到 Exception table
*   from ：字节码指令起始地址
*   to ：字节码指令结束地址
*   target ：出现异常跳转至地址为 11 的指令执行
*   type ：捕获异常的类型

![](image/JVMRuntimeDataArea1.assets/0041.webp)



## 一些附加信息

栈帧中还允许携带与Java虚拟机实现相关的一些附加信息。例如：对程序调试提供支持的信息。



## 栈相关面试题

**举例栈溢出的情况？**

SOF（StackOverflowError），栈大小分为固定的，和动态变化。如果是固定的就可能出现StackOverflowError。如果是动态变化的，内存不足时就可能出现OOM



**调整栈大小，就能保证不出现溢出么？**

不能保证不溢出，只能保证SOF出现的几率小



**分配的栈内存越大越好么？**

不是，一定时间内降低了OOM概率，但是会挤占其它的线程空间，因为整个虚拟机的内存空间是有限的



**垃圾回收是否涉及到虚拟机栈？**

不会



| 位置                                        | 是否有Error  | 是否存在GC |
| ------------------------------------------- | ------------ | ---------- |
| PC计数器                                    | 无           | 不存在     |
| 虚拟机栈                                    | 有，SOF、OOM | 不存在     |
| 本地方法栈(在HotSpot的实现中和虚拟机栈一样) |              |            |
| 堆                                          | 有，OOM      | 存在       |
| 方法区                                      | 有           | 存在       |



**方法中定义的局部变量是否线程安全？**

具体问题具体分析

1.  如果只有一个线程才可以操作此数据，则必是线程安全的。
2.  如果有多个线程操作此数据，则此数据是共享数据。如果不考虑同步机制的话，会存在线程安全问题。

**具体问题具体分析：**

* 如果对象是在内部产生，并在内部消亡，没有返回到外部，那么它就是线程安全的，反之则是线程不安全的。

    


```java
/**
 * 面试题：
 * 方法中定义的局部变量是否线程安全？具体情况具体分析
 *
 *   何为线程安全？
 *      如果只有一个线程才可以操作此数据，则必是线程安全的。
 *      如果有多个线程操作此数据，则此数据是共享数据。如果不考虑同步机制的话，会存在线程安全问题。
 */
public class StringBuilderTest {

    int num = 10;

    //s1的声明方式是线程安全的（只在方法内部用了）
    public static void method1(){
        //StringBuilder:线程不安全
        StringBuilder s1 = new StringBuilder();
        s1.append("a");
        s1.append("b");
        //...
    }
    //sBuilder的操作过程：是线程不安全的（作为参数传进来，可能被其它线程操作）
    public static void method2(StringBuilder sBuilder){
        sBuilder.append("a");
        sBuilder.append("b");
        //...
    }
    //s1的操作：方法内部是线程安全的，外部是不安全的（有返回值，可能被其它线程操作）
    public static StringBuilder method3(){
        StringBuilder s1 = new StringBuilder();
        s1.append("a");
        s1.append("b");
        return s1;
    }
    //s1的操作：是线程安全的（s1自己消亡了，最后返回的只是s1.toString的一个新对象）
    public static String method4(){
        StringBuilder s1 = new StringBuilder();
        s1.append("a");
        s1.append("b");
        return s1.toString();
    }

    public static void main(String[] args) {
        StringBuilder s = new StringBuilder();

        new Thread(() -> {
            s.append("a");
            s.append("b");
        }).start();

        method2(s);

    }
}
```



# 本地方法接口

## 本地方法



1. 简单地讲，**<font color='cornflowerblue'>一个Native Method是一个Java调用非Java代码的接囗</font>**一个Native Method是这样一个Java方法：
1. <font color='cornflowerblue'>该方法的实现由非Java语言实现</font>，比如<font color='cornflowerblue'>**C**</font>。这个特征并非Java所特有，很多其它的编程语言都有这一机制，比如在C++中，你可以用extern 告知C++编译器去调用一个C的函数。
4. “A native method is a Java method whose implementation is provided by non-java code.”（本地方法是一个非Java的方法，它的具体实现是非Java代码的实现）
5. 在定义<font color='cornflowerblue'>一个native method时，并不提供实现体（不提供并非没有）</font>（有些像定义一个Java interface），因为其实现体是由非java语言在外面实现的。
6. 本地接口的作用是融合不同的编程语言为Java所用，它的<font color='cornflowerblue'>初衷是融合C/C++程序</font>。



**举例**

需要注意的是：<font color='red'>标识符native可以与其它java标识符连用，但是abstract除外</font>

```java
public class IHaveNatives {
    public native void Native1(int x);

    public native static long Native2();

    private native synchronized float Native3(Object o);

    native void Native4(int[] ary) throws Exception;
}
```



**为什么要使用 Native Method？**

Java使用起来非常方便，然而有些层次的任务用Java实现起来不容易，或者我们对程序的效率很在意时，问题就来了。



- **与Java环境外交互**

**有时Java应用需要与Java外面的硬件环境交互，这是本地方法存在的主要原因**。你可以想想Java需要与一些**底层系统**，如操作系统或某些硬件交换信息时的情况。本地方法正是这样一种交流机制：它为我们提供了一个非常简洁的接口，而且我们无需去了解Java应用之外的繁琐的细节。



- **与操作系统的交互**

1. JVM支持着Java语言本身和运行时库，它是Java程序赖以生存的平台，它由一个解释器（解释字节码）和一些连接到本地代码的库组成。
2. 然而不管怎样，它毕竟不是一个完整的系统，它经常依赖于一底层系统的支持。这些底层系统常常是强大的操作系统。
3. **通过使用本地方法，我们得以用Java实现了jre的与底层系统的交互，甚至JVM的一些部分就是用C写的**。
4. 还有，如果我们要使用一些Java语言本身没有提供封装的操作系统的特性时，我们也需要使用本地方法。



- **Sun’s Java**

1. Sun的解释器是用C实现的，这使得它能像一些普通的C一样与外部交互。jre大部分是用Java实现的，它也通过一些本地方法与外界交互。
2. 例如：类java.lang.Thread的setPriority()方法是用Java实现的，但是它实现调用的是该类里的本地方法setPriority0()。这个本地方法是用C实现的，并被植入JVM内部在Windows 95的平台上，这个本地方法最终将调用Win32 setpriority() API。这是一个本地方法的具体实现由JVM直接提供，更多的情况是本地方法由外部的动态链接库（external dynamic link library）提供，然后被JVM调用。



**本地方法的现状**

目前该方法使用的越来越少了，除非是与硬件有关的应用，比如通过Java程序驱动打印机或者Java系统管理生产设备，在企业级应用中已经比较少见。因为现在的异构领域间的通信很发达，比如可以使用Socket通信，也可以使用Web Service等等，不多做介绍。

1. 当某个线程调用一个本地方法时，它就进入了一个全新的并且不再受虚拟机限制的世界。它和虚拟机拥有同样的权限。
   * <font color='red'>本地方法可以通过本地方法接口来访问虚拟机内部的运行时数据区。</font>
   * <font color='red'>它甚至可以直苦使处理器中的寄存器</font>
   * <font color='red'>直接从本地内存的堆中分配任意数量的内存。</font>
   * <font color='cornflowerblue'>本地方法栈通过本地方法接口访问运行时数据区</font>
2. 并不是所有的JVM都支持本地方法。因为Java虚拟机规范并没有明确要求本地方法栈的使用语言、具体实现方式、数据结构等。如果JVM产品不打算支持native方法，也可以无需实现本地方法栈。
3. 在Hotspot JVM中支持，并且直接将木地方法栈和虚拟机栈合二为一。



## 本地方法栈

1. **Java虚拟机栈于管理Java方法的调用，而本地方法栈用于管理本地方法的调用**。
2. 本地方法栈，**也是线程私有的**。
3. 允许被实现成**固定**或者是可**动态扩展**的内存大小（在内存溢出方面和虚拟机栈相同）
   * 如果线程请求分配的栈容量超过本地方法栈允许的最大容量，Java虚拟机将会抛出一个stackoverflowError 异常。
   * 如果本地方法栈可以动态扩展，并且在尝试扩展的时候无法申请到足够的内存，或者在创建新的线程时没有足够的内存去创建对应的本地方法栈，那么Java虚拟机将会抛出一个outofMemoryError异常。
4. 本地方法一般是使用C语言或C++语言实现的。
5. 它的具体做法是Native Method Stack中登记native方法，在Execution Engine 执行时加载本地方法库。









PC Register没有GC和OOM

Java栈没有GC，有OOM

本地方法栈没有GC，有OOM
