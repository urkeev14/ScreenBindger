package com.example.screenbindger.view.fragment.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentSplashBinding
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@InternalCoroutinesApi
@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        animateView()
        return binding.root
    }

    private fun animateView(){
        val animImageAlpha = ObjectAnimator.ofFloat(binding.ivScreenBindger, "alpha", 1f).also {
            it.repeatCount = 1
            it.repeatMode = ValueAnimator.REVERSE
        }

        val animImageFall = ObjectAnimator.ofFloat(binding.ivScreenBindger, "translationY", -300f, 0f)

        val animTitle = ObjectAnimator.ofFloat(binding.tvScreenBindgerText, "alpha", 1f).also {
            it.repeatCount = 1
            it.repeatMode = ValueAnimator.REVERSE
        }

        AnimatorSet().apply {
            playTogether(animImageAlpha, animImageFall, animTitle)
            duration = 2000
            start()
            doOnEnd {

            }
        }
    }

    override fun onResume() {
        super.onResume()

        observeIfUserLoggedIn()
    }

    /**
     * This method navigates to LoginFragment.class if there is no logged in user
     * in local database, or to MainFragment.class if there is.
     */
    private fun observeIfUserLoggedIn() {
        viewModel.isLoggedIn().observe(viewLifecycleOwner, Observer { isLoggedIn ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (isLoggedIn == null) {
                    gotoLoginFragment()
                } else {
                    gotoMainActivity()
                }
            }, 2000)
        })
    }

    private fun gotoLoginFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }

    private fun gotoMainActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}