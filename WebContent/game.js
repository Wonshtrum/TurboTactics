const side = 5;

let drawMap = function() {
	for (let x=0 ; x<map.w ; x++) {
		for (let y=0 ; y<map.h ; y++) {
			let tile = map.map[x][y];
			if (tile == 0) {
			} else if (tile == 1) {
				drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 0, 0.8, 0.4);
				//drawQuad(x*side, (y+1)*side-1, (x+1)*side, (y+1)*side, 0, 0.7, 0.1, 0);
			} else if (tile == -1) {
				drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 0, 0.5, 0.4, 0);
			} else if (typeof(tile) == "string") {
				if (tile[0] == "P") {
					drawQuad(x*side+1, y*side+1, (x+1)*side-1, (y+1)*side-1, 1, 0, 0.8, 0);
				} else if (tile[0] == "M") {
					drawQuad(x*side+1, y*side+1, (x+1)*side-1, (y+1)*side-1, 0, 1, 0.8, 0);
				}
			} else {
				console.log("Unknow tile:", tile);
			}
		}
	}
}

let checkMap = function(x, y) {
	if (x<0 || y<0 || x>=map.w || y>map.h) {
		return 1000;
	} else {
		return map.map[x][y];
	}
}

let serverValidPos = function(x, y) {
	socket.send("pos:"+x+","+y);
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

let xyOnMapPixel = function(e) {
	let x = Math.floor(width*(e.x-canvas.offsetTop)/canvas.offsetWidth);
	let y = Math.floor(height*(e.y-canvas.offsetLeft)/canvas.offsetHeight);
	return [Math.floor(x), Math.floor(y)];
}

let drawCursor = function(x, y, r, g, b) {
	drawQuad(x*side, y*side, x*side+1, y*side+1, r, g, b);
	drawQuad((x+1)*side, y*side, (x+1)*side-1, y*side+1, r, g, b);
	drawQuad(x*side, (y+1)*side, x*side+1, (y+1)*side-1, r, g, b);
	drawQuad((x+1)*side, (y+1)*side, (x+1)*side-1, (y+1)*side-1, r, g, b);
}

let mouseMove = function(e) {
	clearMap();
	let [x, y] = xyOnMapPixel(e);
	gl.uniform2f(locLight, x, height-y);
	[x ,y] = xyOnMap(e);
	drawMap();
	drawCursor(x, y, 1, 0, 0);
}

let mouseDown = function(e) {
	clearMap();
	let [x ,y] = xyOnMap(e);
	let tile = map.map[x][y];
	console.log(tile);
	if (tile[0] === "P") {
		let player = PLAYERS[tile];
		console.log(player);
		let paScale = player.pa+1;
		for (let [i, j, k] of paths(x, y)) {
			drawQuad(i*side, j*side, (i+1)*side, (j+1)*side, 0+k/paScale,0.5+0.5*k/paScale,0.5+0.5*k/paScale, 0);
		}
	}
	drawMap();
	drawCursor(x, y, 0.5, 0, 0);
}

canvas.addEventListener("mousemove",mouseMove);
canvas.addEventListener("mousedown",mouseDown);