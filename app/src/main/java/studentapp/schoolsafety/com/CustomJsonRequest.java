package studentapp.schoolsafety.com;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by yasinyaqoobi on 10/6/16.
 */

public class CustomJsonRequest<T> extends JsonRequest<JSONObject> {

    private JSONArray mRequestArray;
    private Response.Listener<JSONObject> mResponseListener;

    public CustomJsonRequest(int method, String url, JSONArray requestArray, Response.Listener<JSONObject> responseListener,  Response.ErrorListener errorListener) {
        super(method, url, (requestArray == null) ? null : requestArray.toString(), responseListener, errorListener);
        mRequestArray = requestArray;
        mResponseListener = responseListener;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mResponseListener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            try
            {
                return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e)
            {
                return Response.error(new ParseError(e));
            }
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
