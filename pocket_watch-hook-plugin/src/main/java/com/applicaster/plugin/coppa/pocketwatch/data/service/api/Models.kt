package com.applicaster.plugin.coppa.pocketwatch.data.service.api

import com.google.gson.annotations.SerializedName

data class UninstallResponse(@SerializedName("ok") val success: Boolean)
data class UninstallChannel(
    @SerializedName("channel_id") val channelId: String,
    @SerializedName("device_type") val deviceType: String
)

