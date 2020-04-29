package com.example.myapplication05

class StudentModel (
        var studentno:String ,
        var firstname:String,
        var lastname:String,
        var grp:String,
        var sectioncode:String,
        var gender:String
){

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


class IndividualModel (
        var ampm: String,
        var myDate: String,
        var attendanceStatus: String,
        var remark: String
)




class SummaryModel(
    var studentNo: String,
    var completeName:String,
    var prsentCount:Int,
    var lateCount:Int,
    var absentCount:Int,
    var excuseCount:Int
)



class ActivityModel(
        var activityCode: String,
        var sectionCode: String,
        var description: String,
        var item: Int,
        var status: String
)



