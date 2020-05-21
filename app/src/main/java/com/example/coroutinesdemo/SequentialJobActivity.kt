package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sequential_job.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class SequentialJobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequential_job)

        btn_sequential_job.setOnClickListener {
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest() {
        CoroutineScope(IO).launch {

            val executionTime = measureTimeMillis {
                val result1 = async {
                    getResultFromApi1()
                }.await()

                val result2 = async {
                    try {
                        getResultFromApi2(result1 + "aa")
                    } catch (e: CancellationException) {
                        e.message
                    }

                }.await()

                println("Result :$result2")

            }

            println("Total time elapsed $executionTime")
        }
    }

    // suspend keyword tells this method can be worked with Coroutines
    private suspend fun getResultFromApi1(): String {
        delay(1000)
        return "Result#1"
    }

    private suspend fun getResultFromApi2(result1: String): String {
        delay(1500)
        if (result1.equals("Result#1"))
            return "Result#2"
        else
            throw CancellationException("result Number 1 is incorrect")
    }

    private fun setText(input: String) {
        val newText = txt_output.text.toString() + "\n$input"
        txt_output.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        // This will switch the context of coroutine to main Thread
        withContext(Dispatchers.Main)
        {
            setText(input)
        }
    }

}
