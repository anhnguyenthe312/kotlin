package com.pubnub.api.endpoints.presence

import com.google.gson.JsonElement
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubUtil
import com.pubnub.api.builder.StateOperation
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.models.server.Envelope
import com.pubnub.api.throwIfEmpty
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

class SetState(pubnub: PubNub) :
    Endpoint<Envelope<JsonElement>, PNSetStateResult>(pubnub) {

    var channels = emptyList<String>()
    var channelGroups = emptyList<String>()
    var uuid = pubnub.configuration.uuid
    lateinit var state: Any

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isNullOrEmpty() && channelGroups.isNullOrEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
        if (!::state.isInitialized) {
            throw PubNubException(PubNubError.STATE_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<JsonElement>> {
        if (uuid == pubnub.configuration.uuid) {
            pubnub.subscriptionManager.adaptStateBuilder(
                StateOperation(
                    state = state
                ).apply {
                    this.channels = this@SetState.channels
                    this.channelGroups = this@SetState.channelGroups
                }
            )
        }

        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.toCsv()
        }
        queryParams["state"] = PubNubUtil.urlEncode(pubnub.mapper.toJson(state))

        queryParams.putAll(encodeParams(queryParams))

        return pubnub.retrofitManager.presenceService.setState(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            uuid,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<JsonElement>>): PNSetStateResult? {
        input.throwIfEmpty()
        return PNSetStateResult(
            state = input.body()!!.payload
        )
    }


    override fun operationType() = PNOperationType.PNSetStateOperation

    override fun isSubKeyRequired() = true
    override fun isPubKeyRequired() = false
    override fun isAuthRequired() = true
}



