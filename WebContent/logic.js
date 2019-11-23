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