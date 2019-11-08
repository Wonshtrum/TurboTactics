/*============ Creating a canvas =================*/

let canvas = document.getElementById('myCan');
let gl = canvas.getContext('webgl', { preserveDrawingBuffer: true });

/*========== Defining and storing the geometry =========*/

const width = 25;
const height = 25;
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
'	vec3 pos = vec3((coordinates.x*(u_size[2]-u_size[0])+u_size[0])/'+width/2.0+'-1.0, (coordinates.y*(u_size[3]-u_size[1])+u_size[1])/'+height/2.0+'-1.0, coordinates.z);' +
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
'	gl_FragColor = vec4(v_position*0.5+0.5, 1.0);' +
'	gl_FragColor = u_color;' +
'}';

// Create fragment shader object 
let fragShader = gl.createShader(gl.FRAGMENT_SHADER);

// Attach fragment shader source code
gl.shaderSource(fragShader, fragCode);

// Compile the fragmentt shader
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

let clearCan = function(r, g, b) {
	if (!(r && g && b)) {
		r = 1;
		g = 1;
		b = 1;
	}
	// Clear the color buffer bit
	gl.clear(gl.COLOR_BUFFER_BIT);
	// Clear the canvas
	gl.clearColor(r, g, b, 1);
}

let drawQuad = function(x1, y1, x2, y2, r, g, b, a) {
	a = a || 1;
	gl.uniform4f(locSize, x1, y1, x2, y2);
	gl.uniform4f(locColor, r, g, b, a);
	//Draw the triangles
	gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT,0);	
}