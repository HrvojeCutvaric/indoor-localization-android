package co.be4you.indoorlocalization.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.be4you.indoorlocalization.R
import co.be4you.core.ui.theme.CommonBlue
@Composable
fun AuthHeader(
    modifier: Modifier = Modifier,
    height: Dp = 280.dp,
    logoHeight: Dp = 140.dp,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CommonBlue,
                        CommonBlue.copy(alpha = 0.9f),
                        Color.Transparent
                    )
                )
            )
            .padding(horizontal = 16.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo,),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.7f)
                .height(logoHeight),
            contentScale = ContentScale.Fit
        )

        bottomContent?.let {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                it()
            }
        }
    }
}
