#version 400 core

const int MAX_LIGHTS = 4;

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector[MAX_LIGHTS];
in float visiblityOfPixelCausedByFog;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvTexture;
uniform sampler2D normalTexture;
uniform sampler2D depthTexture;
uniform vec3 lightColor[MAX_LIGHTS];
uniform vec3 lightAttenuation[MAX_LIGHTS];
uniform float waterWaveMoveSpeed;
uniform float waterWaveStrength;
uniform float waterReflectivityFactor;
uniform vec3 skyColor;
uniform float waterShineDamper;
uniform float waterReflectivity;
uniform float nearPlane;
uniform float farPlane;

out vec4 out_Color;


void main(void) {

	vec2 nornmalizedDeviceCoords = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 reflectionTextureCoords = vec2(nornmalizedDeviceCoords.x, -nornmalizedDeviceCoords.y);
	vec2 refractionTextureCoords = vec2(nornmalizedDeviceCoords.x, nornmalizedDeviceCoords.y);

	float depth = texture(depthTexture, refractionTextureCoords).r;
	float floorDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - (2.0 * depth - 1.0) * (farPlane - nearPlane));

	depth = gl_FragCoord.z;
	float waterDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - (2.0 * depth - 1.0) * (farPlane - nearPlane));
	float waterDepth = floorDistance - waterDistance;

	vec2 distortedTexureCoords = texture(dudvTexture, vec2(textureCoords.x + waterWaveMoveSpeed, textureCoords.y)).rg * 0.1;
	distortedTexureCoords = textureCoords + vec2(distortedTexureCoords.x, distortedTexureCoords.y + waterWaveMoveSpeed);
	vec2 totalDistortion = (texture(dudvTexture, distortedTexureCoords).rg * 2.0 - 1.0) * waterWaveStrength * clamp(waterDepth / 20.0, 0.0, 1.0);

	reflectionTextureCoords += totalDistortion;
	reflectionTextureCoords.x = clamp(reflectionTextureCoords.x, 0.0001, 0.999);
	reflectionTextureCoords.y = clamp(reflectionTextureCoords.y, -0.999, -0.001);

	refractionTextureCoords += totalDistortion;
	refractionTextureCoords = clamp(refractionTextureCoords, 0.001, 0.999);

	vec4 reflectionColor = texture(reflectionTexture, reflectionTextureCoords);
	vec4 refractionColor = texture(refractionTexture, refractionTextureCoords);

	vec4 normalTextureColor = texture(normalTexture, distortedTexureCoords);
	vec3 normal = vec3(normalTextureColor.r * 2.0 - 1.0, normalTextureColor.b * 3.0, normalTextureColor.g * 2.0 - 1.0);
	normal = normalize(normal);

	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, normal);
	refractiveFactor = pow(refractiveFactor, waterReflectivityFactor);
	refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);

	vec3 totalSpecularHighlight = vec3(0.0);
	for(int i = 0 ; i < MAX_LIGHTS ; i++){
		float distanceFromLight = length(fromLightVector[i]);
		float attenuationFactor = lightAttenuation[i].x + lightAttenuation[i].y  * distanceFromLight + lightAttenuation[i].z * distanceFromLight * distanceFromLight;

		vec3 reflectedLight = reflect(normalize(fromLightVector[i]), normal);
		float specularFactor = max(dot(reflectedLight, viewVector), 0.0);
		specularFactor = pow(specularFactor, waterShineDamper);
		vec3 specularHighlight = lightColor[i] * specularFactor * waterReflectivity * clamp(waterDepth / 5.0, 0.0, 1.0);
		totalSpecularHighlight = totalSpecularHighlight + specularHighlight / attenuationFactor;
	}

	out_Color = mix(reflectionColor, refractionColor, refractiveFactor);
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + vec4(totalSpecularHighlight, 0.0);
	out_Color.a = clamp(waterDepth / 5.0, 0.0, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visiblityOfPixelCausedByFog); // Fog Effect Applyed Here
}
