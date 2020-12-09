package com.szymanski.sharelibrary.features.home.presentation.requirements

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.api.model.request.ExecuteExchangeRequest
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.home.presentation.model.RequirementDisplayable
import kotlinx.android.synthetic.main.dialog_choose_book.view.*
import kotlinx.android.synthetic.main.dialog_other_user_book_details.view.*
import kotlinx.android.synthetic.main.fragment_requirements.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequirementsFragment : BaseFragment<RequirementsViewModel>(R.layout.fragment_requirements),
    RequirementsAdapter.Listeners {

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
        requirementsAdapter.setListener(this)
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.requirement.observe(this) {
            requirementsAdapter.setRequirements(it)
        }
    }

    override fun onItemClick(position: Int) {
        val requirement = viewModel.requirement.value?.get(position)!!
        viewModel.getUserBooks(requirement.user?.id!!)
        viewModel.otherUserBooks.observe(this) {
            displayExchangeDialog(requirement)
        }
    }

    override fun onIdleState() {
        super.onIdleState()
        requirement_progress_bar.visibility = View.GONE
        requirement_swipe_layout.isRefreshing = false
    }

    override fun onPendingState() {
        super.onPendingState()
        requirement_progress_bar.visibility = View.VISIBLE

    }

    private fun displayExchangeDialog(requirement: RequirementDisplayable) {
        val dialogBuilder = AlertDialog.Builder(requireActivity(),
            android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        val choices = mutableListOf("Deposit - ${requirement.exchange?.deposit}")
        viewModel.otherUserBooks.value?.forEach {
            choices.add(it.title!!)
        }
        val contentView = layoutInflater.inflate(R.layout.dialog_choose_book, null)
        val builder = dialogBuilder
            .setCancelable(false)
            .setView(contentView)
            .setTitle("Choose for what you want to exchange: ")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Exchange") { _, _ ->
            }

        val chooseAdapter = ChooseBookAdapter()
        contentView.dialog_choose_book_recycler_view.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = chooseAdapter
        }
        chooseAdapter.setListeners(object : ChooseBookAdapter.ChooseBookAdapterListeners {
            override fun onItemClick(position: Int) {
                if (position > 0) {
                    val book = viewModel.otherUserBooks.value?.get(position - 1)
                    viewModel.downloadImage(book!!)
                    viewModel.bookDetails.observe(this@RequirementsFragment) {
                        displayBookDetails(it)
                    }
                }
            }
        })
        chooseAdapter.setChoices(choices)
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val position = chooseAdapter.selectedPosition
            var params = mapOf<String, Long>()
            params =
                params.plus(Pair(ExecuteExchangeRequest.WITH_USER_ID_KEY, requirement.user?.id!!))
            params = params.plus(Pair(ExecuteExchangeRequest.EXCHANGE_ID_KEY,
                requirement.exchange?.id!!))
            if (position != 0) {
                params = params.plus(Pair(ExecuteExchangeRequest.FOR_BOOK_ID_KEY,
                    viewModel.otherUserBooks.value?.get(position - 1)?.id!!))
            }
            viewModel.executeExchange(params)
            dialog.dismiss()
            this@RequirementsFragment.requirement_swipe_layout.isRefreshing = true
        }
    }

    private fun displayBookDetails(book: BookDisplayable) {
        val content = layoutInflater.inflate(R.layout.dialog_other_user_book_details, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(content)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create()
        with(content!!) {
            other_user_book_details_title.text = book.title
            if (book.cover!!.isNotEmpty()) {
                Glide.with(this)
                    .load(book.cover)
                    .into(other_user_book_details_cover)
            }
            other_user_book_details_author.text = book.authorsDisplayable?.let {
                convertAuthorDisplayableListToString(it)
            }
            other_user_book_details_status_value.text = when (book.status) {
                BookStatus.SHARED -> {
                    getString(R.string.book_status_during_exchange)
                }
                BookStatus.EXCHANGED -> {
                    getString(R.string.book_status_exchanged)
                }
                else -> {
                    getString(R.string.book_status_at_owner)
                }
            }
        }
        dialog.show()
    }

    private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
        var endString = ""
        list.forEach { author ->
            endString += "${author.name} ${author.surname}\n"
        }
        endString = endString.trim()
        return endString.substring(endString.indices)
    }

}