package com.github.bissel.kardigen.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CONSTRUCTOR)
annotation class KodeinInject


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class KodeinSingleton


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class KodeinModule(val module: String = "kardigen")




