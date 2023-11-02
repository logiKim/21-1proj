package com.example.greengym_admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Equip_detail extends AppCompatActivity {

    //listView 관련
    private ListView equipDetail_list;
    private Equip_detailAdapter equip_detailAdapter;
    private ArrayList<equip_item> equipDetail_item;
    private Button equip_add;
    private Button equip_delete;
    private Spinner equip_spinner;
    private Spinner category_spinner;
    private Button equip_cancel;
    private Button real_add;
    private String selectid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_detail);

        //액션바 제목
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("상세 정보");

        equipDetail_list = (ListView) findViewById(R.id.equipDetail_list);
        equip_add = (Button) findViewById(R.id.equip_add);
        equip_delete = (Button) findViewById(R.id.equip_delete);

        //선택 공원 id
        Intent intent = getIntent();
        selectid = intent.getExtras().getString("selectid");
        //통신 - 리스트뷰
        sendRequest(selectid);

        //운동기구 추가 버튼
        equip_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog 생성
                View dialogView = getLayoutInflater().inflate(R.layout.equip_spinner, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(dialogView);
                final AlertDialog add_dialog = builder.create();
                add_dialog.setCancelable(false);
                add_dialog.show();

                equip_cancel = (Button) dialogView.findViewById(R.id.equip_cancel);
                real_add = (Button) dialogView.findViewById(R.id.real_add);

                //기구 스피너
                ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(Equip_detail.this, R.array.equip_add_listArray, android.R.layout.simple_spinner_dropdown_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                equip_spinner = (Spinner) dialogView.findViewById(R.id.equip_spinner);
                equip_spinner.setAdapter(spinnerAdapter);

                //카테고리 스피너
                ArrayAdapter spinnerAdapter_c = ArrayAdapter.createFromResource(Equip_detail.this, R.array.category_add_ListArray, android.R.layout.simple_spinner_dropdown_item);
                spinnerAdapter_c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category_spinner = dialogView.findViewById(R.id.category_spinner);
                category_spinner.setAdapter(spinnerAdapter_c);


                //dialog 닫기 버튼
                equip_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_dialog.dismiss();
                    }
                });

                //dialog 추가 버튼
                real_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //카테고리 선택 값 숫자로
                        int pick = category_spinner.getSelectedItemPosition()+1;
                        String pick_n = String.valueOf(pick);
                        String e_name = equip_spinner.getSelectedItem().toString();
                        sendInsert(selectid, e_name, pick_n);

                        equipDetail_item.add(new equip_item(e_name, selectid));
                        equipDetail_list.clearChoices();
                        equip_detailAdapter.notifyDataSetChanged();

                        add_dialog.dismiss();
                    }
                });
            }
        });

        //운동기구 삭제 버튼
        equip_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder list_dialog = new AlertDialog.Builder(Equip_detail.this);
                list_dialog.setTitle("운동기구 삭제");
                list_dialog.setMessage("정말로 삭제하시겠습니까?");

                list_dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count, checked;
                        count = equip_detailAdapter.getCount();

                        if(count > 0){
                            checked = equipDetail_list.getCheckedItemPosition();
                            if(checked > -1 && checked < count){
                                String id = equipDetail_item.get(checked).getE_id();
                                sendDelete(id);

                                equipDetail_item.remove(checked);
                                equipDetail_list.clearChoices();
                                equip_detailAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

                list_dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                list_dialog.setCancelable(false);
                list_dialog.show();
            }
        });
    }
    public void sendRequest(String id){

        String url = String.format("http://15.164.250.186:8000/api/v1/manager_equip/p_e_list?id=%s", id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response){

                try {
                    JSONObject name = response.getJSONObject("response");
                    JSONArray equip = name.getJSONArray("List");

                    equipDetail_item = new ArrayList<>();
                    for(int i = 0; i <equip.length(); i++){
                        JSONObject chk = equip.getJSONObject(i);
                        String e_name = chk.getString("e_name");
                        String e_id = chk.getString("e_id");
                        equipDetail_item.add(new equip_item(e_name, e_id));
                    }
                    //리스트뷰-어댑터 연결
                    equip_detailAdapter = new Equip_detailAdapter(Equip_detail.this, equipDetail_item);
                    equipDetail_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    equipDetail_list.setAdapter(equip_detailAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error){
                String e = error.getMessage();
                Toast.makeText(Equip_detail.this, e, Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(jsonObjectRequest);

    }

    public void sendDelete(String id){

        String url = String.format("http://15.164.250.186:8000/api/v1/manager_equip/delete?e_id=%s",id);

        StringRequest StringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Toast.makeText(Equip_detail.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error){
                String e = error.getMessage();
                Toast.makeText(Equip_detail.this, e, Toast.LENGTH_SHORT).show();
            }
        });

        StringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(StringRequest);

    }
    public void sendInsert(String p_id, String e_name, String cate){

        String url = String.format("http://15.164.250.186:8000/api/v1/manager_equip/insert");

        StringRequest StringRequest = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Toast.makeText(Equip_detail.this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                String e = error.getMessage();
                Toast.makeText(Equip_detail.this, p_id+e_name+cate, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override //Post방식
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("p_id", p_id);
                params.put("e_name", e_name);
                params.put("category", cate);
                return params;
            }
        };
        StringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(StringRequest);

    }
}