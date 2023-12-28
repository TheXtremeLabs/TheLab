package com.riders.thelab.ui.contacts

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityContactsDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ContactDetailActivity : AppCompatActivity() {

    companion object {
        const val CONTACT_NAME = "contact_name"
        const val CONTACT_SURNAME = "contact_surname"
        const val CONTACT_EMAIL = "contact_email"
        const val CONTACT_IMAGE = "contact_image"
    }

    private var mContext: Context? = null

    private var _viewBinding: ActivityContactsDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    var itemNameDetail: String? = null
    var itemSurnameDetail: String? = null
    private var itemEmailDetail: String? = null
    private var itemImage: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityContactsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        getBundle()

        ViewCompat.setTransitionName(binding.appBarLayout, "icon")
        supportPostponeEnterTransition()

        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        setSupportActionBar(binding.contactDetailToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.contactDetailCollapsingToolbar.title = itemNameDetail
        binding.contactDetailCollapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                R.color.transparent
            )
        )
        //        getSupportActionBar().setTitle(itemNameDetail);

        binding.appBarLayout.setExpanded(true)
        binding.appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.contactDetailCollapsingToolbar.title = itemNameDetail
                    isShow = true
                } else if (isShow) {
                    binding.contactDetailCollapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    private fun getBundle() {
        val bundle = intent.extras
        if (null == bundle) {
            Timber.e("bundle is null")
            return
        }
        itemNameDetail = bundle.getString(CONTACT_NAME)
        itemEmailDetail = bundle.getString(CONTACT_EMAIL)
        itemImage = bundle.getString(CONTACT_IMAGE)
        setViews()
    }

    private fun setViews() {
        binding.tvNameDetail.text = itemNameDetail
        binding.tvEmailDetail.text = itemEmailDetail
    }
}