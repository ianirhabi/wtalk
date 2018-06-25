package com.example.irhabi.wtalk;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

     LinearLayout layout;
     RelativeLayout layout_2;
     ImageView sendButton;
     EditText messageArea;
     ScrollView scrollView;
     Firebase reference1, reference2;
    private MediaPlayer mp;
     SessionManager untukpemilik;
    String pengguna; // variabel ini akan digunakan untuk SessionManager

    String text, hasil, messageText, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fungsi_pesan();
    }

    public void addMessageBox(String message, int type){

        TextView textview = new TextView(MainActivity.this);
        textview.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        lp2.weight = 1.0f;

        if (type == 1){
            lp2.gravity = Gravity.LEFT;
            textview.setBackgroundResource(R.drawable.bubble_in);
        }
        else {
            lp2.gravity = Gravity.RIGHT;
            textview.setBackgroundResource(R.drawable.bubble_out);
            mainkan();
        }

        textview.setLayoutParams(lp2);
        layout.addView(textview);
    }

    public void mainkan(){
        mp = MediaPlayer.create(this, R.raw.notification);

        try{
            mp.prepare();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        mp.start();
    }

    public void notification (String pengguna, String pesan){
        Intent resultIntent = new Intent (this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent piResult = PendingIntent.getActivity(this,(int)
            Calendar.getInstance().getTimeInMillis(), resultIntent,0);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle(pengguna);
        inboxStyle.addLine("Lihat Pesan");
        inboxStyle.addLine(pesan);
        inboxStyle.setSummaryText("+2 more");

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(pengguna)
                .setContentText(pesan)
                .setStyle(inboxStyle)
                .addAction(R.drawable.ic_launcher_background, "buka", piResult);;

        NotificationManager notificationManager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
        notificationManager.notify (0, mBuilder.build());
    }

    public void fungsi_pesan(){

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        /**
         * mengambil session yang tersimpan di memori sharedpreferences
         */
        untukpemilik = new SessionManager(getApplicationContext());
        HashMap<String, String> penggunasesion = untukpemilik.getUserDetails();
        pengguna = penggunasesion.get(SessionManager.KEY_EMAIL);

        UserDetails.username = pengguna;


        Firebase.setAndroidContext(this);
        reference1 = new Firebase ("https://intalk-4dfe0.firebaseio.com/bisamu/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase ("https://intalk-4dfe0.firebaseio.com/bisamu/" + UserDetails.chatWith + "_" + UserDetails.username);

        scrollView.post(new Runnable (){
            @Override
            public void run() {scrollView.fullScroll(ScrollView.FOCUS_DOWN);}
        });

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                messageText="";
                 text = messageArea.getText().toString();

                //algoritma untuk enskript
                for(int i = 0; i < text.length(); i++){
                    int index = text.charAt(i);
                    char l = (char)(index-20);
                    messageText = messageText + String.valueOf(l);

                }

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String,  String>();
                    map.put("vragrig", messageText);//isps
                    map.put("girag", UserDetails.username);//pengna
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");// mengambalikan kolom di edit text menjadi tidak ada text
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue (Map.class);
                String messagee= map.get ("vragrig").toString();
                message = "";

                //algoritma untuk deskripsi
                for(int i = 0; i < messagee.length(); i++){
                    int index = messagee.charAt(i);
                    char l = (char)(index+20);
                   message = message + String.valueOf(l);
            }


                String userName = map.get("girag").toString();

                //fungsi waktu
                Calendar c1 = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:m:s a"); // menampilkan menit
                String strdate1 = sdf1.format(c1.getTime());
                SimpleDateFormat sdf2 = new SimpleDateFormat("d/M/yyyy"); // menampilkan tanggal
                String strdate2 = sdf2.format(c1.getTime());

                //jika di database sama dengan sesion user
                if (userName.equals(UserDetails.username)){
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    addMessageBox(strdate2 +"\n"+ "Anda :  " +""+ strdate1+"\n" + message, 1);
                }
                else{
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    addMessageBox(strdate2 +"\n"+ UserDetails.chatWith  +":  "+""+ strdate1 + "\n" + message, 2);
                    notification(UserDetails.chatWith, message);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    
}
