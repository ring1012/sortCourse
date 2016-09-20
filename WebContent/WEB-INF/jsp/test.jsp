<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<script>
	$(document).ready(function() {
		$("p").click(function() {
			$(this).hide();
		});
	});
</script>
</head>
<body>
	<form action="/sortCourse/testdeal.action" method="post">
		<table id="tab" border="1"
			class="table table-hover table-striped table-bordered">
			<tr>
				<td><input type="text" name="first" value="1"></td>
				<td><input type="checkbox" name="second" ></td>
			</tr>
			<tr>
				<td><input type="text" name="first" value="11"></td>
				<td><input type="checkbox" name="second" value="做过兼职1"></td>
			</tr>
			<tr>
				<td><input type="text" name="first" value="11"></td>
				<td><input type="checkbox" name="second"value="做过兼职2" ></td>
			</tr>
			<tr>
				<td><input type="text" name="first" value="11"></td>
				<td><input type="checkbox" name="second"value="做过兼职3" ></td>
			</tr>
			
		</table>
	<input type="submit"value="tijiao">
	</form>
</body>
</html>
