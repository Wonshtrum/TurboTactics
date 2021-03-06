/*============ Creating a canvas =================*/

let canvas = document.getElementById('myCan');
let chat = document.getElementById('chat');
let gl = canvas.getContext('webgl', { preserveDrawingBuffer: true, premultipliedAlpha: false });
gl.enable(gl.BLEND);
gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);

/*========== Defining and storing the geometry =========*/

const side = 16;
let width;
let height;

let vertices = [
    0,1,0,
    0,0,0,
    1,0,0,
    1,1,0 
];
let texCoords = [
    0,1,
    0,0,
    1,0,
    1,1 
];
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
uniform float u_width2;
uniform float u_height2;
varying float v_width2;
varying float v_height2;
void main(void) {
	vec3 pos = vec3((a_position.x*(u_size[2]-u_size[0])+u_size[0])/u_width2-1.0, (a_position.y*(u_size[1]-u_size[3])-u_size[1])/u_height2+1.0, a_position.z);
	gl_Position = vec4(pos, 1);
	v_position  = pos;
	v_texcoord  = a_texcoord;
	v_width2 = u_width2;
	v_height2 = u_height2;
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
varying float v_width2;
varying float v_height2;
uniform int u_width;
uniform int u_height;
uniform int u_side;
uniform float u_time;
uniform vec4 u_color;
uniform bool u_lit;
uniform bool u_tex;
uniform vec3 u_lights[10];
uniform int u_nlights;
uniform sampler2D u_texture;
uniform float u_repeat;
uniform int u_mat[64];
int mat(int x, int y) {
	for (int i=0 ; i<64 ; i++) {
		if (i==u_width-1-y+u_height*x) {
			return u_mat[i];
		}
	}
	return 0;
}
int mat_(int x, int y, bool r) {return r?mat(y/u_side,x/u_side):mat(x/u_side,y/u_side);}
int abs(int x) {return x<0?-x:x;}
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
	int x = int((v_position.x+1.0)*v_width2);
	int y = int((v_position.y+1.0)*v_height2);
	vec4 color = u_color;
	if (u_tex) {
		color *= texture2D(u_texture, v_texcoord*u_repeat);
	}
	if (u_lit) {
		float power;
		int lx;
		int ly;
		float d;
		float minL = 0.6;
		float d2 = 0.0;
		float d3 = 0.0;
		for (int i=0 ; i<100 ; i++) {
			if (i >= u_nlights) {
				break;
			}
			lx = int(u_lights[i].x);
			ly = int(u_lights[i].y);
			power = u_lights[i].z*(1.0+0.2*abs(sin(u_time+float(i))));
			d = distance(vec2(x,y), vec2(lx,ly));
			if ((1.1-d/(power*20.0)>d3 || 1.0-d/(power*10.0)>d2) && line(x,y,lx,ly)) {
				d2 = max(d2, 1.0-d/(power*10.0));
				d3 = max(d3, 1.1-d/(power*20.0));
			}
		}
		if (d3 != 0.0) {
			gl_FragColor = vec4(vec3(1,0.7,0.5)*(vec3(color)+d2*d2*d2)*d3+minL*vec3(color), color[3]);
		} else {
			gl_FragColor = vec4(vec3(color)*minL, color[3]);
		}
	} else {
		gl_FragColor = color;
	}
}`;

let fragCode2 =
`precision mediump float;
varying vec3 v_position;
varying vec2 v_texcoord;
varying float v_width2;
varying float v_height2;
uniform int u_width;
uniform int u_height;
uniform int u_side;
uniform float u_time;
uniform vec4 u_color;
uniform bool u_lit;
uniform bool u_tex;
uniform vec3 u_lights[100];
uniform int u_nlights;
uniform sampler2D u_texture;
uniform float u_repeat;
uniform int u_mat[1024];
int mat(int x, int y) {
	for (int i=0 ; i<1024 ; i++) {
		if (i==u_width-1-y+u_height*x) {
			return u_mat[i];
		}
	}
	return 0;
}
int abs(int x) {return x<0?-x:x;}
bool inbound(int x, int y) {return x>=0 && y>=0 && x<u_width && y<u_height;}
bool unfoldPos(float x, float y, int dx, float dyf, bool reverse, bool flipX, bool flipY) {
	int dy = int(dyf);
	if (reverse) {
		int tmp = dx;
		dx = dy;
		dy = tmp;
	}
	if (flipX)
		dx *= -1;
	if (flipY)
		dy *= -1;
	int ix = int(x+float(dx));
	int iy = int(y+float(dy));
	return inbound(ix, iy)?(mat(ix, iy)!=0):true;
}
bool line_(float x, float y, int dx, float coef, bool reverse, bool flipX, bool flipY) {
	int posX = 0;
	float posY, totPosY;
	posY = totPosY = 0.5+0.5*coef;
	if (posY == 1.0) {
		if (unfoldPos(x, y, posX+1, totPosY-1.0, reverse, flipX, flipY) && unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
			return false;
		posY--;
	}
	for (int posX=1 ; posX<1024 ; posX++) {
		if (posX >= dx) {break;}
		if (unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
			return false;
		posY += coef;
		totPosY += coef;
		if (posY >= 1.0) {
			if (posY == 1.0) {
				if (unfoldPos(x, y, posX+1, totPosY-1.0, reverse, flipX, flipY) && unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
					return false;
			} else {
				if (unfoldPos(x, y, posX, totPosY, reverse, flipX, flipY))
					return false;
			}
			posY --;
		}
	}
	return !unfoldPos(x, y, dx, totPosY, reverse, flipX, flipY);
}
bool line(float xf, float yf, float gxf, float gyf) {
	int x = int(xf);
	int y = int(yf);
	int gx = int(gxf);
	int gy = int(gyf);
	bool flipX, flipY, reverse;
	flipX = flipY = reverse = false;
	int dx = gx - x;
	int dy = gy - y;
	float dxf = gxf - xf;
	float dyf = gyf - yf;
	if (dx < 0) {
		dxf *= -1.0;
		dx *= -1;
		flipX = true;
	}
	if (dy < 0) {
		dyf *= -1.0;
		dy *= -1;
		flipY = true;
	}
	if (dy > dx) {
		float tmpf = dyf;
		dyf = dxf;
		dxf = tmpf;
		int tmp = dy;
		dy = dx;
		dx = tmp;
		reverse = true;
	}
	float coef = dyf/dxf;
	return line_(float(x)-0.5, float(y)-0.5, dx, coef, reverse, flipX, flipY);
}
void main(void) {
	float x = (v_position.x+1.0)*float(u_width)/2.0;
	float y = (v_position.y+1.0)*float(u_height)/2.0;
	vec4 color = u_color;
	if (u_tex) {
		color *= texture2D(u_texture, v_texcoord*u_repeat);
	}
	if (u_lit) {
		float power;
		float lx;
		float ly;
		float d;
		float minL = 0.6;
		float d2 = 0.0;
		float d3 = 0.0;
		for (int i=0 ; i<100 ; i++) {
			if (i >= u_nlights) {
				break;
			}
			lx = u_lights[i].x/float(u_side);
			ly = u_lights[i].y/float(u_side);
			power = u_lights[i].z*(1.0+0.2*abs(sin(u_time+float(i))));
			d = distance(vec2(x,y), vec2(lx,ly));
			if ((1.1-d/(power*20.0)>d3 || 1.0-d/(power*10.0)>d2) && line(x,y,lx,ly)) {
				d2 = max(d2, 1.0-d/(power*10.0));
				d3 = max(d3, 1.1-d/(power*20.0));
			}
		}
		if (d3 != 0.0) {
			gl_FragColor = vec4(vec3(1,0.7,0.5)*(vec3(color)+d2*d2*d2)*d3+minL*vec3(color), color[3]);
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

const locTime = gl.getUniformLocation(shaderProgram, "u_time");
const locSize = gl.getUniformLocation(shaderProgram, "u_size");
const locColor = gl.getUniformLocation(shaderProgram, "u_color");
const locLit = gl.getUniformLocation(shaderProgram, "u_lit");
const locTex = gl.getUniformLocation(shaderProgram, "u_tex");
const locRep = gl.getUniformLocation(shaderProgram, "u_repeat");
const locMat = gl.getUniformLocation(shaderProgram, "u_mat");
const locLights = gl.getUniformLocation(shaderProgram, "u_lights");
const locNLights = gl.getUniformLocation(shaderProgram, "u_nlights");
const locGlobalTex = gl.getUniformLocation(shaderProgram, "u_texture");
const locWidth = gl.getUniformLocation(shaderProgram, "u_width");
const locHeight = gl.getUniformLocation(shaderProgram, "u_height");
const locWidth2 = gl.getUniformLocation(shaderProgram, "u_width2");
const locHeight2 = gl.getUniformLocation(shaderProgram, "u_height2");
const locSide = gl.getUniformLocation(shaderProgram, "u_side");

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
	image.src = "/img/"+src;
	image.addEventListener('load', function() {
		// Now that the image has loaded make copy it to the texture.
		gl.bindTexture(gl.TEXTURE_2D, texture);
		gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA,gl.UNSIGNED_BYTE, image);
		gl.generateMipmap(gl.TEXTURE_2D);
	});
	textures[index] = texture;
};

let setTex = function(index) {
	gl.bindTexture(gl.TEXTURE_2D, textures[index]);
};

loadTex("wall3.png", "wall");
loadTex("wallFull.png", "wallFull");
loadTex("slab0.png", "bg0");
loadTex("slab1.png", "bg1");
loadTex("slab2.png", "bg2");
loadTex("slab3.png", "bg3");
loadTex("slab4.png", "bg4");
loadTex("slab5.png", "bg5");
loadTex("img.png", "kirby");
loadTex("slime.png", "slime");
loadTex("slime2.png", "slime2");
loadTex("knightIdle2.png", "knight");
loadTex("exit.png", "exit");
gl.uniform1i(locGlobalTex, 0);

/*============= Drawing the Quad ================*/

// Enable the depth test
//gl.enable(gl.DEPTH_TEST);

let setMap = function() {
	width = map.w*side;
	height = map.h*side;
	let width2 = width/2;
	let height2 = height/2;
	canvas.width = width;
	canvas.height = height;

	// Set the view port
	gl.viewport(0, 0, width, height);
	gl.uniform1iv(locMat, map.map.flatMap(e=>e).map(e=>e===1?1:0));
	gl.uniform1i(locWidth, map.w);
	gl.uniform1i(locHeight, map.h);
	gl.uniform1f(locWidth2, width2);
	gl.uniform1f(locHeight2, height2);
	gl.uniform1i(locSide, side);

	wallMapping();
};

let drawQuad = function(x1, y1, x2, y2, r, g, b, a, lit, tex, repeat) {
	repeat = repeat || 1;
	if (tex) {
		setTex(tex);
		gl.uniform1i(locTex, 1);
		gl.uniform1f(locRep, repeat);
	} else {
		gl.uniform1i(locTex, 0);
	}
	gl.uniform1i(locLit, lit);
	gl.uniform4f(locSize, x1, y1, x2, y2);
	gl.uniform4f(locColor, r, g, b, a);
	//Draw the triangles
	gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);
};
