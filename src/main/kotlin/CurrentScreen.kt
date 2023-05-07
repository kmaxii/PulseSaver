import java.io.File

object CurrentScreen {
    var screenNumber = 1
    var url = ""
    var dir = File(File(System.getProperty("user.home"), "Desktop"), "pulses").absolutePath

    val dataPoints = arrayListOf(1)
}