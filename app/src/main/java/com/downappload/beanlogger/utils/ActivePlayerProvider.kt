package com.downappload.beanlogger.utils

import com.downappload.beanlogger.data.Player

class ActivePlayerProvider(private val players: List<Player>) {

    private var currentIndex = 0

    fun stepEnd() {
        val newIndex = currentIndex + 1
        currentIndex = if (newIndex > players.size - 1) {
            0
        } else {
            newIndex
        }
    }

    fun getActivePlayer() = players[currentIndex]

    fun setActivePlayer(id: Int) {
        val index = players.indexOfFirst { it.id == id }
        currentIndex = index
    }

}