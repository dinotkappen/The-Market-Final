package com.the.marketapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

public class ThankYouActivity extends AppCompatActivity {
    TextView txtmsg;
    String orderId;
    Utilities utilities;
    ImageView thankyouImg;
    LinearLayout mainlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_thank_you);
                txtmsg = (TextView) findViewById(R.id.txtMsg);
                thankyouImg=(ImageView)findViewById(R.id.thankyouImg);
                mainlayout=(LinearLayout)findViewById(R.id.mainlayout);
                Intent in = getIntent();
                orderId = in.getStringExtra("orderId");
                if (orderId != null && !orderId.isEmpty() && !orderId.equals("null")) {
                    txtmsg.setText("Thank you for shopping with us Your order id is " + orderId);
                }

                mainlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ThankYouActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            } else {
                setContentView(R.layout.msg_no_internet);
            }
        } catch (Exception ex) {
            Log.v("OrderSummary", ex.getMessage().toString());
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent=new Intent(ThankYouActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        // your code.
    }

}
