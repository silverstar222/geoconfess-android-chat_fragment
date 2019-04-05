package txlabz.com.geoconfess.activities;

/**
 * Created by Ivan on 26.4.2016..
 */

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.dialogs.SingleChoiceDialog;
import txlabz.com.geoconfess.utils.ImageResizeListener;
import txlabz.com.geoconfess.utils.ImageUtils;

/**
 * Created by Ivan on 7.5.2015..
 */
public abstract class ImageChooseActivity extends BaseActivity {

    private static final String TAG = ImageChooseActivity.class.getSimpleName();

    private static final int SCALED_IMAGE_HEIGHT = 800;
    private static final int SCALED_IMAGE_WIDTH = 600;
    private static final int PICK_IMAGE = 111;
    private static final int REQUEST_IMAGE_CAPTURE = 222;
    private static final int PERMISSION_REQUEST = 212;
    private static Uri mCapturedImageURI;

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void showPrompt() {
        if (!checkPermissions()) {
            SingleChoiceDialog frag = SingleChoiceDialog.newInstance(
                    R.array.options_choose_image, new SingleChoiceDialog.OnSelectedListener() {
                        @Override
                        public void onSelected(String selected) {
                            String[] values = getResources().
                                    getStringArray(R.array.options_choose_image);
                            if (selected.equals(values[0])) {
                                takePhoto();
                            } else if (selected.equals(values[1])) {
                                pickFromGallery();
                            }
                        }
                    });
            frag.show(this.getSupportFragmentManager(), "attach");
        }
    }

    public void pickFromGallery() {
        mCapturedImageURI = ImageUtils.getOutputMediaFileUri();

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), PICK_IMAGE);
    }

    public void takePhoto() {


        mCapturedImageURI = ImageUtils.getOutputMediaFileUri();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                String filePath = getPath(this, data.getData());
                Log.i(TAG, "filePath " + filePath);
                File newImage = new File(mCapturedImageURI.getPath());
                try {
                    ImageUtils.copy(new File(filePath), newImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageResizeListener listener = new ImageResizeListener() {
                    @Override
                    public void resizingDone() {
                        onMediaChosen(new File(mCapturedImageURI.getPath()));
                    }
                };
                ImageUtils.scaleImage(mCapturedImageURI, SCALED_IMAGE_WIDTH, SCALED_IMAGE_HEIGHT, listener);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                String filePath = getPath(this, mCapturedImageURI);
                Log.i(TAG, "filePath " + filePath);
                ImageResizeListener listener = new ImageResizeListener() {
                    @Override
                    public void resizingDone() {
                        onMediaChosen(new File(mCapturedImageURI.getPath()));
                    }
                };
                ImageUtils.scaleImage(mCapturedImageURI, SCALED_IMAGE_WIDTH, SCALED_IMAGE_HEIGHT, listener);
            }
        }
    }

    public abstract void onMediaChosen(File imageFile);

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
            }
            return true;

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
                return true;
            } else {
                return false;
            }
        }
    }
}
