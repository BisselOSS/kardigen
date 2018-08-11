package com.github.bissel.kardigen.compiler

import com.squareup.kotlinpoet.FunSpec
import javax.lang.model.element.Element

class BindingImpl : Binding {
    override val element: Element
        get() = TODO("not implemented")

    override fun render(target: FunSpec.Builder) {
        TODO("not implemented")
    }

}