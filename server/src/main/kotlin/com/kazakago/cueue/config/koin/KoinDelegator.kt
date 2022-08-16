package com.kazakago.cueue.config.koin

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T> inject(): ReadOnlyProperty<Any?, T> = object : ReadOnlyProperty<Any?, T>, KoinComponent {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }
}
