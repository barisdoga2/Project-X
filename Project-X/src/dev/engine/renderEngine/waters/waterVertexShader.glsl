#version 400 core

const int MAX_LIGHTS = 4;

in vec2 position;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform float waterTilingFactor;
uniform vec3 cameraPosition;
uniform vec3 lightPosition[MAX_LIGHTS];
uniform float densityOfFog;
uniform float gradientOfFog;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector[MAX_LIGHTS];
out float visiblityOfPixelCausedByFog;


void main(void) {

	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);

	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;

	textureCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * waterTilingFactor;
	toCameraVector = cameraPosition - worldPosition.xyz;
	for(int i = 0 ; i < MAX_LIGHTS ; i++)
		fromLightVector[i] = worldPosition.xyz - lightPosition[i];

	float distance = length((viewMatrix * worldPosition).xyz);
	visiblityOfPixelCausedByFog = exp(-pow(distance * densityOfFog, gradientOfFog));

}
