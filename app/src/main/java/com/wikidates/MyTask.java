package com.wikidates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MyTask extends AsyncTask<Integer, Integer, Void> {

    public static String urlwiki = "https://ru.wikipedia.org/wiki/";//Тут храним значение заголовка сайта
    public  static String URL,URLSPORT,URLSCINCE;
    int year1;
    Element temp,temp2;
    Connection.Response res=null;
    Connection.Response res2=null;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    Document doc = null;
    Document doc2 = null;
    int internetstate=0;

    //Здесь хранится будет разобранный html документ
    Elements li,li2,h2;
    String info,month,month1;
    ArrayList<String> listinfo = new ArrayList<String>();
    int progress=0;



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String selectedDate = MainActivity.editText.getText().toString();
                URL=urlwiki+MainActivity.yearR+"_год";
                URLSCINCE=urlwiki+MainActivity.yearR+"_год_в_науке";


        if (MainActivity.monthR==1) month="января";
        if (MainActivity.monthR==2) month="февраля";
        if (MainActivity.monthR==3) month="марта";
        if (MainActivity.monthR==4) month="апреля";
        if (MainActivity.monthR==5) month="мая";
        if (MainActivity.monthR==6) month="июня";
        if (MainActivity.monthR==7) month="июля";
        if (MainActivity.monthR==8) month="августа";
        if (MainActivity.monthR==9) month="сентября";
        if (MainActivity.monthR==10) month="октября";
        if (MainActivity.monthR==11) month="ноября";
        if (MainActivity.monthR==12) month="декабря";
        if (MainActivity.monthR==1) month1="январь";
        if (MainActivity.monthR==2) month1="февраль";
        if (MainActivity.monthR==3) month1="марте";
        if (MainActivity.monthR==4) month1="апрель";
        if (MainActivity.monthR==5) month1="май";
        if (MainActivity.monthR==6) month1="июнь";
        if (MainActivity.monthR==7) month1="июль";
        if (MainActivity.monthR==8) month1="августе";
        if (MainActivity.monthR==9) month1="сентябрь";
        if (MainActivity.monthR==10) month1="октябрь";
        if (MainActivity.monthR==11) month1="ноябрь";
        if (MainActivity.monthR==12) month1="декабрь";

        if (MainActivity.language) {
            URL="https://en.wikipedia.org/wiki/"+MainActivity.yearR;
            URLSCINCE="https://en.wikipedia.org/wiki/"+MainActivity.yearR+"_in_science";


            if (MainActivity.monthR==1) month="January";
            if (MainActivity.monthR==2) month="February";
            if (MainActivity.monthR==3) month="March";
            if (MainActivity.monthR==4) month="April";
            if (MainActivity.monthR==5) month="May";
            if (MainActivity.monthR==6) month="June";
            if (MainActivity.monthR==7) month="July";
            if (MainActivity.monthR==8) month="August";
            if (MainActivity.monthR==9) month="September";
            if (MainActivity.monthR==10) month="October";
            if (MainActivity.monthR==11) month="November";
            if (MainActivity.monthR==12) month="December";

        }

    }

    public static void enableSSLSocket() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ssl error 1");
            e.printStackTrace();
        }
        try {
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (KeyManagementException e) {
            System.out.println("ssl error 2");
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        //-------------------------------------------------------------
    }
    private static String findstring(String text){
        Pattern pattern1 = Pattern.compile("\\d{4}\\D\\d{4}");
        Pattern pattern2 = Pattern.compile("\\d{3}\\D\\d{3}");
        Pattern pattern3 = Pattern.compile("\\d{2}\\D\\d{2}");
        Pattern pattern4 = Pattern.compile("\\d{1}\\D\\d{1}");

        Matcher matcher1 = pattern1.matcher(text);
        Matcher matcher2 = pattern2.matcher(text);
        Matcher matcher3 = pattern3.matcher(text);
        Matcher matcher4 = pattern4.matcher(text);

        if (matcher1.find()) {return matcher1.group();} else
        if (matcher2.find()) {return matcher2.group();} else
        if (matcher3.find()) {return matcher3.group();} else
        if (matcher4.find()) {return matcher4.group();} else
            return null;
    }
//-------------------------------------------------------
    public static Elements findcloseyears(String url){
        Elements li=null;
        Connection.Response res=null;
        Document doc = null;

        enableSSLSocket();

        try {
            res = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .timeout(3000)
                    .execute();
        } catch (IOException e) {
            //Если не получилось считать
            System.out.println("ssl error close year");
            return null;
        }
        try {
            if (res != null) {
                doc = res.parse();
            }

        } catch (IOException e) {
            System.out.println("ssl error close year");
            e.printStackTrace();
            return null;

        }
        if (doc!=null) {
            Element temp = doc.body();

            li = temp.select("li");
        }
        return li;
    }
    //-------------------------------------------------------
    public static Document monthinfo(String month){
        Elements li=null;
        Connection.Response res=null;
        Document doc = null;
        String url = "https://en.wikipedia.org/wiki/"+month+"_"+MainActivity.yearR;
        enableSSLSocket();

        try {
            res = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .timeout(3000)
                    .execute();
        } catch (IOException e) {
            //Если не получилось считать
            System.out.println("ssl error month method");
            return null;
        }
        try {
            if (res != null) {
                doc = res.parse();
            }

        } catch (IOException e) {
            System.out.println("ssl error month method");
            e.printStackTrace();
            return null;

        }
        if (doc!=null) {
            Element temp = doc.body();

            li = temp.select("li");
        }
        return doc;
    }

    //-----------------------------
    public static Document yearinfo(String year){
        Elements li=null;
        Connection.Response res=null;
        Document doc = null;
        String url = "https://en.wikipedia.org/wiki/"+MainActivity.yearR;
        enableSSLSocket();

        try {
            res = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .timeout(3000)
                    .execute();
        } catch (IOException e) {
            //Если не получилось считать
            System.out.println("ssl error month method");
            return null;
        }
        try {
            if (res != null) {
                doc = res.parse();
            }

        } catch (IOException e) {
            System.out.println("ssl error month method");
            e.printStackTrace();
            return null;

        }
        if (doc!=null) {
            Element temp = doc.body();

            li = temp.select("li");
        }
        return doc;
    }

    //-----------------------------

    public static boolean isOnline1(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    private boolean isOnline() {
        boolean has_wifi = false;
        boolean has_mobile_data = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);NetworkInfo[] networkInfos= connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info: networkInfos){ if(info.getTypeName().equalsIgnoreCase("Wifi")){
            if(info.isConnected()){
                has_wifi=true;
            }
        }if(info.getTypeName().equalsIgnoreCase("Mobile")){
            if(info.isConnected()){
                has_mobile_data=true;
            }
        }}return has_wifi || has_mobile_data;
    }


//--------------------------------------------
    @Override
    protected Void doInBackground(Integer... params) {


       if (!MainActivity.language) {
           enableSSLSocket();

           try {
               res = Jsoup.connect(URL)
                       .ignoreContentType(true)
                       .method(Connection.Method.POST)
                       .timeout(5000)
                       .execute();
           } catch (IOException e) {
               //Если не получилось считать
               System.out.println("ssl error 4");
               e.printStackTrace();
               MainActivity.toaster = true;

           }


           try {
               res2 = Jsoup.connect(URLSCINCE)
                       .ignoreContentType(true)
                       .method(Connection.Method.POST)
                       .timeout(5000)
                       .execute();
           } catch (IOException e) {
               System.out.println("ssl error 5");
               e.printStackTrace();
               MainActivity.toaster = true;
           }


           try {
               if (res != null) {
                   doc = res.parse();
               }

           } catch (IOException e) {

               e.printStackTrace();
           }
           try {
               if (res2 != null) {
                   doc2 = res2.parse();
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           String date = MainActivity.dayR + " " + month;

           if (doc != null) {
               temp = doc.body();

               li = temp.select("li");
               listinfo.clear();

               for (int i = 0; i < li.size(); i++) {
                   if (MainActivity.yearR < 1900) {
                       if (li.get(i).text().contains(MainActivity.yearR + "—") || li.get(i).text().contains("—" + MainActivity.yearR) || li.get(i).text().contains(String.valueOf(MainActivity.yearR))) {
                           info = li.get(i).text();
                           listinfo.add(info);
                       }
                   }
                   progress = 10;
                   publishProgress(progress);
  /*      if (li.get(i).text().contains("Химия")||li.get(i).text().contains("Физика")||li.get(i).text().contains("Физеология")||li.get(i).text().contains("Литература")) {
            info = li.get(i).text();
            listinfo.add("Нобелевская премия года: "+info);
        }

   */
                   if (li.get(i).text().contains(date) && !li.get(i).text().contains("1" + MainActivity.dayR) && !li.get(i).text().contains("2" + MainActivity.dayR)) {
                       info = li.get(i).text();
                       listinfo.add(info);
                   }
                   progress = progress + 10;
                   publishProgress(progress);
                   if (li.get(i).text().contains(month1)) {
                       info = li.get(i).text();
                       listinfo.add(info);
                   }
                   progress = progress + 10;
                   publishProgress(progress);
               }
               //-------------------------------
               Elements liy1 = null;
               Elements liy2 = null;
               Elements liy3 = null;
               Elements liy4 = null;
               Elements liy5 = null;
               Elements liy6 = null;


               if (MainActivity.yearR < 1900) {
                   liy1 = findcloseyears(urlwiki + (MainActivity.yearR - 3) + "_год");

                   if (liy1 != null) {
                       for (int i = 0; i < liy1.size(); i++) {
                           if (MainActivity.yearR < 1900) {
                               int y1 = 0;
                               int y2 = 0;
                               String pattern = findstring(liy1.get(i).text());
                               System.out.println(1);
                               System.out.println(pattern);

                               if (pattern != null) {
                                   for (int j = 0; j < pattern.length(); j++) {
                                       if (pattern.charAt(j) == '—') {
                                           y1 = Integer.parseInt(pattern.substring(0, j));
                                           y2 = Integer.parseInt(pattern.substring(j + 1));
                                       }
                                   }
                                   System.out.println(y1);
                                   System.out.println(y2);
                               }
                               if ((y1 <= MainActivity.yearR) && (y2 >= MainActivity.yearR)) {
                                   info = liy1.get(i).text();
                                   listinfo.add(info);
                               }
                           }
                       }
                   }
                   progress = progress + 15;
                   publishProgress(progress);
                   //----------------

                   liy2 = findcloseyears(urlwiki + (MainActivity.yearR - 2) + "_год");

                   if (liy2 != null) {
                       for (int i = 0; i < liy2.size(); i++) {
                           if (MainActivity.yearR < 1900) {
                               int y1 = 0;
                               int y2 = 0;
                               String pattern = findstring(liy2.get(i).text());
                               System.out.println(2 + " " + i);
                               System.out.println(pattern);

                               if (pattern != null) {
                                   for (int j = 0; j < pattern.length(); j++) {
                                       if (pattern.charAt(j) == '—') {
                                           y1 = Integer.parseInt(pattern.substring(0, j));
                                           y2 = Integer.parseInt(pattern.substring(j + 1));
                                       }
                                   }
                                   System.out.println(y1);
                                   System.out.println(y2);
                               }
                               if ((y1 <= MainActivity.yearR) && (y2 >= MainActivity.yearR)) {
                                   info = liy2.get(i).text();
                                   listinfo.add(info);
                               }
                           }
                       }
                   }
                   progress = progress + 10;
                   publishProgress(progress);
                   //-------------------------

                   liy3 = findcloseyears(urlwiki + (MainActivity.yearR - 1) + "_год");

                   if (liy3 != null) {
                       for (int i = 0; i < liy3.size(); i++) {
                           if (MainActivity.yearR < 1900) {
                               int y1 = 0;
                               int y2 = 0;
                               String pattern = findstring(liy3.get(i).text());
                               System.out.println(3);
                               System.out.println(pattern);
                               if (pattern != null) {
                                   for (int j = 0; j < pattern.length(); j++) {
                                       if (pattern.charAt(j) == '—') {
                                           y1 = Integer.parseInt(pattern.substring(0, j));
                                           y2 = Integer.parseInt(pattern.substring(j + 1));
                                       }
                                   }
                                   System.out.println(y1);
                                   System.out.println(y2);
                               }
                               if ((y1 <= MainActivity.yearR) && (y2 >= MainActivity.yearR)) {
                                   info = liy3.get(i).text();
                                   listinfo.add(info);
                               }
                           }
                       }
                   }
                   progress = progress + 10;
                   publishProgress(progress);
                   //------------------------

                   liy4 = findcloseyears(urlwiki + (MainActivity.yearR + 1) + "_год");

                   if (liy4 != null) {
                       for (int i = 0; i < liy4.size(); i++) {
                           if (MainActivity.yearR < 1900) {
                               int y1 = 0;
                               int y2 = 0;
                               String pattern = findstring(liy4.get(i).text());
                               System.out.println(4);
                               System.out.println(pattern);

                               if (pattern != null) {
                                   for (int j = 0; j < pattern.length(); j++) {
                                       if (pattern.charAt(j) == '—') {
                                           y1 = Integer.parseInt(pattern.substring(0, j));
                                           y2 = Integer.parseInt(pattern.substring(j + 1));
                                       }
                                   }
                                   System.out.println(y1);
                                   System.out.println(y2);
                               }
                               if ((y1 <= MainActivity.yearR) && (y2 >= MainActivity.yearR)) {
                                   info = liy4.get(i).text();
                                   listinfo.add(info);
                               }
                           }
                       }
                   }
                   progress = progress + 10;
                   publishProgress(progress);
                   //----------------------

                   liy5 = findcloseyears(urlwiki + (MainActivity.yearR + 2) + "_год");

                   if (liy5 != null) {
                       for (int i = 0; i < liy5.size(); i++) {
                           if (MainActivity.yearR < 1900) {
                               int y1 = 0;
                               int y2 = 0;
                               String pattern = findstring(liy5.get(i).text());
                               System.out.println(5);
                               System.out.println(pattern);

                               if (pattern != null) {
                                   for (int j = 0; j < pattern.length(); j++) {
                                       if (pattern.charAt(j) == '—') {
                                           y1 = Integer.parseInt(pattern.substring(0, j));
                                           y2 = Integer.parseInt(pattern.substring(j + 1));
                                       }
                                   }
                                   System.out.println(y1);
                                   System.out.println(y2);
                               }
                               if ((y1 <= MainActivity.yearR) && (y2 >= MainActivity.yearR)) {
                                   info = liy5.get(i).text();
                                   listinfo.add(info);
                               }
                           }
                       }
                   }
                   progress = progress + 15;
                   publishProgress(progress);
                   //------------------------

                   liy6 = findcloseyears(urlwiki + (MainActivity.yearR + 3) + "_год");

                   if (liy6 != null) {
                       for (int i = 0; i < liy6.size(); i++) {
                           if (MainActivity.yearR < 1900) {
                               int y1 = 0;
                               int y2 = 0;
                               String pattern = findstring(liy6.get(i).text());
                               System.out.println(6);
                               System.out.println(pattern);

                               if (pattern != null) {
                                   for (int j = 0; j < pattern.length(); j++) {
                                       if (pattern.charAt(j) == '—') {
                                           y1 = Integer.parseInt(pattern.substring(0, j));
                                           y2 = Integer.parseInt(pattern.substring(j + 1));
                                       }
                                   }
                                   System.out.println(y1);
                                   System.out.println(y2);
                               }
                               if ((y1 <= MainActivity.yearR) && (y2 >= MainActivity.yearR)) {
                                   info = liy6.get(i).text();
                                   listinfo.add(info);
                               }
                           }
                       }
                   }
               } else {
                   progress = progress + 60;
                   publishProgress(progress);
               }
               progress = progress + 10;
               publishProgress(progress);
               //-----------------------
           }
           if (doc2 != null) {
               temp2 = doc2.body();
               li2 = temp2.select("li");

               for (int i = 0; i < li2.size(); i++) {
                   if (MainActivity.yearR < 1900) {
                       if (li2.get(i).text().contains(MainActivity.yearR + "—") || li2.get(i).text().contains("—" + MainActivity.yearR)) {
                           info = li2.get(i).text();
                           listinfo.add(info);
                       }
                   }
                   progress = progress + 10;
                   publishProgress(progress);
                   if (li2.get(i).text().contains(date) && !li.get(i).text().contains("1" + MainActivity.dayR) &&
                           !li.get(i).text().contains("2" + MainActivity.dayR)&&
                           !li.get(i).text().contains("3" + MainActivity.dayR)) {
                       info = li2.get(i).text();
                       listinfo.add(info);
                   }

                   if (li2.get(i).text().contains(month1)) {
                       info = li2.get(i).text();
                       listinfo.add(info);
                   }
               }
               progress = progress + 10;
               publishProgress(progress);
           }
    /*

}
        Elements retur = temp.getElementsContainingOwnText("Retur").next();
        Elements smokegastemp = temp.getElementsContainingOwnText("Smoke Gas Temp").next();
        Elements statetime = temp.getElementsContainingOwnText("In same state:").next();
        Elements state = temp.getElementsContainingOwnText("State").next();

      */
           // internetstate=1;publishProgress(internetstate);

       }else{
           listinfo.clear();

           doc=yearinfo(String.valueOf(MainActivity.yearR));
           temp = doc.body();
           li = temp.select("li");
           for (int i = 0; i < li.size(); i++) {
               if (li.get(i).text().contains(month+" "+MainActivity.dayR)&&
                       !li.get(i).text().contains(MainActivity.dayR+"1")&&
                       !li.get(i).text().contains(MainActivity.dayR+"2")&&
                       !li.get(i).text().contains(MainActivity.dayR+"3")&&
                       !li.get(i).text().contains(MainActivity.dayR+"4")&&
                       !li.get(i).text().contains(MainActivity.dayR+"5")&&
                       !li.get(i).text().contains(MainActivity.dayR+"6")&&
                       !li.get(i).text().contains(MainActivity.dayR+"7")&&
                       !li.get(i).text().contains(MainActivity.dayR+"0")&&
                       !li.get(i).text().contains(MainActivity.dayR+".")&&
                       !li.get(i).text().contains(MainActivity.dayR+"8")&&
                       !li.get(i).text().contains(MainActivity.dayR+"9")) {
                   info = li.get(i).text();
                   listinfo.add(info);
               }

               progress = 100;
               publishProgress(progress);

  /*      if (li.get(i).text().contains("Химия")||li.get(i).text().contains("Физика")||li.get(i).text().contains("Физеология")||li.get(i).text().contains("Литература")) {
            info = li.get(i).text();
            listinfo.add("Нобелевская премия года: "+info);
        }
           */


           }

           if (MainActivity.yearR >= 1900) {
           doc=monthinfo(month);
           temp = doc.body();
           h2 = temp.select("h2");

               for (int i = 0; i < h2.size(); i++) {
                   if (h2.get(i).text().contains(month + " " + MainActivity.dayR + ", " + MainActivity.yearR)) {
                       li = h2.next().get(i).select("li");
                       for (int j = 0; j < li.size(); j++) {
                           info = li.get(j).text();
                           listinfo.add(info);
                       }
                   }
               }
           }

       }

        return null;

    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        MainActivity.textView.setClickable(true);

        MainActivity.openurl=URL;

        if (MainActivity.language&&MainActivity.yearR>=1900) MainActivity.openurl="https://en.wikipedia.org/wiki/"+month+"_"+MainActivity.yearR;



        if (!listinfo.isEmpty()) {
    for (int i = 0; i < listinfo.size(); i++) {
        MainActivity.textView.append(listinfo.get(i));
        MainActivity.textView.append("\n");
        MainActivity.textView.append("---------------------------------");
        MainActivity.textView.append("\n");
        MainActivity.textYN=true;
    }
}else MainActivity.textYN=false;


    //    textView.setText(title);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

MainActivity.progressBar.setProgress(values[0]);

    }
}
