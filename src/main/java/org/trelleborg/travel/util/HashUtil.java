package org.trelleborg.travel.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class HashUtil {

    private static final HashFunction SHA_256 = Hashing.sha256();

    public static String hash(String value) {
        return value == null ? null :
               SHA_256.hashString(value, StandardCharsets.UTF_8).toString();
    }
}
