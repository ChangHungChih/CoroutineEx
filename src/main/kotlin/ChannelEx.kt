import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main() = runBlocking {
    channelEx()
}

private val scope = CoroutineScope(Job())

suspend fun channelEx() {
    val channel = Channel<Int>()
    scope.launch {
        repeat(10) {
            delay(200)
            channel.send(it)
        }
    }

//    repeat(11) {
    repeat(10) {
        val received = channel.receive()
        println(received)
    }
    println("Done!")

    //channel can pass value between different scope
    //scope.launch → outer scope
}

//FIFO(First in first out) Queue like
//https://i.imgur.com/mVhkzPk.png

// send and receive are pairs, should be called with same times, or receive will keep waiting