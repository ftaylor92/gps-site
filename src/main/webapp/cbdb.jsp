<%@ page language="java" %> <!-- import="java.sql.*" -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<html>
	<head></head>
	<body>
<sql:query dataSource="jdbc/testCBDB" var="rst" scope="request">
	select name, value from keyvalue
</sql:query>
<c:forEach items="${rst.rows}" var="row">
	${row.name} ${row.value}<br />
</c:forEach>
</body>
</html>