package com.pubnub.api.managers

import com.pubnub.api.PNConfiguration
import com.pubnub.api.models.server.SubscribeMessage

internal class DuplicationManager(private val config: PNConfiguration) {

    private val hashHistory: ArrayList<String> = ArrayList()

    private fun getKey(message: SubscribeMessage) =
        with(message) {
            "${publishMetaData?.publishTimetoken}-${payload.hashCode()}"
        }

    fun isDuplicate(message: SubscribeMessage) = hashHistory.contains(getKey(message))

    fun addEntry(message: SubscribeMessage) {
        if (hashHistory.size >= config.maximumMessagesCacheSize) {
            hashHistory.removeAt(0)
        }
        hashHistory.add(getKey(message))
    }

    fun clearHistory() = hashHistory.clear()
}
