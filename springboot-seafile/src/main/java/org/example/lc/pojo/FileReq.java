package org.example.lc.pojo;

import lombok.Data;

@Data
public class FileReq {
    private String token;
    private Long timeFrom;
    private Long timeTo;
}
