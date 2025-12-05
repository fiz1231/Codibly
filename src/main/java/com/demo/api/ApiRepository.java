package com.demo.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.context.annotation.Configuration;

import com.demo.dao.ResponseGetIntervaOfEnnergyMix;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
@Configuration
@AllArgsConstructor
public class ApiRepository {

    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    //static private
    public ResponseGetIntervaOfEnnergyMix getIntervalOfEnergyMix(String from , String to) throws IOException{
        String url = "https://api.carbonintensity.org.uk/generation/"+ from + "/" + to;// maybe use DateTimeFormatter?
        URL objUrl = new URL(url);
        
        HttpsURLConnection con = (HttpsURLConnection) objUrl.openConnection();
        con.setRequestMethod("GET");       
       
        
        
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        
        wr.flush();
 
        wr.close();
        
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in;
        if(responseCode ==200){
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        else{
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        System.out.println("dupa");
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response);
        // for now map only for one chunk
        ResponseGetIntervaOfEnnergyMix result = objectMapper.readValue(response.toString(),new TypeReference<ResponseGetIntervaOfEnnergyMix>(){});
        return result;

    }
    // compile exec:java -Dexec.mainClass="com.demo.api.ApiRepository"
    public void main(){
        ApiRepository test = new ApiRepository();
        try {
            System.out.println("Response:"+test.getIntervalOfEnergyMix("2025-01-20T12:00Z","2025-01-20T12:30Z").getGenerationmix());
        }
        catch (Exception e ){
            System.out.println("Exception "+e.getMessage());
        }
    }
}
