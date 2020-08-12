[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [fetchMessages](./fetch-messages.md)

# fetchMessages

`fun fetchMessages(channels: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, maximumPerChannel: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, includeMeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, includeMessageActions: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`FetchMessages`](../../com.pubnub.api.endpoints/-fetch-messages/index.md)

Fetch historical messages from multiple channels.
The `includeMessageActions` flag also allows you to fetch message actions along with the messages.

It's possible to control how messages are returned and in what order. For example, you can:

* Search for messages starting on the newest end of the timeline.
* Search for messages from the oldest end of the timeline.
* Page through results by providing a `start` OR `end` time token.
* Retrieve a slice of the time line by providing both a `start` AND `end` time token.
* Limit the number of messages to a specific quantity using the `count` parameter.
* Batch history returns up to 25 messages per channel, on a maximum of 500 channels.
Use the start and end timestamps to page through the next batch of messages.

**Start &amp; End parameter usage clarity:**

* If you specify only the `start` parameter (without `end`),
you will receive messages that are older than and up to that `start` timetoken.
* If you specify only the `end` parameter (without `start`),
you will receive messages from that `end` timetoken and newer.
* Specify values for both `start` and `end` parameters to retrieve messages between those timetokens
(inclusive of the `end` value).
* Keep in mind that you will still receive a maximum of 25 messages
even if there are more messages that meet the timetoken values.
* Iterative calls to history adjusting the start timetoken is necessary to page through the full set of results
if more than 25 messages meet the timetoken values.

### Parameters

`channels` - Channels to return history messages from.

`maximumPerChannel` - Specifies the number of historical messages to return per channel.
    If [includeMessageActions](fetch-messages.md#com.pubnub.api.PubNub$fetchMessages(kotlin.collections.List((kotlin.String)), kotlin.Int, kotlin.Long, kotlin.Long, kotlin.Boolean, kotlin.Boolean)/includeMessageActions) is `false`, then `1` is the default (and maximum) value.
    Otherwise it's `25`.

`start` - Timetoken delimiting the start of time slice (exclusive) to pull messages from.

`end` - Time token delimiting the end of time slice (inclusive) to pull messages from.

`includeMeta` - Whether to include message metadata in response.
    Defaults to `false`.

`includeMessageActions` - Whether to include message actions in response.
    Defaults to `false`.