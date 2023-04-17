package com.octocat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import com.octocat.R
import com.octocat.databinding.FragmentHomeBinding
import com.octocat.network.exception.ApiRateLimitException
import com.octocat.network.exception.NoNetworkException
import com.octocat.ui.details.DetailsFragment
import com.octocat.ui.home.recyclerview.VerticalMargins
import com.octocat.ui.home.recyclerview.VerticalMarginsDecoration
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.log.taggedLogger
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val logger by taggedLogger("HomeFragment")
    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = Fade()
        enterTransition = Fade()

        observeState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.reposRecyclerView.addItemDecoration(
            VerticalMarginsDecoration(
                VerticalMargins(
                    top = view.context.resources.getDimensionPixelSize(R.dimen.content_top_margin),
                    bottom = view.context.resources.getDimensionPixelSize(R.dimen.content_bottom_margin),
                )
            )
        )
        binding.reposRecyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.reposRecyclerView.adapter = HomeAdapter(view.context) { binding, item ->
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, DetailsFragment.newInstance(item), DetailsFragment.TAG)
                .addSharedElement(binding.repoCard, binding.repoCard.transitionName)
                .addToBackStack(null)
                .commit()
        }

        binding.searchBtn.setOnClickListener {
            findUser()
        }

        binding.userIdEditText.onDone { findUser() }
        postponeEnterTransition()
    }

    private fun findUser() {
        val userId = binding.userIdTextInput.editText?.text.toString().trim()
        if (userId.isEmpty()) {
            binding.userIdTextInput.error = resources.getString(R.string.empty_field)
        } else {
            binding.userIdTextInput.error = ""
            viewModel.loadData(userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    logger.d { "[observeState] state: $it" }
                    binding.bindState(it)
                    (view?.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                }
            }
        }
    }

}

fun FragmentHomeBinding.bindState(state: HomeViewState?) {
    when (state) {
        null -> bindEmpty()
        is HomeViewState.Success -> bindSuccess(state)
        is HomeViewState.Failure -> bindFailure(state)
    }
}

fun FragmentHomeBinding.bindEmpty() {
    warningLayout.isVisible = true
    warningImage.setImageResource(R.drawable.no_results)
    warningTitle.setText(R.string.no_results_title)
    warningBody.setText(R.string.no_results_descr)
}

fun FragmentHomeBinding.bindSuccess(state: HomeViewState.Success) {
    warningLayout.isVisible = false
    val homeAdapter = reposRecyclerView.adapter as HomeAdapter
    homeAdapter.setData(state.data)
}

fun FragmentHomeBinding.bindFailure(state: HomeViewState.Failure) {
    val homeAdapter = reposRecyclerView.adapter as HomeAdapter
    homeAdapter.setData(null)

    warningLayout.isVisible = true
    warningImage.setImageResource(R.drawable.sad_octocat)
    warningTitle.setText(
        when (state.cause) {
            is NoNetworkException -> R.string.no_network_title
            is ApiRateLimitException -> R.string.api_rate_limit_title
            else -> R.string.user_not_found_title
        }
    )
    warningBody.setText(
        when (state.cause) {
            is NoNetworkException -> R.string.no_network_descr
            is ApiRateLimitException -> R.string.api_rate_limit_descr
            else -> R.string.user_not_found_descr
        }
    )
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback()
        }
        false
    }
}