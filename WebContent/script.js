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
let drawMap = function(data) {
	for (let x=0 ; x<data.w ; x++) {
		for (let y=0 ; y<data.h ; y++) {
			console.log(x,y,data.map[x][y]);
			if (data.map[x][y]) {
				drawQuad(x*5+1, y*5+1, (x+1)*5, (y+1)*5, 0, 0.1, 0.2);
			} else {
				drawQuad(x*5+1, y*5+1, (x+1)*5, (y+1)*5, 0, 0.5, 0.4);
			}
		}
	}
}
socket.onmessage = function(event) {
	console.log(event.data);
	data = JSON.parse(event.data);
	if (data.type === "msg") {
		chat.innerHTML = "<div style='background-color:#EEE;margin:5px;padding:5px;width:500px;word-break: break-all;word-wrap:break-word;'>"+data.data+"</div>";
	} else if (data.type === "players") {
		PLAYERS = data.data;
	} else {
		console.log(data);
		drawMap(data.data);
	}
}