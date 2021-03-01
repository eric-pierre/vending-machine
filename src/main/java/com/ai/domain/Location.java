package com.ai.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ai.exception.InvalidLocationException;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class Location implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1450953708775961472L;

    public static final Location NONE = Location.builder().column(Integer.MIN_VALUE).row(Integer.MAX_VALUE).build();

    private final int column;
    private final int row;

    private static final Pattern pattern = Pattern.compile("^([A-Z])([1-9][0-9]?)$");
    private final String value;

    public static Location fromString(String location) {
        Matcher m = pattern.matcher(location);
        if (m.find()) {
            int col = m.group(1).charAt(0) - 'A';
            int r = Integer.parseInt(m.group(2)) - 1;
            return Location.builder().column(col).row(r).build() ;
        }
        throw new InvalidLocationException("Location " + location + " is not valid");
    }



    @Override
    @JsonValue
    public String toString() {
        return String.valueOf((char) (column + 'A')) + (row + 1);
    }
}
