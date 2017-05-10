package at.sw2017.q_up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle bundle = getIntent().getExtras();
        String title  =  "Information of " + bundle.getString("title");
        TextView txtViewTitle = (TextView) findViewById(R.id.txtView_title_info);
        txtViewTitle.setText(title);

    }
}
