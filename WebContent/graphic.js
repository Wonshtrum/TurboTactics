/*============ Creating a canvas =================*/

let canvas = document.getElementById('myCan');
let gl = canvas.getContext('webgl', { preserveDrawingBuffer: true });

/*========== Defining and storing the geometry =========*/

const width = 49;
const height = 49;
canvas.width = width;
canvas.height = height;
vertices = [
    0,1,0,
    0,0,0,
    1,0,0,
    1,1,0 
]

let indices = [3,2,1,3,1,0];

// Create an empty buffer object to store vertex buffer
let vertex_buffer = gl.createBuffer();

// Bind appropriate array buffer to it
gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);

// Pass the vertex data to the buffer
gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

// Unbind the buffer
gl.bindBuffer(gl.ARRAY_BUFFER, null);

// Create an empty buffer object to store Index buffer
let Index_Buffer = gl.createBuffer();

// Bind appropriate array buffer to it
gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, Index_Buffer);

// Pass the vertex data to the buffer
gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);

// Unbind the buffer
gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

/*====================== Shaders =======================*/

// Vertex shader source code
let vertCode = 'attribute vec3 coordinates;' +
'varying vec3 v_position;' +
'uniform vec4 u_size;' +
'void main(void) {' +
'	vec3 pos = vec3((coordinates.x*(u_size[2]-u_size[0])+u_size[0])/'+width/2.0+'-1.0, (coordinates.y*(u_size[1]-u_size[3])-u_size[1])/'+height/2.0+'+1.0, coordinates.z);' +
'	gl_Position = vec4(pos, 1);' +
'	v_position  = vec3(pos);' +
'}';

// Create a vertex shader object
let vertShader = gl.createShader(gl.VERTEX_SHADER);

// Attach vertex shader source code
gl.shaderSource(vertShader, vertCode);

// Compile the vertex shader
gl.compileShader(vertShader);

// Fragment shader source code
let fragCode = 'precision mediump float;' +
'varying vec3 v_position;' +
'uniform vec4 u_color;' +
'void main(void) {' +
'	if (u_color[3]==0.0) {' +
'		float d = distance(v_position, vec3(0,0,v_position.z));' +
'		gl_FragColor = vec4(vec3(u_color)/max(1.0, 5.0*d), 1.0);' +
'	} else {' +
'		gl_FragColor = u_color;' +
'	}' +
'}';

fragCode = 'precision mediump float;' +
'varying vec3 v_position;' +
'uniform vec4 u_color;' +
'uniform vec2 u_light;' +
'uniform int u_mat[100];' +
'int mat(int x, int y) {for (int i=0 ; i<64 ; i++) {if (i==9-y+8*x) {return u_mat[i];}};return 0;}' +
'int mat_(int x, int y, bool r) {return r?mat(y/5,x/5):mat(x/5,y/5);}' +
'int abs(int x) {return x<0?-x:x;}' +
'int lx = int(u_light.x);' +
'int ly = int(u_light.y);' +
'bool line_(int x0, int y0, int x1, int y1, bool r) {int dx=x1>x0?1:-1; float dy=float(y1-y0)/float(dx*(x1-x0)); int x = x0; float y = float(y0); for (int i=0 ; i<1000 ; i++) {x+=dx; y+=dy; if (mat_(x,int(y),r)==1) {return false;} if (x==x1) {return mat_(x0,y0,r)==0;}} return false;}' +
'bool line(int x0, int y0, int x1, int y1) {if (x0==x1 && y0==y1) {return true;} if (abs(x1-x0)>=abs(y1-y0)) {return line_(x0,y0,x1,y1,false);} else {return line_(y0,x0,y1,x1,true);}}' +
'void main(void) {' +
'	if (u_color[3]==0.0 && false) {' +
'		float d = distance(v_position, vec3(0,0,v_position.z));' +
'		gl_FragColor = vec4(vec3(u_color)/max(1.0, 0.1), 1.0);' +
'	} else {' +
'		int x = (int((v_position.x+1.0)*'+(width/2.0)+'));' +
'		int y = (int((v_position.y+1.0)*'+(height/2.0)+')+1);' +
'		float minL = 0.3;' +
'		if (line(x,y,lx,ly)) {' +
'			gl_FragColor = vec4(vec3(u_color)*max(minL,1.2-distance(vec2(x,y),vec2(lx,ly))/30.0),1);' +
'		} else {' +
'			gl_FragColor = vec4(vec3(u_color)*minL,1);' +
'		}' +
'	}' +
'}';

// Create fragment shader object 
let fragShader = gl.createShader(gl.FRAGMENT_SHADER);

// Attach fragment shader source code
gl.shaderSource(fragShader, fragCode);

// Compile the fragment shader
gl.compileShader(fragShader);

// Create a shader program object to
// store the combined shader program
let shaderProgram = gl.createProgram();

// Attach a vertex shader
gl.attachShader(shaderProgram, vertShader);

// Attach a fragment shader
gl.attachShader(shaderProgram, fragShader);

// Link both the programs
gl.linkProgram(shaderProgram);

// Use the combined shader program object
gl.useProgram(shaderProgram);

const locSize = gl.getUniformLocation(shaderProgram, "u_size");
const locColor = gl.getUniformLocation(shaderProgram, "u_color");
const locMat = gl.getUniformLocation(shaderProgram, "u_mat");
const locLight = gl.getUniformLocation(shaderProgram, "u_light");

/* ======= Associating shaders to buffer objects =======*/

// Bind vertex buffer object
gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);

// Bind index buffer object
gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, Index_Buffer); 

// Get the attribute location
let coord = gl.getAttribLocation(shaderProgram, "coordinates");

// Point an attribute to the currently bound VBO
gl.vertexAttribPointer(coord, 3, gl.FLOAT, false, 0, 0);

// Enable the attribute
gl.enableVertexAttribArray(coord);

/*============= Drawing the Quad ================*/

// Enable the depth test
//gl.enable(gl.DEPTH_TEST);

// Set the view port
gl.viewport(0,0,canvas.width,canvas.height);

let setMap = function() {
	gl.uniform1iv(locMat,map.map.flat().map(e=>e==1?1:0));
}

let clearMap = function(r, g, b) {
	if (!(r && g && b)) {
		r = 1;
		g = 1;
		b = 1;
	}
	// Clear the color buffer bit
	gl.clear(gl.COLOR_BUFFER_BIT);
	// Clear the canvas
	gl.clearColor(r, g, b, 1);
	drawQuad(0, 0, map.w*side, map.h*side, r, g, b, 0);
}

let drawQuad = function(x1, y1, x2, y2, r, g, b, a) {
	a = a==undefined?1:a;
	gl.uniform4f(locSize, x1, y1, x2, y2);
	gl.uniform4f(locColor, r, g, b, a);
	//Draw the triangles
	gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT,0);	
}