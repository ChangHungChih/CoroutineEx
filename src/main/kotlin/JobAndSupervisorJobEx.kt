import kotlinx.coroutines.*

fun main() = runBlocking {
    testJob()
//    testSuperVisorJob()
}

fun testJob() = runBlocking {
    with(scope) {
        launch {
            println("work1")
            suspend1()
            suspend2()
            suspend3()
        }.join()

        launch {
            println("work2")
        }.join()
    }
    // if exception happened, parent job will cancel all sub coroutines
}

fun testSuperVisorJob() = runBlocking {
    job = SupervisorJob()
    with(scope) {
        launch {
            println("work1")
            suspend1()
            suspend2()
            suspend3()
        }.join()

        launch {
            println("work2")
        }.join()
    }
    // supervisorJob let every sub coroutine independent
}

private suspend fun suspend1() {
    delay(100)
    println("suspend1")
}

private suspend fun suspend2() {
    delay(500)
    println("suspend2")
    throw RuntimeException()
}

private suspend fun suspend3() {
    delay(200)
    println("suspend3")
}

private var job: Job = Job()
private val scope
    get() = CoroutineScope(Dispatchers.Default + job + CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    })
