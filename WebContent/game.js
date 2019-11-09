const side = 5;

let drawMap = function(data) {
	for (let x=0 ; x<data.w ; x++) {
		for (let y=0 ; y<data.h ; y++) {
			let tile = data.map[x][y];
			if (tile == "W") {
				drawQuad(x*side+1, y*side+1, (x+1)*side, (y+1)*side, 0, 0.1, 0.2);
			} else if (tile[0] == "P") {
				drawQuad(x*side+2, y*side+2, (x+1)*side-1, (y+1)*side-1, 1, 0, 0.8);
			} else if (tile == "E") {
				drawQuad(x*side+1, y*side+1, (x+1)*side, (y+1)*side, 0, 0.5, 0.4);
			} else {
				drawQuad(x*side+1, y*side+1, (x+1)*side, (y+1)*side, 0.5, 1, 0.8);
			}
		}
	}
}

let mouseMove = function(e) {
	clearMap();
	let x = Math.floor(width*(e.x-canvas.offsetTop)/canvas.offsetWidth);
	let y = Math.floor(height*(e.y-canvas.offsetLeft)/canvas.offsetHeight);
	x = side*Math.floor(x/side);
	y = side*Math.floor(y/side);
	drawQuad(x, y, x+side+1, y+side+1, 1, 0, 0);
	drawMap(map);
}

canvas.addEventListener("mousemove",mouseMove)