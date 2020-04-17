package com.example.myapplication05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        btnCompute.setOnClickListener {


                       txtfirst.isEnabled = false;
            txtsecond.isEnabled = false;
            txtanswer.isEnabled = false;
            var num1:String  =  txtfirst.text.toString()
             var num2: String = txtsecond.text.toString()
            var answer =0;
            answer = num1.toInt() + num2.toInt()
          txtanswer.setText(answer.toString())
//
            Toast.makeText(this, "$num1 $num2  200",Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, answer,Toast.LENGTH_SHORT).show()

//            var msg = txtfirst.text
//            txtsecond.setText("")
//            txtanswer.setText("")
        }


        btnClear.setOnClickListener {


         //   Toast.makeText(this, "Second Button",Toast.LENGTH_SHORT).show()
            txtfirst.isEnabled = true;
            txtsecond.isEnabled = true;
            txtanswer.isEnabled = false;
        txtfirst.setText("")
        txtsecond.setText("")
        txtanswer.setText("")
    }



    }
}
