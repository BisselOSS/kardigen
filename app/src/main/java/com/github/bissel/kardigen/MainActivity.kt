package com.github.bissel.kardigen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.bissel.kardigen.annotation.KodeinBind
import com.github.bissel.kardigen.annotation.KodeinInject
import com.github.bissel.kardigen.annotation.KodeinModule
import com.github.bissel.kardigen.annotation.KodeinSingleton
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

interface SomeT


interface Service<T>

@KodeinModule("hallo")
@KodeinSingleton
@KodeinBind(Service::class)
class ServiceImpl @KodeinInject constructor() : Service<SomeT>


val test = Kodein.Module("test") {
    bind<Service<SomeT>>() with provider { ServiceImpl() }
}

