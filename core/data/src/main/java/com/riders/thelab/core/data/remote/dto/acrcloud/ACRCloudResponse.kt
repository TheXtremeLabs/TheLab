package com.riders.thelab.core.data.remote.dto.acrcloud

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import java.io.Serializable

/*
 * JSON example :
 * {
 *      "result_type":0,
 *      "status":{"msg":"Success","code":0,"version":"1.0"},
 *      "cost_time":0.2759997844696,
 *      "metadata":{
 *          "timestamp_utc":"2025-06-12 12:34:15",
 *          "music":[{
 *              "title":"Gojasi feat. eMSA",
 *              "album":{"name":"Gojasi feat. eMSA"},
 *              "artists":[{
 *                  "id":"1b04facf8e450f729e43",
 *                  "name":"DaJiggySA\/DrummeRTee924\/Mfana Mdu"
 *              }],
 *              "release_date":"2025-03-14",
 *              "score":25,
 *              "label":"Rapture Music Group",
 *              "acrid":"2af2cfc32e5e298dc9ceb24499267068",
 *              "external_ids":{},
 *              "external_metadata":{},
 *              "result_from":1,
 *              "duration_ms":423120,
 *              "db_begin_time_offset_ms":140860,
 *              "db_end_time_offset_ms":144980,
 *              "sample_begin_time_offset_ms":0,
 *              "sample_end_time_offset_ms":4720,
 *              "play_offset_ms":145317
 *          }]
 *      }
 * }
 */

@kotlinx.serialization.Serializable
data class ACRCloudResponse(
    @SerialName("result_type")
    var resultType: Int = 0,
    @SerialName("status")
    var status: ACRCloudStatus? = null,
    @SerialName("cost_time")
    var costTime: Double = 0.0,
    @SerialName("metadata")
    var metadata: ACRCloudMetadata? = null,
) : Serializable {

    constructor(result: String) : this() {
        val json: Json = Json { ignoreUnknownKeys = true }
        val responseToObject = json.decodeFromString<ACRCloudResponse>(result)

        this.resultType = responseToObject.resultType
        this.status = responseToObject.status
        this.costTime = responseToObject.costTime
        this.metadata = responseToObject.metadata
    }
}
