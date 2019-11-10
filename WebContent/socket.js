let socket = new WebSocket("ws:"+document.URL.replace("http:","")+"socket");
let PLAYERS = {};
let map;

socket.onmessage = function(event) {
	console.log(event.data);
	data = JSON.parse(event.data);
	if (data.type === "msg") {
		chat.innerHTML = "<div style='background-color:#EEE;margin:5px;padding:5px;width:500px;word-break: break-all;word-wrap:break-word;'>"+data.data+"</div>";
	} else if (data.type === "players") {
		PLAYERS = data.data;
	} else if (data.type === "map") {
		console.log(data);
		map = data.data;
		clearMap();
		drawMap(map);
	}
}