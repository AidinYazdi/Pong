#version 460 core

// these are the variables that we've stored in the VAO
// they must be accessed in the order of the location that we've stored them in
// the VAO
in vec3 position;
in vec3 color;
in vec2 textureCoord;

// these are the output variables which are output to the Fragment Shader
out vec3 passColor;
out vec2 passTextureCoord;
out float shouldBeTextured;

// the uniform that helps scale the texture onto/with the mesh
uniform mat4 model;
// the uniform that deals with the camera
uniform mat4 view;
// the uniform that helps the projection work (the projection matrix)
uniform mat4 projection;
// the uniform that specifies if a texture should be used or not
uniform int isTextured;

void main() {
	gl_Position = projection * view * model * vec4(position, 1.0);
	passColor = color;
	passTextureCoord = textureCoord;
	shouldBeTextured = isTextured;
}