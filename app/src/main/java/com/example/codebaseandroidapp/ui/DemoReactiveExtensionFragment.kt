package com.example.codebaseandroidapp.ui

import android.util.Log
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoReactiveExtensionBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers.io

@AndroidEntryPoint
class DemoReactiveExtensionFragment :
    OthersBaseFragment<FragmentDemoReactiveExtensionBinding>(FragmentDemoReactiveExtensionBinding::inflate) {

    private var disposable: Disposable? = null

    companion object {
        @JvmStatic fun newInstance() = DemoReactiveExtensionFragment()
    }

    override fun initObserve() {
        createObservable().subscribeOn(io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(createObserver())
    }

    override fun initialize() {
        binding.tvReactive.text = ""
    }

    private fun getListUser(): ArrayList<User> {
        val list = ArrayList<User>()
        list.add(User("0328562861", "Hieu", "eHieu"))
        list.add(User("0328562862", "Thao", "eThao"))
        list.add(User("0328562863", "Nhi", "eNhi"))
        list.add(User("0328562864", "Chau", "eChau"))
        return list;
    }

    private fun createObservable() : Observable<User> {
        val list = getListUser()
        return Observable.create {
            if(!it.isDisposed) {
                if(list.isNullOrEmpty()) {
                    it.onError(Exception())
                }
                if(!list.isEmpty()) {
                    for (item in list) {
                        Thread.sleep(500)
                        it.onNext(item)
                        Log.d("Thread",Thread.currentThread().name)
                    }
                }
                Thread.sleep(500)
                it.onComplete()
            }
        }
    }

    private fun createObserver(): Observer<User> = object: Observer<User> {
        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(t: User) {
            val info = "${binding.tvReactive.text} \n\n ${t.toString()}"
            binding.tvReactive.text = info
        }

        override fun onError(e: Throwable) {
            val info = "onError"
            binding.tvReactive.text = info
        }

        override fun onComplete() {
            val info = "${binding.tvReactive.text} \n\n onComplete" +
                    "\n\nThread: ${Thread.currentThread().name}"
            binding.tvReactive.text = info
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let {
            if(!it.isDisposed) {
                it.dispose()
            }
        }
    }
}