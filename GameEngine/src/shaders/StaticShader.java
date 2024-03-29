package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.Maths;

import entities.Camera;
import entities.Light;

public class StaticShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 10; // Make sure the value is the same as in vert/frag
	
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_lightAtt[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_toShadowMapSpace;
	private int location_shadowMap;
	private int location_shadowDistance;
	private int location_mapSize;
	private int location_specularMap;
	private int location_usesSpecularMap;
	private int location_modelTexture;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		location_shadowMap = super.getUniformLocation("shadowMap");
		location_shadowDistance = super.getUniformLocation("shadowDistance");
		location_mapSize = super.getUniformLocation("mapSize");
		location_specularMap = super.getUniformLocation("specularMap");
		location_usesSpecularMap = super.getUniformLocation("usesSpecularMap");
		location_modelTexture = super.getUniformLocation("modelTexture");
		
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int [MAX_LIGHTS];
		location_lightAtt = new int[MAX_LIGHTS];
		
		for(int i =0; i < MAX_LIGHTS; i++)
		{
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_lightAtt[i] = super.getUniformLocation("lightAttenuation[" + i + "]");
		}
	}
	
	public void connectTextureUnits()
	{
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_specularMap, 1);
	}
	
	public void loadUseExtraInfoMap(boolean useSpec)
	{
		super.loadBoolean(location_usesSpecularMap, useSpec);
	}
	
	public void loadShadowMap(float shadowDistance, float mapSize)
	{
		super.loadInt(location_shadowMap, 5);
		super.loadFloat(location_shadowDistance, shadowDistance);
		super.loadFloat(location_mapSize, mapSize);
	}
	
	public void loadToShadowSpaceMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_toShadowMapSpace, matrix);
	}
	
	public void loadSkyColour(float r, float g, float b)
	{
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadFakeLighting(boolean useFake)
	{
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	public void loadNumberOfRows(int numberOfRows)
	{
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y)
	{
		super.loadVector(location_offset, new Vector2f(x,y));
	}
	
	public void loadShineVariables(float damper, float reflectivity)
	{
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadClipPlane(Vector4f plane)
	{
		super.loadVector(location_plane, plane);
	}
	
	public void loadLights(List<Light> lights)
	{
		for(int i = 0; i < MAX_LIGHTS; i++)
		{
			if(i<lights.size())
			{
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_lightAtt[i], lights.get(i).getAttenuation());
			}
			else //So lights are never more/less than MAX_LIGHTS - load empty light
			{
				super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector(location_lightColour[i],  new Vector3f(0,0,0));
				super.loadVector(location_lightAtt[i], new Vector3f(1,0,0));
			}
		}
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	

}
