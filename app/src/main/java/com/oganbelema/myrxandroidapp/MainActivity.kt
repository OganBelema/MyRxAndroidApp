package com.oganbelema.myrxandroidapp

import android.os.Bundle
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        mapAndFilter()
        initLayout()
        //map()
    }

    private fun initLayout() {
        sugar()
        //noSugar()
    }

    /**
     * Function to increment counter value with Rx without syntax sugar
     */
    private fun noSugar() {
        val emitter = PublishSubject.create<View>()

        counterButton.setOnClickListener {
            emitter.onNext(it)
        }

        val observer = object: Observer<View> {

            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: View) {
                incrementCounter2()
            }

            override fun onComplete() {}

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

    /**
     * Function to increment counter value with Rx with syntax sugar
     */
    private fun sugar() {
        addDisposable(
            RxView.clicks(counterButton)
                .map {
                    incrementCounter1()
                }.throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe {
                    incrementCounter2()
                }
        )
    }

    /**
     * Shows the capability of the Rx map operator
     */
    private fun map(){
        addDisposable(
            Observable.just(1, 5, 10, 20)
                .map { theNumber ->
                    theNumber * 3
                }
                .subscribe {
                    Log.i("map", it.toString())
                }
        )
    }

    /**
     * Shows the capability of the Rx filter operator
     */
    private fun mapAndFilter(){
        addDisposable(
            Observable.just(1, 5, 10, 20)
                .map {
                    it * 3
                }
                .filter {
                    it % 2 == 0
                }
                .subscribe {
                    Log.i("map unpacked", it.toString())
                }
        )
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
