package com.example.myapplication05

class StudentModel (var studentno:String ,  var firstname:String, var lastname:String, var grp:String, var sectioncode:String ){

}


class ScheduleModel(
    var ampm: String,
    var myDate: String,
    var sectioncode: String,
    var renark: String
)

class AttendanceModel(
    var ampm: String,
    var myDate: String,
    var sectionCode: String,
    var studentNo: String,
    var completeName:String,
    var groupNumber:String,
    var attendanceStatus: String,
    var remark: String
)


