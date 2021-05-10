package com.riders.thelab.core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.riders.thelab.R;
import com.riders.thelab.core.views.toast.TheLabToast;
import com.riders.thelab.core.views.toast.ToastTypeEnum;
import com.riders.thelab.data.local.bean.SnackBarType;

import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

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
            if (negativeMessage.equalsIgnoreCase("Réessayer")
                    && LabNetworkManager.isConnected(context)) {
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

    public static void showActionInSnackBar(final Activity context, final View view,
                                            final String message, final SnackBarType type,
                                            @Nullable final String actionText,
                                            @Nullable final View.OnClickListener listener) {
        // create instance
        Snackbar snackBar =
                Snackbar.make(
                        context.findViewById(android.R.id.content),
                        message,
                        listener != null
                                ? BaseTransientBottomBar.LENGTH_INDEFINITE
                                : BaseTransientBottomBar.LENGTH_LONG);

        snackBar.setBackgroundTint(ContextCompat.getColor(context, type.getBackgroundColor()));

        // get snackBar view
        View snackBarView = snackBar.getView();

        // change snackBar text color
        int snackBarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView) snackBarView.findViewById(snackBarTextId);
        // set action text color
        textView.setTextColor(ContextCompat.getColor(context, type.getTextColor()));

        if (null != actionText && null != listener) {

            // change snackBar button text color
            int snackBarButtonId = com.google.android.material.R.id.snackbar_action;
            Button buttonView = (Button) snackBarView.findViewById(snackBarButtonId);
            // set action text color
            buttonView.setBackgroundColor(ContextCompat.getColor(context, type.getTextColor()));

            snackBar.setAction(actionText, listener);
        }

        snackBar.show();
    }


    // Showing the status in Snackbar
    public static void showConnectionStatusInSnackBar(Activity context, boolean isConnected) {
        Timber.d("showConnectionStatusInSnackBar()");

        Snackbar snackbar =
                Snackbar.make(
                        context.findViewById(android.R.id.content),
                        context.getString(
                                !isConnected
                                        ? R.string.network_status_disconnected
                                        : R.string.network_status_connected),
                        BaseTransientBottomBar.LENGTH_LONG);

        snackbar.setBackgroundTint(
                ContextCompat.getColor(
                        context,
                        !isConnected
                                ? R.color.locationColorPrimaryDark
                                : R.color.contactsDatabaseColorPrimaryDark));

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
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

    /**
     * Reference : https://stackoverflow.com/questions/37775675/imageview-set-color-filter-to-gradient
     *
     * @param originalBitmap
     * @return
     */
    public static Bitmap addGradientToImageView(Context context, Bitmap originalBitmap) {
        final int width = originalBitmap.getWidth();
        final int height = originalBitmap.getHeight();
        final Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(updatedBitmap);

        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();

        final int[] colors = {
                ContextCompat.getColor(context, R.color.admin_splash_bg),
                ContextCompat.getColor(context, R.color.adminDashboardColorPrimary),
                ContextCompat.getColor(context, R.color.adminDashboardSelectedItemAccent),
                ContextCompat.getColor(context, R.color.multiPaneColorPrimaryDark),
        };

        final LinearGradient shader =
                new LinearGradient(
                        0, 0,
                        0, height,
                        colors,
                        null,
                        Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    public static void loadImageBlurred(final Context context,
                                        final Object imageURL,
                                        final ShapeableImageView targetImageView) {
        //Load the background  thumb image
        Glide.with(context)
                .load(imageURL)
                .apply(bitmapTransform(new BlurTransformation(5, 5)))
                .into(targetImageView);
    }
}
