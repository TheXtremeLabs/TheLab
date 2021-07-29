package com.riders.thelab.core.utils

import android.content.Context
import android.content.SharedPreferences

class LabPreferenceManager(context: Context?) {
    companion object {

        // Sharedpref file name
        private const val PREF_NAME = "lab_pref"

        // All Shared Preferences Keys
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_NOTIFICATIONS = "notifications"
    }


    // Shared Preferences
    var pref: SharedPreferences

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor? = null

    // Context
    var mContext: Context? = context

    // Shared pref mode
    var PRIVATE_MODE = 0


    init {
        pref = mContext!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun isUserAlreadyLogged(): Boolean {
        return !pref.getString(KEY_USER_EMAIL, "")!!.isEmpty()
    }

    /*
    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.commit();

        Timber.e("User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);

            User user = new User(id, name, email);
            return user;
        }
        return null;
    }*/

    fun addNotification(notification: String) {

        // get old notifications
        var oldNotifications = getNotifications()
        if (oldNotifications != null) {
            oldNotifications += "|$notification"
        } else {
            oldNotifications = notification
        }
        editor!!.putString(KEY_NOTIFICATIONS, oldNotifications)
        editor!!.commit()
    }

    fun getNotifications(): String? {
        return pref.getString(KEY_NOTIFICATIONS, null)
    }

    fun clear() {
        editor!!.clear()
        editor!!.commit()
    }
}