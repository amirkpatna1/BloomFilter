package com.bloomfilterdemo.bloomfilter.service.impl;

import com.bloomfilterdemo.bloomfilter.service.HashService;
import org.springframework.stereotype.Service;

@Service
public class AddCharsHashService implements HashService {
    @Override
    public int getHash(String key) {
        int hash = 0;
        for (char c : key.toCharArray()) {
            hash += (int) c;
        }
        return hash;
    }
}
