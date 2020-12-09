package com.szymanski.sharelibrary.features.book.presentation.save

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
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

    override fun initViews() {
        super.initViews()
        initActionBar()
        initRecyclerView()
        setListeners()
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
            attemptSaveBook()
        }
    }

    private fun attemptSaveBook() {
        saveButton.isClickable = false
        val title = book_title.text.toString().replace("\"", "")
        val authors = addAuthorAdapter.getAuthors()
        var cancel = false
        var focusView = View(requireContext())
        if (TextUtils.isEmpty(title)) {
            book_title.error = getString(R.string.field_required_error)
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
        if (cover.isEmpty()) {
            cancel = true
            focusView = cover_image
            displayAlertDialog()
        }

        if (cancel) {
            focusView.requestFocus()
            saveButton.isClickable = true
        } else {
            val book = BookDisplayable(
                id = null,
                title = title,
                authorsDisplayable = authors,
                cover = this.cover,
                status = BookStatus.AT_OWNER,
                atUserDisplayable = null
            )
            viewModel.saveBook(book)
        }
    }

    private fun displayAlertDialog() {
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