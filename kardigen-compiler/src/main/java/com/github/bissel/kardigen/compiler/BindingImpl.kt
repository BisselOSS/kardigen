package com.github.bissel.kardigen.compiler

import com.github.bissel.kardigen.annotation.KodeinSingleton
import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

class BindingImpl(private val environment: ProcessingEnvironment,
                  override val element: Element) : Binding {

    override fun render(target: FunSpec.Builder) {
        when {
            isSingleton -> renderSingleton(target)
            else -> renderProvider(target)
        }
    }

    private fun renderProvider(target: FunSpec.Builder) {
        target.addCode("""
            bind<
        """.trimIndent())
    }

    private fun renderSingleton(target: FunSpec.Builder) {

    }


    private val classElement by lazy {
        element.enclosingElement
    }

    private val isSingleton: Boolean by lazy {
        val singletonAnnotation = classElement.getAnnotation(KodeinSingleton::class.java)
        singletonAnnotation != null
    }

    private val bindingType by lazy {
        // val element.getAnnotation(KodeinBind::class.java)
    }


}
