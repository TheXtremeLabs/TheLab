package com.riders.thelab.data.remote.dto.kat;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Kat {

    private String chatId;
    private String messageId;
    private String senderId;
    private String message;
    private String messageType;
    private long timestamp;

    public Kat() {
    }

    public Kat(Map.Entry<String, Object> item) {
        switch (item.getKey()) {
            case "chatId":
                setChatId((String) item.getValue());
                break;
            case "messageId":
                setMessageId((String) item.getValue());
                break;
            case "senderId":
                setSenderId((String) item.getValue());
                break;
            case "message":
                setMessage((String) item.getValue());
                break;
            case "messageType":
                setMessageType((String) item.getValue());
                break;
            case "timestamp":
                setTimestamp((long) item.getValue());
                break;
            default:
                break;

        }
    }

    @SuppressLint("NewApi")
    public static List<Kat> buildKatMessagesList(HashMap<String, Object> katModelDto) {

        List<Kat> list = new ArrayList<>();

        // Loop on value to create a fetched list of message from database
        for (Map.Entry<String, Object> element : katModelDto.entrySet()) {
            Map<String, Object> innerMap = (Map<String, Object>) element.getValue();

            Kat kat = new Kat();

            for (Map.Entry<String, Object> item : innerMap.entrySet()) {
                switch (item.getKey()) {
                    case "chatId":
                        kat.setChatId((String) item.getValue());
                        break;
                    case "messageId":
                        kat.setMessageId((String) item.getValue());
                        break;
                    case "senderId":
                        kat.setSenderId((String) item.getValue());
                        break;
                    case "message":
                        kat.setMessage((String) item.getValue());
                        break;
                    case "messageType":
                        kat.setMessageType((String) item.getValue());
                        break;
                    case "timestamp":
                        kat.setTimestamp((long) item.getValue());
                        break;
                    default:
                        break;

                }
            }
            list.add(kat);
        }

        List<Kat> result;

        if (!LabCompatibilityManager.isNougat()) {
            Collections.sort(list, katComparator);
            result = list;
        } else {
            result = list
                    .stream()
                    .sorted((o1, o2) ->
                            String.valueOf(o1.getTimestamp())
                                    .compareTo(String.valueOf(o2.getTimestamp())))
                    .collect(Collectors.toList());

        }
        return result;
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<Kat> katComparator = new Comparator<Kat>() {

        public int compare(Kat s1, Kat s2) {
            long timestamp1 = s1.getTimestamp();
            long timestamp2 = s2.getTimestamp();

            //ascending order
            return String.valueOf(timestamp1).compareTo(String.valueOf(timestamp2));

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
}
