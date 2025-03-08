package io.trafficflow.partner.controller.v1

import io.trafficflow.partner.controller.v1.data.AddPartnerRequest
import io.trafficflow.partner.controller.v1.data.AddProductRequest
import io.trafficflow.partner.controller.v1.data.PartnerResponse
import io.trafficflow.partner.controller.v1.data.ProductResponse
import io.trafficflow.partner.service.PartnerService
import io.trafficflow.partner.service.ProductService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/v1/partners")
@RestController
class PartnerRestController(
    private val partnerService: PartnerService,

    private val productService: ProductService,
) {
    @PostMapping
    @ResponseStatus(CREATED)
    fun add(@RequestBody request: AddPartnerRequest) = partnerService.add(request.toDomain())

    @GetMapping
    fun getAll() = partnerService.getAll().map(::PartnerResponse)

    @GetMapping("open")
    fun getAllOpenPartners() = partnerService.getAllPartnersByOpened(isOpened = true).map(::PartnerResponse)

    @GetMapping("{id}")
    fun get(@PathVariable id: Long) = partnerService.get(id).let(::PartnerResponse)

    @PostMapping("{id}/products")
    @ResponseStatus(CREATED)
    fun addProduct(
        @PathVariable id: Long,
        @RequestBody request: AddProductRequest,
    ) = productService.add(request.toDomain(partnerId = id))

    @GetMapping("{id}/products/open")
    fun getAllOpenProducts(@PathVariable id: Long) =
        productService.getAllByPartnerIdAndOpened(partnerId = id, isOpened = true).map(::ProductResponse)

    @GetMapping("{id}/products/{productId}")
    fun getProduct(
        @PathVariable id: Long,
        @PathVariable productId: Long,
    ) = productService.get(partnerId = id, productId = productId).let(::ProductResponse)
}
