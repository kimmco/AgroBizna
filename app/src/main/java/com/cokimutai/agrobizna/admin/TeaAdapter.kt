package com.cokimutai.agrobizna.admin






import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cokimutai.agrobizna.R
import com.cokimutai.agrobizna.supports.FarmDetails


class TeaAdapter(private val teaList: ArrayList<FarmDetails>, val teaType: Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class TeaViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val teaDate : TextView = itemView.findViewById(R.id.tea_date_holder)
        val teaWeight : TextView = itemView.findViewById(R.id.tea_weight_holder)
        val weed_card : CardView = itemView.findViewById(R.id.pluck_card)

        fun bind(farmDetails: FarmDetails) {
            if (farmDetails.date.isNullOrEmpty() || farmDetails.pluckingWeight.isNullOrEmpty()) {
                weed_card.visibility = View.GONE
            } else {
                weed_card.visibility = View.VISIBLE
                teaDate.text = farmDetails.date
                teaWeight.text = farmDetails.pluckingWeight + "kg"
            }
        }

    }

    class WeedingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val weedingDate : TextView = itemView.findViewById(R.id.weed_date_holder)
        val weedSize : TextView = itemView.findViewById(R.id.weed_size_holder)
        val weedingCost : TextView = itemView.findViewById(R.id.weed_cost_holder)
        val weed_card : CardView = itemView.findViewById(R.id.weed_card)

        fun bind(farm: FarmDetails){
      //      for (farm in farmDetails) {
                if (farm.weedingDate.isNullOrEmpty() ||
                        farm.weedingDate.isNullOrEmpty() ||
                        farm.weedingDate.isNullOrEmpty()) {
                  // return

                    weed_card.visibility = View.GONE
               } else {
                    weed_card.visibility = View.VISIBLE
                    weedingDate.text = farm.weedingDate
                    weedSize.text = farm.weedingSize
                    weedingCost.text = "KSHs." + farm.weedingCost
                 }

        }

    }

    class OthersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val othersDate : TextView = itemView.findViewById(R.id.other_tea_date_holder)
        val othersTeaDetails : TextView = itemView.findViewById(R.id.other_tea_details_holder)
        val othersTeaExpCost : TextView = itemView.findViewById(R.id.other_tea_cost_holder)
        val othersTeaExpSize : TextView = itemView.findViewById(R.id.other_tea_unit_holder)
        val others_teaExpns_card : CardView = itemView.findViewById(R.id.other_tea_exps_card)

        fun bind(farm: FarmDetails){
            //      for (farm in farmDetails) {
           if (farm.othersTeaExpnsDate.isNullOrEmpty() ||
                    farm.othersTeaExpsDescription.isNullOrEmpty() ||
                    farm.othersTeaExpsCost.isNullOrEmpty() ||
                    farm.othersTeaExpsSize.isNullOrEmpty()) {
                // return

               others_teaExpns_card.visibility = View.GONE
            } else {
                others_teaExpns_card.visibility = View.VISIBLE
                othersTeaExpCost.visibility = View.VISIBLE
                othersDate.text = farm.othersTeaExpnsDate
                othersTeaDetails.text = farm.othersTeaExpsDescription
                othersTeaExpSize.text = farm.othersTeaExpsSize
                othersTeaExpCost.text = farm.othersTeaExpsCost
            }

        }

    }

    class CattleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val othersDate : TextView = itemView.findViewById(R.id.other_tea_date_holder)
        val othersTeaDetails : TextView = itemView.findViewById(R.id.other_tea_details_holder)
        val othersTeaExpCost : TextView = itemView.findViewById(R.id.other_tea_cost_holder)
        val othersTeaExpSize : TextView = itemView.findViewById(R.id.other_tea_unit_holder)
        val others_teaExpns_card : CardView = itemView.findViewById(R.id.other_tea_exps_card)

        fun bind(farm: FarmDetails){
            //      for (farm in farmDetails) {
            if (farm.cattleDate.isNullOrEmpty() ||
                farm.cattleExpensDescription.isNullOrEmpty() ||
                farm.cattleExpensCost.isNullOrEmpty()) {
                // return

                others_teaExpns_card.visibility = View.GONE
            } else {
                others_teaExpns_card.visibility = View.VISIBLE
                othersDate.text = farm.cattleDate
                othersTeaDetails.text = farm.cattleExpensDescription
                 othersTeaExpSize.visibility = View.INVISIBLE
                othersTeaExpCost.text = farm.cattleExpensCost
            }

        }

    }

    class GeneralExpnsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val othersDate : TextView = itemView.findViewById(R.id.other_tea_date_holder)
        val othersTeaDetails : TextView = itemView.findViewById(R.id.other_tea_details_holder)
        val othersTeaExpCost : TextView = itemView.findViewById(R.id.other_tea_cost_holder)
        val othersTeaExpSize : TextView = itemView.findViewById(R.id.other_tea_unit_holder)
        val others_teaExpns_card : CardView = itemView.findViewById(R.id.other_tea_exps_card)

        fun bind(farm: FarmDetails){
            //      for (farm in farmDetails) {
            if (farm.genExpnsDate.isNullOrEmpty() ||
                    farm.othersGeneralExpsDescription.isNullOrEmpty() ||
                    farm.othersGeneralExpsCost.isNullOrEmpty()) {

                // return

                others_teaExpns_card.visibility = View.GONE
           } else {
                others_teaExpns_card.visibility = View.VISIBLE
                othersDate.text = farm.genExpnsDate
                othersTeaDetails.text = farm.othersGeneralExpsDescription
                othersTeaExpSize.visibility = View.INVISIBLE
                othersTeaExpCost.text = farm.othersGeneralExpsCost
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(teaType){
            0  -> return TeaViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.tea_list_item, parent, false))

            1  -> return WeedingViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.weeding_list_item, parent, false))

            2  -> return OthersViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.tea_others_expslist_item, parent, false))

            3  -> return CattleViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.tea_others_expslist_item, parent, false))

            4  -> return GeneralExpnsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.tea_others_expslist_item, parent, false))

        }
       /* val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tea_list_item,
        parent, false)

        return TeaViewHolder(itemView)*/
         return WeedingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tea_list_item,
                 parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        //   teaList[position]
          // teaList.addAll(listOf(teaList[position]))


      /* teaList.removeIf { it.weedingDate.isNullOrEmpty() }
        teaList.filter { it.weedingCost.isNullOrEmpty() ||
                it.weedingSize.isNullOrEmpty() || it.weedingDate.isNullOrEmpty() } */

      //  if (teaList[position].weedingSize != null) {

            val currentTea = teaList[position]   //teaList[position]
            when (teaType) {
                0 -> ((TeaViewHolder(holder.itemView).bind(currentTea)))
                1 -> ((WeedingViewHolder(holder.itemView).bind(currentTea)))
                2 -> ((OthersViewHolder(holder.itemView).bind(currentTea)))
                3 -> ((CattleViewHolder(holder.itemView).bind(currentTea)))
                4 -> ((GeneralExpnsViewHolder(holder.itemView).bind(currentTea)))
            }

    }

    override fun getItemCount(): Int {
        return teaList.size
    }


}