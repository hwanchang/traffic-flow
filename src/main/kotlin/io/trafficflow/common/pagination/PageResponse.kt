package io.trafficflow.common.pagination

import org.springframework.data.domain.Page

class PageResponse<T, U>(
    page: Page<T>,

    val contents: List<U>,
) {
    val currentPage = page.number + 1

    val totalPages: Int = page.totalPages

    val isLastPage: Boolean = page.isLast

    val currentCount: Int = page.size

    val totalCount: Long = page.totalElements
}
