let paths = function(x, y) {
	let entity = entities[map.map[x][y]];
	let visited = [];
	let parents = [[x, y]];
	let newParents;
	for (let pa=0 ; pa<entity.pa && parents.length>0 ; pa++) {
		newParents = [];
		for (let [x, y] of parents) {
			for (let [i, j] of [[-1, 0], [0, -1], [1, 0], [0, 1]]) {
				if (!visited.some(e => e[0]==x+i && e[1]==y+j) && checkMap(x+i, y+j)==0) {
					visited.push([x+i, y+j, pa+1]);
					newParents.push([x+i, y+j]);
				}
			}
		}
		parents = newParents;
	}
	return visited;
}

let inbound = function(x, y) {
	return x>=0 && y>=0 && x<map.w && y<map.h;
}

let unfoldPos = function(x, y, dx, dy, reverse, flipX, flipY) {
	dy = Math.floor(dy);
	if (reverse) {
		[dx, dy] = [dy, dx];
	}
	if (flipX) {
		dx *= -1;
	}
	if (flipY) {
		dy *= -1;
	}
	return inbound(x+dx, y+dy)?(map.map[x+dx][y+dy] != 0):true;
}

let line_ = function(x, y, dx, dy, reverse, flipX, flipY) {
	let coef = dy/dx;
	let posX = 0;
	let posY = totPosY = 0.5+0.5*coef;
	if (posY == 1) {
		if (unfoldPos(x, y, posX+1, totPosY-1, reverse, flipX, flipY) && unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
			return false;
		posY--;
	}
	for (posX=1 ; posX<dx ; posX++) {
		if (unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
			return false;
		posY += coef;
		totPosY += coef;
		if (posY >= 1) {
			if (posY == 1) {
				if (unfoldPos(x, y, posX+1, totPosY-1, reverse, flipX, flipY) && unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
					return false;
			} else {
				if (unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
					return false;
			}
			posY --;
		}
	}
	return !unfoldPos(x, y, dx, dy, reverse, flipX, flipY);
}

let line = function(x, y, gx, gy, range) {
	let flipX, flipY, reverse;
	flipX = flipY = reverse = false;
	let dx = gx - x;
	let dy = gy - y;
	if (Math.abs(dx) + Math.abs(dy) <= range) {
		if (dx < 0) {
			dx *= -1;
			flipX = true;
		}
		if (dy < 0) {
			dy *= -1;
			flipY = true;
		}
		if (dy > dx) {
			[dx, dy] = [dy, dx];
			reverse = true;
		}
		return line_(x, y, dx, dy, reverse, flipX, flipY);
	}
	return false;
}

let range = function(x, y, r) {
	r = 5;
	let visited = [];
	let entity = entities[map.map[x][y]];
	for (let gx=Math.max(x-r, 0) ; gx<Math.min(x+r+1, map.w) ; gx++) {
		for (let gy=Math.max(y-r, 0) ; gy<Math.min(y+r+1, map.h) ; gy++) {
			if (line(x, y, gx, gy, r)) {
				visited.push([gx, gy, Math.abs(x-gx)+Math.abs(y-gy)]);
			}
		}
	}
	return visited;
}