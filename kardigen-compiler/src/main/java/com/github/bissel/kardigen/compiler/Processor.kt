package com.github.bissel.kardigen.compiler

import com.github.bissel.kardigen.annotation.KodeinInject
import com.github.bissel.kardigen.annotation.KodeinModule
import com.github.bissel.kardigen.annotation.KodeinSingleton
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class Processor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = mutableSetOf(
        KodeinModule::class.java.name,
        KodeinSingleton::class.java.name,
        KodeinInject::class.java.name)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        return true
    }

}