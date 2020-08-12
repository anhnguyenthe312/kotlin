[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [subscribe](./subscribe.md)

# subscribe

`fun subscribe(channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), channelGroups: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList(), withPresence: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, withTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0L): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Causes the client to create an open TCP socket to the PubNub Real-Time Network and begin listening for messages
on a specified channel.

To subscribe to a channel the client must send the appropriate [PNConfiguration.subscribeKey](../-p-n-configuration/subscribe-key.md) at initialization.

By default, a newly subscribed client will only receive messages published to the channel
after the `subscribe()` call completes.

If a client gets disconnected from a channel, it can automatically attempt to reconnect to that channel
and retrieve any available messages that were missed during that period.
This can be achieved by setting [PNConfiguration.reconnectionPolicy](../-p-n-configuration/reconnection-policy.md) to [PNReconnectionPolicy.LINEAR](../../com.pubnub.api.enums/-p-n-reconnection-policy/-l-i-n-e-a-r.md), when
initializing the client.

### Parameters

`channels` - Channels to subscribe/unsubscribe. Either `channel` or [channelGroups](subscribe.md#com.pubnub.api.PubNub$subscribe(kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.Boolean, kotlin.Long)/channelGroups) are required.

`channelGroups` - Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels](subscribe.md#com.pubnub.api.PubNub$subscribe(kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.Boolean, kotlin.Long)/channels) are required.

`withPresence` - Also subscribe to related presence channel.

`withTimetoken` - A timetoken to start the subscribe loop from.