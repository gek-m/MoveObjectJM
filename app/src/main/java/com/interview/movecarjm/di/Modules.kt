package com.interview.movecarjm.di

import com.interview.movecarjm.ui.main.MainFragment
import com.interview.movecarjm.ui.main.MainPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

object Modules {

    val mainPresenterModule = module {
        scope<MainFragment> {
            scoped<MainPresenter>(qualifier = named(Scopes.MAIN_PRESENTER)) { MainPresenter() }
        }
    }
}