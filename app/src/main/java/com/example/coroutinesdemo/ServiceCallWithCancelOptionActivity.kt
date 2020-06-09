package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_service_call_with_cancel_option.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

// Third
// Cancelling job
class ServiceCallWithCancelOptionActivity : AppCompatActivity() {

    private val PROGRESS_MAX = 100
    private val PROGRESS_MIN = 0
    private val JOB_TIME = 4000
    lateinit var completableJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_call_with_cancel_option)

        job_button.setOnClickListener {
            if (!::completableJob.isInitialized) {
                initJob()
            }
            job_progress_bar.startORCancelJob(completableJob)
        }
    }

    private fun ProgressBar.startORCancelJob(job: Job) {
        if (this.progress > 0) {
            println("$completableJob is already Active. Cancelling..")
            resetJob()
        } else {
            job_button.text = "Cancel Job#1"

            CoroutineScope(IO + completableJob).launch {

                for (i in PROGRESS_MIN..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startORCancelJob.progress = i
                }
                updateText("Job is Complete")
            }
        }
    }

    private fun updateText(text: String) {
        GlobalScope.launch(Main) {
            job_complete_text.text = text
        }
    }

    private fun resetJob() {
        if (completableJob.isActive || completableJob.isCompleted) {
            completableJob.cancel(CancellationException("Resetting Job"))
        }
        initJob()
    }

    private fun initJob() {
        job_button.text = "Start Job#1"
        updateText("")
        completableJob = Job()
        completableJob.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "unknown Cancellation error"
                }
                println("$completableJob was cancelled. Reason $msg ")
                showToast(msg)
            }
        }

        job_progress_bar.max = PROGRESS_MAX
        job_progress_bar.progress = PROGRESS_MIN
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Main) {
            Toast.makeText(this@ServiceCallWithCancelOptionActivity, text, Toast.LENGTH_LONG).show()
        }
    }
}

