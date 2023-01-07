package com.example.navermovieapp.util

import java.text.SimpleDateFormat
import java.util.*

object DataParseUtil {
    fun removeTags(title: String?): String?{
        var new_title = title?.replace("<b>","")
        new_title = new_title?.replace("</b>","")
        new_title = new_title?.replace("&amp;","&")
        return new_title
    }
}