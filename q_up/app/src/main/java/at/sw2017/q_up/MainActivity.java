package at.sw2017.q_up;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    Button buttonLogin;
    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        editTextUsername = (EditText) findViewById(R.id.editText2);
        editTextUsername.setHint("hint");
        editTextPassword = (EditText) findViewById(R.id.editTextPasswort);




    }

    @Override
    public void onClick(View v) {
       Button clickedButton = (Button) v;
        if(editTextUsername.getText().toString().equals("admin")
                && editTextPassword.getText().toString().equals("1234")) {
            Toast.makeText(getApplicationContext(),
                    "supi", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "ohoh", Toast.LENGTH_SHORT).show();
        }
    }
}
