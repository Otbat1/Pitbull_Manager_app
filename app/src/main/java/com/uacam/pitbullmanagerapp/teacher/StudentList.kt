package com.uacam.pitbullmanagerapp.teacher

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class StudentList(
    val nameStudent: String? = null,
    val nameTeacher: String? = null,
    val belt: String? = null,
    val description: String? = null,
    val url: String? = null,
    @Exclude val key: String? = null)