package com.riders.thelab.core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;
import com.riders.thelab.R;
import com.riders.thelab.core.views.toast.TheLabToast;
import com.riders.thelab.core.views.toast.ToastTypeEnum;
import com.riders.thelab.data.local.bean.SnackBarType;

import timber.log.Timber;

public class UIManager {

    private UIManager() {
    }

    /**
     * Hide the keyboard
     *
     * @param view
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Show an alertDialog
     *
     * @param
     * @param title
     * @param message
     * @param negativeMessage
     * @param positiveMessage
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void showAlertDialog(final Activity activity,
                                       final Context context,
                                       String title,
                                       String message,
                                       final String negativeMessage,
                                       final String positiveMessage) {
        Timber.i("Show alert dialog");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton(negativeMessage, (dialog, which) -> {
            UIManager.showActionInToast(context, negativeMessage);
            if (negativeMessage.equalsIgnoreCase("Réessayer")) {
                //launchActivity(context, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK, null, null);
            }
            if (negativeMessage.equalsIgnoreCase("Réessayer") && LabNetworkManager.isConnected(context)) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton(positiveMessage, (dialog, which) -> {
            UIManager.showActionInToast(context, positiveMessage);
            if (activity != null)
                activity.onBackPressed();
            if (negativeMessage.equalsIgnoreCase("Quitter")) {
                activity.finish();
            }

        });

        alertDialog.setCancelable(false);

        // Showing Alert Message
        alertDialog.show();
    }


    public static void showActionInToast(final Context context, final String textToShow) {
        Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
    }

    public static void showCustomToast(final Context context, ToastTypeEnum type, String message) {
        new TheLabToast.Builder(context)
                .setType(type)
                .setText(message)
                .show();
    }


    public static void showActionInSnackBar(final Context context, final View view, final String message, SnackBarType type) {
        // create instance
        Snackbar snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        /*switch (type){
            case NORMAL:
                // set action button color
                snackBar.setActionTextColor(context.getResources().getColor(R.color.indigo));
                break;

            case WARNING:
                snackBar.setActionTextColor(context.getResources().getColor(R.color.indigo));
                break;

            case ALERT:
                snackBar.setActionTextColor(context.getResources().getColor(R.color.indigo));
                break;
        }*/

        // get snackBar view
        View snackBarView = snackBar.getView();

        // change snackbar text color
        int snackBarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView) snackBarView.findViewById(snackBarTextId);
        switch (type) {
            case NORMAL:
                // set action button color
                textView.setTextColor(context.getResources().getColor(R.color.white));
                break;

            case WARNING:
                textView.setTextColor(context.getResources().getColor(R.color.warning));
                break;

            case ALERT:
                textView.setTextColor(context.getResources().getColor(R.color.error));
                break;
        }

        snackBar.show();

        /*
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setActionTextColor()
                .setAction("Action", null).show();
                */
    }


    // Showing the status in Snackbar
    public static void showConnectionStatusInSnackBar(Activity context, boolean isConnected) {
        Timber.d("showConnectionStatusInSnackBar()");

        String message;
        int backgroundColor;
        int textColor;

        if (isConnected) {
            message = "Good! Connected to Internet";
            backgroundColor = context.getResources().getColor(R.color.contactsDatabaseColorPrimaryDark);
            textColor = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            backgroundColor = context.getResources().getColor(R.color.locationColorPrimaryDark);
            textColor = Color.WHITE;
        }

        Snackbar snackbar =
                Snackbar.make(
                        context.findViewById(android.R.id.content),
                        message,
                        Snackbar.LENGTH_LONG);

        snackbar.setBackgroundTint(backgroundColor);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(textColor);
        snackbar.show();
    }

    /**
     * Returns the background color of a view (mostly requested the background of the root view)
     * <p>
     * source : https://stackoverflow.com/questions/14779259/get-background-color-of-a-layout
     *
     * @param xmlRootView
     * @return
     */
    public static int getDefaultBackgroundColor(View xmlRootView) {
        int color = Color.TRANSPARENT;
        Drawable background = xmlRootView.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        return color;
    }
}
