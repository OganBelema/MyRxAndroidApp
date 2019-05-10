package com.oganbelema.myrxandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        initLayout()
    }

    private fun initLayout() {
        //sugar
        noSugar()
    }

    private fun noSugar() {
        val emitter = PublishSubject.create<View>()

        counterButton.setOnClickListener {
            emitter.onNext(it)
        }

        val observer = object: Observer<View> {
            override fun onComplete() {}

            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: View) {
                incrementCounter2()
            }

            override fun onError(e: Throwable) {}
        }

        emitter.map(object: Function<View, View> {
            override fun apply(t: View): View {
                incrementCounter1()
                return t
            }
        })
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .subscribe(observer)
    }

    private fun sugar() {
        RxView.clicks(counterButton)
            .map {
                incrementCounter1()
            }.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .subscribe {
                incrementCounter2()
            }
    }

    private fun incrementCounter2() {
        var newVal = counter2.text.toString().toInt()
        newVal++
        counter2.text = newVal.toString()
    }

    private fun incrementCounter1() {
        var newVal = counter1.text.toString().toInt()
        newVal++
        counter1.text = newVal.toString()
    }
}
