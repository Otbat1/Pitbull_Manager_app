package com.uacam.pitbullmanagerapp.teacher

import com.google.firebase.database.Exclude

data class Pagos(
    val namePagos:   String? = null,
    val precio:      String?=null,
    val date:        String? = null,
    val nameStudent: String? = null,
    val nameTeacher: String? = null,
    @Exclude val key: String? = null)