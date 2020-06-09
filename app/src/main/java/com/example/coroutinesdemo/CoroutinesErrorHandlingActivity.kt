package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.NonCancellable.cancel

// Structure concurrency - When multiple jobs are executed parallel then if one job fail then how to propagate error to other job
@InternalCoroutinesApi
class CoroutinesErrorHandlingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines_error_handling)

        main()
    }

    private fun main() {

        val handler =
            CoroutineExceptionHandler { coroutineContext, throwable -> println("CoroutinesErrorHandlingActivity -- Exception thorown in one of the children") }
        val parentJob = CoroutineScope(IO).launch(handler) {

            // jobA
            val jobA = launch {
                val resultA = getResult(1)
                println("CoroutinesErrorHandlingActivity -- ResultA ${resultA}")
            }
            jobA.invokeOnCompletion {
                if (it != null) {
                    println("CoroutinesErrorHandlingActivity -- JobA Failed")
                } else {
                    println("CoroutinesErrorHandlingActivity --  JobA Completed Succcessfully")
                }
            }

            // jobB
            val jobB = launch {
                val resultB = getResult(2)
                println("CoroutinesErrorHandlingActivity -- ResultB ${resultB}")
            }

            // Case 2
//            delay(200)
//            jobB.cancel(CancellationException("Error Getting result for number"))
            jobB.invokeOnCompletion {
                if (it != null) {
                    println("CoroutinesErrorHandlingActivity -- JobB Failed")
                } else {
                    println("CoroutinesErrorHandlingActivity --  JobB Completed Succcessfully")
                }
            }

            // jobC
            val jobC = launch {
                val resultC = getResult(3)
                println("CoroutinesErrorHandlingActivity -- ResultC ${resultC}")
            }
            jobC.invokeOnCompletion {
                if (it != null) {
                    println("CoroutinesErrorHandlingActivity -- JobC Failed")
                } else {
                    println("CoroutinesErrorHandlingActivity --  JobC Completed Succcessfully")
                }
            }
        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                println("CoroutinesErrorHandlingActivity -- parentJob Failed reason ${it}")
            } else {
                println("CoroutinesErrorHandlingActivity --  parentJob Completed Succcessfully")
            }
        }
    }

    private suspend fun getResult(num: Int): Int {
        delay(num * 1000L)
        if (num == 2) {
            // Case 1 : throwing exception
            throw Exception("Error Getting result for number $num")

            // Case
//            throw CancellationException("Error Getting result for number $num")

        }
        return num * 2
    }
}
