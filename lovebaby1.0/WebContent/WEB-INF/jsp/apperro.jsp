<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Map map=new HashMap();
map.put("flag", "0");
JSONObject jsonObject = JSONObject.fromObject(map);
response.getWriter().write(jsonObject.toString());
%>