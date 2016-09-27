<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
#div1,#div2 {
	width: 488px;
	height: 70px;
	padding: 10px;
	border: 1px solid #aaaaaa;
}
</style>
<script type="text/javascript"
	src="${ctx}/Resources/js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	function allowDrop(ev) {
		ev.preventDefault();
	}

	function drag(ev) {
		ev.dataTransfer.setData("Text", ev.target.id);
	}

	function drop(ev) {
		ev.preventDefault();
		var data = ev.dataTransfer.getData("Text");
		ev.target.appendChild(document.getElementById(data));
	}
</script>
</head>
<body>

	<p>请把 W3School 的图片拖放到矩形中：</p>

	<div id="div1" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	<div id="div2" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
	<br/>
	<div id="drag1" draggable="true"
		ondragstart="drag(event)"><input type="text" value="sssseeeeeeeeddddddddaaaaaaaaaa"></div>
		<div id="drag2" draggable="true"
		ondragstart="drag(event)"><input type="text" value="sssseeeeeeeeddddddddaaaaaaaaaa"></div>

</body>
</html>

