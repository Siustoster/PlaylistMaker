package com.example.playlistmaker.model

interface Observable {
    fun add(observer: Observer)
    fun remove(observer: Observer)
    fun notifyObservers()
}