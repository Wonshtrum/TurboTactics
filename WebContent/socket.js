let socket = new WebSocket("ws:"+document.URL.replace("http:","")+"socket");
let entities = {};
let map;
let target;
let me = {id:0, paths:[]};
let idToTex = {P: "knight", M:"kirby"};

socket.onmessage = function(event) {
	console.log(event.data);
	data = JSON.parse(event.data);
	if (data.type === "msg") {
		chat.innerHTML = "<div style='background-color:#EEE;margin:5px;padding:5px;width:500px;word-break: break-all;word-wrap:break-word;'>"+data.data+"</div>";
	} else if (data.type === "entities") {
		entities = data.data;
	} else if (data.type === "map") {
		console.log(data);
		map = data.data;
		map.buffer = [];
		map.animate = [];
		setMap();
		clearMap();
		start();
	} else if (data.type === "me") {
		me.id = data.data;
	} else if (data.type === "move") {
		me.paths = [];
		map.buffer[0] = [];
		let [id, pa, path] = data.data;
		let player = entities[id];
		let [x, y] = path[0];
		map.map[player.x][player.y] = 0;
		player.pa = pa;
		path.push([player.x, player.y]);
		player.x = x;
		player.y = y;
		//map.map[x][y] = id;
		let r = g = b = 1;
		if (id == me.id) {
			g = b = 0;
		}
		animateMultipleSteps(id, [side, side, r, g, b, 1, true, idToTex[id[0]]], path.reverse().map(e => [e[0]*side, e[1]*side]), side/2, [x, y, id]);
	} else if (data.type === "end") {
		let player = entities[data.data];
		player.pa = player.pamax;
		mouseDown();
	} else if (data.type === "rm") {
		let player  = entities[data.data];
		map.map[player.x][player.y] = 0;
		delete entities[data.data];
		mouseDown();
	}
}

let tryMove = function(x, y) {
	socket.send("move:"+x+","+y);
}

let endTurn = function() {
	socket.send("endTurn:");
}

let gameLoop;
let start = function() {
	clearInterval(gameLoop);
	canvas.addEventListener("mousemove",mouseMove);
	canvas.addEventListener("mousedown",mouseDown);
	gameLoop = setInterval(()=>drawScene(), 20);
}