package com.attendace.leopard.data.model

enum class RolePermissionEnum {
    Monthly,
    Requests,
    Portfolio,
    MyProfile,
    IndexCard,
    PaysLip,
    PersonnelStatusReport,
    RegisterAttendance
}

data class RolePermissionModel(
    var monthly: Boolean,
    var requests: Boolean,
    var portfolio: Boolean,
    var myProfile: Boolean,
    var indexCard: Boolean,
    var PaysLip: Boolean,
    var personnelStatusReport: Boolean,
    var registerAttendance: Boolean
)