package com.cokimutai.agrobizna.supports

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SavedPreference {

    const val EMAIL= "email"
    const val USERNAME="username"
    const val TIPPED_AMOUNT="tippingAddedUp"
    const val PLUCKED_AMOUNT="pluckingAddedUp"
    const val TOTAL_RECEIVEDED_AMOUNT="allExpensesCumulated"
    const val RECENT_RECEIVEDED_AMOUNT="teaMoneyJustReceived"
    const val TEA_EXPENSES_TOTAL="receivedAmntCumulated"
    const val TEA_RECENT_PLUCKED="recentWeightPlucked"
    const val RECENT_PLUCKED_DATE="recentDatePlucked"
    const val PLUCKING_ACCUM_NODE="pluckAccuNode"

    private  fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun  editor(context: Context?, const:String, string: String){
        getSharedPreference(
            context
        )?.edit()?.putString(const,string)?.apply()
    }

    fun getEmail(context: Context)= getSharedPreference(
        context
    )?.getString(EMAIL,"")

    fun getRecentReceivedAmount(context: Context)= getSharedPreference(
            context
    )?.getString(  RECENT_RECEIVEDED_AMOUNT,"0")

    fun getTotalReceivedAmount(context: Context)= getSharedPreference(
            context
    )?.getString(  TOTAL_RECEIVEDED_AMOUNT,"0")

    fun getLastDatePlucked(context: Context)= getSharedPreference(
            context
    )?.getString(  RECENT_PLUCKED_DATE,"00")

    fun setEmail(context: Context, email: String){
        editor(
            context,
            EMAIL,
            email
        )
    }

    fun setUsername(context: Context, username:String){
        editor(
            context,
            USERNAME,
            username
        )
    }
    fun setTipping(context: Context, weight:String){
        editor(
                context,
                `TIPPED_AMOUNT`,
                weight
        )
    }
    fun setPlucking(context: Context?, weight:String){
        editor(
                context,
                PLUCKED_AMOUNT,
                weight
        )
    }

    fun setRecentMoney(context: Context, amount:String){
        editor(
                context,
                RECENT_RECEIVEDED_AMOUNT,
                amount
        )
    }

    fun setTotalMoney(context: Context, amount:String){
        editor(
                context,
                TOTAL_RECEIVEDED_AMOUNT,
                amount
        )
    }
    fun setTeaExpnsTotal(context: Context, weight:String){
        editor(
                context,
                TEA_EXPENSES_TOTAL,
                weight
        )
    }

    fun setRecentPluckedTeaWeight(context: Context, weight:String){
        editor(
                context,
                TEA_RECENT_PLUCKED,
                weight
        )
    }

    fun setRecentPluckedTeaDate(context: Context, theDate:String){
        editor(
                context,
                RECENT_PLUCKED_DATE,
                theDate
        )
    }

    fun setPluckingAccumNode(context: Context, theDate:String){
        editor(
            context,
            PLUCKING_ACCUM_NODE,
            theDate
        )
    }

    fun getPluckAccumNode(context: Context) = getSharedPreference(
        context
    )?.getString(PLUCKING_ACCUM_NODE,"MleYeMBBeTotals")


    fun getPluckedTotal(context: Context) = getSharedPreference(
            context
    )?.getString(PLUCKED_AMOUNT, "0.0f")


    fun getTippedTotal(context: Context) = getSharedPreference(
            context
    )?.getString(TIPPED_AMOUNT, "0.0f")


    fun getRecentTeaWeight(context: Context) = getSharedPreference(
            context
    )?.getString(TEA_RECENT_PLUCKED, "0.0f")


}