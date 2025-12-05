package co.be4you.indoorlocalization.view.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.be4you.indoorlocalization.viewmodel.main.MainAction

@Composable
fun AssetsScreen(
    floorMapId: String,
    floorMapName: String,
    onAction: (MainAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF2B85ED))
                .padding(top = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Assets",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Assets content will go here")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Button(
                onClick = { onAction(MainAction.NavigateBack()) },
                colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2B85ED),
                contentColor = Color.White
            )
            ) {
                Text(
                    text = "Back",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                    .padding(8.dp)
                    .clickable{ onAction(MainAction.NavigateBack()) }
                )
            }
        }
    }
}
