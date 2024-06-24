//package com.example.myapplication05
//
//class KeyWordAdapter {}


package com.example.myapplication05

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.fragment.PictureFragment
import com.example.myapplication05.testpaper.GetQuizKeyWord
import com.example.myapplication05.testpaper.SetQuizKeyWord
import com.example.myapplication05.testpaper.TestCapture
import com.example.myapplication05.testpaper.TestCheck
import kotlinx.android.synthetic.main.answer_main.*
import kotlinx.android.synthetic.main.answer_row.view.*
import kotlinx.android.synthetic.main.answer_row.view.btnEdit
import kotlinx.android.synthetic.main.answer_row.view.btnSave
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.fragment_picture.view.*
import kotlinx.android.synthetic.main.keyword_row.view.*
import kotlinx.android.synthetic.main.paperpic_row.view.*
import kotlinx.android.synthetic.main.random_row.view.*
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.test_check.view.*
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class KeyWordAdapter(val context: Context, val keyword: List<KeywordModel>) :
    RecyclerView.Adapter<KeyWordAdapter.MyViewHolder>() {
    val db2: DatabaseHandler = DatabaseHandler(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.keyword_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return keyword.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myKeyword = keyword[position]
        holder!!.setData(myKeyword, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentKeyword: KeywordModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnKeyWordAdapter.setOnClickListener {
                var keyword =itemView.btnKeyWordAdapter.text.toString()
                val e = currentKeyword
                if (e!!.Category == "GRP"){
                    PictureFragment.inputDialogKeyword!!.dismiss()
                    PictureFragment.viewpicture!!.btnPaperKeyWord.setText(keyword)
                    val e = currentKeyword
                    db2.UpdateDefaultKeyWord(e!!.Category, e!!.Section, e!!.Keyword)

                } else {
//                    PaperPicture.inputDialogKeyword!!.dismiss()
//                    PaperPicture.varbtnPaperKeyWord!!.setText(keyword)
                    val e = currentKeyword
                    Log.e("TTT", Util.TAB_TEST_POSITION.toString())
                    if (Util.TAB_TEST_POSITION == 2) {
                        db2.SetQuizKeyWord(TestCheck.currentSubject, TestCheck.currentQuizCode, e!!.Keyword)
                        TestCheck.inputDialogKeyword!!.dismiss()
                        TestCheck.viewcheck!!.btnKeyWord.setText(keyword)
                    }
                    else if (Util.TAB_TEST_POSITION == 0) {
                        db2.UpdateDefaultKeyWord(e!!.Category, e!!.Section, e!!.Keyword)
                        TestCapture.inputDialogKeyword!!.dismiss()
                        TestCapture.viewcapture!!.btnPaperKeyWord.setText(keyword)
                    }


                }

            }

            itemView.btnDeleteKeyWord.setOnClickListener {

                val e = currentKeyword
                db2.DeleteKeyWord(e!!.Section, e!!.Keyword)
                notifyDataSetChanged()

            }



        }


        //  dlgAttendance!!.btnPresentDialog.setBackgroundResource(android.R.drawable.btn_default);
        fun setData(myatt: KeywordModel, pos: Int) { // itemView.btnLastName.setText(myatt!!.lastname.toString())
            itemView.btnKeyWordAdapter.setText(myatt!!.Keyword)
            itemView.btnPeriod.setText(myatt!!.GradingPeriod)
            this.currentKeyword = myatt;
            this.currentPosition = pos
        }
    }
}


fun DatabaseHandler.DeleteKeyWord(section: String, keyword: String) {

    val keywordList: ArrayList<KeywordModel> = ArrayList<KeywordModel>()

    var sql: String = """ DELETE FROM tbkeyword
                                where Section='$section' 
                                and  KeyWord='$keyword' 
                """
   SQLManage(sql)
}





