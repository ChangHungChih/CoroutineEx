import kotlinx.coroutines.*

//  Coroutine Builder ： launch() 、 async()
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
    coroutineScope {
        launchSuspend()
    }
    println("Done")
    // coroutineScope { } won't build new coroutine, just inherit outside scope
    // Coroutine context = same with outside scope
    // print and launchSuspend are in same coroutine
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

// launch() async() will execute immediately, unless use CoroutineStart.LAZY
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