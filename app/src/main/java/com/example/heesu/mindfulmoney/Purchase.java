package com.example.heesu.mindfulmoney;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

// Must throw exception for all Net functions because of exceptions caused by URL()
// Example for http request: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
// Need (?) to set property for "Accept: application/json"
// Assuming all http requests are of format: curl -X GET --header "Accept: application/json" <URL_String>
// Need to do something about Key (parameterize?)

public class Purchase {

    // Key used for querying Capitol One Nessie data base
    private static String key = "key=f42694af69bea32185ab6de531cff8ab";

    // Parameters: String for Account's ID
    // Returns: URL to request all purchases made from the Account
    private static URL makeURL_Purchase_Account(String account_id) throws Exception {
        String prefix = "http://api.reimaginebanking.com";
        String account = "/accounts/" + account_id;
        String suffix = "/purchases?" + key;
        String spec = prefix + account + suffix;
        URL url = new URL(spec);
        return url;
    }

    // Parameters: String for Merchant's ID, String for Account's ID
    // Returns: URL to request all purchases made to the Merchant from the Account
    private static URL makeURL_Purchase_Merchant_Account(String merchant_id, String account_id) throws Exception {
        String prefix = "http://api.reimaginebanking.com";
        String merchant = "/merchants/" + merchant_id;
        String account = "/accounts/" + account_id;
        String suffix = "/purchases?" + key;
        String spec = prefix + merchant + account + suffix;
        URL url = new URL(spec);
        return url;
    }

    // Parameters: String for Merchant's ID
    // Returns: URL to request all purchases made to the Merchant
    private static URL makeURL_Purchase_Merchant(String merchant_id) throws Exception {
        String prefix = "http://api.reimaginebanking.com";
        String merchant = "/merchants/" + merchant_id;
        String suffix = "/purchases?" + key;
        String spec = prefix + merchant + suffix;
        URL url = new URL(spec);
        return url;
    }

    private static URL makeURL_MakeAPurchase(String account_id, int amount) throws Exception {
        String prefix = "http://api.reimaginebanking.com/accounts/";
        String payer = account_id + "/purchases?";
        String spec = prefix + payer + key;
        URL url = new URL(spec);
        return url;
    }

    // Parameters: URL for request
    // Returns: String representation of list of JSONs
    private static String sendPurchaseRequest(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        String responseMessage = conn.getResponseMessage();
        System.out.println(responseMessage);
        if (!responseMessage.equals("OK")) {
            System.err.println("ERROR");
            return "ERROR";
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String bodyLine;
        StringBuffer response = new StringBuffer();
        while ((bodyLine = in.readLine()) != null) {
            response.append(bodyLine);
        }
        in.close();
        return response.toString();
    }

    public static String putPurchase(String account_id, String merchant_id, int amount) throws Exception {
        URL url = makeURL_MakeAPurchase(account_id, amount);
        System.err.println(url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");

        DateFormat df = new SimpleDateFormat("yyyy/mm/dd");

        JSONObject post = new JSONObject();
        post.put("merchant_id", merchant_id);
        post.put("medium", "balance");
        post.put("purchase_date", df.format(new Date()));
        post.put("amount", amount);
        post.put("status", "pending");
        post.put("description", "string");

        System.err.println("checkpoint");

        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");
        conn.connect();

        OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
        wr.write(post.toString());

        wr.close();


        String responseMessage = conn.getResponseMessage();

        System.err.println(responseMessage);
        if (!responseMessage.equals("OK")) {
            return "ERROR";
        }
        System.err.println("checkpoint2");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String bodyLine;
        StringBuffer response = new StringBuffer();
        while ((bodyLine = in.readLine()) != null) {
            response.append(bodyLine);
        }
        in.close();
        return response.toString();
    }

    // Parameters: String for Account's ID
    // Returns: String representation of list of JSONs for all purchases made from the Account
    public static String getPurchase_Account(String account_id) throws Exception {
        URL url = makeURL_Purchase_Account(account_id);
          return sendPurchaseRequest(url);
    }

    // Parameters: String for Merchant's ID, String for Account's ID
    // Returns: String representation of list of JSONs for all purchases made to the Merchant from the Account
    public static String getPurchase_Merchant_Account(String merchant_id, String account_id) throws Exception {
        URL url = makeURL_Purchase_Merchant_Account(merchant_id, account_id);
        return sendPurchaseRequest(url);
    }

    // Parameters: String for Merchant's ID
    // Returns: String representation of list of JSONs for all purchases made to the Merchant
    public static String getPurchase_Merchant(String merchant_id) throws Exception {
        URL url = makeURL_Purchase_Merchant(merchant_id);
        return sendPurchaseRequest(url);
    }

    // Tester main method
    public static void main(String[] args) throws Exception {
        String account_id_1 = "asdasdas";
        String merchant_id_1 = "bad_id";
        //String account_id_1 = "56c66be7a73e492741508217";
        String account_id_2 = "56c66be7a73e492741508219";
        //String merchant_id_1 = "56c66be6a73e49274150762e";
        String merchant_id_2 = "56c66be6a73e49274150762f";
        System.out.println(getPurchase_Account(account_id_1));
        System.out.println(getPurchase_Merchant_Account(merchant_id_2, account_id_1));
        System.out.println(getPurchase_Merchant(merchant_id_2));
    }
}
