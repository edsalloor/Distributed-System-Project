package com.amazonaws.lambda.demo;

import java.io.File;
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

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;
import ws.schild.jave.VideoSize;

public class VideoConverter implements RequestStreamHandler {
	final String MP4 = "mp4";
	final String WEBM = "webm";
	final String FLV = "flv";

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        // TODO: implement your handler
        
    	ObjectMapper mapper = new ObjectMapper();
		String result = "";
		
		//Building input json
		JsonNode jsonNode = mapper.readTree(input);
        
		//Getting binary data
        String input_video_base64 = jsonNode.get("vid").asText();
        byte[] data = Base64.decode(input_video_base64.getBytes());
        
        //Creating empty temporary files
		File source = new File("/tmp/src_video");
		File target = new File("/tmp/dst_video");
		source.createNewFile();
		target.createNewFile();
	    
		//Writing binary data to source file
	    FileOutputStream fos = new FileOutputStream("/tmp/src_video");
        fos.write(data);
        fos.close();
        
        //Reading destination format
        String dst_format = jsonNode.get("dst_format").asText();
        
        //Audio attributes
  		AudioAttributes audio = new AudioAttributes();
  		audio.setBitRate(128000);
  		audio.setChannels(2);
  		audio.setSamplingRate(44100);
  		
  		//Video attributes
  		VideoAttributes video = new VideoAttributes();
  		video.setBitRate(new Integer(160000));
  		video.setFrameRate(new Integer(15));
        video.setSize(new VideoSize(400, 300));
        
  		//Encoding attributes
  		EncodingAttributes attrs = new EncodingAttributes();
  		if (dst_format.equalsIgnoreCase(FLV)) {
			audio.setCodec("libvorbis");	//Audio codec
			video.setCodec("flv");			//Video codec

			attrs.setFormat("flv");			//Encoding format
        } else if (dst_format.equalsIgnoreCase(WEBM)) {
			audio.setCodec("libvorbis");	//Audio codec
			video.setCodec("libvpx");		//Video codec

			attrs.setFormat("webm");		//Encoding format
        } else {
			audio.setCodec("aac");			//Audio codec
			video.setCodec("h264");			//Video codec

			attrs.setFormat("mp4");			//Encoding format
        }
  		attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
  		
        //Encode
        Encoder encoder = new Encoder();
        try {
			encoder.encode(new MultimediaObject(source), target, attrs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}
        
        //Turning converted file into base 64 string
        byte[] videoBytes = Files.readAllBytes(target.toPath());
        String videoBase64 = Base64.encodeAsString(videoBytes);
        result = videoBase64;
        
        source.delete();
        target.delete();
		
        //Building output json
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("result", result);
		String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		output.write(jsonString.getBytes(Charset.forName("UTF-8")));
    }

}
