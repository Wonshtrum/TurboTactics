<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<canvas id="myCan" width=500 height=500></canvas>
	<div id="chat">
	</div>
	<script>
		let ctx = myCan.getContext("2d");
		let socket = new WebSocket("ws:"+document.URL.replace("http:","")+"socket");
		let PLAYERS = {};
		let KEYS = 0;
		const UP = 1;
		const DOWN = 2;
		const RIGHT = 4;
		const LEFT = 8;
		let setKey = function(key, value) {
			if (value) {
				KEYS |= key
			} else {
				KEYS = (KEYS | key) ^ key;
			}
		}
		document.addEventListener('keydown', (event) => {
			switch (event.key) {
			case "ArrowUp":
				setKey(UP, true);
				break;
			case "ArrowDown":
				setKey(DOWN, true);
				break;
			case "ArrowRight":
				setKey(RIGHT, true);
				break;
			case "ArrowLeft":
				setKey(LEFT, true);
				break;
			default:
				break;
			}
		})
		document.addEventListener('keyup', (event) => {
			switch (event.key) {
			case "ArrowUp":
				setKey(UP, false);
				break;
			case "ArrowDown":
				setKey(DOWN, false);
				break;
			case "ArrowRight":
				setKey(RIGHT, false);
				break;
			case "ArrowLeft":
				setKey(LEFT, false);
				break;
			default:
				break;
			}
		})
		socket.onmessage = function(event) {
			console.log(event.data);
			data = JSON.parse(event.data);
			if (data.type === "msg") {
				chat.innerHTML = "<div style='background-color:#EEE;margin:5px;padding:5px;width:500px;word-break: break-all;word-wrap:break-word;'>"+data.data+"</div>";
			} else if (data.type === "players") {
				PLAYERS = data.data;
			} else {
				console.log(data);
			}
		}
		let loop = function() {
			socket.send(KEYS);
			ctx.clearRect(0, 0, myCan.width, myCan.height);
			for (let [x,y,s] of Object.values(PLAYERS)) {
				ctx.fillRect(x-s*10, y-s*10, s*20, s*20);
			}
		}
		//setInterval(loop, 20);
	</script>
</body>
</html>