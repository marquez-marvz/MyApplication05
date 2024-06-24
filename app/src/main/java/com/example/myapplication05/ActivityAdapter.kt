package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_computation.view.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.student_import.view.*
import kotlinx.android.synthetic.main.student_row.view.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_row.view.*

import kotlinx.android.synthetic.main.util_confirm.view.*


class ActivityAdapter(val context: Context, val activity: List<ActivityModel>) :
    RecyclerView.Adapter<ActivityAdapter.MyViewHolder>()   {


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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {

        var currentActivity: ActivityModel? = null
        var currentPosition: Int = 0

        init {
//            itemView.btnEdit.setOnLongClickListener {
//                ActivityMain.ShowDialog("EDIT", context, currentActivity)
//                true
//            }
            itemView.btnEdit.setOnClickListener {
                itemView.rowItem.isEnabled = true
                itemView.rowDescription.isEnabled = true
                itemView.btnEdit.setVisibility(View.INVISIBLE);
                itemView.btnSave.setVisibility(View.VISIBLE);
                itemView.btnActivityCategory.isEnabled = true
            }

            itemView.btnActivityCode.setOnClickListener {
                Util.ACT_CODE = currentActivity!!.activityCode
                Util.ACT_CURRENT_SECTION = currentActivity!!.sectionCode
                Util.ACT_DESCRIPTION = currentActivity!!.description
                Util.ACT_ITEM = currentActivity!!.item
                Util.ACT_CATEGORY = currentActivity!!.category
                val intent = Intent(context, ScoreMain::class.java)
                context.startActivity(intent)

            }

            itemView.btnSave.setOnClickListener {
                itemView.rowItem.isEnabled = true
                itemView.btnEdit.setVisibility(View.VISIBLE);
                itemView.btnSave.setVisibility(View.INVISIBLE);
                var e = currentActivity
               var  description = itemView.rowDescription.text.toString()
               var item = itemView.rowItem.text.toString()
                val category = itemView.btnActivityCategory!!.text.toString()
                val db: TableActivity = TableActivity(context)
                db.ManageActivity("EDIT", e!!.activityCode, e!!.sectionCode, description, item, category, e!!.gradingPeriod)
                ActivityMain.ActivityUpdateListContent(context)
                ActivityMain.activityAdapter!!.notifyDataSetChanged()
                itemView.btnActivityCategory.isEnabled = false

            }


            itemView.setOnLongClickListener {


                true
            }

            itemView.btnActivityCategory.setOnClickListener {
               var txt =  itemView.btnActivityCategory!!.text
                if (txt=="PT") {
                    itemView.btnActivityCategory!!.text = "QUIZ"

                //   .backgroundTintList =  "69F0AE"

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        itemView.btnActivityCategory.backgroundTintList = AppCompatResources.getColorStateList(context, R.color.color_yellow)
                    }
                }
                else if (txt=="QUIZ") {
                    itemView.btnActivityCategory!!.text = "PROJECT"
                    itemView.btnActivityCategory.setBackgroundColor(Color.parseColor("#69F0AE"))
                }
                else if (txt=="PROJECT")
                    itemView.btnActivityCategory!!.text = "PARTICIPATION"
                else if (txt=="PARTICIPATION")
                    itemView.btnActivityCategory!!.text = "EXAM"
                else if (txt=="EXAM")
                    itemView.btnActivityCategory!!.text = "ATTENDANCE"
                else
                    itemView.btnActivityCategory!!.text = "PT"


                val db: TableActivity = TableActivity(context)
                var e = currentActivity
                val newCategory =  itemView.btnActivityCategory!!.text.toString()
                db.UpdateCategory(e!!.sectionCode, e!!.activityCode, newCategory)

            }



//            itemView.btnSubmissionNo.setOnClickListener {
//                val dlgStudent =
//                    LayoutInflater.from(context).inflate(R.layout.grade_computation, null)
//                val title = "No " + currentActivity!!.description
//                val mBuilder = android.app.AlertDialog.Builder(context).setView(dlgStudent)
//                    .setTitle("No " + currentActivity!!.description)
//
//                val confirmDialog = mBuilder.show()
//                confirmDialog.setCanceledOnTouchOutside(false);
//                confirmDialog.getWindow()!!
//                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
//                confirmDialog.setCanceledOnTouchOutside(false);
//                val act = currentActivity
//                var stud = ""
//                val dbactivity: TableActivity = TableActivity(context)
//                val activity: List<ScoreModel>
//                activity =
//                    dbactivity.GetScoreList(act!!.sectionCode, act!!.activityCode, "STATUS", "NO")
//
//                var x = 1;
//                for (e in activity) {
//                    stud = stud + x + ")" + e.lastnanme + "  " + e.firstname + "\n"
//                    x++
//                }
//                dlgStudent.txtComputation.setText(stud)
//
//
//                dlgStudent.btnCopy.setOnClickListener {
//                    val msg = title + "\n" + dlgStudent.txtComputation.text.toString()
//                    Util.CopyText(context, msg, "COPY")
//                }
//
//                dlgStudent.btnExport.setOnClickListener {
//                    val msg = title + "\n" + dlgStudent.txtComputation.text.toString()
//                    Util.CopyText(context, msg, "COPY")
//
//
//
//                    Util.ExportToGoogleSheet(context, currentActivity!!.sectionCode, msg, "Export Grades", "ExportActivity")
//                }
//
//
//            }
//
//
//
//            itemView.btnMinusInfo.setOnClickListener {
//                itemView.btnPlusInfo.setVisibility(View.VISIBLE);
//                itemView.btnMinusInfo.setVisibility(View.INVISIBLE);
//                itemView.btnSubmissionNo.setVisibility(View.GONE);
//                itemView.btnSubmissionOk.setVisibility(View.GONE);
//                itemView.btnSubmissionYes.setVisibility(View.GONE);
//                itemView.btnCategory.setVisibility(View.GONE);
//                itemView.btnSubmissionDropped.setVisibility(View.GONE);
//            }

            itemView.rowBtnMenu.setOnClickListener {
                  showPopMenu(itemView)
            }



            itemView.rowItem.setOnClickListener{
                Log.e("RRR", "@@@")
                var num = itemView.rowItem.text.toString().toInt()
               num= num + 5
                itemView.rowItem.setText(num.toString())

                var e = currentActivity
                var  description = itemView.rowDescription.text.toString()
                val category = itemView.btnActivityCategory!!.text.toString()
                val db: TableActivity = TableActivity(context)
                db.ManageActivity("EDIT", e!!.activityCode, e!!.sectionCode, description, num.toString(), category, e!!.gradingPeriod)
            }


            itemView.rowItem.setOnLongClickListener(){
                itemView.rowItem.setText("0")
                var e = currentActivity
                var  description = itemView.rowDescription.text.toString()
                val category = itemView.btnActivityCategory!!.text.toString()
                val db: TableActivity = TableActivity(context)
                db.ManageActivity("EDIT", e!!.activityCode, e!!.sectionCode, description, "0",  category, e!!.gradingPeriod)
                true
            }

            itemView.btnGradingPeriod.setOnClickListener(){
                var txt = itemView.btnGradingPeriod.text.toString()
                Log.e("4580", txt)
                var gradingPeriod= ""
                if (txt=="FIRST") {
                    itemView.btnGradingPeriod.text = "SECOND"
                    gradingPeriod = "SECOND"
                }

                else if (txt=="SECOND") {
                    itemView.btnGradingPeriod.text = "THIRD"
                    gradingPeriod = "THIRD"
                }

                else if (txt=="THIRD") {
                    itemView.btnGradingPeriod.text = "FIRST"
                    gradingPeriod = "FIRST"
                }


                Log.e("4580", gradingPeriod)
                var e = currentActivity
                val db: TableActivity = TableActivity(context)
                db.ManageActivity("EDIT", e!!.activityCode, e!!.sectionCode, e!!.description, e!!.item.toString(),  e!!.category, gradingPeriod)
                true
            }



//       211




            itemView.rowBtnDelete.setOnClickListener {
                val mymain = LayoutInflater.from(context)
                    .inflate(R.layout.sched_main, null) // LayoutInflater layoutInflater = getLayoutInflater(null); //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);
                val section = ActivityMain.GetSubject()
                val activityCode = currentActivity!!.activityCode
                val description = currentActivity!!.description


                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $description in $section?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false)

                dlgconfirm.btnYes.setOnClickListener {
                    val mydatabase: TableActivity = TableActivity(context)
                    mydatabase.ManageActivity("DELETE", activityCode, section)
                    ActivityMain.ActivityUpdateListContent(context)
                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                } //
                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                } //
            }


        } //init

        fun setData(myactivity: ActivityModel?, pos: Int) {
            val db: TableAttendance = TableAttendance(context)
            itemView.btnActivityCode.text = myactivity!!.activityCode
            itemView.btnGradingPeriod.text = myactivity!!.gradingPeriod
            itemView.rowDescription.setText(myactivity!!.description.toString())
            itemView.rowDescription.setTextColor(Color.BLACK);
            itemView.rowItem.setText(myactivity.item.toString())
            itemView.rowItem.setTextColor(Color.BLACK);
            itemView.rowItem.isEnabled = false
            //itemView.btnCategory.setText(myactivity.category.toString())
            itemView.btnActivityCategory.isEnabled = false
            if (myactivity.category=="QUIZ")
                itemView.btnActivityCategory.setBackgroundColor(Color.parseColor("#69F0AE"))
            else if (myactivity.category=="PROJECT")
                itemView.btnActivityCategory.setBackgroundColor(Color.parseColor("#64B5F6"))
            else if (myactivity.category=="PT")
                itemView.btnActivityCategory.setBackgroundColor(Color.parseColor("#FFB74D"))
            else if (myactivity.category=="EXAM")
                itemView.btnActivityCategory.setBackgroundColor(Color.parseColor("#BA68C8"))
            else if (myactivity.category=="PARTICIPATION")
                itemView.btnActivityCategory.setBackgroundColor(Color.parseColor("#FFCCCB"))

            var countYes =
                db.GetYesSubmissionCount(myactivity!!.sectionCode, myactivity!!.activityCode)
            itemView.rowActivityCount.setText(countYes) //itemView.rowBtnStatus.text = myactivity.gradingPeriod
            //itemView.btnActivityCategory.text = myactivity.category
            this.currentActivity = myactivity
            this.currentPosition = pos
//            itemView.btnSubmissionNo.text =
//                "NO=" + db.GetSubmissionCount(myactivity!!.sectionCode, myactivity!!.activityCode, "NO")
//            itemView.btnSubmissionOk.text =
//                "OK=" + db.GetSubmissionCount(myactivity!!.sectionCode, myactivity!!.activityCode, "OK")
//            itemView.btnSubmissionYes.text =
//                "YES=" + db.GetSubmissionCount(myactivity!!.sectionCode, myactivity!!.activityCode, "YES")
//            itemView.btnSubmissionDropped.text =
//                "DR=" + db.GetSubmissionCount(myactivity!!.sectionCode, myactivity!!.activityCode, "DRP")

//            itemView.btnSubmissionNo.setVisibility(View.GONE);
//            itemView.btnSubmissionOk.setVisibility(View.GONE);
//            itemView.btnSubmissionYes.setVisibility(View.GONE);
//            itemView.btnCategory.setVisibility(View.GONE);
//            itemView.btnSubmissionDropped.setVisibility(View.GONE);
        }


        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun showPopMenu(v: View) {
            val popup = PopupMenu(context,itemView.rowBtnMenu, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_activity)
            popup.show()
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            val selected = item.toString() //

            if (selected == "Duplicate Activity") {
                val db: TableActivity = TableActivity(context)
                var e = currentActivity
                val  activityCode =  db.GetNewActivityCode(e!!.sectionCode)
                db.ManageActivity("ADD", activityCode, e!!.sectionCode, e!!.description, e!!.item.toString(), e!!.category, e!!.gradingPeriod)
                db.AddStudeScore(e!!.sectionCode, activityCode);
                ActivityMain.ActivityUpdateListContent(context)
                notifyDataSetChanged()

            }


            Log.e("MMM", selected)
            if (selected == "Duplicate With Score") {
                Log.e("MMM", "inside duplicate ")

                val db: TableActivity = TableActivity(context)
                var e = currentActivity
                val  activityCode =  db.GetNewActivityCode(e!!.sectionCode)
                db.ManageActivity("ADD", activityCode, e!!.sectionCode, e!!.description, e!!.item.toString(), e!!.category, e!!.gradingPeriod)
                db.DuplicateActivityWithScore(e!!.sectionCode, activityCode, e!!.activityCode);
                ActivityMain.ActivityUpdateListContent(context)
                notifyDataSetChanged()



            }

            if (selected == "Delete Activity") {

                val mymain = LayoutInflater.from(context)
                    .inflate(R.layout.sched_main, null) // LayoutInflater layoutInflater = getLayoutInflater(null); //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);
                val section = ActivityMain.GetSubject()
                val activityCode = currentActivity!!.activityCode
                val description = currentActivity!!.description


                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $description in $section?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false)

                dlgconfirm.btnYes.setOnClickListener {
                    val mydatabase: TableActivity = TableActivity(context)
                    mydatabase.ManageActivity("DELETE", activityCode, section)
                    ActivityMain.ActivityUpdateListContent(context)
                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                } //
                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                }
            }

            if (selected=="Show Dialog Activity"){
                ActivityMain.ShowDialog("EDIT", context, currentActivity)
            }
           return  true
        }





        //            StudentMain.UpdateListContent(context, "FIRST_ORDER") //        }

            //        else  if (selected =="Sort by LastName" ){
            //            Log.e("menu", "last")
            //            StudentMain.UpdateListContent(context, "LAST_ORDER")
            //        }
            //
            //        else if (selected =="Sort by FirstName"){
            //            StudentMain.UpdateListContent(context, "GENDER_ORDER")
            //        }
            //        else  if (selected =="Sort by Group" ){
            //            StudentMain.UpdateListContent(this, "GROUP_ORDER")
            //        }
            //
            //        StudentMain.adapter!!.notifyDataSetChanged()
           // return true;
        }
    }