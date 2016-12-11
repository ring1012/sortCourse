<%@page import="com.huan.sort.util.startSortCourse"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.huan.model.BaseTeacher"%>
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
<link href="${ctx}/Resources/css/myStyle.css" rel="stylesheet"
	type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<title>排课结果</title>

<style type="text/css">
#myOper {
	width: 120px;
	position: fixed;
	right: 0%;
	top: 10%;
}
</style>
</head>
<script>
	$(function() {
		$("#accordion").accordion({
			collapsible : true
		});
	});

	var classNums;
	var lessonNums;
	var changeStr;
	var fixTable;
	$(document).ready(function() {
		classNums = $("tr[id='rooms'] td").length - 1;
		lessonNums = ($("tr").length / 2 - 8) / 7;
		changeStr = new Array(classNums);
		fixTable = new Array(classNums);
		for (var i = 0; i < classNums; i++) {
			fixTable[i] = new Array((lessonNums * 7));
			for (var j = 0; j < lessonNums * 7; j++) {
				fixTable[i][j] = 0;
			}
		}
		for (var i = 0; i < classNums; i++) {
			changeStr[i] = "";
		}
	})

	function allowDrop(ev) {
		ev.preventDefault();
	}

	function drop(ev) {

		ev.preventDefault();
		var dropPosotion = new Array(2);
		dropStr = ev.target.className;
		dropPosotion = dropStr.split(" ");

		var dragPosition = new Array(2)
		dragStr = ev.dataTransfer.getData("Text");
		dragPosition = dragStr.split(" ");
		if (dragPosition[0] != dropPosotion[0]) {
			alert("不允许跨班级");
		} else if (dragPosition[1] != dropPosotion[1]) {
			var dragTag = "[class='" + dragPosition[0] + " " + dragPosition[1]
					+ "']";
			var dropTag = "[class='" + dropPosotion[0] + " " + dropPosotion[1]
					+ "']";
			temp0 = $("body").find(dragTag).eq(0).html();
			temp1 = $("body").find(dragTag).eq(1).html();
			$("body").find(dragTag).eq(0).html(
					$("body").find(dropTag).eq(0).html());
			$("body").find(dragTag).eq(1).html(
					$("body").find(dropTag).eq(1).html());
			$("body").find(dropTag).eq(0).html(temp0);
			$("body").find(dropTag).eq(1).html(temp1);
			changeStr[dragPosition[0]] += dragPosition[1] + "="
					+ dropPosotion[1] + "&";
		}
	}

	function drag(ev) {
		ev.dataTransfer.setData("Text", ev.target.className);
	}
	// 	var fix = false;

	// 	function fixSite() {
	// 		fix = true;
	// 		$("td").css({
	// 			cursor : "pointer"
	// 		});
	// 	}
	// 	function cancelFix() {
	// 		fix = false;
	// 		$("td").css({
	// 			cursor : "auto"
	// 		});

	// 	}

	function fixCell(ev) {
		var position = new Array(2);
		positionStr = ev.target.className;
		position = positionStr.split(" ");

		if (fixTable[position[0]][position[1]] == 0) {
			var tag = "[class='" + position[0] + " " + position[1] + "']";
			$("body").find(tag).eq(0).attr("style", "background:red");
			$("body").find(tag).eq(1).attr("style", "background:red");
			fixTable[position[0]][position[1]] = 1;
		} else {
			var tag = "[class='" + position[0] + " " + position[1] + "']";
			$("body").find(tag).eq(0).attr("style", "background:white");
			$("body").find(tag).eq(1).attr("style", "background:white");
			fixTable[position[0]][position[1]] = 0;
		}
	}

	function changeSubmit() {
		var form = $("<form method='post' target='_blank'></form>");
		form.attr({
			"action" : "/sortCourse/change.action"
		});
		var args = new Array(2);
		args[0] = fixTable;
		args[1] = changeStr;
		var input = $("<input type='text'>");
		input.attr({
			"name" : "fixTable"
		});
		input.val(args[0]);
		form.append(input);
		input = $("<input type='text'>");
		input.attr({
			"name" : "changeStr"
		});
		input.val(args[1]);
		form.append(input);
		form.submit();

	}
	$(document).ready(function() {
		$(".myShow").click(function() {
			if ($(".myInfo").css("display") == "none") {
				$(".myInfo").css("display", "inline");
			} else {
				$(".myInfo").css("display", "none");
			}
		});
	});
</script>
<body>
	<div class="left">
		<div id="accordion" class="container">
			<%
				Object result = request.getAttribute("result");
				Object myCourse = session.getAttribute("myCourse");
				if (result == null || myCourse == null) {
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
					int odd[][] = ret.oddSheet;
					int even[][] = ret.evenSheet;
					List<BaseTeacher> datas = ((startSortCourse) myCourse).datas;
					int classNum = ret.classNum;
			%>


			<h3>年级老师-结果</h3>
			<div>
				<table id="tab" border="1"
					class="table table-hover table-striped table-bordered">
					<tr id="rooms">
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
										if (sheet[i][j] >= 0) {
						%>
						<td class="<%=String.format("%d %d", i, j)%>" draggable="true"
							ondragstart="drag(event)" ondrop="drop(event)"
							ondragover="allowDrop(event)" onclick="fixCell(event)"><%=datas.get(sheet[i][j]).teacherName%></td>
						<%
							} else {
											String temp = "";
											if (odd[i][j] >= 0) {
												temp += String.format("%so", datas.get(odd[i][j]).teacherName);
											}
											if (even[i][j] >= 0) {
												temp += String.format("%se", datas.get(even[i][j]).teacherName);
											}
											if (temp != "") {
						%>
						<td class="<%=String.format("%d %d", i, j)%>" draggable="true"
							ondragstart="drag(event)" ondrop="drop(event)"
							ondragover="allowDrop(event)" onclick="fixCell(event)"><%=temp%></td>

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
						<%
							}
						%>
					
				</table>
			</div>


			<button type="button" class="btn btn-info btn-lg"
				style="aligen: center" id="btnConfirm" onclick="changeSubmit()">提交</button>
			<br /> <br /> <br /> <br /> <br />
		</div>
	</div>
	<div class="right">
		<h6 class="myShow">tips(单击显示或隐藏)</h6>
		<div class="myInfo">
			分为教师处理结果和 <br /> 课程处理结果表单; <br /> 对两张表单的操作会同步; <br /> 拖动以调整位置; <br />
			单击以固定位置. <br />
		</div>
	</div>
</body>
</html>