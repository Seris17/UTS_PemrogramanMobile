package com.example.sigit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.sigit.adapter.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText TxEmail, TxUsername, TxPassword;
    Button BtnRegister;
    DBHelper dbHelper;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        TxEmail = (EditText)findViewById(R.id.txEmailReg);
        TxPassword = (EditText)findViewById(R.id.txPasswordReg);
        TxUsername = (EditText)findViewById(R.id.txUsernameReg);
        BtnRegister = (Button)findViewById(R.id.btnRegister);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.txEmailReg, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.txUsernameReg, RegexTemplate.NOT_EMPTY, R.string.invalid_username);
        awesomeValidation.addValidation(this, R.id.txPasswordReg, ".{6,}", R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.txPasswordReg, RegexTemplate.NOT_EMPTY, R.string.invalid_passw);



        TextView tvRegister = (TextView)findViewById(R.id.tvRegister);

        tvRegister.setText(fromHtml("Back to " +
                "</font><font color='#1DA1F2'>Login</font>"));

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });



        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = TxEmail.getText().toString().trim();
                String username = TxUsername.getText().toString().trim();
                String password = TxPassword.getText().toString().trim();

                ContentValues values = new ContentValues();

                //check validation
                if (awesomeValidation.validate()){
                    //on success
                    values.put(DBHelper.login_email, email);
                    values.put(DBHelper.login_username, username);
                    values.put(DBHelper.login_password, password);
                    dbHelper.insertData(DBHelper.table_login, values);

                    Toast.makeText(RegisterActivity.this, "Berhasil membuat akun, silahkan Login", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal membuat akun", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public static Spanned fromHtml(String html){
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}