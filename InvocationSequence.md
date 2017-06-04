## gamma launch 启动器
+ code [right here](https://github.com/NagaseMinato/openjdk/blob/jdk7u/jdk7u/hotspot/src/share/tools/launcher/java.c#L389)

0. leave标签，launch很多分支出现错误都将goto至此。详细代码如下：
```cpp
 leave:
    /*
     * Wait for all non-daemon threads to end, then destroy the VM.
     * This will actually create a trivial new Java waiter thread
     * named "DestroyJavaVM", but this will be seen as a different
     * thread from the one that executed main, even though they are
     * the same C thread.  This allows mainThread.join() and
     * mainThread.isAlive() to work as expected.
     */
    (*vm)->DestroyJavaVM(vm);

    if(message != NULL && !noExitErrorMessage)
      ReportErrorMessage(message, messageDest);
    return ret; //ret == -1
```

1. main

：函数最后调用return ContinueInNewThread(JavaMain, threadStackSize, (void*)&args);启动新线程并运行JavaMain函数，该函数将伴随应用程序整个生命周期

2. JavaMain：

用`InitializeJVM(&vm, &env, &ifn)`初始化三个最重要的结构体(InvocationFunctions)ifn,(JavaVM)vm,(JNIEnv)env

`mainClass = LoadClass(env, classname);`获取main class

`mainID = (*env)->GetStaticMethodID(env, mainClass, "main","([Ljava/lang/String;)V");`使用mainClass获取static void main方法

检查main method是否为public，然后构建arguments数组

`(*env)->CallStaticVoidMethod(env, mainClass, mainID, mainArgs);`传参调用static void main，

`(*vm)->DetachCurrentThread(vm)`与主线程断开，这里不太明白

上面一步执行完会主动`goto leave;`，然后等待non-daemon线程退出，最后创建DestoryJVM线程执行销毁JVM任务






