package io.trafficflow.partnerconnector.client.data

class CompareFeignRequest(
    val id: Long,

    val companyId: Long = 0,

    val userEmail: String,

    val partnerId: Long,

    val productId: Long,
)
