package com.uacam.pitbullmanagerapp.teacher

import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ClassesList(
    val nameClasses: String? = null,
    val date: String? = null,
    val nameStudent: String? = null,
    val nameTeacher: String? = null,
    val description: String? = null,
    val url: String? = null,
    @Exclude val key: String? = null)
