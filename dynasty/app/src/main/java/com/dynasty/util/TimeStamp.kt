package com.dynasty.util


import com.dynasty.BaseStructureApplication
import com.dynasty.R
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeStamp {
    private val tz = TimeZone.getDefault()
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    val DateFormatVideoName = "ddMMyy-HHmm"
    val DateFormatVideoEndTime = "-HHmm"
    val DateFormat = "dd-MM-yyyy"
    val DateFormatGTS = "dd MMMM yyyy HH:mm:ss"
    val DateFormatGTSUploadEvent = "yyyy-MM-dd HH:mm:ss"
    val DateFormatHistory = "dd MMM yyyy @ hh:mm aa"
    val TimeFormatHistory = "hh:mm aa"
    val DateFormatMonth = "dd MMM yyyy"
    val DateFormatFullMonth = "dd MMMM yyyy"
    val MyPostDateFormat = "EEE. dd.MM.yyyy"
    val MonthYear = "MMM yyyy"
    val DAY = "dd"
    val MONTH = "MM"
    val MONTH_NAME_SHORT = "MMM"
    val YEAR = "yyyy"
    val TimeFormat = "hh:mm aa"
    val TimeFormat24Hours = "HH:mm:ss"
    val TimeFormatWithoutampm = "HH:mm"
    val RideDetailDateFormat = "dd MMM yyyy, HH:mm"
    val FullDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.Z"
    val RELEASE_DAY = "dd/MM/yyyy"
    val RELEASE_DATE_ONLY = "dd/MM/yyyy"
    val RELEASE_DATE_TIME = "dd/MM/yyyy HH:mma"

    /*Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        String[] strDate = new String[4];
        strDate[0] = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
        strDate[1] = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        strDate[2] = String.valueOf(cal.get(Calendar.MONTH));
        strDate[3] = String.valueOf(cal.get(Calendar.YEAR));*/ val currentDateElements: Array<String>
        get() {

            val strDate = arrayOf<String>()
            strDate[0] = getDateFromTimestamp(System.currentTimeMillis(), "EEEE")
            strDate[1] = getDateFromTimestamp(System.currentTimeMillis(), "dd | MMMM | yyyy")
            return strDate
        }

    val currentUTC: Long
        get() {
            val cal = Calendar.getInstance()
            val currenttime = System.currentTimeMillis()
            Logger.e("TimestampRaw", "==> $currenttime")
            val offset = cal.timeZone.getOffset(currenttime).toLong()
            Logger.e("Current offset", "==> $offset")
            return currenttime - offset
        }

    val currentYear: Int
        get() = Calendar.getInstance().get(Calendar.YEAR)

    val currentMonth: Int
        get() = Calendar.getInstance().get(Calendar.MONTH)

    /*"releaseDate":"25\/07\/2019 12:00am","releaseDateOnly":"25\/07\/2019","releaseTimeOnly":"12:00am","releaseDay":"Thursday"*/
    fun formatToSeconds(value: String, format: String): Long {
        try {
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val mDate = sdf.parse(value)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun formatToSecondsLocal(value: String, format: String): Long {
        try {
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            sdf.timeZone = TimeZone.getDefault()
            val mDate = sdf.parse(value)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun dateToSeconds(givenDateString: String): Long {
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val mDate = sdf.parse(givenDateString)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun dateToSecondsInUTC(givenDateString: String): Long {
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val mDate = sdf.parse(givenDateString)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun ddMMMyyyyHHmmssToSecondsInUTC(givenDateString: String): Long {
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val mDate = sdf.parse(givenDateString)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun ISOtoEST(givenDateString: String): Long {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mma", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("EST")
            val mDate = sdf.parse(givenDateString)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun ddMMMyyyyhhmma(secondsTime: String): String {
        return ddMMMyyyyhhmma(java.lang.Long.parseLong(secondsTime))
    }


    fun ddMMMyyyy(secondsTime: String): String {
        return ddMMMyyyy(java.lang.Long.parseLong(secondsTime))
    }

    fun ddMMMyyyyhhmma(secondsTime: Long): String {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        return SimpleDateFormat("dd MMM yyyy, hh:mm a ", Locale.ENGLISH).format(cal.time)
    }

    fun ddMMMMyyyyhhmma(secondsTime: Long): String {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        return SimpleDateFormat("dd | MMMM | yyyy - hh:mm a ", Locale.ENGLISH).format(cal.time)
    }

    fun ddMMMyyyy(secondsTime: Long): String {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        return SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(cal.time)
    }

    fun hhmma(secondsTime: Long): String {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        return SimpleDateFormat("hh:mm a ", Locale.ENGLISH).format(cal.time)
    }

    fun getHoursMinutesFromTimestamp(timestamp: Long): String {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeZone = tz
        cal.timeInMillis = timestamp * 1000L
        return SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(cal.time)
    }

    fun secondsToFormat(secondsTime: String, format: String): String {
        return secondsToFormat(java.lang.Long.parseLong(secondsTime), format)
    }

    fun secondsToFormat(secondsTime: Long, format: String): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(cal.time)

    }

    fun canReturn(date: Long, duration: Int): Boolean {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val after48hrs = Calendar.getInstance(Locale.ENGLISH)
        after48hrs.timeInMillis = date * 1000L
        if (duration > 0)
            after48hrs.add(Calendar.MILLISECOND, TimeUnit.HOURS.toMillis(duration.toLong()).toInt())
        else
            after48hrs.add(Calendar.DATE, +2)
        after48hrs.timeZone = tz
        return System.currentTimeMillis() < after48hrs.timeInMillis
    }

    fun timeToSeconds(givenDateString: String): Long {
        try {
            val getTime =
                givenDateString.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val hours: Long
            val mins: Long
            val newTime =
                getTime[0].split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (getTime[1].equals("am", ignoreCase = true)) {
                hours = java.lang.Long.parseLong(newTime[0]) * 60 * 60
                mins = java.lang.Long.parseLong(newTime[1]) * 60
            } else {
                hours = (java.lang.Long.parseLong(newTime[0]) + 12) * 60 * 60
                mins = java.lang.Long.parseLong(newTime[1]) * 60
            }
            return hours + mins
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    fun getYear(timestamp: Long): String {
        var date = ""
        val formatter = SimpleDateFormat(YEAR, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT+05:30")
        date = formatter.format(Date(timestamp * 1000))
        return date
    }

    fun getMonthNameShort(timestamp: Long): String {
        var date = ""
        val formatter = SimpleDateFormat(MONTH_NAME_SHORT, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT+05:30")
        date = formatter.format(Date(timestamp * 1000))
        return date
    }

    fun MonthYear(timestamp: Long): String {
        var date = ""
        val formatter = SimpleDateFormat(MonthYear, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT+05:30")
        date = formatter.format(Date(timestamp * 1000))
        return date
    }

    fun getFullDateAndTime(timestamp: Long): String {
        var date = ""
        val formatter = SimpleDateFormat(FullDateTimeFormat, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        date = formatter.format(Date(timestamp * 1000))
        return date
    }

    fun getDifferencesYear(timestampstart: Long, timestampend: Long): Int {

        val starttime = timestampstart / 60
        val endtime = timestampend / 60
        val difference = endtime - starttime
        return if (difference > 0) {
            if (difference > 525600) {
                difference.toInt() / 525600
            } else {
                0
            }
        } else {
            0
        }
    }

    fun getUTCDate(timestamp: Long, DateFormat: String): String {
        var date = ""
        val formatter = SimpleDateFormat(DateFormat, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        date = formatter.format(Date(timestamp))
        return date
    }

    fun getRemainingMonth(timestampstart: Long, timestampend: Long): Int {

        val starttime = timestampstart / 60
        val endtime = timestampend / 60
        var difference = endtime - starttime
        if (difference > 0) {
            difference = difference % 525600

            return difference.toInt() / 43800
        } else {
            return 0
        }
    }


    fun getGTSDateFromTimestamp(timestamp: Long): String {
        return getDateFromTimestamp(timestamp, DateFormatGTS)
    }

    fun getDateFromTimestamp(timestamp: Long, DateFormate: String): String {
        var timestamp = timestamp

        if (timestamp < 1000000000000L) {
            timestamp *= 1000
        }
        val sdf = SimpleDateFormat(DateFormate, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("GMT+05:30")
        //        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
        if (timestamp > 0) {
            val milidate = Date(timestamp)
            return sdf.format(milidate)
        } else {
            return ""
        }
    }

    fun getUTCDateFromTimestamp(timestamp: Long, DateFormat: String): String {
        var timestamp = timestamp
        if (timestamp < 1000000000000L) {
            timestamp *= 1000
        }

        var date = ""
        val formatter = SimpleDateFormat(DateFormat, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT+05:30")
        date = formatter.format(Date(timestamp))
        return date
    }

    fun findAge(secondsTime: Long): Int {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val cal = Calendar.getInstance()
        cal.timeInMillis = secondsTime * 1000L
        cal.timeZone = tz
        val dateFormat = SimpleDateFormat("dd MM yyyy")
        val date = dateFormat.format(cal.time).toString()
        val age = date.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        return calculateMyAge(
            Integer.parseInt(age[2]),
            Integer.parseInt(age[1]),
            Integer.parseInt(age[0])
        )
    }

    private fun calculateMyAge(year: Int, month: Int, day: Int): Int {
        val birthCal = GregorianCalendar(year, month, day)

        val nowCal = GregorianCalendar()

        var age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)

        val isMonthGreater = birthCal.get(Calendar.MONTH) >= nowCal.get(Calendar.MONTH)

        val isMonthSameButDayGreater =
            birthCal.get(Calendar.MONTH) == nowCal.get(Calendar.MONTH) && birthCal.get(Calendar.DAY_OF_MONTH) > nowCal.get(
                Calendar.DAY_OF_MONTH
            )

        if (isMonthGreater || isMonthSameButDayGreater) {
            age = age - 1
        }
        return age
    }

    fun getTimeFromHours(time: Long): String {
        val min = (time / 60).toInt()
        val hours = min / 60
        val remaining_min = min % 60

        return if (hours > 12) {
            String.format(Locale.ENGLISH, "%02d:%02d %s", hours - 12, remaining_min, "PM")
        } else if (hours < 12) {
            String.format(Locale.ENGLISH, "%02d:%02d %s", hours, remaining_min, "AM")
        } else {
            String.format(Locale.ENGLISH, "%02d:%02d %s", hours, remaining_min, "PM")
        }
    }

    fun dd_MM_yyyy(secondsTime: Long): String {
        val tz = TimeZone.getTimeZone(TimeZone.getDefault().id)
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        return SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cal.time)
    }

    fun getPrettyTime(seconds: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeZone = tz
        calendar.timeInMillis = seconds * 1000L
        val now = Calendar.getInstance(Locale.ENGLISH)
        now.timeZone = tz
        return if (now.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            BaseStructureApplication.instance.getString(R.string.today)
        } else if (now.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1) {
            BaseStructureApplication.instance.getString(R.string.yesterday)
        } else {
            SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(calendar.time)
        }
    }

    fun ddMMyyyy(secondsTime: Long): String {
        val cal = Calendar.getInstance()
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        return SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(cal.time)
    }

    fun customDateFormat(secondsTime: Long, dateFormat: String): String {
        val cal = Calendar.getInstance()
        cal.timeZone = tz
        cal.timeInMillis = secondsTime * 1000L
        return SimpleDateFormat(dateFormat, Locale.ENGLISH).format(cal.time)
    }

    fun getHours(string: String): String {
        val time = string.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        return time[0].split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
    }

    fun getMins(string: String): String {
        val time = string.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        return time[0].split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
    }

    fun getDay(timestamp: Long): Int {

        val cal = Calendar.getInstance()
        cal.timeZone = tz
        cal.timeInMillis = timestamp * 1000L
        val date = SimpleDateFormat("dd", Locale.ENGLISH).format(cal.time)
        return if (date != null && date.length > 0) {
            Integer.parseInt(date)
        } else {
            -1
        }
    }

    internal fun getAdditionalMinutes(value: String, currentTime: Long): Int {
        try {
            val sdf = SimpleDateFormat(TimeStamp.TimeFormat24Hours, Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val mDate = sdf.parse(value)

            val currentHourMinutes = getHoursMinutesFromTimestamp(currentTime / 1000)
            Logger.e("currentHoursMinutes: ", currentHourMinutes)

            val mDate1 = sdf.parse(currentHourMinutes)
            val currentMinutes = (TimeUnit.MILLISECONDS.toSeconds(mDate1.time) / 60).toInt()
            Logger.e("currentMinutes: ", currentMinutes.toString())

            val remainingMinutes = (TimeUnit.MILLISECONDS.toSeconds(mDate.time) / 60).toInt()
            Logger.e("remainingMinutes: ", remainingMinutes.toString())

            return if (currentMinutes < remainingMinutes) {
                1440 - remainingMinutes
            } else {
                -remainingMinutes
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    internal fun getLastUpdateTimestamp(value: String, currentTime: Long): Long {
        try {
            val sdf = SimpleDateFormat(TimeStamp.TimeFormat24Hours, Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val mDate = sdf.parse(value)

            val currentHourMinutes = getHoursMinutesFromTimestamp(currentTime / 1000)

            val mDate1 = sdf.parse(currentHourMinutes)
            val currentMinutes = (TimeUnit.MILLISECONDS.toSeconds(mDate1.time) / 60).toInt()

            val remainingMinutes = (TimeUnit.MILLISECONDS.toSeconds(mDate.time) / 60).toInt()

            val lastUpdateTimestamp: Long

            if (currentMinutes < remainingMinutes) {
                lastUpdateTimestamp = formatToSecondsLocal(
                    customDateFormat(
                        (currentTime - 86400000) / 1000,
                        DateFormatFullMonth
                    ) + " " + value, DateFormatGTS
                )
            } else {
                lastUpdateTimestamp = formatToSecondsLocal(
                    customDateFormat(
                        currentTime / 1000,
                        DateFormatFullMonth
                    ) + " " + value, DateFormatGTS
                )
            }

            Logger.e("lastUpdateTimestamp: ", lastUpdateTimestamp.toString())
            return lastUpdateTimestamp
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun getVideoStartEndTime(startTimeStamp: Long, endTimeStamp: Long): String {
        var startDate = ""
        var endDate = ""

        Logger.e("startTimeStamp", "" + startTimeStamp)
        val formatter = SimpleDateFormat(DateFormatVideoName, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("GMT+05:30")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTimeStamp
        startDate = formatter.format(calendar.time)
        Logger.e("startDate", "" + startDate)

        val format1 = SimpleDateFormat(DateFormatVideoEndTime, Locale.ENGLISH)
        format1.timeZone = TimeZone.getTimeZone("GMT+05:30")
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = endTimeStamp
        endDate = format1.format(calendar1.time)

        return startDate + endDate
    }

    fun getMinutesFromMilliseconds(millis: Long): String {
        var minutes: Long = 0
        var seconds: Long = 0
        var minutesSeconds = "00:00"
        try {
            minutes = millis / 1000 / 60
            seconds = (millis / 1000 % 60).toInt().toLong()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //        minutesSeconds = String.format(Locale.ENGLISH,"%02d", minutes) + ":" + String.format(Locale.ENGLISH,"%02d", seconds);
        val formatter = DecimalFormat("00")
        minutesSeconds = formatter.format(
            Integer.parseInt(minutes.toString()).toLong()
        ) + ":" + formatter.format(Integer.parseInt(seconds.toString()).toLong())
        return minutesSeconds
    }

    fun getDateByMonthAndYear(date: String): String {

        val dateFormat = SimpleDateFormat(RELEASE_DATE_ONLY, Locale.ENGLISH)
        var sourceDate: Date? = null

        try {
            sourceDate = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val targetFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
        return targetFormat.format(sourceDate)
    }

    fun getDateByMonthDay(date: String?): String {
        if (date != null && !date.isEmpty()) {
            val dateFormat = SimpleDateFormat(RELEASE_DATE_ONLY, Locale.ENGLISH)
            var sourceDate: Date? = null

            try {
                sourceDate = dateFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val targetFormat = SimpleDateFormat("MMMM d", Locale.ENGLISH)
            return targetFormat.format(sourceDate)
        } else {
            return ""
        }
    }

    fun getDateByMonthDayAndYear(date: String?): String {
        if (date != null && !date.isEmpty()) {
            val dateFormat = SimpleDateFormat(RELEASE_DATE_ONLY, Locale.ENGLISH)
            var sourceDate: Date? = null

            try {
                sourceDate = dateFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val targetFormat = SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH)
            return targetFormat.format(sourceDate)
        } else {
            return ""
        }
    }

    fun getFormattedDate(date: String?): String {
        if (date != null && !date.isEmpty()) {
            val dateFormat = SimpleDateFormat(RELEASE_DATE_ONLY, Locale.ENGLISH)
            var sourceDate: Date? = null

            try {
                sourceDate = dateFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            return targetFormat.format(sourceDate)
        } else {
            return ""
        }
    }

    fun getFormattedTime(date: String?): String {
        if (date != null && !date.isEmpty()) {
            val dateFormat = SimpleDateFormat(RELEASE_DATE_TIME, Locale.ENGLISH)
            var sourceDate: Date? = null

            try {
                sourceDate = dateFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val targetFormat = SimpleDateFormat("hh:mma", Locale.ENGLISH)
            val targetdatevalue = targetFormat.format(sourceDate)
            return targetdatevalue.replace("AM", "am").replace("PM", "pm")
        } else {
            return ""
        }
    }
}