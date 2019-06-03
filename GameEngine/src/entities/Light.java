package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Light 
{
	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1,0,0);
	
	
	public Light(Vector3f position, Vector3f colour) 
	{
		this.position = position;
		this.colour = colour;
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) 
	{
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	public List<Light> createLightList()
	{
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0000,10000,-10000), new Vector3f(1,1,1)));
		lights.add(new Light(new Vector3f(-200,10,-200), new Vector3f(10,0,0)));
		lights.add(new Light(new Vector3f(200,10,200), new Vector3f(0,0,10)));
		
		return lights;
	}
	

	public Vector3f getPosition() {
		return position;
	}


	public void setPosition(Vector3f position) {
		this.position = position;
	}


	public Vector3f getColour() {
		return colour;
	}


	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	public Vector3f getAttenuation()
	{
		return attenuation;
	}
	
	
}
