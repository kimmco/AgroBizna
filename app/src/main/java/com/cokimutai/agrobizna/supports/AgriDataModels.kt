package com.cokimutai.agrobizna.supports

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FarmDetails (
     val  pluckingWeight : String? = null,
     val  tippingWeight : String? = null,
     val  date : String? = null,
     val  weedingDate : String? = null,
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
     val  othersGeneralExpsCost : String? = null
         ) : Parcelable