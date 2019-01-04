package com.camillebc.fusy.di

import com.camillebc.fusy.FusyApplication

class Injector private constructor() {
    companion object {
        fun getFictionComponent(): FictionComponent = FusyApplication.get().fictionComponent
    }
}