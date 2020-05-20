package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_parallel_job.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class ParallelJobExecutionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallel_job)

        button.setOnClickListener {
            fakeApiRequestNormalJob()
        }
        btn_asyncawait.setOnClickListener {
            fakeApiRequestAsyncAwaitJob()
        }
    }

    private fun fakeApiRequestAsyncAwaitJob() {
        CoroutineScope(IO).launch {

            val executionTime = measureTimeMillis {
                val result1: Deferred<String> = async {
                    getResultFromApi1()
                }

                val result2: Deferred<String> = async {
                    getResultFromApi2()
                }

                println("Result1 ${result1.await()}")
                println("Result2 ${result2.await()}")
            }
            println("Total time elapsed $executionTime")

        }
    }

    private fun fakeApiRequestNormalJob() {
        val startTime = System.currentTimeMillis()
        val parentJob = CoroutineScope(IO).launch {
            val job1 = launch {
                val result1 = getResultFromApi1()
                println("Job 1 is Completed $result1")
            }

            val job2 = launch {
                val result2 = getResultFromApi2()
                println("Job 2 is Completed $result2")
            }
        }

        parentJob.invokeOnCompletion {
            println("All Jobs completed in time ${System.currentTimeMillis() - startTime}")
        }
    }

    private suspend fun getResultFromApi1(): String {
        logThread("getResultFromApi1")
        delay(1000)
        return "Result#1"
    }

    private suspend fun getResultFromApi2(): String {
        logThread("getResultFromApi2")
        delay(2000)
        return "Result#2"
    }

    // Log the Thread
    private fun logThread(methodName: String) {
        println("debug: $methodName : ${Thread.currentThread().name}")
    }
}
