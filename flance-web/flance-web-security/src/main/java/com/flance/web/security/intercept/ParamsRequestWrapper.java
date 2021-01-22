package com.flance.web.security.intercept;


import com.flance.web.security.utils.HttpContextUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * requestBody复读wrapper
 * @author jhf
 */
public class ParamsRequestWrapper extends HttpServletRequestWrapper {

    private String tempBody;

    public ParamsRequestWrapper(HttpServletRequest request) {
        super(request);
        tempBody = HttpContextUtils.getBodyString(request);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tempBody.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public int readLine(byte[] b, int off, int len) throws IOException {
                return super.readLine(b, off, len);
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return this.tempBody;
    }

    public void setBody(String body) {
        this.tempBody = body;
    }


}
