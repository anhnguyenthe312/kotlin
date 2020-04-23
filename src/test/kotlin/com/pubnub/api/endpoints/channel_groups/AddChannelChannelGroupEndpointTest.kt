package com.pubnub.api.endpoints.channel_groups

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import org.awaitility.Awaitility
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class AddChannelChannelGroupEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson())
        )

        pubnub.addChannelsToChannelGroup().apply {
            channelGroup = "groupA"
            channels = listOf("ch1", "ch2")
        }.sync()!!
    }

    @Test
    fun testSyncGroupMissing() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson())
        )

        try {
            pubnub.addChannelsToChannelGroup().apply {
                channels = listOf("ch1", "ch2")
            }.sync()!!
        } catch (e: Exception) {
            assertPnException(PubNubError.GROUP_MISSING, e)
        }
    }

    @Test
    fun testSyncGroupIsEmpty() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(emptyJson())
        )

        try {
            pubnub.addChannelsToChannelGroup().apply {
                channelGroup = ""
                channels = listOf("ch1", "ch2")
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.GROUP_MISSING, e)
        }
    }

    @Test
    fun testSyncChannelMissing() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(emptyJson())
        )

        try {
            pubnub.addChannelsToChannelGroup().apply {
                channelGroup = "groupA"
            }.sync()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testIsSubRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson())
        )

        pubnub.configuration.subscribeKey = " "

        try {
            pubnub.addChannelsToChannelGroup().apply {
                channelGroup = "groupA"
                channels = listOf("ch1", "ch2")
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testIsAuthRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson())
        )
        pubnub.configuration.authKey = "myKey"

        pubnub.addChannelsToChannelGroup().apply {
            channelGroup = "groupA"
            channels = listOf("ch1", "ch2")
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessAsync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson())
        )

        val atomic = AtomicInteger(0)

        pubnub.addChannelsToChannelGroup().apply {
            channelGroup = "groupA"
            channels = listOf("ch1", "ch2")
        }.async { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNAddChannelsToGroupOperation, status.operation)
            assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            assertTrue(status.affectedChannels == listOf("ch1", "ch2"))
            assertTrue(status.affectedChannelGroups == listOf("groupA"))
            atomic.incrementAndGet()
        }

        Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAtomic(atomic, IsEqual.equalTo(1))
    }

    @Test
    fun testErrorBodyForbidden() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson().withStatus(403))
        )

        val atomic = AtomicInteger(0)

        pubnub.addChannelsToChannelGroup().apply {
            channelGroup = "groupA"
            channels = listOf("ch1", "ch2")
        }.async { _, status ->
            assertTrue(status.error)
            assertEquals(PNOperationType.PNAddChannelsToGroupOperation, status.operation)
            assertEquals(PNStatusCategory.PNAccessDeniedCategory, status.category)
            atomic.incrementAndGet()
        }

        Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAtomic(atomic, IsEqual.equalTo(1))
    }

    @Test
    fun testTelemetryParam() {
        val success = AtomicBoolean()

        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(emptyJson())
        )

        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )

        lateinit var telemetryParamName: String

        pubnub.addChannelsToChannelGroup().apply {
            channelGroup = "groupA"
            channels = listOf("ch1", "ch2")
        }.async { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNAddChannelsToGroupOperation, status.operation)
            telemetryParamName = "l_${status.operation.queryParam}"
            assertEquals("l_cg", telemetryParamName)
            success.set(true)
        }

        success.listen()

        pubnub.time().async { _, status ->
            assertFalse(status.error)
            assertNotNull(status.param(telemetryParamName))
            success.set(true)
        }

        success.listen()
    }
}