<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="mx.com.ferbo.model.Usuario"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.InputStream"%>
<% Usuario usr = (Usuario) session.getAttribute("usuario"); %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":" + request.getServerPort() + path;
response.setHeader("Access-Control-Allow-Origin", "http://localhost");

Properties prop = new Properties();
InputStream in = getClass().getResourceAsStream("/config/gestion.properties");
prop.load(in);
String ipSgp = prop.getProperty("sgp.url");
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title</title>
		
		<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.js"></script>
		<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.13.0/jquery-ui.min.js"></script>
		<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.13.0/jquery-ui.css">
		
		<style type="text/css">
			.spinner {
			  border: 4px solid rgba(0, 0, 0, 0.1);
			  width: 36px;
			  height: 36px;
			  border-radius: 50%;
			  border-left-color: #09f;
			 margin: 0px auto;
			  animation: spin 1s ease infinite;
			}
			
			@keyframes spin {
				  0% {
				    transform: rotate(0deg);
				  }
				
				  100% {
				    transform: rotate(360deg);
				  }
			}
			
			.login-body {
				height: 100vh;
				font-family: "latoregular", "Trebuchet MS", Arial, Helvetica, sans-serif;
				font-size: 16px;
				margin: 0;
				padding: 100px 0 0 0;
				background-image: linear-gradient(to top, #6b77a1, #737ea5 3%, #9599b3 15%, #b1b0bf 28%,
					#c7c1c8 41%, #d6cdcf 57%, #dfd5d3 74%, #e2d7d4);
				background-image: -ms-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%, #B1B0BF
					28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				background-image: -moz-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%,
					#B1B0BF 28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				background-image: -o-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%, #B1B0BF
					28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0, #6B77A1),
					color-stop(3, #737EA5), color-stop(15, #9599B3),
					color-stop(28, #B1B0BF), color-stop(41, #C7C1C8),
					color-stop(57, #D6CDCF), color-stop(74, #DFD5D3),
					color-stop(100, #E2D7D4));
				background-image: -webkit-linear-gradient(bottom, #6B77A1 0%, #737EA5 3%, #9599B3 15%,
					#B1B0BF 28%, #C7C1C8 41%, #D6CDCF 57%, #DFD5D3 74%, #E2D7D4 100%);
				filter: progid:DXImageTransform.Microsoft.gradient( startColorstr="#E2D7D4",
					endColorstr="#6B77A1", GradientType=0);
				box-sizing: border-box;
				background-repeat: no-repeat;
				background-attachment: fixed;
			}
			
			.dialog{
				text-align: center; 
				display: none;
			}
		
		</style>
		
		<script type="text/javascript">
		
			function registry() {
				myAlert();
				var num ="<%=usr.getNumEmpleado() %>";
		
				$.ajax({
					type : "GET",
					dataType : "json",
					url : "<%=ipSgp%>"+"/detEmpleado/empleadoBio?numEmp="+num,
					success : function(jsonObj) {
						var respuesta = jsonObj;
						console.log("Solicitando peticion al microservicio biometrico...");
						invokeScan(respuesta.huella, respuesta.huella2);				
					},
					error : function(jsonObj) {
						var respuesta = JSON.parse(jsonObj.responseText);
						myAlert(respuesta);
					}
				});
			}
			
			function myAlert() {
				$("#dialog-message").dialog({
					title : "Aviso del sistema"
				});
				$("#dialog-message").dialog("open");
			}
			
			function closeDialog (){
		 		$("#dialog-message").dialog("close");
		 	}
			
		 	setTimeout(() => {closeDialog()}, 2700);
		
		 	
			function invokeScan(f1, f2) {
			
				var obj = new Object();
				obj.TpAccion = "VerifyFingerprint";
				obj.FingerPrinToVerify = listado(f1, f2);
				var jsonString = JSON.stringify(obj);
				myAlert();		
				$.ajax({
							type : "POST",
							dataType : 'json',
							data : jsonString,
							url : "http://localhost:23106",
							timeout: 60000,
							success : function(jsonObj) {
								if (jsonObj.VerifyBiometricData == true) {
									window.location.href = "<%=basePath%>"+"/dashboard.xhtml";
								} else {
									window.location.href = "<%=basePath%>"+"/logout";
								}
							},
							error : function(jsonObj) {
								myAlert("No hay respuesta del lector de huella.");
							}
						});	
			}
		
			function listado(f1, f2) {
				const arreglo = [];
				arreglo.push(f1);
				arreglo.push(f2);
				return arreglo;
			};
			
		</script>
	
	</head>

<body onload="registry();" class="login-body"   >

		<div id="dialog-message"  class="dialog">
			<p>Cargando Scanner ...</p>
			<div class="spinner" ></div>
		</div>

</body>
</html>


