package com.riders.thelab.core.data.remote.dto.acrcloud

import org.junit.Test
import kotlin.test.assertEquals

class ACRCloudResponseTest {

    @Test
    fun convertStringToACRCloudResponseModel() {
        val response = ACRCloudResponse(JSON_TO_TEST_2)
        assertEquals("0", response.status?.code.toString())
    }

    companion object {
        const val JSON_TO_TEST: String =
            "{\"result_type\":0,\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"cost_time\":0.2759997844696,\"metadata\":{\"timestamp_utc\":\"2025-06-12 12:34:15\",\"music\":[{\"title\":\"Gojasi feat. eMSA\",\"album\":{\"name\":\"Gojasi feat. eMSA\"},\"artists\":[{\"id\":\"1b04facf8e450f729e43\",\"name\":\"DaJiggySA\\/DrummeRTee924\\/Mfana Mdu\"}],\"release_date\":\"2025-03-14\", \"score\":25, \"label\":\"Rapture Music Group\",\"acrid\":\"2af2cfc32e5e298dc9ceb24499267068\", \"external_ids\":{},\"external_metadata\":{}, \"result_from\":1,\"duration_ms\":423120,\"db_begin_time_offset_ms\":140860,\"db_end_time_offset_ms\":144980,\"sample_begin_time_offset_ms\":0,\"sample_end_time_offset_ms\":4720,\"play_offset_ms\":145317}]}}"

        const val JSON_TO_TEST_2: String =
            "{\"result_type\":0,\"status\":{\"msg\":\"Success\",\"code\":0,\"version\":\"1.0\"},\"cost_time\":0.082000017166138,\"metadata\":{\"timestamp_utc\":\"2025-06-12 13:38:15\",\"music\":[{\"title\":\"Forgive Our Trespasses (feat. DemaloViolinist)\",\"album\":{\"name\":\"Rebound\"},\"artists\":[{\"name\":\"Nandipha808 & Ceeka RSA\"}],\"release_date\":\"2023-09-01\",\"score\":100,\"acrid\":\"8879e685558aa9e7c8ebe7d5cf9c4970\",\"external_ids\":{},\"external_metadata\":{\"spotify\":{\"artists\":[{\"id\":\"3hw14sG4z2GSidk66In1eW\",\"name\":\"Nandipha808\"},{\"id\":\"5DuhT9Ix9p7qvE42w9liWY\",\"name\":\"Ceeka RSA\"},{\"id\":\"5cUFIkBWAwJOJZDEiACH6m\",\"name\":\"DEMOLA\"}],\"album\":{\"id\":\"0B6JQgASgpBzZvLcFEBMpN\",\"name\":\"Rebound\"},\"track\":{\"id\":\"58YUdESjlrIcfRrvJuuGYz\",\"name\":\"Forgive Our Trespasses\"}},\"youtube\":{\"vid\":\"lrLVjRvf2CY\"},\"deezer\":{\"artists\":[{\"id\":\"186241497\",\"name\":\"Nandipha808\"},{\"id\":\"209272567\",\"name\":\"Ceeka RSA\"},{\"id\":\"11818563\",\"name\":\"Demola\"}],\"album\":{\"id\":\"488477985\",\"name\":\"Rebound\"},\"track\":{\"id\":\"2456309675\",\"name\":\"Forgive Our Trespasses (feat. Demola)\"}}},\"result_from\":3,\"play_offset_ms\":105555,\"sample_end_time_offset_ms\":3800,\"db_begin_time_offset_ms\":100740,\"db_end_time_offset_ms\":104540,\"sample_begin_time_offset_ms\":0,\"duration_ms\":460800}]}}"
    }
}