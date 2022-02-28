package com.interview.movecarjm

import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.interview.movecarjm.ui.CustomView.MovingData
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun View.click(click: () -> Unit) = setOnClickListener { click() }

fun MutableList<Pair<Float, Float>>.toMovingData(): List<MovingData> {
    val movingData: MutableList<MovingData> = mutableListOf()

    if (this.isNotEmpty()) {
        var currentX = this[0].first
        var currentY = this[0].second

        for (i in 1 until this.size) {
            movingData.add(
                MovingData(
                    currentX,
                    currentY,
                    this[i].first,
                    this[i].second
                )
            )
            currentX = this[i].first
            currentY = this[i].second
        }
    }

    return movingData
}

fun <V : ViewBinding> Fragment.viewBinding(bind: (view: View) -> V) =
    ViewBindingDelegate(this, bind)

class ViewBindingDelegate<V : ViewBinding>(
    fragment: Fragment,
    private val bind: (view: View) -> V
) :
    ReadOnlyProperty<Fragment, V> {

    private var viewBinding: V? = null

    private val mainThreadHandler = android.os.Handler(Looper.getMainLooper())

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) {
            it.lifecycle.addObserver(object : LifecycleObserver {

                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun destroyed() {
                    mainThreadHandler.post {
                        viewBinding = null
                    }
                }
            })
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {

        return viewBinding ?: run {

            val view = thisRef.requireView()
            bind.invoke(view).also {
                viewBinding = it
            }
        }
    }
}