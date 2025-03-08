package io.trafficflow.loancomparison.repository

import io.trafficflow.loancomparison.domain.LoanComparison
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LoanComparisonRepository : JpaRepository<LoanComparison, Long>
