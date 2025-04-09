package com.example.smartnote.utils.camera

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect

@Composable
fun DeviceSensor(modifier: Modifier = Modifier): State<Float?> {
    // Obținem contextul și managerul de senzori
    val ctx = LocalContext.current
    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val temperatureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    // Cream un state pentru a ține temperatura
    val temperatureState: MutableState<Float?> = remember { mutableStateOf(null) }

    // Implementăm un listener pentru senzorul de temperatură
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (it.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    temperatureState.value = it.values[0] // Temperatura în grade Celsius
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Nu avem nevoie să facem nimic aici
        }
    }

    // Înregistrăm listener-ul pentru senzor la prima lansare
    LaunchedEffect(temperatureSensor) {
        temperatureSensor?.let {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // Folosim DisposableEffect pentru a ne asigura că anulăm listener-ul la distrugerea composable-ului
    DisposableEffect(Unit) {
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    return temperatureState
}

@Composable
fun DisplayTemperature(temperatureState: State<Float?>, modifier: Modifier = Modifier) {
    val temperature = temperatureState.value
    Text(
        text = if (temperature != null) "Temp: ${"%.1f".format(temperature)}°C" else "No Temp Sensor",
        style = TextStyle(fontSize = 14.sp),
        modifier = modifier
    )
}
