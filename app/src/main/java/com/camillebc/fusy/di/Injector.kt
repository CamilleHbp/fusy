package com.camillebc.fusy.di

import com.camillebc.fusy.FusyApplication

class Injector private constructor() {
    companion object {
        var fictionComponent: FictionComponent = FusyApplication.get().fictionComponent
    }
}