package co.be4you.indoorlocalization.view.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.ui.theme.IndoorLocalizationTheme
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardState
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { currentState ->
        DashboardLayout(
            state = currentState,
        )
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun DashboardLayout(
    state: DashboardState,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            state.floorMap?.let { floorMap ->
                AsyncImage(
                    model = floorMap.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.None,
                )
            } ?: run {
                Text(
                    text = stringResource(R.string.failed_to_load_floor_map),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error,
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    IndoorLocalizationTheme {
        DashboardLayout(
            state = DashboardState(floorMap = null)
        )
    }
}
