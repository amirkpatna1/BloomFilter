package com.bloomfilterdemo.bloomfilter.service.impl;

import com.bloomfilterdemo.bloomfilter.service.HashService;
import org.springframework.stereotype.Service;

@Service
public class PolynomialRollingHashService implements HashService {
    @Override
    public int getHash(String key) {
        int p = 31;
        int m = (int) 1e9 + 9;
        long hash = 0;
        long pPower = 1;
        for (char c : key.toCharArray()) {
            hash = (hash + (c - 'a' + 1) * pPower) % m;
            pPower = (pPower * p) % m;
        }

        return (int) Math.abs(hash);
    }
}
