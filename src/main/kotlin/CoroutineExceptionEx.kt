import kotlinx.coroutines.*

fun main()= runBlocking {
//    testException()
//    testHandleExceptionWithTryCatch()
//    `test handle exception with CoroutineExceptionHandler`()

//    `test async exception`()
    `test async exception handle`()
}


// CoroutineException --------------------------------------------------------------
fun testException() = runBlocking {
    val job = launch {
        launch {
            try {
                println("First children start")
                delay(1000)
                println("First children done")
            } finally {
                println("First children are cancelled")
            }
        }

        launch {
            println("Second child start")
            delay(100)
            println("Second child throws an exception")
            throw RuntimeException()
        }
    }
    job.join()
    // if exception happened, parent job will cancel all sub coroutines
}

fun testHandleExceptionWithTryCatch() = runBlocking {
    val job = launch {
        launch {
            try {
                println("First children start")
                delay(1000)
                println("First children done")
            } finally {
                println("First children are cancelled")
            }
        }

        launch {
            println("Second child start")
            delay(100)
            println("Second child throws an exception")
            try {
                throw RuntimeException()
            } catch (e: Exception) {
                println("Catch exception")
            }
        }
    }
    job.join()
}

private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
}
private val scopeWithExceptionHandler = CoroutineScope(coroutineExceptionHandler)

fun `test handle exception with CoroutineExceptionHandler`() = runBlocking {
    val job = scopeWithExceptionHandler.launch {
        launch {
            try {
                println("First children start")
                delay(1000)
                println("First children done")
            } finally {
                println("First children are cancelled")
            }
        }

        launch {
            println("Second child start")
            delay(100)
            println("Second child throws an exception")
            throw RuntimeException()
        }
    }
    job.join()
    //unhandled exception will all send to coroutineExceptionHandler
}

private suspend fun asyncException(): Int {
    val deferred1 = scopeWithExceptionHandler.async {
        delay(100)
        10
    }
    val deferred2 = scopeWithExceptionHandler.async<Int> {
        delay(200)
        20
        throw RuntimeException("Incorrect")
    }
    return deferred1.await() + deferred2.await()
}

fun `test async exception`() = runBlocking {
    println(asyncException())
    // async cannot handle exception by coroutineExceptionHandler
}

fun `test async exception handle`() = runBlocking {
    try {
        println(asyncException())
    } catch (e: Exception) {
        println("${e.message}")
    }
}