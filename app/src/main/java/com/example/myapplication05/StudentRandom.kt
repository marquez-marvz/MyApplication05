package com.example.myapplication05


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.student_random.*

import kotlin.math.roundToInt




class StudentRandom : AppCompatActivity() {
    var count = 0;
    var randomNumber = Array(10) { 0 }
    var arr = Array(10) { "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_random)
        MyArray();




        btnShow.setOnClickListener {
            var i = randomNumber[count];
            txtname.setText(arr[i])
            Toast.makeText(this, "$count  abc",Toast.LENGTH_SHORT).show();
            count++;

        }
    }

        fun MyArray(){

        var flag= Array(10) { false }
        //var randomNumber= Array(10) { 0}

        arr[0] = "Marvin"
        arr[1] = "Glenn"
        arr[2] = "Karl"
        arr[3] = "Alden"
        arr[4] = "Jessa"
        arr[5] = "Erwim"
        arr[6] = "Chloe"
        arr[7] = "Raphael"
        arr[8] = "Nathan"
        arr[9] = "Ching"
        print(arr[0])


        var num = 0;
        for (i in 0..9) {
            do {
                 num = (0 until 10).random()

            }while (flag[num]!=false);
            print(num);
            flag[num] = true;
            randomNumber [i] = num;


        }

        for (i in 0..9) {
            var ctr = randomNumber [i]
            println(arr[ctr])
        }
    }
}
