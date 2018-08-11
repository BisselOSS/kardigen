package com.github.bissel.kardigen.compiler

import com.squareup.kotlinpoet.*
import java.io.File
import java.net.URI
import javax.annotation.processing.ProcessingEnvironment

class ModuleImpl(override val name: String,
                 override val bindings: List<Binding>) : Module {

    override fun render(target: ProcessingEnvironment) {
        val file = createFile(target)

        val fileSpecBuilder = FileSpec.builder("com.github.bissel.kardigen", name)
        fileSpecBuilder.addImport("org.kodein.di", "Kodein")
        val modulePropertySpec = PropertySpec.builder(name = name, type = kodeinModuleClassName())
        modulePropertySpec.initializer("createModule()")
        fileSpecBuilder.addProperty(modulePropertySpec.build())
        renderCreateFunction(fileSpecBuilder)
        renderBuildFunction(fileSpecBuilder)

        fileSpecBuilder.build().writeTo(File(file))
    }

    private fun renderCreateFunction(builder: FileSpec.Builder) {
        val function = FunSpec.builder("createModule")
        function.addModifiers(KModifier.PRIVATE)
        function.returns(kodeinModuleClassName())
        function.addCode("return Kodein.Module(::build)")
        builder.addFunction(function.build())
    }

    private fun renderBuildFunction(builder: FileSpec.Builder) {
        val function = FunSpec.builder("build")
        function.addModifiers(KModifier.PRIVATE)
        function.receiver(kodeinBuilderClassName())
        for (binding in bindings) {
            binding.render(function)
        }

        builder.addFunction(function.build())
    }


    private fun kodeinModuleClassName(): ClassName {
        return ClassName("org.kodein.di", "Kodein.Module")
    }

    private fun kodeinBuilderClassName(): ClassName {
        return ClassName("org.kodein.di", "Kodein.Builder")
    }

    private fun createFile(target: ProcessingEnvironment): URI {
        val elements = bindings.map(Binding::element).toTypedArray()
        return target.filer.createSourceFile(name, *elements).toUri()
    }
}