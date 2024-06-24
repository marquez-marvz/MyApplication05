package com.example.myapplication05

class StudentModel (
        var studentno:String ="" ,
        var firstname:String="",
        var lastname:String="",
        var grp:String="",
        var sectioncode:String="",
        var gender:String="",
        var extension:String="",
        var contactNumber:String="",
        var parentcontact:String="",
        var enrollmentStatus:String="",
        var address:String="",
        var emailAddress:String="",
        var orderNumm: Int=0,
        var schoolStudentNumber:String="",
        var expand:String="",
        var middleName: String=""
)



class EnrolleModel (
        var ctr:Int,
        var studentno:String ,
        var firstname:String,
        var lastname:String,
        var Section:String,
        var gender:String,
        var enrollmentStatus:String,
        var studentID:String,
        var grpNumber:String,
        var folderLink:String,
        var randomNum:Int = 0,
        var studentStatus:String = ""
)


class RecitationIndividualModel (
        var studentno:String="" ,
        var firstname:String="",
        var lastname:String="",
        var date:String="",
        var sectioncode:String="",
        var gradingPeriod:String="",
        var renmark:String = "",
)


class RecitationDateModel (
        var schedCode:String="" ,
        var recitationDate:String="",
        var gradingPeriod:String="",
        var section:String="",
)



class AnswerNewModel (
      var  Subject:String,
      var QuizCode	:String,
      var OrderNum	:String,
      var AnswerID	:String,
      var Number	:String,
      var Answer	:String,
      var Points:String,
      var QuizSet:String

)

class GeneralModel (
        var  fileName:String,
        )
class KeywordModel (
        var Section:String,
        var Keyword	:String,
        var GradingPeriod	:String,
        var Category	:String,
        var DefautKeyword	:String,
        )




class StudentInfoModel (
        var studentID:String ="" ,
        var firstname:String="",
        var lastname:String="",
        var originalSection:String="",
        var gender:String="",
        var extension:String="",
        var contactNumber:String="",
        var parentcontact:String="",
        var address:String="",
        var emailAddress:String="",
        var schoolStudentNumber:String="",
        var middleName: String="",
        var ctr: Int=0

)




class ScheduleModel(
    var ampm: String,
    var myDate: String,
    var sectioncode: String,
    var renark: String,
    var term:String,
    var schedId:String,
    var day:String=""
)

class ScheduleDateModel(
        var date: String,
        var day:String=""
)


class AttendanceModel(
    var ampm: String="",
    var myDate: String="",
    var sectionCode: String="",
    var studentNo: String="",
    var completeName:String="",
    var groupNumber:String="",
    var attendanceStatus: String="",
    var remark: String="",
    var recitationPoint: Int=0,
    var TaskPoints: Int=0,
    var randomNumber: Int=0,
    var firstName: String="" ,
    var lastName: String=""

)

class AttendanceCSVModel(
        var studentNo: String,
        var completeName:String,
        var attendanceStatus: String,
        var TaskPoints: Int,
        var Recitation: Int,
        var theTime:String
)


class AttendanceScanModel(

        var num: String,
        var studNumber:String,
        var completeName: String,
        var status: String

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
    var excuseCount:Int,
    var firstName:String="",
    var lastName:String=""

)



class ActivityModel(
        /*
        CREATE TABLE tbactivity  (ActivityCode  TEXT, SectionCode  text, Description  text, Item  INTEGER,
        Status  text, Category text, GradingPeriod text, ScoreColumn TEXT, NameColumn TEXT, SheetName TEXT,
         */
        var activityCode: String,
        var sectionCode: String,
        var description: String,
        var item: Int,
        var gradingPeriod: String,
        var category: String,
        var visibleStatus: String
)


class RubricModel(
        /*
        CREATE TABLE tbactivity  (ActivityCode  TEXT, SectionCode  text, Description  text, Item  INTEGER,
        Status  text, Category text, GradingPeriod text, ScoreColumn TEXT, NameColumn TEXT, SheetName TEXT,
         */

                 //  RubricCode	RubricNUm	Description	Points
        var RubricCode: String,
        var RubricNUm: Int,
        var Description: String,
        var Points: Int,
        )


class ScoreModel(
        var activitycode: String,
        var sectioncode: String,
        var firstname: String,
        var lastnanme: String,
        var studentNo:String,
        var completeName: String,
        var score: Int,
        var remark: String,
        var status: String,
        var checkBoxStatus: String,
        var SubmissionStatus: String,
        var AdjustedScore: Int,
        var grp: String,
        var description:String,
        var  first:Int=0,
        var  second:Int=0,
        var  third:Int=0,
        var  fourth:Int=0,
        var  five:Int=0

)



class ScoreCsvModel(
        var studentNo:String,
        var completeName: String,
        var score: String,
        var adj: String,
        var remark: String,
        var status: String="NO"
)


class GrpModel(
        var grpnumber: String,
        var sectioncode: String,
        var num:Int,
        var flag:String = "NO",
        var folderLink:String =""
)

class GrpMemberModel(
        var firstname: String,
        var lastnanme: String,
        var studentNo:String,
        var grp: String,
        var score: Int,
        var remark: String,
        var AdjustedScore: Int,
        var saveVisibility:String ="NO"
)
class ScoreIndividualModel(
        var activitycode: String,
        var sectioncode: String,
        var studentNo:String,
        var description: String,
        var score: Int,
        var remark: String,
        var status: String,
        var item:Int,
        var gradingperiod: String,
        var category: String,
        var adjustedScore: Int,
        var submissionStatus: String,
        var lastname: String=""



)


class RandomModel(
            var sequenceNumber: String,
        var sectioncode: String,
        var studentNo:String,
        var completeName: String,
        var recitationCount: Int,
        var remark: String,
        var randomCode:String

)





class SectionModel(
        var sectionCode: String,
        var sectionName:String,
        var school:String,
        var status:String,
        var Message:String,
        var originalSection: String,
        var subjectDescription: String ="",
        var folderLink:String  ="",
        var schhoolYear:String ="",
        var semester:String = ""
)

class SubjectModel(
        var subjectCode: String,
        var subjectDescription: String,
        var pt: String,
        var quiz: String,
        var exam: String,
        var project: String,
        var classStanding: String,
        var participation: String,
        var classStandingCoverage:String
)



class MiscModel(
     var    MiscCode: String,
      var     MiscDescription : String,
     var    OptionNumber : String,
     var     OptionDescription : String,
     var     SectionCode: String
)

//class SectionModel(
//        var  section: String,
//)



class MiscStudentModel(
       var  studentNo: String,
       var  optionNumber: String,
       var  sectionCode: String,
       var  completeName: String,
       var  grpNumber: String,
       var  gender: String,
       var  optionDescription: String,
       var  miscDescription: String
)


class LogModel(
        var  LogID: String,
        var  DateTime: String,
        var  Description: String
)


class GradeModel(
        var sectioncode: String,
        var studentNo:String,
        var firstname: String,
        var lastname: String,
        var firstGrade: Double,
        var firstEquivalent: Double,
        var firstOriginalGrade: Double,
        var secondGrade: Double,
        var secondEquivalent: Double,
        var secondOriginalGrade: Double,
        var CumulativeGrade: Double,
        var CumulativeGradeEquivalent: Double,
        var remark: String,
        var MidtermStatus:String,
        var FinalStatus:String,
        var gender:String
)


class GradeExportedModel(
        var sectioncode: String = "",
        var studentNo:String="",
        var firstname: String="",
        var lastname: String="",
        var firstGrade: Double=0.0,
        var secondGrade: Double=0.0,
        var cumulativeGrade: Double=0.0,
)


class TaskModel(
    var  taskCode: String,
    var itemCode:String,
    var answer:String,
    var point:String
)

class TaskInfoModel(
     var    TaskCode:String,
   var      TaskTitle:String,
     var  SubjectL:String,
    var   SectionNane:String
)


class TaskScoreModel(
       var  TaskCode : String ,
       var  ItemCode : String ,
       var  StudentNumber : String ,
       var  Score : Int ,
       var  Answer : String ,
       var  Points : Int
)




class TaskRecordModel(
        var  TaskCode : String ,
        var  StudentNumber : String ,
        var  FirstName : String ,
        var  LastName : String ,
        var  Section : String ,
        var Score:Int,
        var ActivityScore:Int,
        var ActivityCode:String
)


class KeyModel(
        var keyCode: String,
        var description:String,
        var staus:String
)


class AnswerModel(
        var keyCode: String,
        var number:Int,
        var answer:String,
        var status:String
)

class ScoreGroup(
        var remark: String= "",
        var score :Int = 0,
        var adjusted:Int=0
)

class ScoreDrive(
        var remark: String= "",
        var score :Int = 0,
        var adjusted:Int=0,
        var first:Int=0,
        var second:Int=0,
        var third:Int=0,
        var fourth:Int=0,
        var fifth:Int=0,
        var firstName:String="",
        var lastName:String="",


)

class GroupRemarkModel(

        var GroupNUmber: String= "",
        var SectionCode: String= "",
        var RemarkDate: String= "",
        var RemarkTime: String= "",
        var Remark: String= "",
        var RemarkStatus: String= "",
        var RemarkID: String= ""
)


class SectionNoteModel(
        var RemarkID: String= "",
        var SectionCode: String= "",
        var RemarkDate: String= "",
        var RemarkTime: String= "",
        var Remark: String= "",
        var RemarkStatus: String= ""
)



class tbGroupRemark {
    companion object {
        var Name: String = "tbGroup_Remark"
        var GroupNUmber: String = "GroupNUmber"
        var SectionCode: String = "SectionCode"
        var RemarkDate: String = "RemarkDate"
        var RemarkTime: String = "RemarkTime"
        var Remark: String = "Remark"
    }
}


class ScoreImportModel(
        var activitycode: String="",
        var sectioncode: String="",
        var completeName: String="",
        var score: Int=0,
        var studentNumber: String="",
        var quizTitle: String="",
        var status:String  = "-"

)

























