package com.dzh.springsecurity02.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

	public static void responseJson(HttpServletResponse response, int status, Object data) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);

			ObjectMapper mapper = new ObjectMapper();
			response.getWriter().write(mapper.writeValueAsString(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
