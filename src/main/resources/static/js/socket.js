let socket = new WebSocket(document.URL.replace("http","ws").split("game")[0]+"socket/"+idGame);
let entities = {};
let map;
let target;
let me = {id:0, paths:[], rgba:[0.8, 0.5, 1, 1]};
let idToTex = {P: [1, 1, 1, 1, true, "knight"], M: [0, 1, 0.5, 1, true, "slime2"]};

socket.onmessage = function(event) {
	console.log(event.data);
	let data = JSON.parse(event.data);
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
		let [r, g, b, a, l, t] = idToTex[id[0]];
		if (id === me.id) {
			[r, g, b, a] = me.rgba;
		}
		animateMultipleSteps(id, [side, side, r, g, b, a, l, t], path.reverse().map(e => [e[0]*side, e[1]*side]), side/2, [x, y, id]);
	} else if (data.type === "end") {
		let player = entities[data.data];
		player.pa = player.paMax;
		mouseDown();
	} else if (data.type === "rm") {
		let player = entities[data.data];
		map.map[player.x][player.y] = 0;
		delete entities[data.data];
		mouseDown();
	}
};

let tryMove = function(x, y) {
	socket.send("move:"+x+","+y);
};

let endTurn = function() {
	target = undefined;
	socket.send("endTurn:");
};

let gameLoop;
let start = function() {
	clearInterval(gameLoop);
	canvas.addEventListener("mousemove",mouseMove);
	canvas.addEventListener("mousedown",mouseDown);
	gameLoop = setInterval(()=>drawScene(), 20);
};