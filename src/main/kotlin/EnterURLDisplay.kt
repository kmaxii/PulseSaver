import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.FileDialog
import java.awt.Frame
import javax.swing.JFileChooser


@Composable
fun EnterURLDisplay(
    onStartClick: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        var text by remember { mutableStateOf("") }
        Column(Modifier.padding(16.dp)) {


            val dialog = FileDialog(null as Frame?, "Select Directory")
            dialog.file = ""
            dialog.directory = ""

            Button({
                dialog.isVisible = true
                val selectedDirectory = dialog.directory + dialog.file
                println("Selected directory: $selectedDirectory")
            }){
                Text("Select directory")
            }

            Spacer(Modifier.height(16.dp))

            TextField(
                value = text,
                onValueChange = { text = it; },
                label = {
                    Text(
                        text = "Enter URL",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center

                    )
                },
                //modifier = Modifier.fillMaxWidth(0.9f),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),

                )
        }

        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button({ onStartClick(text) }) {
                Text("Start")
            }
        }
    }
}

