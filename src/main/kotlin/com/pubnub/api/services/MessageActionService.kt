package com.pubnub.api.services

import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.http.*

interface MessageActionService {

    @POST("v1/message-actions/{subKey}/channel/{channel}/message/{messageTimetoken}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun addMessageAction(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path("messageTimetoken") messageTimetoken: String,
        @Body body: Any,
        @QueryMap options: Map<String, String>
    ): Call<EntityEnvelope<PNMessageAction>>

    @GET("v1/message-actions/{subKey}/channel/{channel}")
    fun getMessageActions(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap options: Map<String, String>
    ): Call<EntityEnvelope<List<PNMessageAction>>>

    @DELETE("v1/message-actions/{subKey}/channel/{channel}/message/{messageTimetoken}/action/{actionTimetoken}")
    fun deleteMessageAction(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path("messageTimetoken") messageTimetoken: String,
        @Path("actionTimetoken") actionTimetoken: String,
        @QueryMap options: Map<String, String>
    ): Call<Void>

}