package com.example.mufgtest.controller;

import com.example.mufgtest.pojo.Move;
import com.example.mufgtest.pojo.OrderInput;
import com.example.mufgtest.pojo.Position;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bot")
public class RoverController {

    @Autowired
    ResourceLoader resourceLoader;

    @PostMapping("/move")
    public ResponseEntity<InputStreamResource> moveRover(@RequestBody OrderInput orderInput) {
        Position currPosition = orderInput.getPosition();
        String startingDir = currPosition.getDirection();
        int currX = currPosition.getX();
        int currY = currPosition.getY();

        List<Move> moves = orderInput.getMove();
        Collections.sort(moves, Comparator.comparingInt(Move::getOrder));

        String[] directions = {"N", "E", "S", "W"};
        List<String> dir = Arrays.stream(directions).collect(Collectors.toList());


        int currIndex = dir.indexOf(startingDir);
        String newDirection = null;
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (move.getLeft() == 0) {
                if (move.getRight() == 90) {
                    currIndex = currIndex + 1 <= directions.length - 1 ? currIndex + 1 : (currIndex + 1) - directions.length;
                }
                if (move.getRight() == 180) {
                    currIndex = currIndex + 2 <= directions.length - 1 ? currIndex + 2 : (currIndex + 2) - directions.length;
                }
                if (move.getRight() == 270) {
                    currIndex = currIndex + 3 <= directions.length - 1 ? currIndex + 3 : (currIndex + 3) - directions.length;
                }
            }

            if (move.getRight() == 0) {
                if (move.getLeft() == 90) {
                    currIndex = currIndex - 1 >= 0 ? currIndex - 1 : directions.length - Math.abs(currIndex - 1);
                }
                if (move.getLeft() == 180) {
                    currIndex = currIndex - 2 >= 0 ? currIndex - 2 : directions.length - Math.abs(currIndex - 2);
                }
                if (move.getLeft() == 270) {
                    currIndex = currIndex - 3 >= 0 ? currIndex - 3 : directions.length - Math.abs(currIndex - 3);
                }
            }

            newDirection = dir.get(currIndex);

            if (dir.get(currIndex).equals("W")) {
                currX = currX - move.getForward() + move.getBackward();
            } else if (dir.get(currIndex).equals("E")) {
                currX = currX + move.getForward() - move.getBackward();
            }

            if (dir.get(currIndex).equals("N")) {
                currY = currY + move.getForward() - move.getBackward();
            } else if (dir.get(currIndex).equals("S")) {
                currY = currY - move.getForward() + move.getBackward();
            }

        }

        System.out.println("Result>>>>newDirection:" + newDirection + ",X:" + currX + ",Y:" + currY);

        Position position = new Position();
        position.setDirection(newDirection);
        position.setX(currX);
        position.setY(currY);

        ObjectMapper mapper = new ObjectMapper();
        byte[] buf = new byte[0];
        try {
            buf = mapper.writeValueAsBytes(position);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity
                .ok()
                .header("Content-Disposition", "attachment; filename=\"Output.json\"")
                .contentLength(buf.length)
                .contentType(
                        MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new ByteArrayInputStream(buf)));
    }

    @GetMapping("/read")
    public ResponseEntity<String> readCurrentPosition() {
        Resource resource = resourceLoader.getResource("classpath:input.json");
        InputStream inputStream;
        JsonObject position = null;
        try {
            inputStream = resource.getInputStream();
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            String data = new String(bdata, StandardCharsets.UTF_8);
            JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
            position = jsonObject.getAsJsonObject("Position");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new Gson().toJson(position));
    }
}
