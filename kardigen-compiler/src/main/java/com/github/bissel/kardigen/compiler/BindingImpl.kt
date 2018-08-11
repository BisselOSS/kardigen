package com.github.bissel.kardigen.compiler

import com.github.bissel.kardigen.annotation.KodeinBind
import com.github.bissel.kardigen.annotation.KodeinSingleton
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement

class BindingImpl(private val environment: ProcessingEnvironment,
                  override val element: Element) : Binding {

    override fun render(target: FunSpec.Builder) {
        when {
            isSingleton -> renderSingleton(target)
            else -> renderProvider(target)
        }
    }

    private fun renderProvider(target: FunSpec.Builder) {
        target.addCode(
            """
            bind<%T>() with provider {
                %T($injectStatement)
            }
            """.trimIndent(),
            bindingClassName, realClassName)
    }

    private fun renderSingleton(target: FunSpec.Builder) {
        target.addCode(
            """
            bind<%T>() with singleton {
                %T($injectStatement)
            }
            """.trimIndent(),
            bindingClassName, realClassName)
    }


    private val classElement by lazy {
        element.enclosingElement
    }

    private val isSingleton: Boolean by lazy {
        val singletonAnnotation = classElement.getAnnotation(KodeinSingleton::class.java)
        singletonAnnotation != null
    }

    private val bindingClassName: ClassName by lazy {
        val bindAnnotation: KodeinBind? = element.getAnnotation(KodeinBind::class.java)
        when {
            bindAnnotation != null -> bindAnnotation.value.java.asTypeName() as ClassName
            else -> realClassName
        }
    }


    private val realClassName: ClassName by lazy {
        classElement.asType().asTypeName() as ClassName
    }


    private val parameterCount by lazy {
        val executableElement = element as ExecutableElement
        executableElement.parameters.size
    }

    private val injectStatement by lazy {
        (0 until parameterCount).asSequence()
            .map { "instance()" }
            .joinToString(", ")
    }

}
