package com.github.bissel.kardigen.compiler

import com.github.bissel.kardigen.annotation.KodeinInject
import com.github.bissel.kardigen.annotation.KodeinModule
import com.github.bissel.kardigen.annotation.KodeinSingleton
import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class Processor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = mutableSetOf(
        KodeinModule::class.java.name,
        KodeinSingleton::class.java.name,
        KodeinInject::class.java.name)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(set: MutableSet<out TypeElement>, environment: RoundEnvironment): Boolean {
        val injectConstructors = environment.getElementsAnnotatedWith(KodeinInject::class.java)
        val modules = modules(injectConstructors)
        render(modules)

        return true
    }


    private fun modules(constructors: Set<Element>): List<Module> {
        val modules = mutableMapOf<String, MutableList<Binding>>()

        constructors.forEach { element ->
            val classElement = element.enclosingElement
            val moduleName =
                classElement.getAnnotation(KodeinModule::class.java)?.module
                    ?: DEFAULT_MODULE_NAME

            val bindingImpl = BindingImpl(processingEnv, element)

            if (modules.containsKey(moduleName)) {
                modules[moduleName]?.add(bindingImpl)
            } else {
                val value: MutableList<Binding> = mutableListOf(bindingImpl)
                modules[moduleName] = value
            }
        }

        return modules.map { entry -> ModuleImpl(entry.key, entry.value) }
    }


    private fun render(modules: List<Module>) {
        for (module in modules) {
            module.render(processingEnv)
        }
    }

    companion object {
        const val DEFAULT_MODULE_NAME = "kardigen"
    }
}


interface Module : Renderable<ProcessingEnvironment> {
    val name: String
    val bindings: List<Binding>
}

interface Binding : Renderable<FunSpec.Builder> {
    val element: Element
}


interface Renderable<Target> {
    fun render(target: Target)
}