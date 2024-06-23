package com.bloomfilterdemo.bloomfilter.service.impl;

import com.bloomfilterdemo.bloomfilter.service.AdvancedHashService;
import com.bloomfilterdemo.bloomfilter.service.HashService;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class MD5HashService implements AdvancedHashService {
    @Override
    public int getHash(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(key.getBytes());
            return Math.abs(ByteBuffer.wrap(hashBytes).getInt());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
