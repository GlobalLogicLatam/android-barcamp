package com.globallogic.barcamp.asyntask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gustavo on 9/20/16
 */
public class UserDataFromFacebookTask extends AsyncTask<AccessToken, Void, Void> {

    private static final String TAG = UserDataFromFacebookTask.class.getSimpleName();

    private String emailId;
    private String fbId;
    private OnFacebookLoginListener mLoginListener;

    public interface OnFacebookLoginListener {
        void onShowWaitDialog();

        void onDismissWaitDialog();

        void onFacebookLogin(String email, String fbId);
    }

    public UserDataFromFacebookTask(OnFacebookLoginListener listener) {
        this.mLoginListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoginListener.onShowWaitDialog();
    }

    @Override
    protected Void doInBackground(AccessToken... params) {
        GraphRequest request = GraphRequest.newMeRequest(params[0],
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        Log.v("LoginActivity", response.toString());
                        try {
                            emailId = object.getString("email");
                            fbId = object.getString("id");
                        } catch (JSONException e) {
                            Log.w(TAG, "Error getting facebook data", e);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAndWait();

        return null;
    }

    @Override
    protected void onPostExecute(Void data) {
        mLoginListener.onDismissWaitDialog();
        mLoginListener.onFacebookLogin(emailId, fbId);
    }


}
