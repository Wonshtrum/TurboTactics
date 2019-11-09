const side = 5;

let drawMap = function(data) {
	for (let x=0 ; x<data.w ; x++) {
		for (let y=0 ; y<data.h ; y++) {
			let tile = data.map[x][y];
			if (tile == 0) {
				drawQuad(x*side+1, y*side+1, (x+1)*side, (y+1)*side, 0.9, 0.9, 1);
			} else if (tile == 1) {
				drawQuad(x*side+1, y*side+1, (x+1)*side, (y+1)*side, 0, 0.1, 0.2);
			} else if (tile == -1) {
				drawQuad(x*side+1, y*side+1, (x+1)*side, (y+1)*side, 0, 0.5, 0.4);
			} else if (typeof(tile) == "string") {
				if (tile[0] == "P") {
					drawQuad(x*side+2, y*side+2, (x+1)*side-1, (y+1)*side-1, 1, 0, 0.8);
				} else if (tile[0] == "M") {
					drawQuad(x*side+2, y*side+2, (x+1)*side-1, (y+1)*side-1, 0, 1, 0.8);
				}
			} else {
				console.log("Unknow tile:", tile);
			}
		}
	}
}

let clamp = function(x, a, b) {
	return a>x?a:b<x?b:x;
}

let xyOnMap = function(e) {
	let x = Math.floor(width*(e.x-canvas.offsetTop)/canvas.offsetWidth);
	let y = Math.floor(height*(e.y-canvas.offsetLeft)/canvas.offsetHeight);
	x = Math.floor(x/side);
	y = Math.floor(y/side);
	return [clamp(x, 0, map.w-1), clamp(y, 0, map.h-1)];
}

let mouseMove = function(e) {
	clearMap();
	let [x ,y] = xyOnMap(e);
	drawQuad(x*side, y*side, (x+1)*side+1, (y+1)*side+1, 1, 0, 0);
	drawMap(map);
}

let mouseDown = function(e) {
	clearMap();
	let [x ,y] = xyOnMap(e);
	let tile = map.map[x][y];
	console.log(tile);
	if (tile[0] === "P") {
		console.log(PLAYERS[tile]);
	}
	drawQuad(x*side, y*side, (x+1)*side+1, (y+1)*side+1, 0.5, 0, 0);
	drawMap(map);
}

canvas.addEventListener("mousemove",mouseMove)
canvas.addEventListener("mousedown",mouseDown)