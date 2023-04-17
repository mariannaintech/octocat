package com.octocat.base

import io.getstream.log.StreamLog
import io.getstream.log.kotlin.KotlinStreamLogger
import org.junit.Before

abstract class BaseTest {

    @Before
    fun setupLogger() {
        StreamLog.setValidator { _, _ -> true }
        StreamLog.install(KotlinStreamLogger())
    }

}