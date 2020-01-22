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

public class AudioConverter implements RequestStreamHandler {
	final String MP3 = "mp3";
	final String ACC = "acc";
	final String OGG = "ogg";

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub
		
		ObjectMapper mapper = new ObjectMapper();
		String result = "";

		//Building input json
		JsonNode jsonNode = mapper.readTree(input);

		//Getting binary data
        String input_img_base64 = jsonNode.get("aud").asText();
        byte[] data = Base64.decode(input_img_base64.getBytes());

        //Creating empty temporary files
		File source = new File("/tmp/src_audio");
		File target = new File("/tmp/dst_audio");
		source.createNewFile();
		target.createNewFile();

		//Writing binary data to source file
	    FileOutputStream fos = new FileOutputStream("/tmp/src_audio");
        fos.write(data);
        fos.close();

        //Reading destination format
        String dst_format = jsonNode.get("dst_format").asText();

        //Audio Attributes
		AudioAttributes audio = new AudioAttributes();
		audio.setBitRate(128000);
		audio.setChannels(2);
		audio.setSamplingRate(44100);
		
		//Encoding attributes
		EncodingAttributes attrs = new EncodingAttributes();
        if (dst_format.equalsIgnoreCase(OGG)) {
			audio.setCodec("libvorbis");	//Audio codec

			attrs.setFormat("ogg");			//Encoding format
        } else if (dst_format.equalsIgnoreCase(ACC)) {
			audio.setCodec("acc");		//Audio codec

			attrs.setFormat("acc");		//Encoding format
        } else {
			audio.setCodec("libmp3lame");	//Audio codec

			attrs.setFormat("mp3");			//Encoding format
        }
        attrs.setAudioAttributes(audio);

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
		byte[] audioBytes = Files.readAllBytes(target.toPath());
		String audioBase64 = Base64.encodeAsString(audioBytes);
        result = audioBase64;

        //Deleting temporary files
        source.delete();
        target.delete();

        //Building output json
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("result", result);
		String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		output.write(jsonString.getBytes(Charset.forName("UTF-8")));
	}
}