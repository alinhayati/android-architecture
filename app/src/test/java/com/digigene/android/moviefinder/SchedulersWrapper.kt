package com.digigene.android.moviefinder

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SchedulersWrapper {
    fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    fun main(): Scheduler {
        return Schedulers.trampoline()
    }
}