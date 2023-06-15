import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class StopWatch {


    var formattedTime by mutableStateOf("00:00")
    var textBoxText by mutableStateOf("")

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isActive = false;

    private var timeMillils = 0L
    private var lastTimestamp = 0L;

    private var url = URLSaver.readUrlFromFile()

    fun start() {
        if (isActive) return


        WatchPulse.renameLatestCsv()
        CurrentScreen.dataPoints.clear()

        if (!checkURL()) return


        coroutineScope.launch {
            this@StopWatch.isActive = true
            lastTimestamp = System.currentTimeMillis()
            while (isActive) {
                delay(1000)
                timeMillils += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime = formatTime(timeMillils)


                val lastHearRate = WatchPulse.saveHeartRate(url).toInt()

                CurrentScreen.dataPoints.add(lastHearRate)
                LatestPulseSaver.saveLatestPulse(lastHearRate.toString())


                textBoxText = "Latest: \n"
                for (i in (CurrentScreen.dataPoints.size - 1) downTo if (CurrentScreen.dataPoints.size > 100) CurrentScreen.dataPoints.size - 101 else 0) {
                    textBoxText  += CurrentScreen.dataPoints[i].toString() + ", "
                }

            }

        }
    }


    private fun checkURL() : Boolean {
        if (url.isEmpty()) {

            url = "hyperate"
            textBoxText = ""
            return true
        }

        if (!url.startsWith("http")) {
            textBoxText = "Please enter a valid URL"
            return false
        }

        if (!WatchPulse.TestURL(url)){
            textBoxText = "Could not connect to URL"
            return false
        }

        textBoxText = ""

        URLSaver.saveURLToFile(url)

        return true
    }

    fun pause() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        isActive = false
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillils = 0L
        formattedTime = "00:00"
        isActive = false

        CurrentScreen.dataPoints.clear()
    }

    fun typedText(text: String) {
        url = text
    }

    private fun formatTime(time: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(time),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern("mm:ss", Locale.getDefault())

        return localDateTime.format(formatter)
    }

}