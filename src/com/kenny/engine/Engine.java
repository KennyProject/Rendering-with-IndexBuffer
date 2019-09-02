package com.kenny.engine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import com.kenny.engine.input.Keyboard;
import com.kenny.engine.input.Mouse;

public class Engine 
{
	public static final int WIDHT = 640;
	public static final int HEIGHT = 360;
	public static final String TITLE = "Engine 0.0.1 pre-alpha";
	private EngineWindow engineWindow;
	
	public void run() 
	{
		this.init();
	}
	
	public void init() 
	{
		this.engineWindow = new EngineWindow(WIDHT, HEIGHT, TITLE);
		this.engineWindow.create();
		this.update();
	}
	
	public IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	public FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	public void update() 
	{
		float[] v_quad = {0.5f, 0.5f, 0,   -0.5f, 0.5f, 0,   -0.5f, -0.5f, 0,   0.5f, -0.5f, 0 };
		
		int [] i_quad = {0, 1, 2, 0, 2, 3};
		
		//���������� Vertex Array � ��������� ���
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		
		//���������� Element Vertex Buffer (Ibo) � ��������� ���
		int iboId = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, iboId);
		IntBuffer iBuffer = this.storeDataInIntBuffer(i_quad);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL30.GL_STATIC_DRAW);
		MemoryUtil.memFree(iBuffer);
		
		//���������� Vertex Buffer � ��������� ���
		int vboId = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
		//������ float ������ � ���������� ���� v_quad ������� ������������
		FloatBuffer fBuffer = this.storeDataInFloatBuffer(v_quad);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, fBuffer, GL30.GL_STATIC_DRAW);
		MemoryUtil.memFree(fBuffer);
		//������ ������� ��� �������, �������� id, ��� ������.
		GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		
		//����������� Vbo's � ��� ���� Vao
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
		GL30.glBindVertexArray(vaoId);
	
		while(!this.engineWindow.isCloseRequest())
		{
			Keyboard.handleKeyboardInput();
			Mouse.handleMouseInput();
			
			//�������������� ����� ��� ����������, ������� ���� � ����������� ������ � �������
			//������ �������� / �����.
			GL11.glClearColor(0, 1, 1, 1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			GL30.glBindVertexArray(vaoId);
			//�������� ��������� ���� 0 ������� �������
			GL30.glEnableVertexAttribArray(0);
			//������ ������ ��������� ������������.
			GL11.glDrawElements(GL11.GL_TRIANGLES, i_quad.length, GL11.GL_UNSIGNED_INT, 0);
			//��������� ��������� ���� 0 ������� �������
			GL30.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(vaoId);

			this.engineWindow.update();
		}
		
		this.engineWindow.destroy();
	}

	public EngineWindow getEngineWindow() 
	{
		return this.engineWindow;
	}
}
