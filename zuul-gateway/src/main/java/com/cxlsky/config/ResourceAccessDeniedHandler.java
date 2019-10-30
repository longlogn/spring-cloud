package com.cxlsky.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * className ResourceAccessDenyHandler
 * description ResourceAccessDenyHandler
 *
 * @author cxl
 * @date 2018-11-1
 */
@Component
public class ResourceAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String json = JSONObject.toJSONString(e.getMessage());
        writer.write(json);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        writer.flush();
        writer.close();
    }
}
