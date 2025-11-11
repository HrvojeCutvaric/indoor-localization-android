package co.be4you.indoorlocalization.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object Registration : Route

    @Serializable
    data object Login : Route
}
