package com.github.bissel.kardigen.compiler

import com.github.bissel.kardigen.annotation.KodeinBind
import com.github.bissel.kardigen.annotation.KodeinSingleton
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror


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

    private val bindingClassName: TypeName by lazy {
        val bindAnnotation: KodeinBind? = classElement.getAnnotation(KodeinBind::class.java)
        when {
            bindAnnotation != null -> projectionType(bindAnnotation).asTypeName()
            else -> realClassName
        }
    }

    private fun projectionType(bind: KodeinBind): TypeMirror {
        val bindType = environment.typeUtils.asElement(bind.getValueType())
        val classElement = classElement as TypeElement
        val classSuperTypes = environment.typeUtils.directSupertypes(classElement.asType())
        return classSuperTypes.first {
            val erasedInterface = environment.typeUtils.erasure(it)
            val erasedBindInterface = environment.typeUtils.erasure(bindType.asType())
            environment.typeUtils.isSameType(erasedBindInterface, erasedInterface)
        }
    }

    private fun KodeinBind.getValueType(): TypeMirror {
        try {
            this.value // this should throw
        } catch (e: MirroredTypeException) {
            return e.typeMirror
        }

        throw IllegalStateException("According to StackOverflow... We are fucked now")
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
