//package com.example.myapplication05;
//
//import android.graphics.Canvas
//import android.graphics.Paint;
//import android.graphics.pdf.PdfDocument;
//import android.os.Build;
//import android.os.Environment;
//import androidx.annotation.RequiresApi;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//@RequiresApi(api = Build.VERSION_CODES.KITKAT)
//public class hhh {
//
//    void Hello(){
//            PdfDocument myPdfDocument = new PdfDocument();
//            Paint myPaint=  new Paint();
//
//            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
//            PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
//            Canvas canvas = myPage1.getCanvas();
//
//            canvas.drawText("Welcome to Sarthi Technology", 40, 50, myPaint);
//            canvas.drawText("Some Text", 10, 25, ,myPaint);
//            myPdfDocument.finishPage(myPage1);
//
//            File file = new File (Environment.getExternalStorageDirectory(),"Hello.pdf");
//
//            try{
//
//            myPdfDocument.writeTo(new FileOutputStream(file));
//
//            } catch (IOException e){
//                  e.printStackTrace();
//            }
//
//            myPdfDocument.close();
//            }
//
//            void xxx(){
//
//            }
//}
