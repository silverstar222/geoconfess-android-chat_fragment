package txlabz.com.geoconfess.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import java.net.URL;
import java.util.Date;

import txlabz.com.geoconfess.utils.AmazonUploadListener;
import txlabz.com.geoconfess.utils.GeneralUtility;

/**
 * Created by Ivan on 18.5.2016..
 */
public class AmazonImageUploader extends AsyncTask<Void, Void, String> {

    private static final String TAG = AmazonImageUploader.class.getSimpleName();
    private final AmazonUploadListener listener;
    String filePath;

    public AmazonImageUploader(String filePath, AmazonUploadListener listener) {
        this.filePath = filePath;
        this.listener = listener;
 }

    @Override
    protected String doInBackground(Void... params) {
        URL url = null;
        String urlStr = "";
        try {
            AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(GeneralUtility.AWS_ACCESS_KEY_ID, GeneralUtility.AWS_SECRET_ACCESS_ID));
            s3Client.createBucket(GeneralUtility.MY_PICTURE_BUCKET);
            String pictureName = "";

            pictureName = "image" + "-" + System.currentTimeMillis() + "/id.jpg";
            PutObjectRequest por = new PutObjectRequest(GeneralUtility.MY_PICTURE_BUCKET, pictureName, new java.io.File(filePath));
            s3Client.putObject(por);

            ResponseHeaderOverrides override = new ResponseHeaderOverrides();
            override.setContentType("image/jpeg");

            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(GeneralUtility.MY_PICTURE_BUCKET, pictureName);
            urlRequest.setExpiration(new Date(System.currentTimeMillis() + (63115200 * 10)));  // Added an hour's worth of milliseconds to the current time.
            urlRequest.setResponseHeaders(override);

            url = s3Client.generatePresignedUrl(urlRequest);


            urlStr = url.toURI().toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlStr;

    }
    @Override
    protected void onPostExecute(String obj) {
        super.onPostExecute(obj);
        Log.i(TAG, "amazon image url: " + obj);
        listener.imageUploaded(obj);
    }
}
