package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*

class SuperVisorJobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_visor_job)

        main()
    }

    private fun main() {

        // One way to do is write try catch every where
        // Second way is to remove all the try catch from each job and use supervisorScope and provide hander to specific jobs, this provides ability to jobs to handle exception
        // Its better way to pass handler in parent job
        val handler =
            CoroutineExceptionHandler { coroutineContext, throwable -> println("SuperVisorJobActivity -- Exception thorown in one of the children") }
        val parentJob = CoroutineScope(Dispatchers.IO).launch(handler) {

            supervisorScope {
                // jobA
                val jobA = launch {
                    try {
                        val resultA = getResult(1)
                        println("SuperVisorJobActivity -- ResultA ${resultA}")
                    } catch (e: java.lang.Exception) {
                        println("SuperVisorJobActivity -- Exception in JobA ${e}")
                    }

                }
                jobA.invokeOnCompletion {
                    if (it != null) {
                        println("SuperVisorJobActivity -- JobA Failed")
                    } else {
                        println("SuperVisorJobActivity --  JobA Completed Succcessfully")
                    }
                }

                // jobB
                val jobB = launch {
//                    try {
//                        val resultB = getResult(2)
//                        println("SuperVisorJobActivity -- ResultB ${resultB}")
//                    } catch (e: java.lang.Exception) {
//                        println("SuperVisorJobActivity -- Exception in JobB ${e}")
//                    }
                        val resultB = getResult(2)
                        println("SuperVisorJobActivity -- ResultB ${resultB}")


                }

                // Case 2
//            delay(200)
//            jobB.cancel(CancellationException("Error Getting result for number"))
                jobB.invokeOnCompletion {
                    if (it != null) {
                        println("SuperVisorJobActivity -- JobB Failed")
                    } else {
                        println("SuperVisorJobActivity --  JobB Completed Succcessfully")
                    }
                }

                // jobC
                val jobC = launch {
                    val resultC = getResult(3)
                    println("SuperVisorJobActivity -- ResultC ${resultC}")
                }
                jobC.invokeOnCompletion {
                    if (it != null) {
                        println("SuperVisorJobActivity -- JobC Failed")
                    } else {
                        println("SuperVisorJobActivity --  JobC Completed Succcessfully")
                    }
                }
            }

        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                println("SuperVisorJobActivity -- parentJob Failed reason ${it}")
            } else {
                println("SuperVisorJobActivity --  parentJob Completed Succcessfully")
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
