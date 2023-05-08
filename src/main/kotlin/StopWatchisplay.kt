import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javax.swing.JFileChooser
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke


@Composable
fun StopWatchDisplay(
    formattedTime: String,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResetClick: () -> Unit,
    onTextEntered: (it: String) -> Unit,
    modifier: Modifier = Modifier,
    textBox: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {


        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

        Button({
            val result = fileChooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedDirectory = fileChooser.selectedFile.absolutePath
                println("Selected directory: $selectedDirectory")
            }
        }) {
            Text("Select directory")
        }

        Spacer(Modifier.height(16.dp))

        Column(Modifier.padding(16.dp)) {
            TextField(
                value = CurrentScreen.dir,
                onValueChange = { CurrentScreen.dir = it },
                label = { Text("Directory") },
                modifier = Modifier.fillMaxWidth(0.9f),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),

                )
        }

        Spacer(Modifier.height(16.dp))


        var text by remember { mutableStateOf(URLSaver.readUrlFromFile()) }
        Column(Modifier.padding(16.dp)) {
            TextField(
                value = text,
                onValueChange = { text = it; onTextEntered(it) },
                label = { Text("URL") },
                modifier = Modifier.fillMaxWidth(0.9f),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),

                )
        }


        Text(
            text = formattedTime,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onStartClick) {
                Text("Start")
            }
            Spacer(Modifier.width(16.dp))
            Button(onPauseClick) {
                Text("Pause")
            }
            Spacer(Modifier.width(16.dp))
            Button(onResetClick) {
                Text("Reset")
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = textBox,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))


        val dataPoints = CurrentScreen.dataPoints

        if (dataPoints.size > 5) {


            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Draw X and Y axis
                drawLine(start = Offset(0f, 0f), end = Offset(width, 0f), color = Color.Black, strokeWidth = 2f)
                drawLine(start = Offset(0f, 0f), end = Offset(0f, height), color = Color.Black, strokeWidth = 2f)

                // Draw data points
                val maxDataValue = dataPoints.maxOrNull() ?: 0
                val xInterval = width / (dataPoints.size - 1)
                val yInterval = height / maxDataValue
                dataPoints.forEachIndexed { index, point ->
                    val x = index * xInterval
                    val y = height - (point * yInterval)
                    drawCircle(color = Color.Magenta, radius = 6f, center = Offset(x, y))
                }

                // Draw lines connecting data points
                dataPoints.forEachIndexed { index, point ->
                    if (index < dataPoints.size - 1) {
                        val startPoint = Offset(index * xInterval, height - (point * yInterval))
                        val endPoint = Offset((index + 1) * xInterval, height - (dataPoints[index + 1] * yInterval))
                        drawLine(start = startPoint, end = endPoint, color = Color.Blue, strokeWidth = 2f)
                    }
                }
            }

        }
    }
}

