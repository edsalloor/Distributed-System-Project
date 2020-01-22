package com.amazonaws.lambda.demo;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TextFileConverter implements RequestStreamHandler {
	final String TXT = "txt";
	final String LOG = "log";
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        // TODO: implement your handler
    	ObjectMapper mapper = new ObjectMapper();
		String result = "";
		
		try {
	        JsonNode jsonNode = mapper.readTree(input);
	        
	        String input_base64 = jsonNode.get("text_file").asText();
	        byte[] data = Base64.decode(input_base64.getBytes());
	        
	        String dst_format = jsonNode.get("dst_format").asText();
	        String textBase64 = "";
			if (dst_format.equalsIgnoreCase(TXT)) {
				FileOutputStream fos = new FileOutputStream("/tmp/temp.txt");
		        fos.write(data);
		        fos.close();
		        
		        byte[] dataOutput = Files.readAllBytes(new File("/tmp/temp.txt").toPath());
				
		        textBase64 += Base64.encodeAsString(dataOutput);
			} else if (dst_format.equalsIgnoreCase(LOG)) {
				FileOutputStream fos = new FileOutputStream("/tmp/temp.log");
		        fos.write(data);
		        fos.close();
		        
		        byte[] dataOutput = Files.readAllBytes(new File("/tmp/temp.log").toPath());
				
		        textBase64 += Base64.encodeAsString(dataOutput);
			} else {
				FileOutputStream fos = new FileOutputStream("/tmp/temp");
		        fos.write(data);
		        fos.close();
		        
		        byte[] dataOutput = Files.readAllBytes(new File("/tmp/temp").toPath());
				
		        textBase64 += Base64.encodeAsString(dataOutput);
			}
	        
	        
	        result = textBase64;
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
