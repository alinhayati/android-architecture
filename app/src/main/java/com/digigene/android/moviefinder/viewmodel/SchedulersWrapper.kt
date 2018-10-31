package com.digigene.android.moviefinder

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SchedulersWrapper {
    open fun io(): Scheduler {
        return Schedulers.io()
    }

    open fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}