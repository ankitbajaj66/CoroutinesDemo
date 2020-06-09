package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_network_timeout.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

// Second
// Network requst with  timeout
class NetworkTimeout : AppCompatActivity() {

    private val JOB_TIMEOUT = 2200L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_timeout)

        btn_TimeOutRequest.setOnClickListener {

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }

    private suspend fun fakeApiRequest() {
        withContext(IO)
        {
            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                val result1 = getResultFromApi1()
                setTextOnMainThread(result1)

                val result2 = getResultFromApi2()
                setTextOnMainThread(result2)

            }

            // If timeout then job will be null
            if (job == null) {
                setTextOnMainThread("Job Cancelled")
            }
        }
    }

    private suspend fun getResultFromApi1(): String {
        logThread("getResultFromApi1")
        delay(1000)
        return "Result1"
    }

    private suspend fun getResultFromApi2(): String {
        logThread("getResultFromApi2")
        delay(1000)
        return "Result2"
    }

    // Log the Thread
    private fun logThread(methodName: String) {
        println("debug: $methodName : ${Thread.currentThread().name}")
    }

    private suspend fun setTextOnMainThread(input: String) {
        // This will switch the context of coroutine to main Thread
        withContext(Dispatchers.Main)
        {
            setText(input)
        }
    }

    private fun setText(input: String) {
        val newText = txt_data_display.text.toString() + "\n$input"
        txt_data_display.text = newText
    }

}
