package com.riders.thelab.core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.riders.thelab.R;
import com.riders.thelab.data.local.bean.SnackBarType;

public class UIManager {

    private UIManager() {
    }

    /**
     * Hide the keyboard
     *
     * @param view
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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
    public static void showAlertDialog(final Activity activity,
                                       final Context context,
                                       String title,
                                       String message,
                                       final String negativeMessage,
                                       final String positiveMessage) {
        Log.i("Activity - AlertDialog", "Show alert dialog");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UIManager.showActionInToast(context, negativeMessage);
                if (negativeMessage.equalsIgnoreCase("Réessayer")) {
                    //launchActivity(context, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK, null, null);
                }
                if (negativeMessage.equalsIgnoreCase("Réessayer") && LabNetworkManager.isConnected(context)) {
                    dialog.dismiss();
                }
            }
        });
        alertDialog.setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UIManager.showActionInToast(context, positiveMessage);
                if (activity != null)
                    activity.onBackPressed();
                if (negativeMessage.equalsIgnoreCase("Quitter")) {
                    activity.finish();
                }

            }
        });

        alertDialog.setCancelable(false);

        // Showing Alert Message
        alertDialog.show();
    }


    public static void showActionInToast(Context context, String textToShow) {
        Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
    }

    public static void showActionInSnackBar(Context context, View view, String message, SnackBarType type) {
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
}
