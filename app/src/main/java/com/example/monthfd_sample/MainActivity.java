package com.example.monthfd_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.InputStream;
import java.io.InputStreamReader;

//import android.app.Activity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText edit_year;
    EditText edit_month;
    TextView text;

    XmlPullParser xpp;
    String key= "20200601APQS10O3HXOQEZR7RH0TKG";
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_year = (EditText)findViewById(R.id.edit_year);
        edit_month = (EditText)findViewById(R.id.edit_month);
        text = (TextView)findViewById(R.id.result);
    }
    public void mOnClick(View v){
        switch( v.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data= getXmlData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    String getXmlData(){
        StringBuffer buffer = new StringBuffer();

        String str_year = edit_year.getText().toString();
        String str_month = edit_month.getText().toString();
        String location_year = URLEncoder.encode(str_year);
        String location_month = URLEncoder.encode(str_month);

        String query= "";

        String queryUrl = "http://api.nongsaro.go.kr/service/monthFd/monthFdmtLst?"
                +"apiKey="+key
                +"&thisYear="+location_year
                +"&thisMonth="+location_month;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();

            int eventType = xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작..\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if(tag.equals("item"));
                        else if(tag.equals("fdmtNm")){//이달의 음식 태그 이름 "fdmtNm"
                            buffer.append("음식명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if(tag.equals("item")) buffer.append("\n");
                        break;
                }
                eventType=xpp.next();
            }

        } catch (Exception e) {

        }
        buffer.append("파싱 끝\n");
        return buffer.toString();

    }
}
