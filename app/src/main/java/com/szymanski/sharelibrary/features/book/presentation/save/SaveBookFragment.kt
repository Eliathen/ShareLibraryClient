package com.szymanski.sharelibrary.features.book.presentation.save

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.fragment_save_book.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

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
        toolbar.subtitle = getString(R.string.save_new_book_toolbar_subtitle)
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
            val book = BookDisplayable(
                id = null,
                title = book_title.text.toString(),
                authorsDisplayable = addAuthorAdapter.getAuthors(),
                cover = this.cover
            )
            viewModel.saveBook(book)
        }
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

    override fun initObservers() {
        super.initObservers()
    }

    override fun onIdleState() {
        progress_bar.visibility = View.GONE
    }

    override fun onPendingState() {
        progress_bar.visibility = View.VISIBLE

    }
}