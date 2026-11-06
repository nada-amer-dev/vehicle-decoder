package com.example.vehicledecoder;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NhtsaResponse(@JsonProperty("Results") List<NhtsaResult> results) {

}
