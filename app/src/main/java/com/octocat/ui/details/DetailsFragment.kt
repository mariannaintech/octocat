package com.octocat.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet
import com.octocat.R
import com.octocat.databinding.FragmentDetailsBinding
import com.octocat.domain.models.RepoInfo
import com.octocat.utils.requireParcelable

class DetailsFragment : Fragment() {

    companion object {
        const val TAG = "DetailsFragment"

        private const val REPO_KEY = "repo_key"
        fun newInstance(repo: RepoInfo) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(REPO_KEY, repo)
            }
        }
    }

    private lateinit var binding: FragmentDetailsBinding
    private val repo by lazy { requireArguments().requireParcelable<RepoInfo>(REPO_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transform = TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
        }
        sharedElementEnterTransition = transform
        sharedElementReturnTransition = transform

        exitTransition = Fade()
        enterTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            initToolbar(toolbar)
            view.transitionName = getString(R.string.details_transition, repo.repoId)
            toolbar.title = repo.name
            descriptionValue.text = repo.description
            updateAtCard.bodyText = repo.updatedAt.toString()
            starsCountCard.bodyText = repo.starsCount.toString()
            forkCountCard.bodyText = repo.forksCount.toString()
        }

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}

fun Fragment.initToolbar(toolbar: Toolbar) {
    (requireActivity() as AppCompatActivity).run {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}