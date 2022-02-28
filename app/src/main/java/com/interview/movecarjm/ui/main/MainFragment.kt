package com.interview.movecarjm.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.interview.movecarjm.click
import com.interview.movecarjm.databinding.MainFragmentBinding
import com.interview.movecarjm.di.Scopes
import com.interview.movecarjm.ui.CustomView.CarMoveAction
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent

class MainFragment : Fragment(), CarMoveAction {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val scope = KoinJavaComponent.getKoin().createScope<MainFragment>()
    private val presenter: MainPresenter = scope.get(named(Scopes.MAIN_PRESENTER))
    private var _binding: MainFragmentBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        presenter.carMoveAction = this
        binding?.root?.post {
            binding?.let {
                presenter.setCarParameters(it.car)
                presenter.setAreaParameters(it.carArea)
            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let { binding ->
            with(binding) {
                btnDrawRoad.isEnabled = false
                btnShowCar.click {
                    presenter.resetCarPosition(car)
                    car.isEnabled = false
                    btnDrawRoad.isEnabled = true
                }
                btnDrawRoad.click {
                    presenter.showRoad()
                    car.isEnabled = true
                }
                car.setOnClickListener { car ->
                    car.isEnabled = false
                    btnDrawRoad.isEnabled = false
                    presenter.startMove(car)
                }
            }
        }
    }

    override fun setCarStartPosition(x: Int, y: Int) {
        binding?.let {
            with(it) {
                val layoutParams: LinearLayout.LayoutParams =
                    car.layoutParams as LinearLayout.LayoutParams
                with(layoutParams) {
                    leftMargin = x
                    topMargin = y
                }
                car.layoutParams = layoutParams
                car.isVisible = true
                carArea.roadPoints = null
                carArea.invalidate()
            }
        }
    }

    override fun drawCarRoad(roadPoints: List<Pair<Float, Float>>, carWidth: Int, carHeight: Int) {
        binding?.let {
            with(it) {
                carArea.roadPoints = roadPoints
                carArea.carWidth = carWidth
                carArea.carHeight = carHeight
                carArea.invalidate()
            }
        }
    }
}