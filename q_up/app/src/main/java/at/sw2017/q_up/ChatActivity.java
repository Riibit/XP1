package at.sw2017.q_up;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    private TextView inputView;

    View.OnKeyListener myKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View arg0, int actionID, KeyEvent event) {
            // TODO: do what you got to do
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (actionID == KeyEvent.KEYCODE_ENTER)) {
                FloatingActionButton click = (FloatingActionButton)findViewById(R.id.fab);
                click.performClick();
                inputView.setSingleLine(true);

            }
            return false;
        }
    };


    private void sendMessageToDatabase() {
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);

                // avoid sending empty messages
                if (!input.getText().toString().equals("")) {
                    Place p = PlaceDetails.getCurrentPlace();
                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance().getReference("chats").child(p.placeId).push()
                            .setValue(new ChatMessage(input.getText().toString(), MainActivity.getUser().userName)
                            );

                    // Clear the input
                    input.setText("");

                }
            }
        });
    }

public void UpdateInput()
{


    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(10);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            inputView.setSingleLine(false);

                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };
        t.start();

}



    private void displayChatMessages() {
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        Place p = PlaceDetails.getCurrentPlace();

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference("chats").child(p.placeId)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendMessageToDatabase();
        displayChatMessages();
        inputView = (TextView) findViewById(R.id.input);
        inputView.setOnKeyListener(myKeyListener);
        UpdateInput();
    }
}
