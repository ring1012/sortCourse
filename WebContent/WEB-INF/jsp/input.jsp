<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<title>输入信息</title>
</head>
<script>
	$(document).ready(
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
																	+ "<td>Dynamic TR"
																	+ _len
																	+ "</td>"
																	+ "<td><input type='text' name='desc"+_len+"' id='desc"+_len+"' /></td>"
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
			var nextTxtVal = $("#desc" + i).val();
			$("tr[id=\'" + i + "\']").replaceWith(
					"<tr id=" + (i - 1) + " align='center'>" + "<td>" + (i - 1)
							+ "</td>" + "<td>Dynamic TR" + (i - 1) + "</td>"
							+ "<td><input type='text' name='desc" + (i - 1)
							+ "' value='" + nextTxtVal + "' id='desc" + (i - 1)
							+ "'/></td>" + "<td><a href=\'#\' onclick=\'deltr("
							+ (i - 1) + ")\'>删除</a></td>" + "</tr>");
		}

	}
</script>
</head>
<body>
<form action="/sortCourse/deal.action" method="post">
	<table id="tab" border="1" width="60%" align="center"
		style="margin-top: 20px" >
		<tr>
			<td width="20%">序号</td>
			<td>标题</td>
			<td>描述</td>
			<td>操作</td>
		</tr>
	</table>
	<div
		style="border: 2px; border-color: #00CC00; margin-left: 20%; margin-top: 20px">
		<input type="button" id="but" value="增加" />
	</div>
	<input type="submit" value="提交">
	</form>
</body>
</html>