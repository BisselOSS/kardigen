package com.github.bissel.kardigen.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import java.io.File
import java.net.URI
import javax.annotation.processing.ProcessingEnvironment

class ModuleImpl(override val name: String,
                 override val bindings: List<Binding>) : Module {

    override fun render(target: ProcessingEnvironment) {
        val file = createFile(target)

        val fileSpecBuilder = FileSpec.builder("com.github.bissel.kardigen", name)

        val modulePropertySpec = PropertySpec.builder(name = name, type = kodeinModuleClassName())


        fileSpecBuilder.addProperty(modulePropertySpec.build())
        fileSpecBuilder.build().writeTo(File(file))
    }


    private fun kodeinModuleClassName(): ClassName {
        return ClassName("org.kodein.di", "Kodein.Module")
    }

    private fun createFile(target: ProcessingEnvironment): URI {
        val elements = bindings.map(Binding::element).toTypedArray()
        return target.filer.createSourceFile(name, *elements).toUri()
    }
}