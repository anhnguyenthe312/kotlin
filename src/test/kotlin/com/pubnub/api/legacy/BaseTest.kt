package com.pubnub.api.legacy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.pubnub.api.CommonUtils.DEFAULT_LISTEN_DURATION
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import okhttp3.HttpUrl
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before

abstract class BaseTest {

    lateinit var wireMockServer: WireMockServer
    protected lateinit var pubnub: PubNub

    @Before
    fun beforeEach() {
        wireMockServer = WireMockServer(
            wireMockConfig()
                .bindAddress("localhost")
                .dynamicPort()
        )
        wireMockServer.start()
        WireMock.configureFor("http", "localhost", wireMockServer.port())
        pubnub = PubNub(PNConfiguration().apply {
            subscribeKey = "mySubscribeKey"
            publishKey = "myPublishKey"
            uuid = "myUUID"
            origin = HttpUrl.parse(wireMockServer.baseUrl())!!.run { "${host()}:${port()}" }
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        })
        DEFAULT_LISTEN_DURATION = 2
        onBefore()
    }

    @After
    fun afterEach() {
        wireMockServer.stop()
        wireMockServer.findAllUnmatchedRequests().forEach {
            println("Unmatched ${it.url}")
        }
        assertTrue(wireMockServer.findAllUnmatchedRequests().isEmpty())
        onAfter()
        pubnub.forceDestroy()
    }

    open fun onBefore() {
    }

    open fun onAfter() {
    }
}
