package com.example.heesu.mindfulmoney;
import java.net.*;
import java.io.*;

// Must throw exception for all Net functions because of exceptions caused by URL()
// Example for http request: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
// Need (?) to set property for "Accept: application/json"
// Assuming all http requests are of format: curl -X GET --header "Accept: application/json" <URL_String>
// Need to do something about Key (parameterize?)

public class Purchase {

    // Key used for querying Capitol One Nessie data base
    private static String key = "key=26919f14786c1d9c548f0e653e67c78d";

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

    // Parameters: URL for request
    // Returns: String represenation of list of JSONs
    private static String sendPurchaseRequest(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        String responseMessage = conn.getResponseMessage();
        System.out.println(responseMessage);
        if (!responseMessage.equals("OK")) {
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
