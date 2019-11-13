/*============ Creating a canvas =================*/

let canvas = document.getElementById('myCan');
let gl = canvas.getContext('webgl', { preserveDrawingBuffer: true });

/*========== Defining and storing the geometry =========*/

const side = 16;
const width = 8*side;
const height = 8*side;
const width2 = (width/2).toFixed(1);
const height2 = (width/2).toFixed(1);
canvas.width = width;
canvas.height = height;
let vertices = [
    0,1,0,
    0,0,0,
    1,0,0,
    1,1,0 
]
let texCoords = [
    0,1,
    0,0,
    1,0,
    1,1 
]
let indices = [3,2,1,3,1,0];

// Create an empty buffer object to store vertex buffer
let vertex_buffer = gl.createBuffer();
// Bind appropriate array buffer to it
gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);
// Pass the vertex data to the buffer
gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

//Create an empty buffer object to store texCoords buffer
let tex_buffer = gl.createBuffer();
//Bind appropriate array buffer to it
gl.bindBuffer(gl.ARRAY_BUFFER, tex_buffer);
//Pass the vertex data to the buffer
gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(texCoords), gl.STATIC_DRAW);

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
let vertCode =
`attribute vec3 a_position;
attribute vec2 a_texcoord;
varying vec3 v_position;
varying vec2 v_texcoord;
uniform vec4 u_size;
void main(void) {
	vec3 pos = vec3((a_position.x*(u_size[2]-u_size[0])+u_size[0])/${width2}-1.0, (a_position.y*(u_size[1]-u_size[3])-u_size[1])/${height2}+1.0, a_position.z);
	gl_Position = vec4(pos, 1);
	v_position  = pos;
	v_texcoord  = a_texcoord;
}`;

// Create a vertex shader object
let vertShader = gl.createShader(gl.VERTEX_SHADER);

// Attach vertex shader source code
gl.shaderSource(vertShader, vertCode);

// Compile the vertex shader
gl.compileShader(vertShader);

// Fragment shader source code
let fragCode =
`precision mediump float;
varying vec3 v_position;
varying vec2 v_texcoord;
uniform vec4 u_color;
uniform bool u_lit;
uniform bool u_tex;
uniform vec2 u_light;
uniform sampler2D u_texture;
uniform int u_mat[100];
int mat(int x, int y) {for (int i=0 ; i<64 ; i++) {if (i==7-y+8*x) {return u_mat[i];}};return 0;}
int mat_(int x, int y, bool r) {return r?mat(y/${side},x/${side}):mat(x/${side},y/${side});}
int abs(int x) {return x<0?-x:x;}
int lx = int(u_light.x);
int ly = int(u_light.y);
bool line_(int x0, int y0, int x1, int y1, bool r) {
	int dx = x1>x0?1:-1;
	float dy = float(y1-y0)/float(dx*(x1-x0));
	int x = x0;
	float y = float(y0);
	for (int i=0 ; i<1000 ; i++) {
		x += dx;
		y += dy;
		if (mat_(x,int(y),r)==1) {return false;}
		if (x==x1) {return mat_(x0,y0,r)==0;}
	}
	return false;
}
bool line(int x0, int y0, int x1, int y1) {
	if (x0==x1 && y0==y1) {return true;}
	if (abs(x1-x0)>=abs(y1-y0)) {return line_(x0,y0,x1,y1,false);}
	else {return line_(y0,x0,y1,x1,true);}
}
void main(void) {
	int x = int((v_position.x+1.0)*${width2});
	int y = int((v_position.y+1.0)*${height2});
	vec4 color = u_color;
	if (u_tex) {
		color *= texture2D(u_texture, v_texcoord);
	}
	if (u_lit) {
		float minL = 0.3;
		if (line(x,y,lx,ly)) {
			float d = max(0.0, 1.2-distance(vec2(x,y), vec2(lx,ly))/50.0);
			gl_FragColor = vec4(vec3(color)*max(minL, d), color[3]);
		} else {
			gl_FragColor = vec4(vec3(color)*minL, color[3]);
		}
	} else {
		gl_FragColor = color;
	}
}`;

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
const locLit = gl.getUniformLocation(shaderProgram, "u_lit");
const locTex = gl.getUniformLocation(shaderProgram, "u_tex");
const locMat = gl.getUniformLocation(shaderProgram, "u_mat");
const locLight = gl.getUniformLocation(shaderProgram, "u_light");
const locGlobalTex = gl.getUniformLocation(shaderProgram, "u_texture");

/* ======= Associating shaders to buffer objects =======*/

//Get the attribute location
let coord = gl.getAttribLocation(shaderProgram, "a_position");
//Get the attribute texture coordinates
let texCoord = gl.getAttribLocation(shaderProgram, "a_texcoord");

//Bind index buffer object
gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, Index_Buffer); 

// Bind vertex buffer object
gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);
// Point an attribute to the currently bound VBO
gl.vertexAttribPointer(coord, 3, gl.FLOAT, false, 0, 0);
// Enable the attribute
gl.enableVertexAttribArray(coord);

//Bind vertex buffer object
gl.bindBuffer(gl.ARRAY_BUFFER, tex_buffer);
//Point an attribute to the currently bound VBO
gl.vertexAttribPointer(texCoord, 2, gl.FLOAT, false, 0, 0);
//Enable the attribute
gl.enableVertexAttribArray(texCoord);

/*============= Textures ================*/

let textures = {};
let loadTex = function(src, index) {
	// Create a texture.
	let texture = gl.createTexture();
	gl.bindTexture(gl.TEXTURE_2D, texture);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
	 
	// Fill the texture with a 1x1 blue pixel.
	gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE,
	              new Uint8Array([0, 0, 255, 255]));
	// Asynchronously load an image
	let image = new Image();
	image.src = src;
	image.addEventListener('load', function() {
		// Now that the image has loaded make copy it to the texture.
		gl.bindTexture(gl.TEXTURE_2D, texture);
		gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA,gl.UNSIGNED_BYTE, image);
		gl.generateMipmap(gl.TEXTURE_2D);
	});
	textures[index] = texture;
}

let setTex = function(index) {
	gl.bindTexture(gl.TEXTURE_2D, textures[index]);
}

loadTex("wall.png", "wall");
loadTex("img.png", "kirby");
loadTex("knight.png", "knight");
gl.uniform1i(locGlobalTex, 0);

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
		r = 0;
		g = 1;
		b = 0.5;
	}
	// Clear the color buffer bit
	gl.clear(gl.COLOR_BUFFER_BIT);
	// Clear the canvas
	drawQuad(0, 0, map.w*side, map.h*side, r, g, b, 1, true);
}

let drawQuad = function(x1, y1, x2, y2, r, g, b, a, lit, tex) {
	if (tex) {
		setTex(tex);
		gl.uniform1i(locTex, true);
	} else {
		gl.uniform1i(locTex, false);
	}
	gl.uniform1i(locLit, lit);
	gl.uniform4f(locSize, x1, y1, x2, y2);
	gl.uniform4f(locColor, r, g, b, a);
	//Draw the triangles
	gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);
}