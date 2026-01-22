package co.be4you.indoorlocalization.viewmodel.createasset

import android.content.res.Resources

data class AddAssetState(
    val isSaving: Boolean = false,
    val name: String = "",
    val x: String = "",
    val y: String = "",
    val color: String = "",
    val active: Boolean = true,

    val errorResource: Int? = null
)