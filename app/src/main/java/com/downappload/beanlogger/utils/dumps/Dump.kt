package com.downappload.beanlogger.utils.dumps

import com.downappload.beanlogger.data.PlayerWrapper

data class Dump(
    val activePlayerId: Int = 0,
    val players: List<PlayerWrapper> = emptyList()
)