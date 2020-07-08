let wallMapping = function() {
    seed = 0;
	map.buffer[1] = [];
	for (let x=0 ; x<map.w ; x++) {
		for (let y=0 ; y<map.h ; y++) {
			if (map.map[x][y] === 1) {
				//map.buffer[1].push([x*side, (y-0.5)*side, (x+1)*side, (y+0.5)*side, 1, 0.8, 0.8, 1, false, "wall", 1]);
				map.buffer[1].push([x*side, (y-1)*side, (x+1)*side, (y+1)*side, 1, 0.8, 0.8, 1, false, "wallFull", 1]);
			}
		}
	}
};

let drawMap = function() {
	for (let x=0 ; x<map.w ; x++) {
		for (let y=0 ; y<map.h ; y++) {
			let tile = map.map[x][y];
			if (tile === 0) {
			} else if (tile === 1) {
				//drawQuad(x*side, (y-0.5)*side, (x+1)*side, (y+0.5)*side, 1, 0.8, 0.8, 1, false, "wall", 1);
				//drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 1, 0.8, 0.8, 1, false, "wall", 1);
				//drawQuad(x*side, (y+0.5)*side, (x+1)*side, (y+1.5)*side, 1, 0.8, 0.8, 1, false, "wall", 1);
				//drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 0.5, 0.4, 0.4, 1, false, "bg", 2);
			} else if (tile === -1) {
                drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 0.8, 0.6, 1, 1, false, "exit");
			} else if (typeof(tile) === "string") {
				if (tile === me.id) {
                    let [r, g, b, a, l, t] = idToTex[tile[0]];
                    [r, g, b, a] = me.rgba;
					drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, r, g, b, a, l, t);
				} else {
					drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, ...idToTex[tile[0]]);
				}
			} else {
				console.log("Unknow tile:", tile);
			}
		}
	}
	let args;
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
				mouseDown();
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
};

const bg = ["bg0", "bg1", "bg2", "bg3", "bg4", "bg5"];
let clearMap = function(r, g, b) {
    seed = 0;
    if (!(r && g && b)) {
        r = 1;
        g = 1;
        b = 1;
    }
    // Clear the color buffer bit
    gl.clear(gl.COLOR_BUFFER_BIT);
    // Clear the canvas
    //drawQuad(0, 0, map.w*side, map.h*side, r, g, b, 1, true, "bg", 8);
    for (let x=0 ; x<map.w ; x++) {
        for (let y=0 ; y<map.h ; y++) {
            drawQuad(x*side, y*side, (x+1)*side, (y+1)*side, 1, 1, 1, 1, true, bg[Math.floor(random()*6)], 1);
        }
    }
};

let checkMap = function(x, y) {
	if (x<0 || y<0 || x>=map.w || y>map.h) {
		return 1000;
	} else {
		return map.map[x][y];
	}
};

let clamp = function(x, a, b) {
	return a>x?a:b<x?b:x;
};

let xyOnMap = function(e) {
	let x = Math.floor(width*(e.x-canvas.offsetLeft-contCan.offsetLeft)/canvas.offsetWidth);
	let y = Math.floor(height*(e.y-canvas.offsetTop-contCan.offsetTop)/canvas.offsetHeight);
	x = Math.floor(x/side);
	y = Math.floor(y/side);
	return [clamp(x, 0, map.w-1), clamp(y, 0, map.h-1)];
};

let xyOnMapPixel = function(e) {
	let x = Math.floor(width*(e.x-canvas.offsetLeft-contCan.offsetLeft)/canvas.offsetWidth);
	let y = Math.floor(height*(e.y-canvas.offsetTop-contCan.offsetTop)/canvas.offsetHeight);
	return [Math.floor(x), Math.floor(y)];
};

let drawCursor = function(x, y, r, g, b, a) {
	map.buffer[2] = [[x*side, y*side, (x+1)*side, y*side+1, r, g, b, a, false],
	[(x+1)*side, y*side, (x+1)*side-1, (y+1)*side, r, g, b, a, false],
	[x*side, (y+1)*side, x*side+1, y*side, r, g, b, a, false],
	[(x+1)*side, (y+1)*side, x*side, (y+1)*side-1, r, g, b, a, false]];
};

let mouseMove = function(e) {
	let [x, y] = xyOnMapPixel(e);
	let lights = [x, map.h*side-y, 2, 56, 56, 1, 88, 8, 2, 24, 72, 1, 120, 88, 1.5, 119.5, 8.5, 1.5];
	gl.uniform1i(locNLights, lights.length/3);
	gl.uniform3fv(locLights, lights);
	[x ,y] = xyOnMap(e);
	let [r, g, b] = me.rgba;
	drawCursor(x, y, r, g, b, 0.5);
};

let mouseDown = function(e) {
	e = e || target;
	target = e;
	map.buffer[0] = [];
	if (e === undefined) {
	    return;
    }
	let [x ,y] = xyOnMap(e);
	let tile = map.map[x][y];
	console.log(tile);
	if (tile === 0 && me.paths.some(e => e[0]===x && e[1]===y)) {
		let[nx, ny] = me.paths.filter(e => e[0]===x && e[1]===y)[0];
		tryMove(nx, ny);
	}
	me.paths = [];
	if (typeof(tile) === "string") {
		let entity = entities[tile];
		console.log(entity);
		let paScale = entity.pa;
		let pos = paths(x, y);
		for (let [i, j, k] of pos) {
			map.buffer[0].push([i*side+1, j*side+1, (i+1)*side-1, (j+1)*side-1, k/paScale, 0.5, 0.5+0.5*k/paScale, 0.5, true]);
		}
		if (tile === me.id) {
			me.paths = pos;
		}
	}
	drawCursor(x, y, 0.5, 0, 0, 1);
};

let animate = function(id, quad, x, y, dx, dy, steps, mat, next) {
	let [w, h] = quad.splice(0, 2);
	if (id) {
		map.animate = map.animate.filter(e => e.id!==id);
	}
	map.animate.push({id:id, quad:quad, w:w, h:h, x:x, y:y, dx:dx/steps, dy:dy/steps, steps:steps, mat:mat, next:next});
};

let animateMultipleSteps = function(id, quad, path, allSteps, mat) {
	let [w, h] = quad.splice(0, 2);
	let anime = {};
	let megaAnime = anime;
	for (let i=0 ; i<path.length-1 ; i++) {
		let [x, y, steps] = path[i];
		let [x1, y1] = path[i+1];
		steps = steps || allSteps;
		anime.id = id;
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
	if (id) {
		map.animate = map.animate.filter(e => e.id!==id);
	}
	map.animate.push(megaAnime);
};

let time = 0;
let timeStep = 0.1;
let drawScene = function() {
	//let start = Date.now();
	time += timeStep*Math.random();
	gl.uniform1f(locTime, time);
	clearMap();
	drawMap();
	//console.log(Date.now()-start);
};