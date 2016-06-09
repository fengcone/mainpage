<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
<head>
<title>fengcone.cn</title>
<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="/css/index.css" />
<script language="JavaScript" src="/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="/js/index.js"></script>
</head>
<body>
	<input id="search_url" type="hidden" value="${searchUrl }" />
	<!-- 主页Logo -->
	<div id="logo" class="logo">
		<a href="http://www.fengcone.cn"><img src="/pic/logo4.jpg" /> </a>
	</div>

	<!-- 搜索输入框 -->
	<div id="input_search" class="search">
		<input id="main_search" type="text" name="keycontent" value=""
			class="input_search"/>
	</div>
	<!-- 搜索引擎款 -->
	<div class="center_1">
		<span id="baidu_button" class="search_button"> <img src="/pic/baidu.jpg" /> </span> <span
			id="haosou_button" class="search_button"> <img src="/pic/haosou.jpg"/> </span> <span
			id="bing_button" class="search_button"> <img src="/pic/bing.jpg" /> </span> <span
			id="google_button" class="search_button"> <img src="/pic/google.jpg" /> </span>
	</div>
	<!-- 
		<div class="center_2">
			<span id="xinlang"> <img src="/pic/xinlang.jpg" /> </span> <span
				id="xinlang"> <img src="/pic/xinlang.jpg" /> </span> <span
				id="xinlang"> <img src="/pic/xinlang.jpg" /> </span> <span
				id="xinlang"> <img src="/pic/xinlang.jpg" /> </span>
		</div>
		
		
		<div class="center_3">
			<span id="xinlang"> <img src="/pic/xinlang.jpg" /> </span> <span
				id="xinlang"> <img src="/pic/xinlang.jpg" /> </span> <span
				id="xinlang"> <img src="/pic/xinlang.jpg" /> </span> <span
				id="xinlang"> <img src="/pic/xinlang.jpg" /> </span>
		</div>
	 -->
</body>
</html>