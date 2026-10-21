package com.example.playlistmaker.data

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.search.MainThread

class MainThreadImpl : MainThread {
    private var handler = Handler(Looper.getMainLooper())
    override fun post(action: () -> Unit) {
        handler.post(action)
    }
}
