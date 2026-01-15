package co.be4you.indoorlocalization.view.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun AppScaffold(
    title: String,
    onBack: (() -> Unit)? = null,
    onLogout: (() -> Unit)?,
    actions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                title = title,
                onBack = onBack,
                onLogout = onLogout,
                actions = actions
            )
        },
        content = content
    )
}
