package com.toseertc.log.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @Throws(Exception::class)
    fun getPreDate(dateGiven: String?): String {
        val formatter = SimpleDateFormat("yyyyMMdd")
        var date: Date = formatter.parse(dateGiven)
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -1) //把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.time //这个时间就是日期往后推一天的结果 
        return formatter.format(date)
    }
}