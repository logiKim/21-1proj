package com.example.greengym_admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ReportActivity extends AppCompatActivity {

    //listView 관련
    private ListView report_list;
    private ListAdapter ListAdapter;
    private ArrayList<item> item;
    private Button detail;
    private Button delete;

    //dialog 관련
    private TextView date_dialog;
    private TextView park_dialog;
    private TextView name_dialog;
    private TextView phone_dialog;
    private TextView content_dialog;
    private Button cancel;
    private Button report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //액션바 제목
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("신고 관리");

        //통신 + 리스트뷰 실행
        sendRequest();

        //listView 관련
        report_list = (ListView) findViewById(R.id.report_list);
        detail = (Button) findViewById(R.id.detail);
        delete = (Button) findViewById(R.id.delete);

        //자세히 버튼
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = report_list.getCheckedItemCount();;
                if(count > 0){
                    //dialog 생성
                    View dialogView = getLayoutInflater().inflate(R.layout.activity_dialog, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setView(dialogView);
                    final AlertDialog detail_dialog = builder.create();
                    detail_dialog.setCancelable(false);
                    detail_dialog.show();

                    date_dialog = (TextView) dialogView.findViewById(R.id.date_dialog);
                    park_dialog = (TextView) dialogView.findViewById(R.id.park_dialog);
                    name_dialog = (TextView) dialogView.findViewById(R.id.name_dialog);
                    phone_dialog = (TextView) dialogView.findViewById(R.id.phone_dialog);
                    content_dialog = (TextView) dialogView.findViewById(R.id.content_dialog);
                    cancel = (Button) dialogView.findViewById(R.id.cancel);
                    report = (Button) dialogView.findViewById(R.id.report);


                    int c = report_list.getCheckedItemPosition();
                    date_dialog.setText(item.get(c).getR_date());
                    park_dialog.setText(item.get(c).getP_name());
                    name_dialog.setText(item.get(c).getR_name());
                    phone_dialog.setText(item.get(c).getR_phone());
                    content_dialog.setText(item.get(c).getR_text());


                    //dialog 닫기 버튼
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            detail_dialog.dismiss();
                        }
                    });

                    //dialog 신고 버튼
                    report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int c = report_list.getCheckedItemPosition();
                            String id = item.get(c).getR_id();
                            urlRequest(id);
                        }
                    });


                }else {
                    Toast.makeText(ReportActivity.this, "공원을 선택하세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //삭제 버튼
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder list_dialog = new AlertDialog.Builder(ReportActivity.this);
                list_dialog.setTitle("신고 삭제");
                list_dialog.setMessage("정말로 삭제하시겠습니까?");

                list_dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //**** 데이터 삭제 시 DB에도 없어지도록 해야합니다.

                        int count, checked;
                        count = ListAdapter.getCount();

                        if(count > 0){
                            checked = report_list.getCheckedItemPosition();
                            if(checked > -1 && checked < count){
                                String id = item.get(checked).getR_id();
                                sendDelete(id);
                                item.remove(checked);
                                report_list.clearChoices();
                                ListAdapter.notifyDataSetChanged();
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
    public void sendRequest(){

        String url = "http://15.164.250.186:8000/api/v1/manager_report/list";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response){
                try {
                    JSONObject name = response.getJSONObject("response");
                    JSONArray report = name.getJSONArray("List");

                    item =new ArrayList<>();
                    for(int i = 0; i <report.length(); i++){
                        JSONObject dtl = report.getJSONObject(i);

                        String pk = dtl.getString("p_name");
                        String dt = dtl.getString("r_date");
                        String x = dtl.getString("r_id");
                        String t = dtl.getString("r_text");
                        String phone = dtl.getString("r_phone");
                        String n = dtl.getString("r_name");
                        item.add(new item(x, pk, t, phone, n, dt));
                    }

                    //리스트뷰-어댑터 연결
                    ListAdapter = new ListAdapter(ReportActivity.this, item);
                    report_list.setChoiceMode(report_list.CHOICE_MODE_SINGLE);
                    report_list.setAdapter(ListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error){
                String e = error.getMessage();
                Toast.makeText(ReportActivity.this, e, Toast.LENGTH_SHORT).show();

            }
        });

        jsonObjectRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(jsonObjectRequest);

    }

    public void sendDelete(String id){

        String url = String.format("http://15.164.250.186:8000/api/v1/manager_report/delete?r_id=%s",id);

        StringRequest StringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){
                Toast.makeText(ReportActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error){
                String e = error.getMessage();
                Toast.makeText(ReportActivity.this, e, Toast.LENGTH_SHORT).show();
            }
        });

        StringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(StringRequest);

    }
    public void urlRequest(String id){

        String url = String.format("http://15.164.250.186:8000/api/v1/manager_report/website?r_id=%s",id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response){
                try {
                    JSONObject name = response.getJSONObject("response");
                    JSONObject web = name.getJSONObject("detail");
                    String weburl = web.getString("website");

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(weburl));
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error){
                String e = error.getMessage();
                Toast.makeText(ReportActivity.this, e, Toast.LENGTH_SHORT).show();

            }
        });

        jsonObjectRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(jsonObjectRequest);
    }



}