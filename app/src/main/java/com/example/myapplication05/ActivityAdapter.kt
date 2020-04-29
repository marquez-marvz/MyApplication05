package com.example.myapplication05


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*


class ActivityAdapter(val context: Context, val activity: List<ActivityModel>) :
    RecyclerView.Adapter<ActivityAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.task_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return activity.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = activity[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentActivity: ActivityModel? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnLongClickListener {
                ActivityMain.ShowDialog("EDIT", context, currentActivity)
                true
            }

            itemView.rowBtnStatus.setOnClickListener {
            var newStatus = ""
            var stat =  itemView.rowBtnStatus.text.toString()
            if (stat == "ACTIVE") {
                itemView.rowBtnStatus.setText("INACTIVE")
                newStatus = "INACTIVE"

            } else {
                itemView.rowBtnStatus.setText("INACTIVE")
                newStatus = "ACTIVE"
            }

                val e = currentActivity
                val db: TableActivity = TableActivity(context)
                db.ManageActivity("EDIT", e!!.activityCode, e!!.sectionCode, e!!.description,e!!.item.toString(), newStatus)
                ActivityMain.ActivityUpdateListContent(context)
                notifyDataSetChanged()
        }



        itemView.rowBtnDelete.setOnClickListener{
            val mymain = LayoutInflater.from(context).inflate(R.layout.sched_main, null)
            // LayoutInflater layoutInflater = getLayoutInflater(null);
            //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);

            val activityCode = currentActivity!!.activityCode
            val description = currentActivity!!.description

            val section = ActivityMain.GetSubject()

            val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                .setTitle("Do you like to delete $description in $section?")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false)

            dlgconfirm.btnYes.setOnClickListener {
                val db: TableActivity = TableActivity(context)
                db.ManageActivity("DELETE", activityCode, section)
                ActivityMain.ActivityUpdateListContent(context)
                notifyDataSetChanged()
                confirmDialog.dismiss()
            }
            //
            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()
            }
            //
        }

    } //init

    fun setData(myactivity: ActivityModel?, pos: Int) {
        itemView.rowActivityCode.text = myactivity!!.activityCode
        itemView.rowDescription.text = myactivity.description
        itemView.rowItem.text = myactivity.item.toString()
        itemView.rowBtnStatus.text = myactivity.status
        this.currentActivity = myactivity
        this.currentPosition = pos
    }
}
}