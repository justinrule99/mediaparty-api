package com.mediaparty.api.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaparty.api.models.Video;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class VideoTextEncoder implements Encoder.Text<Video> {
    @Override
    public String encode(Video video) throws EncodeException {
        // return as json
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(video);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error parsing JSON";
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
