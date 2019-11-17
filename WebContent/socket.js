let socket = new WebSocket("ws:"+document.URL.replace("http:","")+"socket");
let PLAYERS = {};
let map;
let me = {id:0, paths:[]};

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
		map.buffer = [];
		setMap();
		clearMap();
		drawMap(map);
	} else if (data.type === "me") {
		me.id = data.data;
	} else if (data.type === "move") {
		let [id, x, y, pa] = data.data;
		let player = PLAYERS[id];
		map.map[player.x][player.y] = 0;
		map.map[x][y] = id;
		player.pa -= pa;
		player.x = x;
		player.y = y;
	}
}

let tryMove = function(x, y) {
	socket.send("move:"+x+","+y);
}