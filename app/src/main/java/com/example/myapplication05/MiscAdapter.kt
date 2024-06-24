package com.example.myapplication05

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.misc_option.view.*
import kotlinx.android.synthetic.main.misc_row.view.*
import kotlinx.android.synthetic.main.misc_row.view.rowMiscCode
import kotlinx.android.synthetic.main.misc_row.view.rowMiscDescription
import kotlinx.android.synthetic.main.util_confirm.view.*


class MiscAdapter(val context: Context, val misc: List<MiscModel>) :
    RecyclerView.Adapter<MiscAdapter.MyViewHolder>() {

    companion object {
        var optionList = arrayListOf<MiscModel>()
        var optionAdapter: OptionAdapter? = null

        fun UpdateOptionList(e: MiscModel?, context: Context) {
            val db: TableRandom = TableRandom(context)
            optionList.clear()
            var myOptionList = db.GetOptionList(e!!.SectionCode, e!!.MiscCode)
            for (e in myOptionList) {
                optionList.add(MiscModel(e.MiscCode, e.MiscDescription, e.OptionNumber, e.OptionDescription, e.SectionCode))
            }
        }

        fun ViewOption(dlgOptionBox: View, context: Context) {
            val layoutmanager = LinearLayoutManager(context)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            dlgOptionBox.listOption.layoutManager = layoutmanager
            optionAdapter = OptionAdapter(context, optionList)
            dlgOptionBox.listOption.adapter = optionAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.misc_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return misc.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myMisc = misc[position]
        holder.setData(myMisc, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentMisc: MiscModel? = null
        var currentPosition: Int = 0


        init {
            itemView.setOnClickListener {
                val intent = Intent(context, MiscStudent::class.java)
                context.startActivity(intent)
                Util.MISC_CURRENT_SECTION = currentMisc!!.SectionCode
                Util.MISC_DESCRIPTION = currentMisc!!.MiscDescription
                Util.MISC_CODE = currentMisc!!.MiscCode
//                .ATT_CURRENT_DATE = currentSched!!.myDate
//                Util.ATT_CURRENT_SECTION = Util.CURRENT_SECTION
//                Util.ATT_CURRENT_AMPM = currentSched!!.ampm
            }


            itemView.rowBtnEdit.setOnClickListener {
                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                itemView.rowBtnSave.setVisibility(View.VISIBLE);
                itemView.rowMiscDescription.isEnabled = true
            } //init

            itemView.rowBtnSave.setOnClickListener {
                val db: TableRandom = TableRandom(context)
                var e = currentMisc
                var newDescription = itemView.rowMiscDescription.getText().toString()
                db.EditMiscDescription(e!!.MiscCode, newDescription)

                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                itemView.rowMiscDescription.isEnabled = false
                //db.ManageMisc("ADD", miscCode, description, miscCode + "-1", "-", sectionCode)
                //SaveData()
            } //init


            itemView.rowBtnDelete.setOnClickListener {
                var desc = currentMisc!!.MiscDescription
                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $desc?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {
                    val db: TableRandom = TableRandom(context)
                    db.DeleteMisc(currentMisc!!.MiscCode)
                    MiscMain.MiscUpdateListContent(context, currentMisc!!.SectionCode)
                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                } //init\

            } //init

            itemView.setOnLongClickListener {


                val dlgOptionBox = LayoutInflater.from(context).inflate(R.layout.misc_option, null)
                val mBuilder =
                    AlertDialog.Builder(context).setView(dlgOptionBox).setTitle("MANAGE OPTION")
                val optionBoxDialog = mBuilder.show()
                optionBoxDialog.setCanceledOnTouchOutside(false);
                dlgOptionBox.rowMiscCode.setText(currentMisc!!.MiscCode.toString())
                dlgOptionBox.rowMiscDescription.setText(currentMisc!!.MiscDescription.toString())
                dlgOptionBox.rowMiscDescription.isEnabled = false
                UpdateOptionList(currentMisc, context)
                ViewOption(dlgOptionBox, context)

                dlgOptionBox.btnAddOption.setOnClickListener {

                    val db: TableRandom = TableRandom(context)
                    val e = currentMisc
                    val newOptionNumber = db.GetNewOptionNumber(e!!.SectionCode, e!!.MiscCode);
                    Log.e("SSS", newOptionNumber)
                    db.ManageMisc("ADD", e!!.MiscCode, e!!.MiscDescription, newOptionNumber, "-", e!!.SectionCode)

                    UpdateOptionList(e, context)
                    optionAdapter!!.notifyDataSetChanged()
                }

                true
            } //long


        }


        fun setData(myatt: MiscModel?, pos: Int) {
            itemView.rowMiscCode.text = myatt!!.MiscCode
            itemView.rowMiscDescription.setText(myatt!!.MiscDescription)
            itemView.rowMiscDescription.isEnabled = false
            this.currentMisc = myatt;
            this.currentPosition = pos
        }


    }
}
