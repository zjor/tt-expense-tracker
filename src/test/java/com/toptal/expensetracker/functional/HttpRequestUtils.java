package com.toptal.expensetracker.functional;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Sergey Royz
 * Date: 03.01.2015
 */
public class HttpRequestUtils {

    public static UrlEncodedFormEntity createUrlEncodedEntity(String... params) throws UnsupportedEncodingException {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Number of arguments should be even");
        }
        List<NameValuePair> pairs = new ArrayList<>(params.length / 2);
        for (int i = 0; i < params.length; i += 2) {
            pairs.add(new BasicNameValuePair(params[i], params[i + 1]));
        }

        return new UrlEncodedFormEntity(pairs);
    }

}
