package com.riders.thelab.core.utils

import android.content.Context
import android.content.SharedPreferences

class LabPreferenceManager(context: Context) {

    // Shared Preferences
    private var pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Editor for Shared preferences
    private var editor: SharedPreferences.Editor? = null


    init {
        editor = pref.edit()
    }

    fun isUserAlreadyLogged(): Boolean = pref.getString(KEY_USER_EMAIL, "")!!.isNotEmpty()


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

    private fun getNotifications(): String? {
        return pref.getString(KEY_NOTIFICATIONS, null)
    }

    fun clear() {
        editor!!.clear()
        editor!!.commit()
    }


    companion object {

        // Sharedpref file name
        private const val PREF_NAME: String = "lab_pref"

        // All Shared Preferences Keys
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_NOTIFICATIONS = "notifications"
    }

}