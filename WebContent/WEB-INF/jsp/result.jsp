<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.huan.model.allData"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.huan.definition.ResultType"%>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${ctx}/Resources/css/jquery-ui-1.9.2.custom.min.css"
	rel="stylesheet" type="text/css">
<link href="${ctx}/Resources/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<title>排课结果</title>
</head>
 <script>
 $(function() {
	    $( "#accordion" ).accordion({
	      collapsible: true
	    });
	  });
  </script>
<body>


<div id="accordion" class="container">
<%
			Object result = request.getAttribute("result");

			if (result == null) {
				out.print("<h1>无解<h1/>");
			} else {
				Map<Integer, String> indexMap = new HashMap<Integer, String>();
				indexMap.put(1, "一");
				indexMap.put(2, "二");
				indexMap.put(3, "三");
				indexMap.put(4, "四");
				indexMap.put(5, "五");
				indexMap.put(6, "六");
				indexMap.put(7, "日");
				ResultType ret = (ResultType) result;
				int lessonNum = ret.lessonNum;
				int needLessons = lessonNum * 7;
				boolean everyWeek[] = ret.everyWeek;
				int sheet[][] = ret.sheetInfor;
				List<allData> datas = ret.datas;
				int classNum = ret.classNum;
		%>


  <h3>年级老师-结果</h3>
  <div>
    <table id="tab" border="1"
			class="table table-hover table-striped table-bordered">
			<tr>
				<td></td>
				<%
					for (int i = 0; i < classNum; i++) {
				%>
				<td><%=String.format("%d 班", i + 1)%></td>
				<%
					}
				%>

			</tr>
			<%
				for (int j = 0; j < needLessons; j++) {
						if (j % lessonNum == 0) {
			%>
			<tr>
				<td rowspan="<%=(lessonNum + 1)%>">星期<%=indexMap.get(j / lessonNum + 1)%></td>
			</tr>
			<%
				}
			%>
			<tr>

				<%
					for (int i = 0; i < classNum; i++) {
								if (everyWeek[j] == false) {
				%>
				<td><%=datas.get(sheet[i][j]).teacherName%></td>
				<%
					} else {
				%>
				<td></td>
				<%
					}
				%>




				<%
					}
				%>

				<%
					}
				%>


			
		</table>
  </div>
  <h3>年级课程-结果</h3>
  <div>
   <table id="tab" border="1"
			class="table table-hover table-striped table-bordered">
			<tr>
				<td></td>
				<%
					for (int i = 0; i < classNum; i++) {
				%>
				<td><%=String.format("%d 班", i + 1)%></td>
				<%
					}
				%>

			</tr>
			<%
				for (int j = 0; j < needLessons; j++) {
						if (j % lessonNum == 0) {
			%>
			<tr>
				<td rowspan="<%=(lessonNum + 1)%>">星期<%=indexMap.get(j / lessonNum + 1)%></td>
			</tr>
			<%
				}
			%>
			<tr>

				<%
					for (int i = 0; i < classNum; i++) {
								if (everyWeek[j] == false) {
				%>
				<td><%=datas.get(sheet[i][j]).courseName%></td>
				<%
					} else {
				%>
				<td></td>
				<%
					}
				%>




				<%
					}
				%>

				<%
					}
				%>


				<%
					}
				%>
			
		</table>
		
  </div>
  <br/>	<br/>	<br/>	<br/>	<br/>
</div>

</body>
</html>