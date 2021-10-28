#version 460 core

// these are the variables that were output from the Vertex Shader to the
// Fragment Shader
in vec3 passColor;
in vec2 passTextureCoord;
in float shouldBeTextured;

// this is the color that the GPU should render the pixel in
out vec4 outColor;

// the texture which we are loading in
uniform sampler2D tex;

void main() {
	// the output color (either a regular color or a texture)
	if (shouldBeTextured == 1) {
		// this will output the correct color of the texture for the given
		// coordinate
		outColor = texture(tex, passTextureCoord);
	} else {
		outColor = vec4(passColor, 1.0f);
	}
}