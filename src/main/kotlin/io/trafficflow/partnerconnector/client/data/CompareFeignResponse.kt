package io.trafficflow.partnerconnector.client.data

class CompareFeignResponse(
    val id: Long,

    val isSucceeded: Boolean,

    val message: String,

    val companyId: Long,

    val userEmail: String,

    val productId: Long,
)
