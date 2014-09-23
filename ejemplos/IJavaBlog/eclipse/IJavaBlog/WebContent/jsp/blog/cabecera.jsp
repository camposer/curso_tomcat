<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<link href="css/cabecera.css" rel="stylesheet" type="text/css" />
<div id="cabecera">
	<form action="autenticar.blog" method="post">
	<div>IJavaBlog</div>
	<div>
		<c:choose>
			<c:when test="${empty sessionScope.usuario}">
				<input type="text" name="correo" /> 	
				<input type="text" name="clave" /> 	
				<input type="submit" value="Entrar" />
			</c:when>
			<c:otherwise>
				Bienvenido, ${sessionScope.usuario.correo }
				<a href="cerrarSesion.blog">Cerrar sesión</a>		
			</c:otherwise>
		</c:choose>
	</div> 	
	</form>
</div>