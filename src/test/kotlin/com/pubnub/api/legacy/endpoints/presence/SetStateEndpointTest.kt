package com.pubnub.api.legacy.endpoints.presence

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.PubNubError
import com.pubnub.api.assertPnException
import com.pubnub.api.failTest
import com.pubnub.api.legacy.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SetStateEndpointTest : BaseTest() {

    @Test
    fun applyStateForChannelSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "payload": {
                            "age": 20,
                            "status": "online"
                          },
                          "service": "Presence"
                        }
                    """.trimIndent()
                    )
                )
        )

        val result = pubnub.setPresenceState().apply {
            channels = listOf("testChannel")
            state = mapOf("age" to 20)
        }.sync()!!

        assertEquals(pubnub.mapper.elementToInt(result.state!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(result.state, "status"), "online")

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
    }

    @Test
    fun applyStateForSomebodyElseChannelSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/someoneElseUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "payload": {
                            "age": 20,
                            "status": "online"
                          },
                          "service": "Presence"
                        }
                    """.trimIndent()
                    )
                )
        )

        val result = pubnub.setPresenceState().apply {
            channels = listOf("testChannel")
            state = mapOf("age" to 20)
            uuid = "someoneElseUUID"
        }.sync()!!

        assertEquals(pubnub.mapper.elementToInt(result.state!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(result.state, "status"), "online")

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
    }

    @Test
    fun applyStateForChannelsSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel,testChannel2/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "payload": {
                            "age": 20,
                            "status": "online"
                          },
                          "service": "Presence"
                        }
                    """.trimIndent()
                    )
                )
        )

        val result = pubnub.setPresenceState().apply {
            channels = listOf("testChannel", "testChannel2")
            state = mapOf("age" to 20)
        }.sync()!!

        assertEquals(pubnub.mapper.elementToInt(result.state!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(result.state, "status"), "online")

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
    }


    @Test
    fun applyStateForChannelGroupSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "age": 20,
                                "status": "online"
                              },
                              "service": "Presence"
                            }
                        """.trimIndent()
                    )
                )
        )

        val result = pubnub.setPresenceState().apply {
            channelGroups = listOf("cg1")
            state = mapOf("age" to 20)
        }.sync()!!

        assertEquals(pubnub.mapper.elementToInt(result.state!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(result.state, "status"), "online")

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
    }

    @Test
    fun applyStateForChannelGroupsSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "age": 20,
                                "status": "online"
                              },
                              "service": "Presence"
                            }
                        """.trimIndent()
                    )
                )
        )

        val result = pubnub.setPresenceState().apply {
            channelGroups = listOf("cg1", "cg2")
            state = mapOf("age" to 20)
        }.sync()!!

        assertEquals(pubnub.mapper.elementToInt(result.state!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(result.state, "status"), "online")

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("cg1,cg2", requests[0].queryParameter("channel-group").firstValue())
    }

    @Test
    fun applyStateForMixSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "age": 20,
                                "status": "online"
                              },
                              "service": "Presence"
                            }
                        """.trimIndent()
                    )
                )
        )

        val result = pubnub.setPresenceState().apply {
            channels = listOf("ch1")
            channelGroups = listOf("cg1", "cg2")
            state = mapOf("age" to 20)
        }.sync()!!

        assertEquals(pubnub.mapper.elementToInt(result.state!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(result.state, "status"), "online")

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
    }

    @Test
    fun applyNon200Sync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "age": 20,
                                "status": "online"
                              },
                              "service": "Presence"
                            }
                        """.trimIndent()
                    ).withStatus(400)
                )
        )

        try {
            pubnub.setPresenceState().apply {
                channels = listOf("ch1")
                channelGroups = listOf("cg1", "cg2")
                state = mapOf("age" to 20)
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.HTTP_ERROR, e)
        }
    }

    @Test
    fun missingStateSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "age": 20,
                                "status": "online"
                              },
                              "service": "Presence"
                            }
                        """.trimIndent()
                    )
                )
        )

        try {
            pubnub.setPresenceState().apply {
                channels = listOf("testChannel")
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.STATE_MISSING, e)
        }
    }

    @Test
    fun testIsAuthRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "age": 20,
                                "status": "online"
                              },
                              "service": "Presence"
                            }
                        """.trimIndent()
                    )
                )
        )

        pubnub.configuration.authKey = "myKey"

        pubnub.setPresenceState().apply {
            channels = listOf("testChannel")
            state = mapOf("age" to 20)
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))

        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testBlankSubKeySync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        "{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : " +
                                "20, \"status\" : \"online\" }, \"service\": \"Presence\"}"
                    )
                )
        )

        pubnub.configuration.subscribeKey = " "

        try {
            pubnub.setPresenceState().apply {
                channels = listOf("testChannel")
                state = mapOf("age" to 20)
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testEmptySubKeySync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        "{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : " +
                                "20, \"status\" : \"online\" }, \"service\": \"Presence\"}"
                    )
                )
        )

        pubnub.configuration.subscribeKey = ""

        try {
            pubnub.setPresenceState().apply {
                channels = listOf("testChannel")
                state = mapOf("age" to 20)
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testChannelAndGroupMissingSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "payload": {
                            "age": 20,
                            "status": "online"
                          },
                          "service": "Presence"
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.setPresenceState().apply {
                state = mapOf("age" to 20)
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_AND_GROUP_MISSING, e)
        }
    }

    @Test
    fun testNullPayloadSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "service": "Presence"
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.setPresenceState().apply {
                channels = listOf("testChannel")
                state = mapOf("age" to 20)
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

}