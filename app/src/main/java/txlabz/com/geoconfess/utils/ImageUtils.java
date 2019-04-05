package txlabz.com.geoconfess.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by Ivan on 27.4.2016..
 */
public class ImageUtils {

    public static String TAG = ImageUtils.class.getSimpleName();


    /**
     * Used to scale an existing image located on originalUri.getPath().
     * Creates a temporary file in the same folder as the original image,
     * in which the converted Bitmap is saved.
     * Overwrites the original with the downscaled image upon completion.
     *
     * @param originalUri File containing the original image
     * @param reqWidth    width to which to scale the image
     * @param reqHeight   height to which to scale the image
     * @param listener
     */
    public static void scaleImage(final Uri originalUri, final int reqWidth, final int reqHeight, final ImageResizeListener listener) {
        final File original = new File(originalUri.getPath());
        final File temporary = new File(original + "_temp");
        Log.d(TAG, "the original image file is: " + original);
        Log.d(TAG, "the destination for image file is: " + temporary);

        AsyncTask<Object, Void, Boolean> asyncTask = new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                try {
                    Log.i(TAG, "scaleImage: decoding original file: " + original.getName());
                    Bitmap bitmap = BitmapFactory.decodeFile(originalUri.getPath());

                    FileOutputStream out = new FileOutputStream(temporary);

                    ExifInterface exifInterface = new ExifInterface(original.getAbsolutePath());
                    Log.i(TAG, "run: " + (exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)));

                    Log.i(TAG, "scaleImage: scaling original file:" + original.getName());

                    if (bitmap.getHeight() < bitmap.getWidth()) {
                        Log.i(TAG, "run: landscape");
                        bitmap = Bitmap.createScaledBitmap(bitmap, reqHeight, reqWidth, true);
                    } else {
                        Log.i(TAG, "run: portrait");
                        bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
                    }

                    Log.i(TAG, "scaleImage: compressing original to file: " + temporary.getName());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    Log.i(TAG, "scaleImage: closing output stream");
                    out.flush();
                    out.close();

                    // overwrite saved photo with temp photo
                    return temporary.renameTo(original);

                } catch (Exception e) {
                    Log.e(TAG, "scaleImage: ERROR:" + e.toString());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                Log.i(TAG, "scaleImage: overwriting original with temporary. successfully - " + aBoolean);
                if (listener != null) {
                    listener.resizingDone();
                }
            }
        };
        asyncTask.execute(new Void[]{}, new Void[]{}, new Void[]{});
    }

    /**
     * Create a file Uri for saving an image
     *
     * @return Uri with image storage path
     */
    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "confess_profile.png"));
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static String convertImageToBase64(File file) {
        Log.i(TAG, "file to convert to base 64 string: " + file.getAbsolutePath());
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static File convertStringBase64ToFile(Context context, String imageBase64) {
        byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        File imageFile = new File(context.getCacheDir(), "profile - " + Calendar.getInstance().getTime().getTime());
        try {
            imageFile.createNewFile();


//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

}
