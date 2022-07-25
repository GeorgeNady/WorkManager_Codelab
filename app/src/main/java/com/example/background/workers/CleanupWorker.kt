package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File

/**
 * # Cleans up temporary files generated during blurring process
 */
class CleanupWorker(ctx: Context, workerParameters: WorkerParameters) :
    Worker(ctx, workerParameters) {

    override fun doWork(): Result {

        makeStatusNotification("Cleaning up old temporary files", applicationContext)

        // USED TO SLOW DOWN THE WORKER
        sleep()

        return try {
            val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDir.exists()) {
                val entries = outputDir.listFiles()
                Log.d(TAG, "doWork: $entries")

                entries?.forEach { entry ->
                    val name = entry.name
                    if (name.isNotEmpty() && name.endsWith(".png")) {
                        val deleted = entry.delete()
                        Log.d(TAG, "doWork: file name: $name ${if (deleted) " has been deleted" else "not deleted"}.")
                    }
                }
            }

            Result.success()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }

    private companion object {
        const val TAG = "CleanupWorker"
    }
}