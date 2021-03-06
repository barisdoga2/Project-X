#version 400 core

const int maxLightCount = 4;					// Multiple Lights

in vec2 pass_textureCoords;
in vec3 toLightVector[maxLightCount];			// Per-Pixel Lighting
in vec3 toCameraVector;							// Specular Lighting
in float visibility;							// Fog

uniform sampler2D modelTexture;
uniform sampler2D normalMapTexture;				// Normal Mapping
uniform sampler2D specularTexture;				// Specular Mapping
uniform float useSpecularMapping;				// Specular Mapping
uniform vec3 lightColor[maxLightCount];			// Per-Pixel Lighting
uniform vec3 lightAttenuation[maxLightCount];	// Point Lights
uniform float shineDamper;						// Specular Lighting
uniform float reflectivity;						// Specular Lighting
uniform vec3 skyColor;							// Fog
uniform float minDiffuseLighting;				// Per-Pixel Lighting
uniform float minSpecularLighting;				// Specular Lighting

out vec4 out_Color;

void main(){

	vec4 normalMapColor = 2.0 * texture(normalMapTexture, pass_textureCoords) - 1.0;																// Normal Mapping

	vec3 normalizedSurfaceNormal = normalize(normalMapColor.xyz);																					// Per-Pixel Lighting - Specular Lighting
	vec3 normalizedToCameraVector = normalize(toCameraVector);																						// Specular Lighting

	vec3 totalDiffuseLighting = vec3(0.0);																											// Per-Pixel Lighting - Specular Lighting
	vec3 totalSpecularLighting = vec3(0.0);																											// Per-Pixel Lighting - Specular Lighting

	for(int i = 0 ; i< maxLightCount ; i++){
		float distance = length(toLightVector[i]);
		float attenuationLightFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);	// Point Lights
		vec3 normalizedToLightVector = normalize(toLightVector[i]);																					// Per-Pixel Lighting
		float dotProduct = dot(normalizedSurfaceNormal, normalizedToLightVector);																	// Per-Pixel Lighting
		float brightness = max(dotProduct, 0.0);																									// Per-Pixel Lighting
		vec3 lightDirection = -normalizedToLightVector;																								// Specular Lighting
		vec3 reflectedLightDirection = reflect(lightDirection, normalizedSurfaceNormal);															// Specular Lighting
		float specularFactor = dot(reflectedLightDirection, normalizedToCameraVector);																// Specular Lighting
		specularFactor = max(specularFactor, 0.0);																									// Specular Lighting
		float dampedFactor = pow(specularFactor, shineDamper);																						// Specular Lighting
		totalDiffuseLighting = totalDiffuseLighting + (brightness * lightColor[i]) / attenuationLightFactor;										// Per-Pixel Lighting
		totalSpecularLighting = totalSpecularLighting + (dampedFactor * reflectivity * lightColor[i]) / attenuationLightFactor;						// Specular Lighting
	}
	totalDiffuseLighting = max(totalDiffuseLighting, minDiffuseLighting);
	totalSpecularLighting = max(totalSpecularLighting, minSpecularLighting);
	
	vec4 textureColor = texture(modelTexture, pass_textureCoords);
	if(textureColor.a < 0.5){
		discard;
	}

	if(useSpecularMapping > 0.5){
		vec4 specularMapColor = texture(specularTexture, pass_textureCoords);
		totalSpecularLighting *= specularMapColor.r;
		if(specularMapColor.g > 0.5){
			totalDiffuseLighting = vec3(1.0, 1.0, 1.0);
		}
	}

	out_Color = vec4(totalDiffuseLighting, 1.0) * textureColor + vec4(totalSpecularLighting, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}
