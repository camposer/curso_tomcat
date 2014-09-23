<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>IJavaBlog</title>
</head>
<body>
	<%@ include file="cabecera.jsp" %>

	<div id="errores">
		<c:forEach var="e" items="${errores}">
			${e}<br/>
		</c:forEach>
	</div>
	
	<h1>Regístrate</h1>
	<form:form action="agregarUsuario.blog" commandName="usuarioForm" method="post">
		<table>
			<tr>
				<td>Correo:</td>
				<td><form:input path="correo"/> </td> 
			</tr>		
			<tr>
				<td>Contraseña:</td>
				<td><form:input path="clave"/> </td> 
			</tr>		
			<tr>
				<td>Confirmación de contraseña:</td>
				<td><form:input path="confirmacionClave"/> </td> 
			</tr>		
			<tr>
				<td>Fecha de nacimiento:</td>
				<td><form:input path="fechaNacimiento"/> </td> 
			</tr>		
			<tr>
				<td colspan="2">
					<input type="submit" value="Dame de alta!"/>				
				</td>
			</tr>		
		</table>	
	</form:form>
	<%@ include file="pie.jsp" %>
</body>
</html>