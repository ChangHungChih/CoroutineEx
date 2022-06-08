import kotlinx.coroutines.*

fun main() = runBlocking {
//    launchEx()
//    coroutineScopeEx()
//    jobEx()
//    joinEx()
//    asyncAwaitEx()
//    onlyAsyncEx()
    lazyStarterEx()
}

suspend fun launchSuspend() {
    println("launch Start")
    delay(500)
    println("launch End")
}

fun launchEx() = runBlocking {
    // outside coroutine will wait all sub coroutine finished
    launch { //Coroutine context = Dispatchers.Default (run at background)
        launchSuspend()
    }
    println("Done")
}

fun coroutineScopeEx() = runBlocking {
    coroutineScope { //Coroutine context = same with outside scope
        launchSuspend()
    }
    println("Done")
}

// https://i.imgur.com/1vOFhPL.png
fun jobEx() = runBlocking {
    val job = launch {
        repeat(10_000) { index ->
            println("launch Start $index")
            delay(500)
            println("launch End $index")
        }
    }

    delay(1100)
    job.cancel()
    println("Done")
}


fun joinEx() = runBlocking {
    val job = launch {
        launchSuspend()
    }
    job.join()
    // join multiple job
    // joinAll(job)
    println("Done")
}

fun asyncAwaitEx() = runBlocking {
    val deferred = async {
        println("async start")
        delay(500)
        println("async end")
        50
    }
    //similar with join()
    val deferredValue = deferred.await()
    println("done $deferredValue")
}

fun onlyAsyncEx() = runBlocking {
    val deferred = async {
        println("async start")
        delay(500)
        println("async end")
        50
    }
    //similar with launch
    println("Done")
}

// launch() 、 async() ，我們知道當程式執行到這邊的時候，就會將這兩個 builder 所建造出來的 coroutine 排進執行的行程中。所以它們預設是立刻就被呼叫的。
//加上 CoroutineStart.LAZY 之後， launch() 裏面的任務就不會立刻執行了。不過，如果沒有啟動 launch() 那麼程式就會在這邊一直等它執行。
fun lazyStarterEx() = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        launchSuspend()
    }

    job.start() // won't start if not call
//        job.join()  // equal function
    println("Done")
}

// launch() async() difference is return value
// both launch() async() return cancelable background mission

// Job , Deferred has same function cancel() 、 start() 、 join()
// cancel to cancel a coroutine,
// start to start a coroutine
// join to wait coroutine

// launch() async() will execute immediately, unless use CoroutineStart.LAZY