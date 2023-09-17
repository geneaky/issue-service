import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ThreadTest {

    @Test
    fun `runnable test`() {
        for(i in 0..5) {
            val thread = Thread(Runnable {
                println("currenct-thread-name : ${Thread.currentThread().name}")
            })
            thread.start()
        }

        println("current-thread-name : ${Thread.currentThread().name}")
    }

    @Test
    fun `executorservice`() {
        val pool = Executors.newFixedThreadPool(5)
        for(i in 0..5) {
            pool.execute { println("currenct-thread-name : ${Thread.currentThread().name}") }
        }

        pool.shutdown()
    }

    @Test
    fun `future`() {
        fun sum(a:Int, b:Int) = a+b

        val pool = Executors.newSingleThreadExecutor()
        val future = pool.submit(Callable {
            Thread.sleep(100L) //timeout
            sum(100, 200)
        })
        println("계산 시작")
        val futureResult = future.get(0L, TimeUnit.MILLISECONDS) // blocking
        println(futureResult)
        println("게산 종료")
    }

    @Test
    fun `completable future`() {
        fun sum(a:Int, b:Int) = a+b
        val completableFuture = CompletableFuture.supplyAsync {
            Thread.sleep(2000)
            sum(100, 200)
        }

        println("계산 시작")
//        completableFuture.get() >> 블로킹 동작
        completableFuture.thenApplyAsync(::println) //논블로킹으로 동작

        while(!completableFuture.isDone) {
            Thread.sleep(500)
            println("계산결과를 집계중")
        }
        println("게산 종료")
    }
}