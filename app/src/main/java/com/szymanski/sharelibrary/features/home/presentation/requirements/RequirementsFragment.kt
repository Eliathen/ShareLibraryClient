package com.szymanski.sharelibrary.features.home.presentation.requirements

import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_requirements.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequirementsFragment : BaseFragment<RequirementsViewModel>(R.layout.fragment_requirements) {

    override val viewModel: RequirementsViewModel by viewModel()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    private val requirementsAdapter: RequirementsAdapter by inject()

    private val TAG = "RequirementsFragment"

    override fun initViews() {
        super.initViews()
        initRecyclerView()
        initListeners()
    }

    private fun initListeners() {
        requirement_swipe_layout.setOnRefreshListener {
            viewModel.refreshRequirements()
        }
    }

    private fun initRecyclerView() {
        requirement_recycler_view.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = requirementsAdapter
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.requirement.observe(this) {
            requirementsAdapter.setRequirements(it)
            Log.d(TAG, "initObservers: I'm displaying requirements")
        }
    }

    override fun onIdleState() {
        super.onIdleState()
    }

    override fun onPendingState() {
        super.onPendingState()
    }
}