package tenyond.yank.com.carlocation.personinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import tenyond.yank.com.carlocation.R;

public class PersonInfoActivity extends AppCompatActivity {

    public TextView Money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        Money=findViewById(R.id.money);

        Money.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
// 用类名跳转，需要在AndroidManifest.xml中申明activity
                Intent intent = new Intent(PersonInfoActivity.this, wealthActivity.class);
                startActivity(intent);
            }
        });
    }

}


