package com.cokimutai.agrobizna.database

import com.cokimutai.agrobizna.supports.FarmDetailsCumulatives








interface FirebaseDatabaseInterface {
    fun getPluckingAmntCm(onResult: (FarmDetailsCumulatives) -> Unit)
    fun getTippingAmntCm(onResult: (FarmDetailsCumulatives) -> Unit)
    fun getReceivedAmntCm(onResult: (FarmDetailsCumulatives) -> Unit)
    fun getTeaExpensesAmntCm(onResult: (FarmDetailsCumulatives) -> Unit)

}
