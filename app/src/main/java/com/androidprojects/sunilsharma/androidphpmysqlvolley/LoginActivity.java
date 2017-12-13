package com.androidprojects.sunilsharma.androidphpmysqlvolley;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://api.leconseals.in/api/login";
    private static final int TAG_LoginStatus = 1;
    private static final String TAG_MESSAGE = "message";

    List<User> userList;


    EditText edtloginid;
    EditText edtLoginPassword;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtloginid = (EditText) findViewById(R.id.edtloginid);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);
        btnlogin = (Button) findViewById(R.id.btnlogin);

        userList = new ArrayList<>();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        final String loginid = edtloginid.getText().toString();
        final String LoginPassword = edtLoginPassword.getText().toString();
        


        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("User", response.toString());

                    for (int i = 0 ; i<jsonArray.length() ; i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        User user = new User();
                        user.setId(jsonObject.getString("$id"));
                        user.setEmpType(jsonObject.getString("EmpType"));
                        user.setLoginID(jsonObject.getString("LoginID"));
                        user.setEmp_Name(jsonObject.getString("Emp_Name"));
                        user.setEmp_Dept(jsonObject.getString("Emp_Dept"));
                        user.setLoginStatus(jsonObject.getInt("LoginStatus"));
                        user.setUser_Message(jsonObject.getString("User_Message"));

                        userList.add(user);
                        if(user.getLoginStatus() == 1)
                            startActivity(new Intent(LoginActivity.this , SuccessActivity.class));
                        else if (user.getLoginStatus() == 0)
                            Toast.makeText(LoginActivity.this, "Sorry Sir", Toast.LENGTH_SHORT).show();

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("loginid", loginid);
                params.put("LoginPassword", LoginPassword);
                return params;
            }


        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}

