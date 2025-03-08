package io.trafficflow.common.pagination

import org.springframework.data.domain.PageRequest

data class Pagination(
    val page: Int = 1,

    val size: Int = 30,
) {
    fun toPageable() = PageRequest.of(
        page - 1,
        size,
    )
}
