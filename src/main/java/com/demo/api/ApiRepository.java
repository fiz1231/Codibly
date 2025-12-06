package com.demo.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.context.annotation.Configuration;

import com.demo.dao.generationData.GenerationInput;
import com.demo.dao.generationData.GenerationMix;
import com.demo.dao.generationData.GenerationOutput;
import com.demo.dao.generationData.ResponseGetIntervaOfEnnergyMix;
import com.demo.dao.generationData.ResponseProcessedIntervalOfEnergyMix;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
@Configuration
@AllArgsConstructor
public class ApiRepository {

    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    //static private
    private List<ResponseGetIntervaOfEnnergyMix> getIntervalOfEnergyMix(String from , String to) throws IOException{
        // URL obj = new URL("https://api.carbonintensity.org.uk/generation/"+ from + "/" + to);
        // HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // con.setRequestMethod("GET");
        // int responseCode = con.getResponseCode();
        // BufferedReader in = new BufferedReader(
        //     new InputStreamReader(con.getInputStream()));
        // String inputLine;
        // StringBuffer response = new StringBuffer();
        // while ((inputLine = in.readLine()) != null) {
        //     response.append(inputLine);
        // }
        // in.close();
        // System.out.println(response.toString());
        // return response.toString();
        
        
        /*I dont know why but upper code downloaded from https://carbon-intensity.github.io/api-definitions/?java#get-generation works but down one doesnt. 
        Why?
        // DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        
        // wr.flush();

        // wr.close();

        this lines couse problems

        Why have I even write output stream to api?

        Oh, now i remember

        
        
         */
        
        System.out.println("Execution : getIntervalOfEnergyMix");
        String url = "https://api.carbonintensity.org.uk/generation/"+ from + "/" + to;// maybe use DateTimeFormatter?
        URL objUrl = new URL(url);
        
        HttpsURLConnection con = (HttpsURLConnection) objUrl.openConnection();
        con.setRequestMethod("GET");       
        con.setRequestProperty("Accept", "application/json");
        
        
        con.setDoOutput(true);
        // DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        
        // wr.flush();

        // wr.close();
        
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
       
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //System.out.println(response.toString());
        
        GenerationInput result = objectMapper.readValue(response.toString(),new TypeReference<GenerationInput>(){});
         System.out.println("dupa");
       
        
        return result.data();

    }
    
    private Map<String,List<ResponseGetIntervaOfEnnergyMix>> groupIntervalsByDate(List<ResponseGetIntervaOfEnnergyMix> intervalListInput){
        
        System.out.println("Execution : groupIntervalsByDate");
        return intervalListInput.stream().collect(Collectors.groupingBy(
            interval->Integer.toString(ZonedDateTime.parse(interval.getFrom()).getDayOfYear()),Collectors.toList()));

    }
    
    private Map<String,Double> calculateAverageValues​(List<ResponseGetIntervaOfEnnergyMix> intervalsGroupedForDay){
        System.out.println("Execution : calculateAverageValues");
        return intervalsGroupedForDay.stream().map(x->x.getGenerationmix()).flatMap(x->x.stream()).collect(Collectors.groupingBy(x->x.fuel(),Collectors.mapping(x->x.perc(),Collectors.averagingDouble(x->x.doubleValue()))));
    }
    
    public GenerationOutput calculateAverageSharesForDays(ZonedDateTime from,  ZonedDateTime to){
        System.out.println("Execution : calculateAverageSharesForDays");
        DateTimeFormatter format =DateTimeFormatter.ofPattern("YYYY-MM-dd'T'hh:mmz");
        List<ResponseProcessedIntervalOfEnergyMix> returnObject =new ArrayList<ResponseProcessedIntervalOfEnergyMix>();
        try{
            List<ResponseGetIntervaOfEnnergyMix> apiCallResponse = this.getIntervalOfEnergyMix(from.format(format),to.format(format));
            Map<String,List<ResponseGetIntervaOfEnnergyMix>> apiCallResponseGroupedByday = groupIntervalsByDate(apiCallResponse);
            List<GenerationMix> resultGenerationMixs = new ArrayList<>();
            System.out.println("keyset: "+apiCallResponseGroupedByday.keySet());
            for (String dayKey :apiCallResponseGroupedByday.keySet()){
                System.out.println("daykey: "+dayKey);
                ResponseProcessedIntervalOfEnergyMix outputData = new ResponseProcessedIntervalOfEnergyMix();
                Map<String,Double> dayAverageMix = this.calculateAverageValues​(apiCallResponseGroupedByday.get(dayKey));
                
                for (String fuel : dayAverageMix.keySet()){
                    
                    resultGenerationMixs.add(new GenerationMix(fuel,dayAverageMix.get(fuel).floatValue()));
                }

                outputData.setFrom(from.toString());
                outputData.setFrom(from.plusDays(1).toString());
                outputData.setGenerationmix(resultGenerationMixs);
                returnObject.add(outputData);
                resultGenerationMixs.clear();
            }
            System.out.println("Createed output ogject");
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return new GenerationOutput(returnObject);
    }

    // testing repository method
    // compile exec:java -Dexec.mainClass="com.demo.api.ApiRepository"
    public void main(){
        ApiRepository test = new ApiRepository();
        try {
            //test.getIntervalOfEnergyMix("2025-01-20T12:00Z","2025-01-20T13:30Z").forEach(e->System.out.println("Result: "+e.getGenerationmix()));
            // var test2=test.groupIntervalsByDate(test.getIntervalOfEnergyMix("2025-01-20T12:00Z","2025-01-23T13:30Z"));

            // var test3=test.calculateAverageValues​(test2.entrySet().stream().findAny().get().getValue());
            // System.out.println(test3.values());
            System.out.println("Createed output ogject");
            ZonedDateTime from = ZonedDateTime.of(2025, 10, 5 ,0 , 0, 0, 0, ZoneId.of("Z"));
            ZonedDateTime to = ZonedDateTime.of(2025, 10, 8 ,0 , 0, 0, 0, ZoneId.of("Z"));
            GenerationOutput test4 = test.calculateAverageSharesForDays(from,to);
            System.out.println(test4.data().toString());
        
        }
        catch (Exception e ){
            System.out.println("Exception "+e.getMessage());
        }
    }
}
