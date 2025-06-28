package com.chatapp.pingnest.ui.photopicker

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoPickerViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val contentResolver: ContentResolver
): ViewModel() {


    fun onPhotoPicked(imageUri: Uri){
        viewModelScope.launch {
            contentResolver.takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}