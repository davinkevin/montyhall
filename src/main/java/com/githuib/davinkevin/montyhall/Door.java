package com.githuib.davinkevin.montyhall;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Door {

    private final Price price;

    public static Door with(Price price) {
        return new Door(price);
    }

    public Boolean has(Price price) {
        return price == this.price;
    }
}
