package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ivan on 17.5.2016..
 */
public class ErrorResponse {

    @SerializedName("result")
    private String result;
    @SerializedName("errors")
    private Object errors;
    @SerializedName("error")
    private String error;
    @SerializedName("error_description")
    private String errorDescription;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getResult() {
        return result;
    }

    public String getErrors() {
        StringBuilder msg = new StringBuilder();

        if (errors != null) {
            LinkedTreeMap<String, ArrayList<String>> errorResponse = (LinkedTreeMap<String, ArrayList<String>>) errors;
            for (Map.Entry<String, ArrayList<String>> entry : errorResponse.entrySet()) {
                msg.append(entry.getKey() + "-");
                for (String reason : entry.getValue()) {
                    msg.append(reason);
                    if (entry.getValue().indexOf(reason) == entry.getValue().size() - 1) {
                        msg.append(". ");
                    }
                }
            }
        } else if (error != null && errorDescription != null) {
            msg.append(errorDescription);
        } else {
            msg.append("Something went wrong");
        }
        return msg.toString();
    }
}
