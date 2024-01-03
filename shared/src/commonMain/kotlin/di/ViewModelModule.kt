package com.attendace.leopard.di

import com.attendace.leopard.presentation.viewmodel.AuthViewModel
import com.attendace.leopard.presentation.viewmodel.SplashViewModel
import com.attendace.leopard.presentation.viewmodel.BulkViewModel
import com.attendace.leopard.presentation.viewmodel.HomeViewModel
import com.attendace.leopard.presentation.viewmodel.IndexCardViewModel
import com.attendace.leopard.presentation.viewmodel.MainViewModel
import com.attendace.leopard.presentation.viewmodel.PersonnelReportStatusViewModel
import com.attendace.leopard.presentation.viewmodel.PortfolioViewModel
import com.attendace.leopard.presentation.viewmodel.ProfileViewModel
import com.attendace.leopard.presentation.viewmodel.RegisterAttendanceViewModel
import com.attendace.leopard.presentation.viewmodel.RegisterRequestViewModel
import com.attendace.leopard.presentation.viewmodel.RequestViewModel
import com.attendace.leopard.presentation.viewmodel.SalaryViewModel
import com.attendace.leopard.presentation.viewmodel.SelectComponentViewModel
import org.koin.dsl.module

val viewModelModule = module {

//    viewModelDefinition { SplashViewModel(get() , get()) }
//    viewModelDefinition { AuthViewModel(get() , get()) }

    viewModelDefinition {
        SplashViewModel(get(), get())
    }
    viewModelDefinition {
        AuthViewModel(get(), get())
    }
    viewModelDefinition {
        MainViewModel(get())
    }
    viewModelDefinition {
        HomeViewModel(get(), get(), get())
    }
    viewModelDefinition {
        PortfolioViewModel(get())
    }
    viewModelDefinition {
        RegisterAttendanceViewModel(get(), get())
    }
    viewModelDefinition {
        RegisterRequestViewModel(get(), get())
    }
    viewModelDefinition {
        RequestViewModel(get(),get())
    }
    viewModelDefinition {
        SelectComponentViewModel(get())
    }
    viewModelDefinition {
        IndexCardViewModel(get(), get())
    }
    viewModelDefinition {
        BulkViewModel(get(), get())
    }
    viewModelDefinition {
        ProfileViewModel(get(), get())
    }
    viewModelDefinition {
        SalaryViewModel(get() , get() , get())
    }
    viewModelDefinition {
        PersonnelReportStatusViewModel(get())
    }

}