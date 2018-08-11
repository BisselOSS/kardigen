package com.github.bissel.kardigen.compiler

import javax.annotation.processing.ProcessingEnvironment

class ModuleImpl(override val name: String,
                 override val bindings: List<Binding>) : Module {

    override fun render(target: ProcessingEnvironment) {
        TODO("not implemented")
    }
}