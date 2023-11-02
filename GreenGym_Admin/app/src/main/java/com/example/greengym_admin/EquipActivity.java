package com.example.greengym_admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EquipActivity extends AppCompatActivity {

    //listView 관련
    private ListView equip_list;
    private Equip_ListAdapter Equip_ListAdapter;
    private ArrayList<park_item> equip_item;
    private Button equip_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip);

        //액션바 제목
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("운동 기구 관리");

        equip_list = (ListView) findViewById(R.id.equip_list);
        equip_detail = (Button) findViewById(R.id.equip_detail);

        sendRequest();

        //자세히 버튼
        equip_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Equip_detail.class);
                int count = equip_list.getCheckedItemCount();
                if(count>0){
                    int c = equip_list.getCheckedItemPosition();
                    String id = equip_item.get(c).getPark_id();
                    intent.putExtra("selectid", id);
                    startActivity(intent);

                }else{
                    Toast.makeText(EquipActivity.this, "공원을 선택하세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void sendRequest(){

        String url = "http://15.164.250.186:8000/api/v1/manager_equip/parklist";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response){

                equip_item = new ArrayList<>();
                try {
                    JSONObject name = response.getJSONObject("response");
                    JSONArray equip = name.getJSONArray("List");

                    for(int i = 0; i <equip.length(); i++){
                        JSONObject chk = equip.getJSONObject(i);
                        String p_name = chk.getString("p_name");
                        String p_id = chk.getString("p_id");
                        equip_item.add(new park_item(p_name, p_id));
                    }
                    //리스트뷰-어댑터 연결
                    Equip_ListAdapter = new Equip_ListAdapter(EquipActivity.this, equip_item);
                    equip_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    equip_list.setAdapter(Equip_ListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error){
                String e = error.getMessage();
                Toast.makeText(EquipActivity.this, e, Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(jsonObjectRequest);

    }
}