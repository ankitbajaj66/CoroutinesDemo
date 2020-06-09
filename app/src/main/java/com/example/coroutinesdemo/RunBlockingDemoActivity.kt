package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_run_blocking_demo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

// Sixth
class RunBlockingDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_blocking_demo)

        btn_RunBlocking.setOnClickListener {
            makeFakeApiRequest()
        }
    }

    private fun makeFakeApiRequest() {

        // Job1
        CoroutineScope(IO).launch {
            val result1 = getResult()
            println("runblocking: $result1")

            val result2 = getResult()
            println("runblocking: $result2")

            val result3 = getResult()
            println("runblocking: $result3")

            val result4 = getResult()
            println("runblocking: $result4")
        }

        // Job2
        CoroutineScope(IO).launch {
            runBlocking {
                println("runblocking: Blocking Thread")
                delay(2000)
                println("runblocking: Released Thread")
            }
        }
    }


    private suspend fun getResult(): Int {
        delay(1000)
        return Random.nextInt(0, 100)
    }
}
