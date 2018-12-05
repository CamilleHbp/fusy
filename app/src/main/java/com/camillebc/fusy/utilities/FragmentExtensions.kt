package com.camillebc.fusy.utilities

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun androidx.fragment.app.FragmentManager.inTransaction(func: androidx.fragment.app.FragmentTransaction.() -> androidx.fragment.app.FragmentTransaction) {
    beginTransaction().func().commit()
}