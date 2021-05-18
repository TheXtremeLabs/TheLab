package com.riders.thelab.ui.kat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.riders.thelab.core.utils.LabDeviceManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.remote.dto.kat.Kat;
import com.riders.thelab.databinding.ActivityKatBinding;
import com.riders.thelab.ui.base.SimpleActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import timber.log.Timber;

public class KatActivity extends SimpleActivity implements TextWatcher {

    private ActivityKatBinding viewBinding;

    private List<Kat> fetchedKatMessageList;
    private List<Kat> currentMessageList;
    private KatMessagesAdapter mAdapter;

    private FirebaseDatabase database;
    private DatabaseReference messageRef;

    private Kat katModel;

    public static String SENDER_ID =
            LabDeviceManager.getBrand() + ", " + LabDeviceManager.getModel();

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityKatBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.btnSendMessage.setEnabled(false);

        initMessageRecyclerView();

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        messageRef = database.getReference("messages");

        Timber.d("setupView()");
        Query messagesQuery = messageRef.getRef();

        // Build object
        initKatObject();


        // Read from the database
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Timber.d("ValueEventListener - onDataChange()");

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<HashMap<String, Object>> genericTypeIndicator =
                        new GenericTypeIndicator<HashMap<String, Object>>() {
                        };
                HashMap<String, Object> value = dataSnapshot.getValue(genericTypeIndicator);

                // Get keys and values
                if (null != value) {

                    fetchedKatMessageList = new ArrayList<>();
                    fetchedKatMessageList = Kat.buildKatMessagesList(value);
                    updateUI();
                } else {
                    removeAll();
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Timber.e(error.toException(), "Failed to read value.");
            }
        });

        viewBinding.etMessage.addTextChangedListener(this);


        // Handle send message button event
        viewBinding.btnSendMessage.setOnClickListener(
                view -> {
                    String messageContent =
                            !Objects.requireNonNull(viewBinding.etMessage.getText()).toString().trim().isEmpty()
                                    ? viewBinding.etMessage.getText().toString().trim()
                                    : "";

                    clearEditTextField();

                    UIManager.hideKeyboard(KatActivity.this, findViewById(android.R.id.content));

                    // Add complement values to kat object
                    katModel.setMessage(messageContent);
                    katModel.setTimestamp(System.currentTimeMillis());

                    String key = messageRef.push().getKey();
                    katModel.setMessageId(key);

                    messageRef
                            .child(key)
                            .setValue(katModel);
                });
    }

    private void initMessageRecyclerView() {
        Timber.d("initMessageRecyclerView()");

        currentMessageList = new ArrayList<>();

        mAdapter = new KatMessagesAdapter(this, currentMessageList);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.rvMessages.setLayoutManager(linearLayoutManager);
        viewBinding.rvMessages.setAdapter(mAdapter);
    }

    private void initKatObject() {
        Timber.d("initKatObject()");
        katModel = new Kat();
        katModel.setSenderId(SENDER_ID);
        katModel.setChatId(String.valueOf(new Random().nextInt(1000)));
    }

    private void clearEditTextField() {
        Timber.d("clearEditTextField()");
        viewBinding.etMessage.setText("");
    }

    private void updateUI() {
        Timber.d("updateUI()");

        Timber.d("isFromEventListener");
        // If it's first time, currentList is empty then populate list with fetched item
        if (currentMessageList.isEmpty()) {
            Timber.d("is first time");
            currentMessageList = fetchedKatMessageList;
            mAdapter.populateAllItems(currentMessageList);
        } else {
            Timber.e("NOT first time");
            Kat kat = fetchedKatMessageList.get(fetchedKatMessageList.size() - 1);
            Timber.e("last item : %s", kat.toString());
            mAdapter.populateItem(kat);
            smoothScrollToLastItem();
        }

    }

    private void smoothScrollToLastItem() {
        runOnUiThread(() -> viewBinding.rvMessages.smoothScrollToPosition(
                Objects.requireNonNull(viewBinding.rvMessages.getAdapter()).getItemCount() - 1));
    }

    private List<Kat> compareListAndGetAddedItem(List<Kat> currentList, List<Kat> fetchedList) {
        Timber.d("compareListAndGetAddedItem()");
        List<Kat> differences = new ArrayList<>(fetchedList);
        differences.removeAll(currentList);

        for (Kat katElement : differences) {
            Timber.e("Differences : %s", katElement.toString());
        }

        return differences;
    }

    private void removeAll() {
        mAdapter.removeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        database = null;
        messageRef = null;
        fetchedKatMessageList = null;
        currentMessageList = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        viewBinding.btnSendMessage.setEnabled(s.toString().trim().length() > 0);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        viewBinding.btnSendMessage.setEnabled(s.toString().trim().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
        viewBinding.btnSendMessage.setEnabled(s.toString().trim().length() > 0);
    }
}
