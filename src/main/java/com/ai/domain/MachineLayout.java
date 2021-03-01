package com.ai.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@Builder
public class MachineLayout {

    private int columns;
    private int rows;
    private int itemCount;

}