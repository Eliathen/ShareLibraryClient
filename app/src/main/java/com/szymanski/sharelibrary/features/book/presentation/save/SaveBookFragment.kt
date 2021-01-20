package com.szymanski.sharelibrary.features.book.presentation.save

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.BookCondition
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.LanguageDisplayable
import kotlinx.android.synthetic.main.fragment_save_book.*
import kotlinx.android.synthetic.main.item_author.view.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class SaveBookFragment : BaseFragment<SaveBookViewModel>(R.layout.fragment_save_book) {


    override val viewModel: SaveBookViewModel by viewModel()

    private val addAuthorAdapter: AddAuthorAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    private var cover: ByteArray = byteArrayOf()

    val REQUEST_IMAGE_CAPTURE = 100
    val PERMISSION_CODE = 101

    companion object {
        val SAVE_BOOK_SCREEN_KEY = "SaveBookScreenKey"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.containsKey(SAVE_BOOK_SCREEN_KEY)!!) {
            val bookDisplayable = arguments?.getParcelable<BookDisplayable>(SAVE_BOOK_SCREEN_KEY)
            if (bookDisplayable != null) {
                viewModel.setBook(bookDisplayable)
            }
        }
    }

    override fun initViews() {
        super.initViews()
        initActionBar()
        initRecyclerView()
        setListeners()
        initConditionRadioGroup()
    }

    private fun initConditionRadioGroup() {
        book_condition_radio_group.setOnCheckedChangeListener { _, checkedId ->
            val condition = when (checkedId) {
                R.id.condition_new -> BookCondition.NEW
                R.id.condition_good -> BookCondition.GOOD
                else -> BookCondition.BAD

            }
            viewModel.condition = condition
        }
    }

    private fun initActionBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    private fun initRecyclerView() {
        author_list.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = addAuthorAdapter
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.categories.observe(this) {
            if (it != null) {
                category_dropdown.isClickable = true
                setActualCategories()
            }
        }
        viewModel.languages.observe(this) {
            setLanguageList(it)
        }
        viewModel.book.observe(this) {
            book_title.text = Editable.Factory.getInstance().newEditable(it.title)
            when (it.condition) {
                BookCondition.NEW -> book_condition_radio_group.check(condition_new.id)
                BookCondition.GOOD -> book_condition_radio_group.check(condition_good.id)
                else -> book_condition_radio_group.check(condition_bad.id)
            }
            addAuthorAdapter.setAuthors(it.authorsDisplayable!!)
            Glide.with(requireContext())
                .load(it.cover)
                .into(cover_image)
            cover = it.cover!!
            var newHint = ""
            for (chosenCategory in it.categoriesDisplayable!!) {
                newHint += "${chosenCategory.name}, "
            }
            category_wrapper.hint = newHint.substring(0..newHint.length - 3)
            language_dropdown.setText(it.languageDisplayable?.name)
        }
    }

    private fun setActualCategories() {
        val choices = mutableListOf<String>()
        if (viewModel.book.value != null) {
            viewModel.categories.value?.forEach {
                choices.add(it.name)
            }
            if (viewModel.book.value?.categoriesDisplayable != null) {
                viewModel.book.value?.categoriesDisplayable?.forEach {
                    choices.forEachIndexed { index, s ->
                        if (s == it.name) {
                            viewModel.chosenCategories[index] = true
                        }
                    }
                }
            }
        }
    }

    private fun setLanguageList(languages: List<LanguageDisplayable>) {
        val languagesNames = mutableListOf<String>()
        for (languageDisplayable in languages) {
            languageDisplayable.name?.let { it1 -> languagesNames.add(it1) }
        }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.language_spinner_item,
            languagesNames
        )
        language_dropdown.apply {
            if (viewModel.book.value == null) {
                setText(languagesNames.first())
            }
            setAdapter(adapter)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        imageButton.setOnClickListener {
            if (checkSelfPermission(requireContext(),
                    Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_DENIED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_CODE)
            } else {
                getPhoto()
            }
        }
        saveButton.setOnClickListener {
            saveButton.isClickable = false
            attemptSaveBook()
        }
        category_dropdown.setOnTouchListener { _, event ->
            if (event.action == ACTION_UP) {
                displayCategoryDialog()
            }
            true
        }
        category_wrapper.setEndIconOnClickListener {
            displayCategoryDialog()
        }
        book_title.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                title_edit_text_wrapper.error = ""
            } else {
                title_edit_text_wrapper.error = getString(R.string.field_required_error)
            }
        }
    }

    private fun displayCategoryDialog() {
        category_wrapper.error = ""
        val choices = mutableListOf<String>()
        viewModel.categories.value?.forEach {
            choices.add(it.name)
        }
        if (viewModel.book.value?.categoriesDisplayable != null) {
            viewModel.book.value?.categoriesDisplayable?.forEach {
                choices.forEachIndexed { index, s ->
                    if (s == it.name) {
                        viewModel.chosenCategories[index] = true
                    }
                }
            }
        }
        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.setCancelable(false)
            .setTitle(getString(R.string.choose_categories))
            .setMultiChoiceItems(
                choices.toTypedArray(),
                viewModel.chosenCategories.toBooleanArray()
            ) { _, which, isChecked ->
                viewModel.chosenCategories[which] = isChecked
            }
            .setPositiveButton("Accept", null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (viewModel.chosenCategories.contains(true)) {
                setChosenCategoriesAsHint()
                dialog.dismiss()
            }
        }
    }

    private fun setChosenCategoriesAsHint() {
        var newHint = ""
        val chosenCategories = mutableListOf<String>()
        viewModel.chosenCategories.forEachIndexed { index, b ->
            if (b) {
                viewModel.categories.value?.get(index)?.name?.let { chosenCategories.add(it) }
            }
        }
        for (chosenCategory in chosenCategories) {
            newHint += "$chosenCategory, "
        }
        category_wrapper.hint = newHint.substring(0..newHint.length - 3)
    }

    private fun attemptSaveBook() {
        clearErrors()
        val title = book_title.text.toString().replace("\"", "")
        val authors = addAuthorAdapter.getAuthors()
        val language = language_dropdown.text.toString()
        var cancel = false
        var focusView = View(requireContext())
        if (TextUtils.isEmpty(title)) {
            title_edit_text_wrapper.error = getString(R.string.field_required_error)
            cancel = true
            focusView = book_title
        }
        authors.forEachIndexed { index, author ->
            if (TextUtils.isEmpty(author.name)) {
                val view = author_list.getChildAt(index)
                view.author_name.error = getString(R.string.field_required_error)
                focusView = view.author_name
                cancel = true
            }
            if (TextUtils.isEmpty(author.surname)) {
                val view = author_list.getChildAt(index)
                view.author_surname.error = getString(R.string.field_required_error)
                focusView = view.author_surname
                cancel = true
            }
        }
        if (TextUtils.isEmpty(language)) {
            language_dropdown.error = getString(R.string.field_required_error)
            focusView = language_dropdown
            cancel = true
        }
        if (!viewModel.chosenCategories.contains(true)) {
            cancel = true
            focusView = category_wrapper
            category_wrapper.error = getString(R.string.field_required_error)
        }
        if (cover.isEmpty()) {
            cancel = true
            focusView = cover_image
            displayDialogCoverIsRequired()
        }

        if (cancel) {
            focusView.requestFocus()
            saveButton.isClickable = true
        } else {
            saveButton.isClickable = true
            val book = BookDisplayable(
                id = null,
                title = title,
                authorsDisplayable = authors,
                cover = this.cover,
                status = BookStatus.AT_OWNER,
                atUserDisplayable = null,
                categoriesDisplayable = null,
                languageDisplayable = viewModel.languages.value?.first { it.name == language },
                condition = BookCondition.BAD
            )
            viewModel.saveBook(book)
        }
    }

    private fun clearErrors() {
        title_edit_text_wrapper.error = ""
        category_wrapper.error = ""
    }

    private fun displayDialogCoverIsRequired() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
            .setMessage("Cover image is required")
            .setNeutralButton("OK") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }


    private fun getPhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            val image = data.extras?.get("data") as Bitmap
            Glide.with(cover_image)
                .load(image)
                .into(cover_image)
            val stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            cover = stream.toByteArray()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        author_list.adapter = null
        author_list.layoutManager = null
    }

    override fun onIdleState() {
        progress_bar.visibility = View.GONE
    }

    override fun onPendingState() {
        progress_bar.visibility = View.VISIBLE

    }
}