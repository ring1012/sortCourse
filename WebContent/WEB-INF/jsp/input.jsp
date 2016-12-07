<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${ctx}/Resources/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<link href="${ctx}/Resources/css/myStyle.css" rel="stylesheet"
	type="text/css">
<link href="${ctx}/Resources/css/jquery-ui-1.9.2.custom.min.css"
	rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>
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
																	+ "<td><input type='checkbox' name='IsNext' value='"+_len+"'/></td>"
																	+ "<td><a href=\'#\' onclick=\'deltr("
																	+ _len
																	+ ")\'>删除</a></td>"
																	+ "</tr>");
										})
					})
$(function() {
		$('#simulator').click(
				function() {
					top.window.location.href  ="/sortCourse/simulator.action";	
				})})
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
					var sum2 = count * (5 * (mNum + aNum) + saNum + suNum);
					if ($(".saturday").val() > mNum + aNum) {
						alert("周六课时不能大于上午和下午课时之和");
						return false;
					}
					if ($(".sunday").val() > mNum + aNum) {
						alert("周日课时不能大于上午和下午课时之和");
						return false;
					}
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
	
	

	$(document).ready(function(){
		  $(".myShow").click(function(){
			if($(".myInfo").css("display")=="none"){
				$(".myInfo").css("display","inline");
			}else{
				$(".myInfo").css("display","none");
			}
		  });
		});
</script>
</head>
<body>
	<div class="container">
		<div class="left">
			<br />
			<h3 class="text-center">年级信息</h3>
			<form action="/sortCourse/deal.action" method="post"
				class="documentForm" target="_blank">
				班级数目:<input type="number" name="classNum" class="classNum" min='1'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				上午几节课:<input type="number" name="morning" class="morning" min='1'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				下午几节课:<input type="number" name="afternoon" class="afternoon"
					min='1'> <br /> <br /> 周六一共几节课:<input type="number"
					name="saturday" class="saturday" min='0' value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				周日一共几节课:<input type="number" name="sunday" class="sunday" min='0'
					value="0">
				空白课可以放在上午最后一节<input type="checkbox" name="allowMorning" />

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
						<td>操作</td>
					</tr>
				</table>
				<table>
					<tr>
						<td align="center"><button type="button"
								class="btn btn-primary btn-lg" id="but">增加</button></td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td align="center">
							<button type="button" class="btn btn-danger btn-lg"
								id="btnConfirm" style="aligen: center">提交</button>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td align="center">
							<button type="button" class="btn btn-danger btn-lg"
								style="aligen: center" id="simulator" style="aligen: center">
								模拟数据
							</button>
						</td>
					</tr>


				</table>

			</form>
			<br />
		</div>
		<div class="right">
			<h6 class="myShow">tips(单击显示或隐藏)</h6>
			<div class="myInfo">
				所有空格必须输入<br /> 点击添加按钮添加教师信息<br /> 删除选项可删除某行<br /> 输入完毕后点击提交按钮<br />
				模拟数据模拟演示<br /> 班主任数目=班级总数<br /> 令a=任教班级数目<br /> 令b=每周每班课时数目<br />
				令c=班级数目<br /> 令m=上午几节课<br /> 令n=下午几节课<br /> 令s=周六一共几节课<br />
				令h=周日一共几节课<br /> 有：∑(a*b)=c*(5*(m+n)+s+h)<br />
			</div>
		</div>
	</div>
</body>
</html>