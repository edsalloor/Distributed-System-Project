package com.amazonaws.lambda.handlers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ImageConverter implements RequestStreamHandler {
	final String JPG = "jpg";
	final String PNG = "png";
	final String GIF = "gif";
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException  {
		// TODO Auto-generated method stub
		
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		
		try {
	        JsonNode jsonNode = mapper.readTree(input);
	        
	        String input_img_base64 = jsonNode.get("img").asText();
	        byte[] data = Base64.decode(input_img_base64.getBytes());
	        ByteArrayInputStream bais = new ByteArrayInputStream(data);
	        BufferedImage bufferedInput, bufferedOutput;
			
			bufferedInput = ImageIO.read(bais);
			
			int width = bufferedInput.getWidth();
			int height = bufferedInput.getHeight();
			
			bufferedOutput = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			
			int px[] = new int[width * height];
			bufferedInput.getRGB(0, 0, width, height, px, 0, width);
			bufferedOutput.setRGB(0, 0, width, height, px, 0, width);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			String dst_format = jsonNode.get("dst_format").asText();
			if (dst_format.equalsIgnoreCase(GIF)) {
				ImageIO.write(bufferedOutput, GIF,  baos);
			} else if (dst_format.equalsIgnoreCase(PNG)) {
				ImageIO.write(bufferedOutput, PNG,  baos);
			} else {
				ImageIO.write(bufferedOutput, JPG,  baos);
			}
			
	        String imgBase64 = Base64.encodeAsString(baos.toByteArray());
	        
	        result = imgBase64;
		} catch (Exception e) {
			String errString = "";
			
			for (StackTraceElement stelement : e.getStackTrace()) {
				errString += (stelement.toString()+"\n");
			}
			
			result =  errString;
		}
		
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("result", result);
		String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		output.write(jsonString.getBytes(Charset.forName("UTF-8")));
	}

}
