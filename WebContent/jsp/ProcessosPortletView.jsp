<%@page session="false" contentType="text/html" pageEncoding="ISO-8859-1" import="java.util.*,javax.portlet.*,com.ibm.processos.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>        
<%@taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model" %>        
<%@taglib prefix="c_rt" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt"%>
<portlet:defineObjects/>
<portlet-client-model:init>
      <portlet-client-model:require module="ibm.portal.xml.*"/>
      <portlet-client-model:require module="ibm.portal.portlet.*"/>   
</portlet-client-model:init> 
<head>
<script type="text/javascript">
	function getValue(){
		var tmp=document.getElementById("member").value;
		document.getElementById("value").value = tmp;
	}
</script>
</head>
<body>
	<form method="POST" action="<portlet:actionURL />">
		<input type="hidden" id="value" name="value"/>
		<label for="member">Responsável: </label>
		<select id="member" name="member" onchange="javascript:getValue();">
			<option selected>Selecione um responsável: </option>
			<c_rt:forEach items="${requestScope.members}" var="member" >
				<option value="${member.uid}">${member.name}</option>	 
			</c_rt:forEach>
		</select>
		<br />
		<label for="name">Processo: </label>
		<input id="name" type="text" name="name" size="20">
		<br />
		<label for="description">Descrição: </label>
		<input id="description" type="text" name="description" size="20">
		<br />
		<p>
			<input type="submit" name="submit" value="Adicionar">
			<input type="reset" value="Limpar">
		</p>
	</form>
	
	<p>
		${requestScope.message}
	</p>
</body>
