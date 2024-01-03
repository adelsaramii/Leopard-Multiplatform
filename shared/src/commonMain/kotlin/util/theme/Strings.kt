package com.attendace.leopard.util.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.attendace.leopard.util.localization.LanguageTypeEnum
import com.attendace.leopard.util.localization.TranslateMap

var language = mutableStateOf(LanguageTypeEnum.English)

fun localization(name: TranslateMap): State<String> {
    return when (language.value) {
        LanguageTypeEnum.English -> {
            mutableStateOf(name.english)
        }

        LanguageTypeEnum.Arabic -> {
            mutableStateOf(name.arabic)
        }

        LanguageTypeEnum.Kurdish -> {
            mutableStateOf(name.kurdish)
        }
    }
}

sealed class MyLanguage(val name: String, val language: LanguageTypeEnum, val image: String) {
    object English :
        MyLanguage("English", LanguageTypeEnum.English, "us_flag.xml")

    object Kurdish :
        MyLanguage("کوردی", LanguageTypeEnum.Kurdish, "flag_kurdish.xml")

    object Arabic : MyLanguage("عربی", LanguageTypeEnum.Arabic, "iq.xml")
}

val item_has_error = TranslateMap(
    english = "Item has error",
    arabic = "العنصر به خطأ",
    kurdish = "ئایتمەکە هەڵەی تیدایە"
)

val login_error = TranslateMap(
    english = "Username or Password is invalid",
    arabic = "اسم المستخدم او كلمة المرور غير صحيحة",
    kurdish = "ناوی بەکارهێنەر یان وشەی نهێنی هەڵەیە"
)

val pleaseTryAgainLater = TranslateMap(
    english = "Please try again later",
    arabic = "الرجاء معاودة المحاولة في وقت لاحق",
    kurdish = "تکایە دووبارە هەوڵبدەرەوە"
)

val refresh = TranslateMap(
    english = "Refresh",
    arabic = "ينعش",
    kurdish = "نوێکردنەوە"
)

val select_language = TranslateMap(
    english = "Select Language",
    arabic = "اختار اللغة",
    kurdish = "زمان دیاریبکە"
)

val error_message = TranslateMap(
    english = "An error occurred",
    arabic = "حصل خطأ ما.",
    kurdish = "گرفتێک ڕوویداوە"
)

val desc_submit_req_intro = TranslateMap(
    english = "Ease of registration of leave, mission, attendance and… applications by automatically identifying the periods required by the application",
    arabic = "سهولة تسجيل الإجازات والمهمة والحضور و ... الطلبات من خلال تحديد الفترات التي يتطلبها التطبيق تلقائيًا",
    kurdish = "ئاسانکاری بۆ تۆمارکردنی مۆڵەت، هاتن و چوون بە پێی لۆکەیشن  و...  به کارهێنانی خۆکارانه بۆ دیاریکردنی ماوه ی پێویست له لایه ن به رنامه که وه"
)

val sub_title_submit_req_intro = TranslateMap(
    english = "Submit a variety of requests",
    arabic = "إرسال مجموعة متنوعة من الطلبات",
    kurdish = "پێشکەشکردنی داواکاری جۆراوجۆر"
)

val title_submit_req_intro = TranslateMap(
    english = "Smart …",
    arabic = "ذكي …",
    kurdish = "زیرەک …"
)

val desc_portfolio_intro = TranslateMap(
    english = "Awareness of entry and exit, performance and work calendar of yourself and subordinates at a glance",
    arabic = "مراقبة االدخول والخروج و الأداء و تقويم العمل الخاص بالموظفین ",
    kurdish = "چاودێری هاتن و چوون ، کردار، ڕۆژمێری کارکردنی کارمەندان"
)

val sub_title_portfolio_intro = TranslateMap(
    english = "Manage portfolio documents",
    arabic = "إدارة الطلبات بشكل الكتروني ",
    kurdish = "بەڕێوەبردنی داواکارییەکان بە شێوەی ئەلکترۆنی"
)

val title_portfolio_intro = TranslateMap(
    english = "Fast and agile …",
    arabic = "سريع و دقيق …",
    kurdish = "خێرا و چالاک..."
)

val desc_att_intro = TranslateMap(
    english = "Awareness of entry and exit, performance and work calendar of yourself and subordinates at a glance",
    arabic = " انتباه بالدخول والخروج والأداء و تقويم العمل الخاص بالموظفین ",
    kurdish = "بە ئاگابوون لە داواکارییەکان ، مۆڵەت ، دەرچوون بۆ کار ، ئوڤەرتایم لە هەر شوێن و کاتیکدا "
)

val sub_title_att_intro = TranslateMap(
    english = "View attendances and performance",
    arabic = "عرض الحضور والأداء",
    kurdish = "بینینی ئامادەبوان و کردار"
)

val title_att_intro = TranslateMap(
    english = "At a glance …",
    arabic = " خلل فی تسجیل 3 الاجازات",
    kurdish = "هەڵەیەک له تۆمارکردنی 3 مۆڵەت هەبووه"
)

val or = TranslateMap(
    english = "OR",
    arabic = "أو",
    kurdish = "یان"
)

var next = TranslateMap(
    english = "Next",
    arabic = "التالي",
    kurdish = "دواتر"
)

val scan_qr_code = TranslateMap(
    english = "Scan Qr code",
    arabic = " Qr مسح كود  ",
    kurdish = " کۆدی کیوئارکۆد سکان بکە"
)

val enter_server_address = TranslateMap(
    english = "Enter server address",
    arabic = "أدخل عنوان سیرفر",
    kurdish = "ناونیشانی سێرڤەر بنووسە"
)

val required_message = TranslateMap(
    english = "This field is required",
    arabic = "هذه حقل مطلوبه",
    kurdish = "ئەم فیلدە پێویستە"
)

val server_address = TranslateMap(
    english = "Server Address",
    arabic = "عنوان سیرفر",
    kurdish = "ناونیشانی سێرڤەر"
)

val login = TranslateMap(
    english = "Login",
    arabic = "تسجیل دخول",
    kurdish = "چوونەژوورەوە"
)

val password = TranslateMap(
    english = "Password",
    arabic = "کلمة المرور",
    kurdish = "ووشەی نهێنی"
)

val username = TranslateMap(
    english = "Username",
    arabic = "اسم المستخدم",
    kurdish = "ناوی بەکارهێنەر"
)

val accessDenied = TranslateMap(
    english = "Access Denied",
    arabic = "تم الرفض",
    kurdish = "دەستڕاگەیشتن ڕەتکراوەتەوە"
)

val accessDeniedMessage = TranslateMap(
    english = "Your access to the system is limited by your administrator",
    arabic = "وصولك إلى النظام مقيد من قبل المسؤول",
    kurdish = "دەستڕاگەیشتن بە سیستەمەکە لەلایەن بەڕێوەبەرەکەتەوە سنووردارە"
)


val anUnknownErrorOccurred = TranslateMap(
    english = "An Unknown error occurred!",
    arabic = "حدث خطأ غير معروف!",
    kurdish = "هەڵەیەکی نەزانراو رویداوە!"
)

val missedAttendance = TranslateMap(

    english = "Missed Attendance",
    arabic = "المجيء والمغادرة غير مكتملة",
    kurdish = "هاتن و چوونی ناتەواو"

)

val done_successfully = TranslateMap(
    english = "Done Successfully",
    arabic = "فعلت بنجاح",
    kurdish = "بە سەرکەوتوویی ئەنجامدرا"
)

val require_requests_count = TranslateMap(

    english = "requires request",

    arabic = "يتطلب الطلب",

    kurdish = "پێویستی بە داواکاریە"

)

val hour = TranslateMap(

    english = "hour",

    arabic = "ساعة",

    kurdish = "کاتژمێر"

)

val minute = TranslateMap(

    english = "minutes",

    arabic = " دقیقة ",

    kurdish = "خولەک"

)

val lastAtt = TranslateMap(

    english = "From",
    arabic = "من",
    kurdish = "لە",

    )

val today = TranslateMap(

    english = "Today",
    arabic = "اليوم",
    kurdish = "ئەمڕۆ",


    )

val noRecord = TranslateMap(
    english = "No Record",
    arabic = "لا يوجد سجل",
    kurdish = "هیچ تۆمارێک نییە",
)

val yesterday = TranslateMap(
    english = "Yesterday",
    arabic = "أمس",
    kurdish = "دوێنێ",
)

val absents = TranslateMap(

    english = "Absents",
    arabic = "الغائب",
    kurdish = "ئەوانەی کە غیابن",


    )

val overtime = TranslateMap(

    english = "Overtime",
    arabic = "العمل الإضافي",
    kurdish = "کاری کاتی زیادە",


    )

val rest = TranslateMap(

    english = "Rest",
    arabic = "وقت للراحة",
    kurdish = "کاتی پشوودان",


    )

val presents = TranslateMap(
    english = "Presents",
    arabic = "الحاضرين",
    kurdish = "ئامادەبووان",
)

val search = TranslateMap(


    english = "Search",

    arabic = "بحث",

    kurdish = "گەڕان"


)


val setting = TranslateMap(


    english = "Setting",

    arabic = "إعدادات",

    kurdish = "ڕێکخستن"


)

val logout = TranslateMap(


    english = "Logout",

    arabic = "تسجيل خروج",

    kurdish = "چوونەدەرەوە"


)

val monthly = TranslateMap(

    english = "Monthly",

    arabic = "شهري",

    kurdish = "مانگانه"


)

val daily = TranslateMap(


    english = "Daily",

    arabic = "يومي",

    kurdish = "ڕۆژانە"


)

val attendance = TranslateMap(


    english = "Attendance",

    arabic = "الحضور",

    kurdish = "هاتن و چوون"


)

val daily_summary = TranslateMap(


    english = "Daily Summary",

    arabic = "ملخص يومي",

    kurdish = "پوختەی ڕۆژانە"


)

val no_data = TranslateMap(

    english = "There is no data!",

    arabic = "!لا یوجد معلومات ",

    kurdish = "هیچ داتایەک بوونی نییە!"

)

val request_required = TranslateMap(

    english = "Request required",

    arabic = "طلب مطلوب",

    kurdish = "پێویستی بە داواکارییە"

)

val total_information = TranslateMap(

    english = "Total information",

    arabic = "جمیع المعلومات",

    kurdish = "سەرجەم زانیاریەکان"

)

val enter = TranslateMap(

    english = "Enter",

    arabic = "أدخل",

    kurdish = "هاتن"

)

val exit = TranslateMap(


    english = "Exit",

    arabic = "خروج",

    kurdish = "دەرچوون"


)


val days = TranslateMap(

    english = "Days",

    arabic = "یوم",

    kurdish = "ڕۆژ"

)


val confirm = TranslateMap(

    english = "Confirm",

    arabic = " قبول",

    kurdish = "پەسەندکردن"

)


val reject = TranslateMap(

    english = "Reject",

    arabic = "رفض",

    kurdish = "پەسەند نەکردن"

)


val select_all = TranslateMap(

    english = "Select All",

    arabic = "اختیار الكل",

    kurdish = "هەموویان هەڵبژێره"

)


val selected = TranslateMap(


    english = "Selected",

    arabic = "المحدد",

    kurdish = "هەڵبژێردراوە"


)


val portfolio = TranslateMap(


    english = "Portfolio",

    arabic = "داشبورد",

    kurdish = "داشبۆرد"


)

val register_attendance = TranslateMap(

    english = "Register Attendance",

    arabic = "تسجيل الحضور",

    kurdish = "تۆمارکردنی ئامادەبوون"

)

val register_request = TranslateMap(

    english = "Register Request",

    arabic = "طلب تسجيل",

    kurdish = "تۆمارکردنی داواکاری"


)


val request = TranslateMap(

    english = "Request",

    arabic = "طلب",

    kurdish = "داواکاری"

)

val indexCard = TranslateMap(

    english = "Index Card",

    arabic = "بطاقة الرصید",

    kurdish = " ئێندێکس کارت"

)


val by = TranslateMap(

    english = "by",

    arabic = "بواسطة",

    kurdish = "لەلایەن"

)


val fromDate = TranslateMap(

    english = "From Date",

    arabic = "من التاريخ",

    kurdish = "لە بەرواری"

)


val toDate = TranslateMap(

    english = "To Date",

    arabic = "الی التاریخ ",

    kurdish = "تا بەرواری"

)

val applicant = TranslateMap(

    english = "Applicant",

    arabic = " طالب ",

    kurdish = "داواکار"

)


val applyFilter = TranslateMap(

    english = "Apply Filter",

    arabic = "تطبيق فلتر",

    kurdish = "جێبەجێکردنی فلتەر"

)


val resetAllFilters = TranslateMap(

    english = "Reset All Filters",

    arabic = "إعادة تعيين جميع الفلاتر ",

    kurdish = "هەموو فلتەرەکان پاک بکەرەوە"

)


val filterPage = TranslateMap(

    english = "Filter Page",

    arabic = "صفحة فلتر",

    kurdish = "لاپەڕەی فلتەر"

)

val retry = TranslateMap(

    english = "Retry",

    arabic = "أعد المحاولة",

    kurdish = "دووبارە هەوڵ بدەرەوە"

)


val added = TranslateMap(

    english = "Added",

    arabic = "مضاف",

    kurdish = "زیادکرا",

    )


val reduced = TranslateMap(


    english = "Reduced",

    arabic = "مخفض",

    kurdish = "که م کراوەتەوە",

    )


val remain = TranslateMap(

    english = "Remain",

    arabic = "باقي",

    kurdish = "بڕی ماوە",

    )


val appVersion = TranslateMap(

    english = "App Version",

    arabic = "نسخة التطبيق",

    kurdish = "وەشانی ئەپ"

)


val logoutSubtitle = TranslateMap(

    english = "Are you sure you want to exit the Leopard?",

    arabic = "هل أنت متأكد أنك تريد الخروج من التطبيق؟",

    kurdish = "ئایا دڵنیای کە دەتەوێت لە ئەپلیکیشنەکە دەربچیت؟"


)


val accept = TranslateMap(


    english = "Accept",

    arabic = "قبول",

    kurdish = "پەسەندکردن"

)


val cancel = TranslateMap(


    english = "Cancel",

    arabic = "إلغاء",

    kurdish = "پاشگەزبوونەوە"


)


val noDataAvailable = TranslateMap(

    english = "No data available",

    arabic = "لا یتوفر بيانات",

    kurdish = "هیچ زانیاریەک بەردەست نییە"


)


val all = TranslateMap(

    english = "All",

    arabic = "کل",

    kurdish = "هەموو"


)


val name = TranslateMap(

    english = "name",

    arabic = "اسم",

    kurdish = "ناو"

)

val inProcess = TranslateMap(

    english = "inProcess",

    arabic = "في تقدم",

    kurdish = "لە پرۆسەدایە"

)

val firstOfPeriod = TranslateMap(

    english = "First Of Period",

    arabic = "أول دورة",

    kurdish = "یەکەم خول"

)

val approvedNotApplied = TranslateMap(

    english = "approvedNotApplied",

    arabic = "لم یتم الموافقة",

    kurdish = "پەسەند نەکراوە"

)


val submit = TranslateMap(

    english = "Submit",

    arabic = "تقدیم",

    kurdish = "ناردن"

)

val delete = TranslateMap(

    english = "Delete",

    arabic = "حذف",

    kurdish = "سڕینەوە"


)


val not_selected = TranslateMap(

    english = "Not selected",

    arabic = "لم يتم اختيار",

    kurdish = "هەڵنەبژێردراوە"

)

val request_type = TranslateMap(

    english = "Request Type",

    arabic = "نوع الطلب",

    kurdish = "جۆری داواکاری"

)

val ss = TranslateMap(

    english = "There was an error in registering three licenses",

    arabic = " خلل في تسجيل  ٣ اجازات",

    kurdish = "هەڵەیەک له تۆمارکردنی 3 مۆڵەت هەبووه"

)

val languageName = TranslateMap(

    english = "Language",

    arabic = "اللغة",

    kurdish = "زمان"

)

val my_request = TranslateMap(

    english = "My Request",

    arabic = "ملفي طلب",

    kurdish = "داواکاری من"

)

val personnel_status_report = TranslateMap(

    english = "Personnel Status Report",
    arabic = "تقرير حالة الموظفين",
    kurdish = "ڕاپۆرتی باری کەسێتی",


    )

val myProfile = TranslateMap(

    english = "My Profile",

    arabic = "ملفي شخصي",

    kurdish = "پڕۆفایلی من"

)
val turn_on_gps = TranslateMap(

    english = "Turn on GPS please",

    arabic = "قم بتشغيل GPS من فضلك",

    kurdish = "چالاک بکە GPS"

)
val turn_on = TranslateMap(

    english = "Turn on",

    arabic = "قم بتشغيل ",

    kurdish = "چالاک کردن"


)
val check_in = TranslateMap(


    english = "Check In",

    arabic = "دخول",

    kurdish = "هاتن"


)
val check_out = TranslateMap(


    english = "Check Out",

    arabic = "خروج",

    kurdish = "دەرچوون"


)
val today_attendance = TranslateMap(


    english = "Today's Attendance",

    arabic = "حضور اليوم",

    kurdish = "هاتن و چوونی ئەمڕۆ"


)


val salary = TranslateMap(


    english = "Payslip",

    arabic = "قسيمة الراتب",

    kurdish = "لێشاوی پارە"


)


val incremental = TranslateMap(


    english = "Incremental",

    arabic = "تدريجي",

    kurdish = "پێدراو"

)

val decremental = TranslateMap(


    english = "Decremental",

    arabic = "تنازلي",

    kurdish = "لێشکێندراو"

)


val workingInfo = TranslateMap(


    english = "Working Info",

    arabic = "معلومات العمل",

    kurdish = "زانیاری کار",


    )

val number = TranslateMap(

    english = "Number",

    arabic = "رقم",

    kurdish = "ژمارە",

    )

val finalPayroll = TranslateMap(

    english = "Final Payroll",
    arabic = "كشوف المرتبات النهائية",
    kurdish = "کۆتا لیستی مووچە",

    )


val changePasswordText = TranslateMap(

    english = "Change Password",
    arabic = "تغيير كلمة المرور",
    kurdish = "گۆڕینی ووشەی نهێنی"

)

val newPasswordText = TranslateMap(

    english = "New Password",
    arabic = "كلمة المرور الجديدة",
    kurdish = "ووشەی نهێنی نوێ"

)

val currentPassword = TranslateMap(

    english = "Current password",
    arabic = "كلمة السر الحالية",
    kurdish = "ووشەی نهێنی ئێستا"

)

val oldPasswordIncorrect = TranslateMap(

    english = "Your password was incorrect",
    arabic = "كلمة مرورك لم تكن صحيحة",
    kurdish = "وشهی نهێنی یهكهت ههڵه بوو"

)