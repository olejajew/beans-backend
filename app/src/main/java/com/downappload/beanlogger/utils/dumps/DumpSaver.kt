package com.downappload.beanlogger.utils.dumps

import android.content.Context
import android.util.Log
import com.downappload.beanlogger.utils.GameProvider
import com.google.gson.Gson

class DumpSaver(private val gameProvider: GameProvider, private val context: Context) {

    private val gson = Gson()

    companion object {
        private const val LAST_SAVE = "last_save"
    }

    private val sharedPreferences = context.getSharedPreferences("game_dump", Context.MODE_PRIVATE)

    fun createDump() {
        if (gameProvider.getPlayers().isEmpty()) {
            return
        }
        val activePlayer = gameProvider.getActivePlayer()
        val playerWrappers = gameProvider.playerWrappers.values
        val dump = Dump(activePlayer.id, playerWrappers.toMutableList())

        val string = gson.toJson(dump)
        Log.i("dump.create():", string)
        sharedPreferences.edit().putString(LAST_SAVE, string).apply()
    }

    fun getDump(): Dump? {
        val dumpString = sharedPreferences.getString(LAST_SAVE, "")
        if (dumpString.isNullOrEmpty()) {
            return null
        }
        return gson.fromJson(dumpString, Dump::class.java)
    }

}