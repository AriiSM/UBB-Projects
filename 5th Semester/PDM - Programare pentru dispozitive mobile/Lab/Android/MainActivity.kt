package com.example.smartnote

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.example.smartnote.ui.theme.SmartNoteTheme
import com.example.smartnote.utils.MyWorker
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var myWorkRequest: OneTimeWorkRequest? = null  // Variabilă globală pentru WorkRequest
    private var workManager: WorkManager? = null  // Instanță WorkManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d("MainActivity", "onCreate")

            // Preluăm noteId din Intent, dacă există
            val noteId = intent.getStringExtra("NOTE_ID")

            // Creăm un NavController
            val navController = rememberNavController()

            SmartNoteTheme {
                Surface {
                    MyAppNavHost(navController = navController, noteId = noteId)  // Trimitem noteId în NavHost
                }
            }

            // Inițierea job-ului de fundal în onCreate
            workManager = WorkManager.getInstance(this)
            startBackgroundJob()
        }
    }

    private fun startBackgroundJob() {
        // Verifică progresul salvat din SharedPreferences
        val currentProgress = getCurrentProgress() // Funcție care citește progresul salvat

        // Dacă jobul nu a fost definit deja, creează-l
        if (myWorkRequest == null) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Setează inputul pentru job
            val inputData = Data.Builder()
                .putInt("currentProgress", currentProgress)  // Pornim de la progresul salvat
                .build()

            // Creează job-ul de tip OneTimeWorkRequest
            myWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()

            // Înregistrează job-ul în WorkManager
            workManager?.enqueue(myWorkRequest!!)

            // Log pentru verificare
            Log.d("MainActivity", "Background job started.")
        }
    }

    override fun onResume() {
        super.onResume()

        // Verifică dacă job-ul este activ și continuă de unde s-a oprit
        myWorkRequest?.let { workRequest ->
            workManager?.getWorkInfoByIdLiveData(workRequest.id)?.observe(this) { workInfo ->
                if (workInfo != null && workInfo.state != WorkInfo.State.RUNNING) {
                    // Dacă jobul nu este deja în execuție, îl reîncepem
                    if (workInfo.state == WorkInfo.State.CANCELLED) {
                        startBackgroundJob()
                    }
                }
            }
        }

        lifecycleScope.launch {
            (application as MyApplication).container.itemRepository.openWsClient()
        }
    }

    override fun onPause() {
        super.onPause()

        // Salvează progresul actual în SharedPreferences
        myWorkRequest?.let { workRequest ->
            workManager?.getWorkInfoByIdLiveData(workRequest.id)?.observe(this) { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.RUNNING) {
                    val progress = workInfo.progress.getInt("progress", 0)
                    saveProgress(progress) // Funcție care salvează progresul
                }
            }
        }

        // Anulează job-ul în fundal când aplicația este în pauză
        myWorkRequest?.let {
            workManager?.cancelWorkById(it.id)
            Log.d("MainActivity", "Background job cancelled.")
        }

        lifecycleScope.launch {
            (application as MyApplication).container.itemRepository.closeWsClient()
        }
    }

    private fun getCurrentProgress(): Int {
        // Citește progresul salvat (poți folosi SharedPreferences sau orice altă metodă)
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getInt("progress", 0)  // Returnează progresul salvat sau 0 dacă nu există
    }

    private fun saveProgress(progress: Int) {
        // Salvează progresul într-un loc persistent, de exemplu SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("progress", progress)
        editor.apply()
    }
}
