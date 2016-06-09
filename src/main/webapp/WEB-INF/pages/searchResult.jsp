<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>SearchResult</title>
</head>
<body>					
	<c:forEach items="${result.allResults }" var="res">
		<div>
			<a href="${res.url }" target="_blank">${res.title }</a>
		</div>
		<br>
		<div>
			${res.content }
		</div>
		
		<div><img src="${res.imageUrl }"> ${res.url }</div> 
		<br>
		<hr>
	</c:forEach>
</body>
</html>