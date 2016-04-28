package edu.njit.stella.smartbus;

/**
 * Created by stella on 4/1/16.
 */


        import android.app.AlertDialog;
        import android.app.AlertDialog.Builder;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.DialogInterface.OnClickListener;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.provider.Settings.Secure;
        import android.support.v7.app.AppCompatActivity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import edu.njit.stella.smartbus.R;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.Timer;
        import java.util.TimerTask;

        import ch.boye.httpclientandroidlib.HttpEntity;
        import ch.boye.httpclientandroidlib.NameValuePair;
        import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
        import ch.boye.httpclientandroidlib.client.methods.CloseableHttpResponse;
        import ch.boye.httpclientandroidlib.client.methods.HttpPost;
        import ch.boye.httpclientandroidlib.impl.client.CloseableHttpClient;
        import ch.boye.httpclientandroidlib.impl.client.HttpClients;
        import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

//


/**
 * Login Activity
 */
public class Login extends AppCompatActivity {

    static int oroutePosition = 1;
    static int droutePosition = 1;
    final Context context = this;
    ArrayAdapter<String> orouteAdapter = null;
    ArrayAdapter<String> odirAdapter = null;
    ArrayAdapter<String> ostopAdapter = null;
    ArrayAdapter<String> drouteAdapter = null;
    ArrayAdapter<String> ddirAdapter = null;
    ArrayAdapter<String> dstopAdapter = null;
    String oroute = null;
    String odir = null;
    String ostop = null;
    String droute = null;
    String ddir = null;
    String dstop = null;



    private boolean btnReqFlag = false;
    private Button mBtnLgn = null;
    private Button mBtnDly = null;
    private Button mBtnReq = null;

    private Button mBtnRc = null;
    private Spinner mORoute = null;
    private Spinner mOStop = null;
    private Spinner mODir = null;
    private Spinner mDRoute = null;
    private Spinner mDStop = null;
    private Spinner mDDir = null;

    private AlertDialog dialog;

    private String[] soroute = new String[]{"M5", "M20", "M21"};

    private String[][] sodir = new String[][]
            {
                    { "N", "S" },
                    { "N" },
                    { "E", "W" }
            };

    private String[][][] sostop = new String[][][]
            {
                    {
                            {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"},
                            {"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65"}
                    },
                    {
                            {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}
                    },
                    {
                            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"},
                            {"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"}
                    }
            };

    private String[] sdroute = new String[]{"M5", "M20", "M21"};

    private String[][] sddir = new String[][]
            {
                    {"N", "S"},
                    {"N"},
                    {"E", "W"}
            };

    private String[][][] sdstop = new String[][][]
            {
                    {
                            {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"},
                            {"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65"}
                    },
                    {
                            {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}
                    },
                    {
                            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"},
                            {"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"}
                    }
            };

    private String urlinput = "http://***.***.***.***:8080/SmartBus/TestInput";
    private String urlhold = "http://***.***.***.***:8080/SmartBus/TestHoldQuery";
    private String urlquery = "http://***.***.***.***:8080/SmartBus/TestQuery";
    private String urlrc = "http://***.***.***.***:8080/SmartBus/TestRoute";
    private String android_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        android_id = Secure.getString(Login.this.getContentResolver(), Secure.ANDROID_ID);
        System.out.println(android_id);
        setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initView() {
        mORoute = (Spinner) findViewById(R.id.oRoute);
        mODir = (Spinner) findViewById(R.id.oDir);
        mOStop = (Spinner) findViewById(R.id.oStop);

        mDRoute = (Spinner) findViewById(R.id.dRoute);
        mDDir = (Spinner) findViewById(R.id.dDir);
        mDStop = (Spinner) findViewById(R.id.dStop);

        orouteAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, soroute);
        mORoute.setAdapter(orouteAdapter);
        mORoute.setSelection(1, true);

        drouteAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sdroute);
        mDRoute.setAdapter(drouteAdapter);
        mDRoute.setSelection(1, true);

        odirAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sodir[1]);
        mODir.setAdapter(odirAdapter);
        mODir.setSelection(0, true);

        ddirAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sddir[1]);
        mDDir.setAdapter(ddirAdapter);
        mDDir.setSelection(0, true);

        ostopAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sostop[1][0]);
        mOStop.setAdapter(ostopAdapter);
        mOStop.setSelection(0, true);

        dstopAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_spinner_item, sdstop[1][0]);
        mDStop.setAdapter(dstopAdapter);
        mDStop.setSelection(0, true);



        mBtnLgn = (Button) findViewById(R.id.btnLgn);
        mBtnDly = (Button) findViewById(R.id.btnDly);
        mBtnReq = (Button) findViewById(R.id.btnReq);
        mBtnReq.setEnabled(false);

        mBtnRc = (Button) findViewById(R.id.btnRc);


        mORoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                odirAdapter = new ArrayAdapter<String>(
                        Login.this, android.R.layout.simple_spinner_item, sodir[position]);

                mODir.setAdapter(odirAdapter);
                oroutePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        mODir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                ostopAdapter = new ArrayAdapter<String>(Login.this,
                        android.R.layout.simple_spinner_item, sostop[oroutePosition][position]);
                mOStop.setAdapter(ostopAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        mDRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                ddirAdapter = new ArrayAdapter<String>(
                        Login.this, android.R.layout.simple_spinner_item, sddir[position]);

                mDDir.setAdapter(ddirAdapter);
                droutePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        mDDir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                dstopAdapter = new ArrayAdapter<String>(Login.this,
                        android.R.layout.simple_spinner_item, sdstop[droutePosition][position]);
                mDStop.setAdapter(dstopAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }

    Timer t = new Timer();
    Timer t1 = new Timer();


    private void setListener() {
        mBtnLgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oroute = mORoute.getSelectedItem().toString();
                odir = mODir.getSelectedItem().toString();
                ostop = mOStop.getSelectedItem().toString();


                droute = mDRoute.getSelectedItem().toString();
                ddir = mDDir.getSelectedItem().toString();
                dstop = mDStop.getSelectedItem().toString();

                new Thread(inputThread).start();


                t.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        System.out.println("Timer is working.");
                        new Thread(rcThread).start();
                    }
                }, 8000, 20000);

            }
        });

        mBtnRc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(rcThread).start();

            }
        });

        mBtnDly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //mBtnRc.performClick();
                        System.out.println("Timer1 is working.");
                        new Thread(delayThread).start();
                    }
                }, 0, 20000);

            }
        });

        mBtnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
             }
        });
    }

    protected void dialog() {

        final Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure to hold the request？");
        builder.setTitle("You clicked the request button");
        builder.setPositiveButton("Hold Request", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(successThread).start();
                dialog.dismiss();
                //Login.this.finish();
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }



    Handler httpHandlerInput = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("input");

            if (val.equals("1")) {
                Toast.makeText(Login.this, "We have got your information!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Login.this, "Sorry. Fail to connect server. Please try later.",
                        Toast.LENGTH_LONG).show();
            }

            btnReqFlag = true;
            mBtnReq.setEnabled(true);
            mBtnReq.setClickable(btnReqFlag);
        }
    };

    Runnable inputThread = new Runnable() {

        @Override
        public void run() {

            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(urlinput);

            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);
            NameValuePair pair2 = new BasicNameValuePair("orn", oroute);
            NameValuePair pair3 = new BasicNameValuePair("poDir", odir);
            NameValuePair pair4 = new BasicNameValuePair("ostop", ostop);
            NameValuePair pair5 = new BasicNameValuePair("drn", droute);
            NameValuePair pair6 = new BasicNameValuePair("ptDir", ddir);
            NameValuePair pair7 = new BasicNameValuePair("dstop", dstop);


            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(pair1);
            pairs.add(pair2);
            pairs.add(pair3);
            pairs.add(pair4);
            pairs.add(pair5);
            pairs.add(pair6);
            pairs.add(pair7);

            try {
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(requestEntity);
                try {
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {

                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        result = reader.readLine();
                    } else {
                        result = "" + response.getStatusLine().getStatusCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("input", result);
            msg.setData(data);
            httpHandlerInput.sendMessage(msg);
        }
    };


    Handler httpHandlerDly = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("delayvalue");

            final TextView delay = (TextView) findViewById(R.id.delay);
            String cur = "", next = "", none = "", toid = "", odid = "";
            try {
                JSONArray jsonArray = new JSONArray(val);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    cur = jsonObject.getString("podBusArrT");
                    next = jsonObject.getString("ptoBusArrT");
                    toid = jsonObject.getString("p_tosp_id");
                    odid = jsonObject.getString("p_odsp_id");
                    none = jsonObject.getString("warn");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (none.equals("null")) {
                delay.setText("At the transfer stop: \nCurrent bus arrival time is: \n   " + cur + "\nNext bus arrival time is: \n   " + next + "\nTransfer Stop ID is: \n   " + toid + "\nDestination Stop ID is: \n   " + odid );
            } else {
                delay.setText(none);
            }

        }
    };

    Handler httpHandlerReq = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");


            final TextView success = (TextView) findViewById(R.id.success);
            String ret = "";
            try {
                JSONArray jsonArray = new JSONArray(val);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ret = jsonObject.getString("p_hold_success");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ret.equals("true")) success.setText("Success hold your request.");
            if (ret.equals("false")) success.setText("We cannot hold your request.");
            if (ret.equals("NoRequest")) success.setText("You haven't sent your hold request.");


        }
    };

    Handler httpHandlerRc = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("rc");


            System.out.println("rcHandler is working. " + val);

            String ret = "";
            try {
                JSONArray jsonArray = new JSONArray(val);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ret = jsonObject.getString("b_route_decision");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(dialog!=null) {
                //dialog.dismiss();
                dialog.setMessage(ret);
            } else {
                dialog = new AlertDialog.Builder(context)
                        .setTitle("Notice")
                        .setMessage(ret)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            /*

            if (val.equals("route change")) {

                new AlertDialog.Builder(context)
                        .setTitle("Notice")
                        .setMessage(val)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else if (val.equals("arrive")){
                Toast.makeText(Login.this, "The bus is arrived", Toast.LENGTH_LONG).show();
                t.cancel();
                t1.cancel();
            }*/

        }
    };

    Runnable rcThread = new Runnable() {


        @Override
        public void run() {

            System.out.println("rcThread is working.");
            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(urlrc);
            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);

            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(pair1);


            try {

                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(requestEntity);

                try {
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {

                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        result = reader.readLine();

                    } else {
                        result = "" + response.getStatusLine().getStatusCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("rc", result);
            msg.setData(data);
            httpHandlerRc.sendMessage(msg);
        }
    };


    Runnable delayThread = new Runnable() {

        @Override
        public void run() {

            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(urlquery);
            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);


            final ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(pair1);

            try {
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(requestEntity);
                try {
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        result = reader.readLine();
                    } else {
                        result = "" + response.getStatusLine().getStatusCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("delayvalue", result);
            msg.setData(data);
            httpHandlerDly.sendMessage(msg);
        }
    };

    Runnable successThread = new Runnable() {

        @Override
        public void run() {

            String result = "-1";
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(urlhold);
            NameValuePair pair1 = new BasicNameValuePair("fid", android_id);
            //NameValuePair pair2 = new BasicNameValuePair("p_hold_request", "1");

            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(pair1);
            //pairs.add(pair2);

            try {
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(requestEntity);

                try {
                    CloseableHttpResponse response = httpClient.execute(httpPost);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        result = reader.readLine();
                    } else {
                        result = "" + response.getStatusLine().getStatusCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", result);
            msg.setData(data);
            httpHandlerReq.sendMessage(msg);
        }
    };



}
