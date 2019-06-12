package com.camillebc.fusy.utilities



import android.util.Log
import com.camillebc.fusy.BuildConfig
import kotlin.reflect.KClass

/* Convenient wrappers over Android Log.* static methods */

/** Wrapper over [Log.i] */
inline fun <reified T> T.logi(message: String, onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null) =
    log(onlyInDebugMode) { Log.i(getClassSimpleName(enclosingClass), message) }

/** Lazy wrapper over [Log.i] */
inline fun <reified T> T.logi(onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null, lazyMessage: () -> String) {
    log(onlyInDebugMode) { Log.i(getClassSimpleName(enclosingClass), lazyMessage.invoke()) }
}

/** Wrapper over [Log.d] */
inline fun <reified T> T.logd(message: String, onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null) =
    log(onlyInDebugMode) { Log.d(getClassSimpleName(enclosingClass), message) }

/** Lazy wrapper over [Log.d] */
inline fun <reified T> T.logd(onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null, lazyMessage: () -> String) {
    log(onlyInDebugMode) { Log.d(getClassSimpleName(enclosingClass), lazyMessage.invoke()) }
}

/** Wrapper over [Log.v] */
inline fun <reified T> T.logv(message: String, onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null) =
    log(onlyInDebugMode) { Log.v(getClassSimpleName(enclosingClass), message) }

/** Lazy wrapper over [Log.v] */
inline fun <reified T> T.logv(onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null, lazyMessage: () -> String) {
    log(onlyInDebugMode) { Log.v(getClassSimpleName(enclosingClass), lazyMessage.invoke()) }
}

/** Wrapper over [Log.e] */
inline fun <reified T> T.loge(message: String, onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null) =
    log(onlyInDebugMode) { Log.e(getClassSimpleName(enclosingClass), message) }

/** Lazy wrapper over [Log.e] */
inline fun <reified T> T.loge(onlyInDebugMode: Boolean = true, enclosingClass: KClass<*>? = null, lazyMessage: () -> String) {
    log(onlyInDebugMode) { Log.e(getClassSimpleName(enclosingClass), lazyMessage.invoke()) }
}

inline fun log(onlyInDebugMode: Boolean, logger: () -> Unit) {
    when {
        onlyInDebugMode && BuildConfig.DEBUG -> logger()
        !onlyInDebugMode -> logger()
    }
}

/**
 * Utility that returns the name of the class from within it is invoked.
 * It allows to handle invocations from anonymous classes given that the string returned by `T::class.java.simpleName`
 * in this case is an empty string.
 *
 * @throws IllegalArgumentException if `enclosingClass` is `null` and this function is invoked within an anonymous class
 */
inline fun <reified T> T.getClassSimpleName(enclosingClass: KClass<*>?): String =

    if(T::class.java.simpleName.isNotBlank()) {
        T::class.java.simpleName
    }
    else { // Enforce the caller to pass a class to retrieve its simple name
        enclosingClass?.simpleName ?: throw IllegalArgumentException("enclosingClass cannot be null when invoked from an anonymous class")
    }