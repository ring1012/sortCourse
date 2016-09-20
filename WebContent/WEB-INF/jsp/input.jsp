<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${ctx}/Resources/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<title>输入信息</title>
</head>
<script>
	$(document)
			.ready(
					function() {
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
																	+ "<td><a href=\'#\' onclick=\'deltr("
																	+ _len
																	+ ")\'>删除</a></td>"
																	+ "</tr>");
										})
					})

	//删除<tr/>
	var deltr = function(index) {
		var _len = $("#tab tr").length;
		$("tr[id='" + index + "']").remove();//删除当前行
		for (var i = index + 1, j = _len; i < j; i++) {
			$("tr[id=\'" + i + "\']").attr("id",(i-1));
			$("tr[id=\'" + (i-1) + "\'] td:nth-child(1)").html(i-1);
			$("tr[id=\'" + (i-1) + "\'] td:nth-child(6) input:checkbox").val(i-1)
		}

	}
</script>
</head>
<body>
	<div class="container">


		<form action="/sortCourse/deal.action" method="post">
			<table id="tab" border="1"
				class="table table-hover table-striped table-bordered">
				<tr>
					<td>序号</td>
					<td>教师名</td>
					<td>课程名</td>
					<td>任教班级数目</td>
					<td>每周课时数目</td>
					<td>是否班主任</td>
					<td>操作</td>
				</tr>
			</table>
			<div
				style="border: 2px; border-color: #00CC00; margin-left: 20%; margin-top: 20px">
				<input type="button" id="but" value="增加" />
			</div>
			<input type="submit" value="提交">
		</form>
	</div>
</body>
</html>