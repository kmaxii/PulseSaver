import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.system.exitProcess


@Composable
@Preview
fun App() {


    MaterialTheme {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            when(CurrentScreen.screenNumber){

                2 -> {
                    EnterURLDisplay(::enteredURL)
                }

                1 -> {
                    val stopWatch = remember { StopWatch() }
                    StopWatchDisplay(
                        formattedTime = stopWatch.formattedTime,
                        onStartClick = stopWatch::start,
                        onPauseClick = stopWatch::pause,
                        onResetClick = stopWatch::reset,
                        onTextEntered = stopWatch::typedText,
                        textBox = stopWatch.textBoxText
                    )
                }

            }


        }
    }
}

fun enteredURL(url : String) {
    CurrentScreen.url = url
    CurrentScreen.screenNumber = 2
    println("Yes")
}

fun exitApp() {
    println("Exiting")

    //WatchPulse.renameLatestCsv()

    exitProcess(0)
}


fun main() = application {
    Window(onCloseRequest = ::exitApp) {
        App()
    }
}
