package com.attendance.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.model.RolePermissionEnum
import com.attendace.leopard.data.model.RolePermissionModel
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RolePermissionDto(
    @SerialName("PermissionItems")
    var permissionItems: ArrayList<String>
) {
    fun rolePermissionDtoToRolePermissionModel(): RolePermissionModel {
        return RolePermissionModel(
            this.permissionItems.contains(RolePermissionEnum.Monthly.name),
            this.permissionItems.contains(RolePermissionEnum.Requests.name),
            this.permissionItems.contains(RolePermissionEnum.Portfolio.name),
            this.permissionItems.contains(RolePermissionEnum.MyProfile.name),
            this.permissionItems.contains(RolePermissionEnum.IndexCard.name),
            this.permissionItems.contains(RolePermissionEnum.PaysLip.name),
            this.permissionItems.contains(RolePermissionEnum.PersonnelStatusReport.name),
            this.permissionItems.contains(RolePermissionEnum.RegisterAttendance.name)
        )
    }
}