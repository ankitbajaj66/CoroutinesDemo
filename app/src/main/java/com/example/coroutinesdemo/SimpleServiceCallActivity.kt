package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// First
// This Class is for Two use case
// To make simple network request and get response
// To make a request and gets a response and then make second request(Sequential Request) - Here we are calling suspend function within same job
class SimpleServiceCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_MakeNetworkRequest.setOnClickListener {

            // IO, main, default
            val job1 = CoroutineScope(IO).launch {
                fakeApiRequest()
            }

        }
    }

    private fun setText(input: String) {
        val newText = txt_data.text.toString() + "\n$input"
        txt_data.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        // This will switch the context of coroutine to main Thread
        withContext(Main)
        {
            setText(input)
        }
    }

    private suspend fun fakeApiRequest() {
        val result1 = getResultFromApi1()
        println("debug: $result1")
        setTextOnMainThread(result1)

        // So its sequential call, we can pass the result of first call to second method call
        val result2 = getResultFromApi2()
        println("debug: $result2")
        setTextOnMainThread(result2)

    }

    // suspend keyword tells this method can be worked with Coroutines
    private suspend fun getResultFromApi1(): String {
        logThread("getResultFromApi1")
        delay(1000)
        return "Result#1"
    }

    private suspend fun getResultFromApi2(): String {
        logThread("getResultFromApi2")
        delay(1000)
        return "Result#2"
    }

    // Log the Thread
    private fun logThread(methodName: String) {
        println("debug: $methodName : ${Thread.currentThread().name}")
    }
}
