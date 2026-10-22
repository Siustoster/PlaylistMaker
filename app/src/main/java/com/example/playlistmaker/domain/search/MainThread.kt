package com.example.playlistmaker.domain.search

interface MainThread {
    fun post(action: () -> Unit)
}
