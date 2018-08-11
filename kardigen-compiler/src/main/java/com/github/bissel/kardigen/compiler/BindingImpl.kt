package com.github.bissel.kardigen.compiler

import com.squareup.kotlinpoet.FunSpec
import javax.lang.model.element.Element

class BindingImpl(override val element: Element) : Binding {

    override fun render(target: FunSpec.Builder) {
        TODO("not implemented")
    }

}