#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;							// Per-Pixel Lighting

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition;				// Per-Pixel Lighting
uniform float useFakeLighting;			// For 2D Objects
uniform float atlasNumberOfRows;		// Texture Atlases
uniform vec2 atlasOffsets;				// Texture Atlases
uniform float densityOfFog;				// Fog
uniform float gradientOfFog;			// Fog

out vec2 pass_textureCoords;
out vec3 surfaceNormal; 				// Per-Pixel Lighting
out vec3 toLightVector;					// Per-Pixel Lighting
out vec3 toCameraVector;				// Specular Lighting
out float visibility;					// Fog


void main(){
	
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	
	pass_textureCoords = (textureCoords / atlasNumberOfRows) + atlasOffsets;
	
	vec3 actualNormal = normal;
	if(useFakeLighting > 0.5)
		actualNormal = vec3(0.0, 1.0, 0.0);
	
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;						// Per-Pixel Lighting
	toLightVector = lightPosition - worldPosition.xyz;											// Per-Pixel Lighting
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;	// Specular Lighting
	
	float distanceFromCamera = length(positionRelativeToCamera.xyz);							// Fog
	visibility = exp(-pow((distanceFromCamera * densityOfFog), gradientOfFog));					// Fog
	visibility = clamp(visibility, 0.0, 1.0);													// Fog
}
