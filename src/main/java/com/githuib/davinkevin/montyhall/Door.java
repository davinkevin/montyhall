package com.githuib.davinkevin.montyhall;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class Door {

    private final Price value;

    public Boolean has(Price price) {
        return price == value;
    }
}
