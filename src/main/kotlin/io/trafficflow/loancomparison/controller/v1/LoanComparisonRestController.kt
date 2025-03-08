package io.trafficflow.loancomparison.controller.v1

import io.trafficflow.common.authentication.jwt.JwtAuthentication
import io.trafficflow.loancomparison.service.LoanComparisonService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/v1/loan-comparisons")
@RestController
class LoanComparisonRestController(
    private val loanComparisonService: LoanComparisonService,
) {
    @PostMapping
    @ResponseStatus(CREATED)
    fun compareLoans(@AuthenticationPrincipal jwtAuthentication: JwtAuthentication) =
        loanComparisonService.compare(jwtAuthentication.id)

    @GetMapping("{id}")
    fun get(@PathVariable id: Long) = loanComparisonService.get(id)

    @PatchMapping("{id}/accepted")
    fun accepted(@PathVariable id: Long) = loanComparisonService.accepted(id)

    @PatchMapping("{id}/rejected")
    fun rejected(@PathVariable id: Long) = loanComparisonService.rejected(id)
}
