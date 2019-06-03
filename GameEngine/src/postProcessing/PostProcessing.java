package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.BrightFilter;
import bloom.CombineFilter;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import models.RawModel;
import renderEngine.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBloomBlur;
	private static VerticalBlur vBloomBlur;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;

	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		hBlur = new HorizontalBlur(Display.getWidth()/8, Display.getHeight()/8);
		vBlur = new VerticalBlur(Display.getWidth()/8, Display.getHeight()/8);
		brightFilter = new BrightFilter(Display.getWidth()/2, Display.getHeight()/2);
		hBloomBlur = new HorizontalBlur(Display.getWidth()/4, Display.getHeight()/4);
		vBloomBlur = new VerticalBlur(Display.getWidth()/4, Display.getHeight()/4);
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int colourTexture, float cameraPosition, int brightTexture){
		start();
		if(cameraPosition < -8)
		{
			hBlur.render(colourTexture);
			vBlur.render(hBlur.getOutputTexture());
			contrastChanger.render(vBlur.getOutputTexture());
		}
		else
		{
			//brightFilter.render(colourTexture);
			hBloomBlur.render(brightTexture);
			vBloomBlur.render(hBloomBlur.getOutputTexture());
			combineFilter.render(colourTexture, vBloomBlur.getOutputTexture());
			contrastChanger.render(colourTexture);
			
		}
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		brightFilter.cleanUp();
		hBloomBlur.cleanUp();
		vBloomBlur.cleanUp();
		combineFilter.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
