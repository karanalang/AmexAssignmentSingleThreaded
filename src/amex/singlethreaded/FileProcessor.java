package amex.singlethreaded;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.IllegalArgumentException;


public class FileProcessor {

    /*
     * HashMap which stores the result
     * */
    HashMap<Long, HashMap<String, Integer>> hm = new HashMap<Long, HashMap<String, Integer>>();
    HashMap<Long, ArrayList<HashMap<String,Integer>>> finalHM = new HashMap<Long, ArrayList<HashMap<String,Integer>>>();

    /*
     * returns the sorted String, this sorted string is the key for which the totalCount is calculated
     * Sorting is done, since for the same anagram received in a particular dateHr, the count needs to be updated
     * */
    public String returnSortedString(String s){
        char[] arr = s.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }

    /*
     * Updates the Hashmap, hashMap has the format {timeHr:Hashmap<sortedString. TotalCount>}
     * */

    public void updateHashMap(String anagram, int cnt, Long time){

        if (hm.containsKey(time)){
            HashMap<String, Integer> hmv = hm.get(time);
            hmv.put(anagram, hmv.getOrDefault(anagram,0)+cnt);
        }else{
            HashMap<String, Integer> hmv = new HashMap<String, Integer>();
            hmv.put(anagram, cnt);
            hm.put(time, hmv);
        }
    }

    /*
     * convert the output to desired format
     * i.e. <Long, HashMap<String, Integer>> to <Long, ArrayList<HashMap<String,Integer>>>
     * */
    public void convertHashMapToArrayOfHashMaps(){
        for (Long key: hm.keySet()){
            ArrayList<HashMap<String,Integer>> arr = new ArrayList<HashMap<String,Integer>>();
            HashMap valueHashMap = hm.get(key);
            for (Object k: valueHashMap.keySet()){
                HashMap<String, Integer> hmSingleEntry = new HashMap<>();
                hmSingleEntry.put((String)k,(int)valueHashMap.get(k));
                arr.add(hmSingleEntry);
            }
            finalHM.put(key, arr);
        }
    }

    /*
     * parses the epoch time to get the key in format - yyyyMMddhh
     * */
    public Long GetDateHourFromEpoch(String epoch){
        Long millis = Long.parseLong(epoch);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh");
        Long dateLong=Long.parseLong(sdf.format(millis));
        return dateLong;

    }


    public void processFile(String input , Boolean hasHeader){
        try{
            /*
             * leverging BufferredReader + FileReader to read the lines in the file
             * This allows bufferred reading of larger files in chunks
             *
             * */
            FileReader fileReader = new FileReader(input);
            BufferedReader br = new BufferedReader(fileReader);

            String line;
            if (hasHeader){
                line = br.readLine();
            }
            while ((line = br.readLine()) != null){
                // throw exception if there is a blank line
                if (line.length() == 0){
                    throw new IllegalArgumentException(" Incorrect input, length of line is 0 ");
                }

                String[] arr = line.split(",");
                if (arr.length != 3){
                    // throw exception if number of splits is not == 3
                    throw new IllegalArgumentException(" Incorrect input, NUmber of splits should be 3 ") ;
                }

                // get the datehour, anagram and update HashMap
                Long datehour = GetDateHourFromEpoch(arr[0]);
                String anagram = returnSortedString(arr[1]);
                updateHashMap(anagram, Integer.parseInt(arr[2]), datehour);
                //convert to required format
                convertHashMapToArrayOfHashMaps();
            }

            //cleanup
            fileReader.close();
            br.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


}
