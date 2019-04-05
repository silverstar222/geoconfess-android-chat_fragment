package txlabz.com.geoconfess.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.models.response.ErrorResponse;

/**
 * Created by Ivan on 17.5.2016..
 */
public class ErrorUtils {

    public static ErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, ErrorResponse> converter =
                GeoConfessApplication.getRetrofit()
                        .responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        ErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponse("Failure", "Something went wrong");
        }

        return error;
    }
}
