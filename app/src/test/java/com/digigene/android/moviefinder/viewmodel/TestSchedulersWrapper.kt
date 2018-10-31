package com.digigene.android.moviefinder

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulersWrapper : SchedulersWrapper() {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun main(): Scheduler {
        return Schedulers.trampoline()
    }
}