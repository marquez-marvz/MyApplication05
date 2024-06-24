


package com.example.myapplication05

import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.misc_option_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlin.random.Random


class OptionAdapter(val context: Context, val myoption: List<MiscModel>) :
    RecyclerView.Adapter<OptionAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.misc_option_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return myoption.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myMisc = myoption[position]
        holder.setData(myMisc, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentOption: MiscModel? = null
        var currentPosition: Int = 0


        init {


            itemView.rowBtnEdit.setOnClickListener {
                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                itemView.rowBtnSave.setVisibility(View.VISIBLE);
                itemView.rowOptionDescription.isEnabled = true
            } //init

            itemView.rowBtnSave.setOnClickListener {
                val db: TableRandom = TableRandom(context)
                var e = currentOption
                var newOptionDescription = itemView.rowOptionDescription.getText().toString()
                db.EditOptionDescription(e!!.MiscCode, e!!.OptionNumber,   newOptionDescription)

                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                itemView.rowOptionDescription.isEnabled = false
                //db.ManageMisc("ADD", miscCode, description, miscCode + "-1", "-", sectionCode)
                //SaveData()
            } //init


            itemView.rowBtnDelete.setOnClickListener {
                var desc = currentOption!!.OptionDescription
                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $desc?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {
                    val db: TableRandom = TableRandom(context)
                    db.DeleteMiscOption(currentOption!!.MiscCode,currentOption!!.OptionNumber)
                    MiscAdapter.UpdateOptionList(currentOption, context)
                    MiscAdapter.optionAdapter!!.notifyDataSetChanged()
                    confirmDialog.dismiss()
                } //init\

                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                } //init\


            } //init





        }


        fun setData(theOption: MiscModel?, pos: Int) {
            Log.e("XXX", theOption!!.OptionDescription)
            itemView.rowOptionDescription.setText(theOption!!.OptionDescription)
           // itemView.rowOptionDescription.setText("AAA")
            itemView.rowOptionDescription.isEnabled = false
            this.currentOption = theOption;
            this.currentPosition = pos
        }






    }
}
