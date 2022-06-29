import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() {
//    testFlowBuilder()
//    testOperator()
//    testFlowOn()
//    testSharedFlow()
    testStateFlow()
}

private fun getSampleFlow() = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).asFlow()

fun testFlowBuilder() {
    runBlocking {
        val flow1 = flow {
            repeat(10) {
                delay(100)
                emit(it)
            }
        }
        flow1.collect { value ->
            println("flow1:$value")
        }

        val flow2 = flowOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        flow2.collect { value ->
            println("flow2:$value")
        }

        val flow3 = getSampleFlow()
        flow3.collect { value ->
            println("flow3:$value")
        }
    }
}

fun testOperator() {
    runBlocking {
        getSampleFlow()
            .onCompletion {
                println("on completion")
            }
            .map {
                println("in map, value=$it")
                it * 2
            }
            .filter {
                println("in filter, value=$it")
                it < 10
            }
            .take(3)
            .onEach {
                println("in onEach, value=$it")
                it * 2
            }
            .onStart {
                println("on start")
            }
            .collect {
                println("in collect, value=$it")
            }
    }
}


fun testFlowOn() {
    runBlocking {
        getSampleFlow()
            .take(3)
            .map {
                println("map: ${Thread.currentThread().name}")
                it * 2
            }
            .flowOn(Dispatchers.Default)
            .filter {
                println("filter: ${Thread.currentThread().name}")
                it < 10
            }
            .flowOn(Dispatchers.IO)
            .collect {
                println("collect: ${Thread.currentThread().name}")
            }
    }

}

// Hot Flow
fun getSampleSharedFlow(): SharedFlow<Int> {
    val scope = CoroutineScope(Job())
    return getSampleFlow().shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(100, 200),//Eagerly, Lazily never stop.
        replay = 3
    )
}

fun testSharedFlow() {
    runBlocking {
        val sampleFlow = getSampleSharedFlow()
        launch {
            sampleFlow.collect {
                println("shared1:$it")
            }
        }
        delay(500)
        launch {
            sampleFlow.collect {
                println("shared2:$it")
            }
        }
        println("done")
    }
}

// Hot Flow
// has initial value
// at lease pass one value to subscriber
fun testStateFlow() {
    val stateFlow = MutableStateFlow(false)
    suspend fun updateStateFlow() {
        delay(200)
        stateFlow.value = true
    }
    runBlocking {
        stateFlow.collect {
            println("stateFlow value:$it")
            updateStateFlow()
        }
        println("done")

//        getSampleFlow().stateIn(CoroutineScope(Job())).collect {
//            println("stateFlow value:$it")
//        }
//        println("done")
    }
}
