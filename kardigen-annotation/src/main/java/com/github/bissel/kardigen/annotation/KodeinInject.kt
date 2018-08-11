package com.github.bissel.kardigen.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CONSTRUCTOR)
annotation class KodeinInject


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class KodeinSingleton

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class KodeinModule(val module: String = "kardigen")

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class KodeinBind(val value: KClass<*>)


interface Service<T>

class ServiceImpl<T> : Service<T>


