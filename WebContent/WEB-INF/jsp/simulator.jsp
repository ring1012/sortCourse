<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.huan.model.Teacher"%>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${ctx}/Resources/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<title>模拟数据展示</title>
</head>
<script>
	$(document)
			.ready(
					function() {
						$("input").attr("readonly",true);
						//<tr/>居中
						$("#tab tr").attr("align", "center");

						//增加<tr/>
						$("#but")
								.click(
										function() {
											var _len = $("#tab tr").length;
											$("#tab")
													.append(
															"<tr id="+_len+" align='center'>"
																	+ "<td>"
																	+ _len
																	+ "</td>"
																	+ "<td><input type='text' name='teacherName' /></td>"
																	+ "<td><input type='text' name='courseName' /></td>"
																	+ "<td><input type='number' name='perWeekClassNum'  min='1' /></td>"
																	+ "<td><input type='number' name='perWeekTimeNum'  min='1' /></td>"
																	+ "<td><input type='checkbox' name='IsHead' value='"+_len+"'/></td>"
																	+ "<td><input type='checkbox' name='IsNext' value='"+_len+"'/></td>"
																	+ "<td><a href=\'#\' onclick=\'deltr("
																	+ _len
																	+ ")\'>删除</a></td>"
																	+ "</tr>");
										})
					})

	$(function() {
		$('#btnConfirm').click(
				function() {
					var milasUrl = {};//新建对象，用来存储所有数据
					var count = 0;
					var subMilasUrlArr = {};//存储每一行数据
					var tableData = {};
					$("#tab tr").each(function(trindex, tritem) {//遍历每一行
						tableData[trindex] = new Array();
						$(tritem).find("input").each(function(tdindex, tditem) {
							tableData[trindex][tdindex] = $(tditem).val();//遍历每一个数据，并存入
							if ($(tditem).is(':checked')) {
								count++;
							}
							subMilasUrlArr[trindex] = tableData[trindex];//将每一行的数据存入
						});
					});
					for ( var key in subMilasUrlArr) {
						milasUrl[key] = subMilasUrlArr[key];//将每一行存入对象
					}
					var vaildate = true;
					if ($(".classNum").val() == "") {
						alert("班级数目不能为空")
						return false;
					}
					if ($(".morning").val() == "") {
						alert("年级信息不能为空")
						return false;
					}
					if ($(".afternoon").val() == "") {
						alert("年级信息不能为空")
						return false;
					}
					if ($(".saturday").val() == "") {
						alert("年级信息不能为空")
						return false;
					}
					if ($(".sunday").val() == "") {
						alert("年级信息不能为空")
						return false;
					}
					if (count != $(".classNum").val()) {
						alert("班级数目与班主任老师数目不符")
						return false;
					}
					var everyRow = {};
					for ( var key in milasUrl) {
						everyRow[key] = milasUrl[key];
						for ( var one in everyRow[key]) {
							if (everyRow[key][one] == "") {
								alert("第" + key + "行" + "第"
										+ (parseInt(one) + 1) + "列不能为空")
								return false;
							}
						}
					}
// 					var sum1 = 0;
// 					for ( var key in milasUrl) {
// 						everyRow[key] = milasUrl[key];
// 						sum1 += everyRow[key][2] * everyRow[key][3];
// 					}
					var mNum = parseInt($(".morning").val());
					var aNum = parseInt($(".afternoon").val());
					var saNum = parseInt($(".saturday").val());
					var suNum = parseInt($(".sunday").val());
// 					var sum2 = count * (5 * (mNum + aNum) + saNum + suNum);
// 					if (sum1 != sum2) {
// 						alert("年级信息显示的课时总数目与教师总课时数目不相等")
// 						return false;
// 					}

					$('form').submit();
				})

	})

	var deltr = function(index) {
		var _len = $("#tab tr").length;
		$("tr[id='" + index + "']").remove();//删除当前行
		for (var i = index + 1, j = _len; i < j; i++) {
			$("tr[id=\'" + i + "\']").attr("id", (i - 1));
			$("tr[id=\'" + (i - 1) + "\'] td:nth-child(1)").html(i - 1);
			$("tr[id=\'" + (i - 1) + "\'] td:nth-child(6) input:checkbox").val(
					i - 1);
			$("tr[id=\'" + (i - 1) + "\'] td:nth-child(7) input:checkbox").val(
					i - 1);
			$("tr[id=\'" + (i - 1) + "\'] td:nth-child(8) a").attr("onclick",
					"deltr(" + (i - 1) + ")");
		}

	}
</script>
</head>
<body>
	<div class="container">

		<br />


		<form action="/sortCourse/deal.action" method="post"
			class="documentForm" target="_blank">
			<h3 class="text-center">年级信息</h3>
			班级数目:<input type="number" name="classNum" class="classNum" min='1'
				value="15">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			上午几节课:<input type="number" name="morning" class="morning" min='1'
				value="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 下午几节课:<input
				type="number" name="afternoon" class="afternoon" min='1' value="3">
			<br /> <br /> 周六一共几节课:<input type="number" name="saturday"
				class="saturday" min='0' value="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			周日一共几节课:<input type="number" name="sunday" class="sunday" min='0'
				value="0">


			<h3 class="text-center">教师信息</h3>
			<table id="tab" border="1"
				class="table table-hover table-striped table-bordered">
				<tr>
					<td>序号</td>
					<td>教师名</td>
					<td>课程名</td>
					<td>任教班级数目</td>
					<td>每周每班课时数目</td>
					<td>是否班主任</td>
					<td>是否单/双周</td>
				</tr>

				<%
					List<Teacher> teachers = (List<Teacher>) request.getAttribute("teachers");

					for (int k = 0; k < teachers.size(); k++) {
				%>
				<tr id="<%=(k + 1)%>">
					<td><%=(k + 1)%></td>
					<td><input type="text" name="teacherName"
						value=<%=teachers.get(k).getTeacherName()%>></td>
					<td><input type="text" name="courseName"
						value=<%=teachers.get(k).getCourseName()%>></td>
					<td><input type="number" name="perWeekClassNum"
						value=<%=teachers.get(k).getPerWeekClassNum()%>></td>
					<td><input type="number" name="perWeekTimeNum"
						value=<%=teachers.get(k).getPerWeekTimeNum()%>></td>
					<%
						if (teachers.get(k).isIsHead()) {
					%>
					<td><input type="checkbox" name="IsHead" checked="true"
						value=<%=(k + 1)%> /></td>
					<%
						} else {
					%>
					<td><input type="checkbox" name="IsHead" value=<%=(k + 1)%> /></td>
					<%
						}
					%>
					<%
						if (teachers.get(k).isIsNext()) {
					%>
					<td><input type="checkbox" name="IsNext" checked="true"
						value=<%=(k + 1)%> /></td>
					<%
						} else {
					%>
					<td><input type="checkbox" name="IsNext" value=<%=(k + 1)%> /></td>
					<%
						}
					%>
				</tr>
				<%
					}
				%>
			</table>
			<table>
				<td align="center"></td>
				<td align="center">
					<button type="button" class="btn btn-danger btn-lg" id="btnConfirm"
						style="aligen: center">提交</button>
				</td>
			</table>


		</form>
	</div>
</body>
</html>