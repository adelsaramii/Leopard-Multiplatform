package com.attendace.leopard.di

import com.attendace.leopard.data.repository.auth.AuthRepositoryImpl
import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.repository.workplace.WorkplaceRepositoryImpl
import com.attendace.leopard.data.repository.workplace.WorkplaceRepository
import com.attendace.leopard.data.repository.attendance.AttendanceRepository
import com.attendace.leopard.data.repository.attendance.AttendanceRepositoryImpl
import com.attendace.leopard.data.repository.bulk.BulkRepository
import com.attendace.leopard.data.repository.bulk.BulkRepositoryImpl
import com.attendace.leopard.data.repository.daily.DailyRepository
import com.attendace.leopard.data.repository.daily.DailyRepositoryImpl
import com.attendace.leopard.data.repository.index_card.IndexCardRepository
import com.attendace.leopard.data.repository.index_card.IndexCardRepositoryImpl
import com.attendace.leopard.data.repository.monthly.MonthRepository
import com.attendace.leopard.data.repository.monthly.MonthRepositoryImpl
import com.attendace.leopard.data.repository.my_request.MyRequestRepository
import com.attendace.leopard.data.repository.my_request.MyRequestRepositoryImpl
import com.attendace.leopard.data.repository.personnel_report_status.PersonnelStatusReportRepository
import com.attendace.leopard.data.repository.personnel_report_status.PersonnelStatusReportRepositoryImpl
import com.attendace.leopard.data.repository.portfolio.PortfolioRepository
import com.attendace.leopard.data.repository.portfolio.PortfolioRepositoryImpl
import com.attendace.leopard.data.repository.request.RequestRepository
import com.attendace.leopard.data.repository.request.RequestRepositoryImpl
import com.attendace.leopard.data.repository.salary.SalaryRepository
import com.attendace.leopard.data.repository.salary.SalaryRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

//    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
//    single<WorkplaceRepository> { WorkplaceRepositoryImpl(get(), get(), get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(get(), get(), get())
    }

    single {
        MonthRepositoryImpl(get())
    } bind MonthRepository::class

    single {
        DailyRepositoryImpl(get())
    } bind DailyRepository::class

    single {
        PortfolioRepositoryImpl(get(), get())
    } bind PortfolioRepository::class

    single {
        AttendanceRepositoryImpl(get(), get(), get())
    } bind AttendanceRepository::class

    single {
        RequestRepositoryImpl(get())
    } bind RequestRepository::class

    single {
        MyRequestRepositoryImpl(get(), get())
    } bind MyRequestRepository::class

    single {
        BulkRepositoryImpl(get(), get())
    } bind BulkRepository::class

    single {
        WorkplaceRepositoryImpl(get(), get(), get())
    } bind WorkplaceRepository::class

    single {
        IndexCardRepositoryImpl(get())
    } bind IndexCardRepository::class

    single {
        SalaryRepositoryImpl(get())
    } bind SalaryRepository::class

    single {
        PersonnelStatusReportRepositoryImpl(get(), get())
    } bind PersonnelStatusReportRepository::class

}