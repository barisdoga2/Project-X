#version 400 core

const int maxLightCount = 4;					// Multiple Lights

in vec2 pass_textureCoords;
in vec3 surfaceNormal;							// Per-Pixel Lighting
in vec3 toLightVector[maxLightCount];			// Per-Pixel Lighting
in vec3 toCameraVector;							// Specular Lighting
in float visibility;							// Fog
in float distanceFromBrush;

uniform sampler2D backgroundTexture;			// Multitexturing
uniform sampler2D rTexture;						// Multitexturing
uniform sampler2D gTexture;						// Multitexturing
uniform sampler2D bTexture;						// Multitexturing
uniform sampler2D aTexture;						// Multitexturing
uniform sampler2D blendMapTexture;				// Multitexturing
uniform float tilingFactor;						// Multitexturing
uniform float minDiffuseLighting;				// Per-Pixel Lighting
uniform float minSpecularLighting;				// Specular Lighting
uniform float brushWidth;
uniform vec4 brushColor;

uniform vec3 lightColor[maxLightCount];			// Per-Pixel Lighting
uniform vec3 lightAttenuation[maxLightCount];	// Point Lights
uniform float shineDamper;						// Specular Lighting
uniform float reflectivity;						// Specular Lighting
uniform vec3 skyColor;							// Fog

out vec4 out_Color;

void main(){

	vec4 blendMapColor = texture(blendMapTexture, pass_textureCoords);											// Multitexturing
	float backgroundTextureAmount = -(blendMapColor.r + blendMapColor.g + blendMapColor.b) + blendMapColor.a;	// Multitexturing
	vec2 tiledTextureCoords = pass_textureCoords * tilingFactor;												// Multitexturing
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledTextureCoords) * backgroundTextureAmount;		// Multitexturing
	vec4 rTextureColor = texture(rTexture, tiledTextureCoords) * blendMapColor.r;								// Multitexturing
	vec4 gTextureColor = texture(gTexture, tiledTextureCoords) * blendMapColor.g;								// Multitexturing
	vec4 bTextureColor = texture(bTexture, tiledTextureCoords) * blendMapColor.b;								// Multitexturing
	vec4 aTextureColor = texture(aTexture, tiledTextureCoords) * (1.0 - blendMapColor.a);
	vec4 totalTextureColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor + aTextureColor;	// Multitexturing
	
	vec3 normalizedSurfaceNormal = normalize(surfaceNormal);													// Per-Pixel Lighting - Specular Lighting
	vec3 normalizedToCameraVector = normalize(toCameraVector);													// Specular Lighting
	
	vec3 totalDiffuseLighting = vec3(0.0);																		// Per-Pixel Lighting - Specular Lighting
	vec3 totalSpecularLighting = vec3(0.0);																		// Per-Pixel Lighting - Specular Lighting
	for(int i = 0 ; i < maxLightCount ; i++){
		float distance = length(toLightVector[i]);
		float attenuationLightFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);	// Point Lights
		vec3 normalizedToLightVector = normalize(toLightVector[i]);												// Per-Pixel Lighting
		float dotProduct = dot(normalizedSurfaceNormal, normalizedToLightVector);								// Per-Pixel Lighting
		float brightness = max(dotProduct, 0.0);																// Per-Pixel Lighting
		vec3 lightDirection = -normalizedToLightVector;															// Specular Lighting
		vec3 reflectedLightDirection = reflect(lightDirection, normalizedSurfaceNormal);						// Specular Lighting
		float specularFactor = dot(reflectedLightDirection, normalizedToCameraVector);							// Specular Lighting
		specularFactor = max(specularFactor, 0.0);																// Specular Lighting
		float dampedFactor = pow(specularFactor, shineDamper);													// Specular Lighting
		totalDiffuseLighting = totalDiffuseLighting + (brightness * lightColor[i]) / attenuationLightFactor;	// Per-Pixel Lighting
		totalSpecularLighting = totalSpecularLighting + (dampedFactor * reflectivity * lightColor[i]) / attenuationLightFactor;		// Specular Lighting
	}
	totalDiffuseLighting = max(totalDiffuseLighting, minDiffuseLighting);
	totalSpecularLighting = max(totalSpecularLighting, minSpecularLighting);

	
	out_Color = vec4(totalDiffuseLighting, 1.0) * totalTextureColor + vec4(totalSpecularLighting, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
	
	if(distanceFromBrush < brushWidth)
		out_Color = mix(out_Color, brushColor, 0.5);

}
