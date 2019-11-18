let seed = 1;
let random = function(s) {
	seed = s || seed;
    let x = Math.sin(seed++) * 10000;
    return x - Math.floor(x);
}

let drawMap = function() {
	for (let x=0 ; x<map.w ; x++) {
		for (let y=0 ; y<map.h ; y++) {
			let tile = map.map[x][y];
			if (tile == 0) {
			} else if (tile == 1) {
				drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 1, 0.8, 0.8, 1, false, "wall", 1);
			} else if (tile == -1) {
				drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 0, 0.5, 0.4, 1, true);
			} else if (typeof(tile) == "string") {
				if (tile[0] == "P") {
					drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 1, 1, 1, 1, true, "knight");
				} else if (tile[0] == "M") {
					drawQuad(x*side+4, y*side+8, (x+1)*side-4, (y+1)*side, 1, 1, 1, 1, true, "kirby");
					drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 1, 1, 1, 1, true, "kirby", 1);
				}
			} else {
				console.log("Unknow tile:", tile);
			}
		}
	}
	for (let i = map.animate.length-1 ; i>=0 ; i--) {
		args = map.animate[i];
		drawQuad(args.x, args.y, args.x+args.w, args.y+args.h, ...args.quad);
		args.x += args.dx;
		args.y += args.dy;
		if (args.steps-- <= 0) {
			map.animate.splice(i, 1);
			if (args.mat) {
				let [x, y, tile] = args.mat;
				map.map[x][y] = tile;
			}
			if (args.next) {
				map.animate.push(args.next);
			}
		}
	}
	for (let layer of map.buffer) {
		if (layer) {
			for (let args of layer) {
				drawQuad(...args);
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
	map.buffer[1] = [[x*side, y*side, (x+1)*side, y*side+1, r, g, b, 1, false],
	[(x+1)*side, y*side, (x+1)*side-1, (y+1)*side, r, g, b, 1, false],
	[x*side, (y+1)*side, x*side+1, y*side, r, g, b, 1, false],
	[(x+1)*side, (y+1)*side, x*side, (y+1)*side-1, r, g, b, 1, false]];
}

let mouseMove = function(e) {
	let [x, y] = xyOnMapPixel(e);
	let lights = [x, map.h*side-y, 2, 50, 50, 5];
	gl.uniform1i(locNLights, lights.length/3);
	gl.uniform3fv(locLights, lights);
	[x ,y] = xyOnMap(e);
	drawCursor(x, y, 1, 0, 0);
}

let mouseDown = function(e) {
	map.buffer[0] = [];
	let [x ,y] = xyOnMap(e);
	let tile = map.map[x][y];
	console.log(tile);
	if (tile[0] === "P") {
		let player = PLAYERS[tile];
		console.log(player);
		let paScale = player.pa;
		for (let [i, j, k] of paths(x, y)) {
			map.buffer[0].push([i*side, j*side, (i+1)*side, (j+1)*side, 0+1*k/paScale, 0.5+0.0*k/paScale, 0.5+0.5*k/paScale, 0.5, true]);
		}
	}
	drawCursor(x, y, 0.5, 0, 0);
}

let animate = function(quad, x, y, dx, dy, steps, mat, next) {
	let [w, h] = quad.splice(0, 2);
	map.animate.push({quad:quad, w:w, h:h, x:x, y:y, dx:dx/steps, dy:dy/steps, steps:steps, mat:mat, next:next});
}

let animateMultipleSteps = function(quad, path, allSteps, mat) {
	let [w, h] = quad.splice(0, 2);
	let anime = {};
	let megaAnime = anime;
	for (let i=0 ; i<path.length-1 ; i++) {
		let [x, y, steps] = path[i];
		let [x1, y1] = path[i+1];
		steps = steps || allSteps;
		anime.quad = quad;
		anime.x = x;
		anime.y = y;
		anime.dx = (x1-x)/steps;
		anime.dy = (y1-y)/steps;
		anime.w = w;
		anime.h = h;
		anime.steps = steps;
		if (i < path.length-2) {
			anime.next = {};
			anime = anime.next;
		}
	}
	anime.mat = mat;
	console.log(megaAnime);
	map.animate.push(megaAnime);
}

let time = 0;
let timeStep = 0.1;
let drawScene = function() {
	let start = Date.now();
	time += timeStep*Math.random();
	gl.uniform1f(locTime, time);
	clearMap();
	drawMap();
	//console.log(Date.now()-start);
}

canvas.addEventListener("mousemove",mouseMove);
canvas.addEventListener("mousedown",mouseDown);

setInterval(()=>drawScene(), 20);