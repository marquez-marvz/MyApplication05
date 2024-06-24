package com.example.myapplication05

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.search_main.*
import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import com.android.volley.Response
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import com.android.volley.VolleyError
import com.example.myapplication05.R
import org.json.JSONArray
import org.json.JSONObject


class Sample : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample)

        btnSection.setOnClickListener { // Util.Msgbox(this, "Hello")

            val url = "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec"
            val stringRequest = object : StringRequest(Request.Method.POST, url, Response.Listener {

            }, Response.ErrorListener {

            }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["section"] = "BSIT-2B"
                    params["data"] = "Hello"
                    return params;
                }
            }
            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)
        }


        btnStudent.setOnClickListener { // Util.Msgbox(this, "Hello")
            val url = "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems"
            var stringRequest =
                StringRequest(Request.Method.GET,url, { response ->
                    Log.e("", response)
                    var obj = JSONArray(response);
                    Log.e("", obj.length().toString())
                    var i = 0;
                    while (i< obj.length()){
                        val jsonObject = obj.getJSONObject(i)

                        val iterator: Iterator<String> = jsonObject.keys();
                        while (iterator.hasNext()) {
                             Log.e("", iterator.next().toString())
                        }

                        Log.e("",jsonObject.getString("FirstName"))
                        Log.e("",jsonObject.getString("LastName"))

                        i++;
                    }



//                    val json_contact:JSONObject = JSONObject()
//                    var jsonarray_info:JSONArray= json_contact.getJSONArray(response)

//                    for (i in 0 until jsonarray_info.length()) {
//                        // ID
//                        val sn = jsonarray_info.getJSONObject(i).getString("SN")
//                        Log.i("ID: ", sn)
//
//
//                        val FirstName = jsonarray_info.getJSONObject(i).getString("FirstName")
//                        Log.i("Employee Name: ", FirstName)
//
//
//                        val LastName = jsonarray_info.getJSONObject(i).getString("LastName")
//                        Log.i("Employee Salary: ", LastName)
//
//
//
//                    }
                }) { }

            val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }
}



}

