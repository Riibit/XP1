package at.sw2017.q_up;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
       Button clickedButton = (Button) v;

        Toast.makeText(getApplicationContext(),
                "supi",Toast.LENGTH_SHORT).show();
    }
}
