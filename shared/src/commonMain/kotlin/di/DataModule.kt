package com.attendace.leopard.di

import com.attendace.leopard.LeopardDb
import com.attendace.leopard.data.local.DatabaseDriverFactory
import com.attendace.leopard.data.local.database.JsonColumnAdapter
import com.attendace.leopard.data.local.database.log.LogDao
import com.attendace.leopard.data.local.database.log.LogDaoImpl
import com.attendace.leopard.data.local.database.workplace.WorkplaceDao
import com.attendace.leopard.data.local.database.workplace.WorkplaceDaoImpl
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.local.setting.AuthSettingsImpl
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.service.auth.AuthService
import com.attendace.leopard.data.source.remote.service.auth.AuthServiceImpl
import com.attendace.leopard.data.source.remote.service.workplace.WorkplaceService
import com.attendace.leopard.data.source.remote.service.workplace.WorkplaceServiceImpl
import com.attendace.leopard.data.source.remote.service.attendance.AttendanceService
import com.attendace.leopard.data.source.remote.service.attendance.AttendanceServiceImpl
import com.attendace.leopard.data.source.remote.service.bulk.BulkService
import com.attendace.leopard.data.source.remote.service.daily.DailyService
import com.attendace.leopard.data.source.remote.service.daily.DailyServiceImpl
import com.attendace.leopard.data.source.remote.service.index_card.IndexCardService
import com.attendace.leopard.data.source.remote.service.index_card.IndexCardServiceImpl
import com.attendace.leopard.data.source.remote.service.monthly.MonthlyService
import com.attendace.leopard.data.source.remote.service.monthly.MonthlyServiceImpl
import com.attendace.leopard.data.source.remote.service.my_request.MyRequestService
import com.attendace.leopard.data.source.remote.service.my_request.MyRequestServiceImpl
import com.attendace.leopard.data.source.remote.service.personnel_report_status.PersonnelStatusReportService
import com.attendace.leopard.data.source.remote.service.portfolio.PortfolioService
import com.attendace.leopard.data.source.remote.service.portfolio.PortfolioServiceImpl
import com.attendace.leopard.data.source.remote.service.request.RequestService
import com.attendace.leopard.data.source.remote.service.request.RequestServiceImpl
import com.attendace.leopard.data.source.remote.service.salary.SalaryService
import com.attendace.leopard.data.source.remote.service.salary.SalaryServiceImpl
import com.attendance.leopard.AddLogInputEntity
import com.attendance.leopard.LogEntity
import com.attendance.leopard.RecorderEntity
import com.attendance.leopard.data.source.remote.service.bulk.BulkServiceImpl
import com.attendance.leopard.data.source.remote.service.personnel_report_status.PersonnelStatusReportServiceImpl
import com.russhwolf.settings.ExperimentalSettingsApi
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun dataPlatformModule(context: Any?): Module

val dataModule = module {

    // region ktor

    single {
        ApiClient(
            get(),get()
        )
    }

    // endregion

    // region sql

    single<SqlDriver> {
        val databaseDriverFactory = get<DatabaseDriverFactory>()
        databaseDriverFactory.createDriver()
    }

    single {
        LogEntity.Adapter(
            typeAdapter = JsonColumnAdapter(),
            workplaceStatusAdapter = JsonColumnAdapter(),
            workflowStatusAdapter = JsonColumnAdapter(),
            recorderAdapter = JsonColumnAdapter()
        )
    }

    single {
        AddLogInputEntity.Adapter(
            typeAdapter = JsonColumnAdapter(),
            recorderAdapter = JsonColumnAdapter()
        )
    }

    single {
        RecorderEntity.Adapter(
            recorderTypeAdapter = JsonColumnAdapter()
        )
    }

    single {
        LeopardDb.invoke(get(), get(), get(), get())
    }

    // endregion

    // region queries

    single {
        get<LeopardDb>().logQueries
    }

    single {
        get<LeopardDb>().workplaceQueries
    }

    // endregion

    // region dao

    single {
        LogDaoImpl(get())
    } bind LogDao::class

    single {
        WorkplaceDaoImpl(get())
    } bind WorkplaceDao::class

    // endregion

    // region service

    single<AuthService> {
        AuthServiceImpl(
            get(), get()
        )
    }
    single<AttendanceService> {
        AttendanceServiceImpl(
            get(), get()
        )
    }

    single<MonthlyService> {
        MonthlyServiceImpl(
            get(), get()
        )
    }

    single<DailyService> {
        DailyServiceImpl(
            get(), get()
        )
    }

    single<PortfolioService> {
        PortfolioServiceImpl(
            get(), get()
        )
    }

    single<RequestService> {
        RequestServiceImpl(
            get(), get()
        )
    }

    single<MyRequestService> {
        MyRequestServiceImpl(
            get(), get()
        )
    }

    single<BulkService> {
        BulkServiceImpl(
            get(), get()
        )
    }

    single<WorkplaceService> {
        WorkplaceServiceImpl(
            get(), get()
        )
    }

    single<IndexCardService> {
        IndexCardServiceImpl(
            get(), get()
        )
    }

    single<SalaryService> {
        SalaryServiceImpl(
            get(), get()
        )
    }

    single<PersonnelStatusReportService> {
        PersonnelStatusReportServiceImpl(
            get(), get()
        )
    }

    // endregion

    // region setting

    @OptIn(ExperimentalSettingsApi::class)
    single<AuthSettings> {
        AuthSettingsImpl(
            get()
        )
    }

    // endregion

}