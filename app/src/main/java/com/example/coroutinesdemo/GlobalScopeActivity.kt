package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_global_scope.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

// Seventh
class GlobalScopeActivity : AppCompatActivity() {

    lateinit var parentJob: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_scope)

        main()

        btn_cancelJob.setOnClickListener {
            parentJob.cancel()
        }
    }

    private fun main() {

        val startTime = System.currentTimeMillis()

        println("GlobalScopeActivity --- Starting Parent Job")
        parentJob = CoroutineScope(IO).launch {

            GlobalScope.launch {
                work(1)
            }

            GlobalScope.launch {
                work(2)
            }
        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                println("GlobalScopeActivity --- Parent Job Failed in ${System.currentTimeMillis() - startTime}")
            } else {
                println("GlobalScopeActivity --- Parent Job Completed Successfully in ${System.currentTimeMillis() - startTime}")
            }
        }
    }


    suspend fun work(i: Int) {
        delay(3000)
        println("GlobalScopeActivity --- Work $i Done in ${Thread.currentThread().name}")
    }
}
