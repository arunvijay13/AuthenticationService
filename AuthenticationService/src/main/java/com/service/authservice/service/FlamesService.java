package com.service.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlamesService {

    @Autowired
    @Qualifier("flames")
    Map<Character, String> flames;

    public String calculateLove(String name1, String name2) {
        HashMap<Character, Integer> map = new HashMap<>();
        if(name1.equalsIgnoreCase(name2))
            return flames.get('s');

        for(char i : name1.toCharArray()){
            map.put(i, map.getOrDefault(i, 0)+1);
        }

        int mismatchedLetterCount = 0;

        for(char i : name2.toCharArray()) {
            if(map.containsKey(i)) {
                int count = map.get(i);
                if(count == 0){
                    map.compute(i, (k,v) -> v+1);
                } else {
                    map.compute(i, (k,v) -> v-1);
                }
            } else {
                mismatchedLetterCount++;
            }
        }

        for(Map.Entry<Character, Integer> entry : map.entrySet()) {
            mismatchedLetterCount += entry.getValue();
        }

        StringBuilder sb = new StringBuilder("flames");
        int n = sb.length();
        while(n > 1) {
            int deletePos = mismatchedLetterCount % n;
            sb.deleteCharAt(deletePos-1);
            n-=1;
        }

        return flames.get(sb.toString().charAt(0));

    }
}
