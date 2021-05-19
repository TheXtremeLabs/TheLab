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

    var itemNameDetail: String? = null
    var itemSurnameDetail: String? = null
    var itemEmailDetail: String? = null
    var itemImage: String? = null

    lateinit var viewBinding: ActivityContactsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityContactsDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mContext = this

        getBundle()

        ViewCompat.setTransitionName(viewBinding.appBarLayout, "icon")
        supportPostponeEnterTransition()

        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        setSupportActionBar(viewBinding.contactDetailToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewBinding.contactDetailCollapsingToolbar.title = itemNameDetail
        viewBinding.contactDetailCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
        //        getSupportActionBar().setTitle(itemNameDetail);

        viewBinding.appBarLayout.setExpanded(true)
        viewBinding.appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    viewBinding.contactDetailCollapsingToolbar.title = itemNameDetail
                    isShow = true
                } else if (isShow) {
                    viewBinding.contactDetailCollapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
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
        viewBinding.tvNameDetail.text = itemNameDetail
        viewBinding.tvEmailDetail.text = itemEmailDetail
    }
}