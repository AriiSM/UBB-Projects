package com.example.smartnote.utils

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Preluăm datele de intrare
        val currentProgress = inputData.getInt("currentProgress", 0)

        // Simulează un proces de incrementare
        var progress = currentProgress
        while (progress < 100) {
            progress++

            // Log pentru verificare
            Log.d("MyWorker", "Progress: $progress%")

            // Setează progresul intermediar
            setProgressAsync(Data.Builder().putInt("progress", progress).build())

            // Așteaptă 1 secundă pentru a simula procesul
            Thread.sleep(1000)
        }

        // La final, setăm rezultatul jobului
        val outputData = Data.Builder()
            .putInt("finalProgress", progress)
            .build()

        // Returnăm succesul
        return Result.success(outputData)
    }
}
