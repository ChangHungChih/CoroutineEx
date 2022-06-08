import kotlinx.coroutines.*

fun main() = runBlocking {
    test()
    testCancel1()
    testCancel2()
    testCancelAll()
}

fun test() = runBlocking {
    val job1 = launch {
        println("Job1 start")
        delay(100)
        println("Job1 done")
    }

    val job2 = launch {
        println("Job2 start")
        delay(1000)
        println("Job2 done")
    }
    println("Outer coroutine done")
}

fun testCancel1() = runBlocking {
    val job1 = launch {
        println("Job1 start")
        delay(100)
        println("Job1 done")
    }

    val job2 = launch {
        println("Job2 start")
        delay(1000)
        println("Job2 done")
    }
    println("Outer coroutine done")
    delay(300)
    job2.cancel()
}

fun testCancel2() = runBlocking {
    val job1 = launch {
        println("Job1 start")
        delay(1000)
        println("Job1 done")
    }

    val job2 = launch {
        println("Job2 start")
        delay(1000)
        println("Job2 done")
    }
    println("Outer coroutine done")
    delay(300)
    job2.cancel()
    job1.cancel()
}

fun testCancelAll() = runBlocking {
    val parentJob = launch {
        println("parentJob start")

        val job1 = launch {
            println("Job1 start")
            delay(1000)
            println("Job1 done")
        }

        val job2 = launch {
            println("Job2 start")
            delay(1000)
            println("Job2 done")
        }

        println("parentJob done")
    }
    println("Outer coroutine done")
    delay(300)
    parentJob.cancel()
    // cancel parent coroutine will cancel all sub coroutines
}


