package com.cokimutai.agrobizna.supports

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class FarmDetails (
     val  pluckingWeight : String? = null,
     val  tippingWeight : String? = null,
     val  date : String? = null,
     val  weedingDate : String? = null,
     val  dateReceivedAmnt : String? = null,
     val  receivedAmnt : String? = null,
     val  cattleDate : String? = null,
     val  othersTeaExpnsDate : String? = null,
     val  genExpnsDate : String? = null,
     val  pluckingCost : String? = null,
     val  weedingSize : String? = null,
     val  weedingCost : String? = null,
     val  othersTeaExpsDescription : String? = null,
     val  othersTeaExpsSize : String? = null,
     val  othersTeaExpsCost : String? = null,
     val  cattleExpensDescription : String? = null,
     val  cattleExpensCost : String? = null,
     val  othersGeneralExpsDescription : String? = null,
     val  othersGeneralExpsCost : String? = null,
     val  receivedAmntCumulative : String? = null,
     val  tippingAmntCumulative : String? = null,
     val  pluckngAmntCumulative : String? = null,
     val  teaExpensesCumulative : String? = null,
         ) : Parcelable
@Parcelize
data class FarmDetailsCumulatives (
        val  receivedAmntCumulative : String? = null,
        val  tippingAmntCumulative : String? = null,
        val  pluckngAmntCumulative : String? = null,
        val  teaExpensesCumulative : String? = null,
          ) : Parcelable