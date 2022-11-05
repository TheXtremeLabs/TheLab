package com.riders.thelab.ui.kat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.riders.thelab.core.utils.LabDeviceManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.remote.dto.kat.Kat
import com.riders.thelab.databinding.ActivityKatBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class KatActivity : AppCompatActivity(), TextWatcher {

    companion object {
        var SENDER_ID: String =
            LabDeviceManager.getBrand().toString() + ", " + LabDeviceManager.getModel()
    }

    private lateinit var viewBinding: ActivityKatBinding

    private var fetchedKatMessageList: List<Kat>? = null
    private var currentMessageList: List<Kat>? = null
    private var mAdapter: KatMessagesAdapter? = null

    private var database: FirebaseDatabase? = null
    private var messageRef: DatabaseReference? = null

    private var katModel: Kat? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityKatBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnSendMessage.isEnabled = false

        initMessageRecyclerView()

        // Write a message to the database
        database = FirebaseDatabase.getInstance()
        messageRef = database!!.getReference("messages")
        Timber.d("setupView()")
        val messagesQuery: Query = messageRef!!.ref

        // Build object
        initKatObject()


        // Read from the database
        messageRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Timber.d("ValueEventListener - onDataChange()")

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val genericTypeIndicator: GenericTypeIndicator<HashMap<String, Any>> =
                    object : GenericTypeIndicator<HashMap<String, Any>>() {}
                val value = dataSnapshot.getValue(genericTypeIndicator)

                // Get keys and values
                if (null != value) {
                    fetchedKatMessageList = ArrayList()
                    fetchedKatMessageList = Kat.buildKatMessagesList(value)
                    updateUI()
                } else {
                    removeAll()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.e(error.toException(), "Failed to read value.")
            }
        })
        viewBinding.etMessage.addTextChangedListener(this)


        // Handle send message button event
        viewBinding.btnSendMessage.setOnClickListener {
            val messageContent =
                if (Objects.requireNonNull(viewBinding.etMessage.text).toString().trim { it <= ' ' }
                        .isNotEmpty())
                    viewBinding.etMessage.text.toString().trim()
                else ""

            clearEditTextField()

            UIManager.hideKeyboard(this@KatActivity, findViewById(android.R.id.content))

            // Add complement values to kat object
            katModel!!.message = messageContent
            katModel!!.timestamp = System.currentTimeMillis()
            val key = messageRef!!.push().key
            katModel!!.messageId = key
            messageRef!!
                .child(key!!)
                .setValue(katModel)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun initMessageRecyclerView() {
        Timber.d("initMessageRecyclerView()")
        currentMessageList = ArrayList()
        mAdapter = KatMessagesAdapter(this, currentMessageList as ArrayList<Kat>)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.rvMessages.layoutManager = linearLayoutManager
        viewBinding.rvMessages.adapter = mAdapter
    }

    private fun initKatObject() {
        Timber.d("initKatObject()")
        katModel = Kat()
        katModel!!.senderId = SENDER_ID
        katModel!!.chatId = Random().nextInt(1000).toString()
    }

    private fun clearEditTextField() {
        Timber.d("clearEditTextField()")
        viewBinding.etMessage.setText("")
    }

    private fun updateUI() {
        Timber.d("updateUI()")
        Timber.d("isFromEventListener")
        // If it's first time, currentList is empty then populate list with fetched item
        if (currentMessageList!!.isEmpty()) {
            Timber.d("is first time")
            currentMessageList = fetchedKatMessageList
            currentMessageList?.let { mAdapter!!.populateAllItems(it) }
        } else {
            Timber.e("NOT first time")
            val kat = fetchedKatMessageList!![fetchedKatMessageList!!.size - 1]
            Timber.e("last item : %s", kat.toString())
            mAdapter!!.populateItem(kat)
            smoothScrollToLastItem()
        }
    }

    private fun smoothScrollToLastItem() {
        runOnUiThread {
            viewBinding.rvMessages.smoothScrollToPosition(
                Objects.requireNonNull(viewBinding.rvMessages.adapter).itemCount - 1
            )
        }
    }

    private fun compareListAndGetAddedItem(
        currentList: List<Kat>,
        fetchedList: List<Kat>
    ): List<Kat> {
        Timber.d("compareListAndGetAddedItem()")
        val differences: MutableList<Kat> = ArrayList(fetchedList)
        differences.removeAll(currentList)
        for (katElement in differences) {
            Timber.e("Differences : %s", katElement.toString())
        }
        return differences
    }

    private fun removeAll() {
        mAdapter!!.removeAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        database = null
        messageRef = null
        fetchedKatMessageList = null
        currentMessageList = null
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        viewBinding.btnSendMessage.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        viewBinding.btnSendMessage.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
    }

    override fun afterTextChanged(s: Editable) {
        viewBinding.btnSendMessage.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
    }
}