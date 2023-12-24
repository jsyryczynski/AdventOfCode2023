package com.jsyryczynski;

import static com.jsyryczynski.Hand.Type.FiveOfKind;
import static com.jsyryczynski.Hand.Type.FourOfKind;
import static com.jsyryczynski.Hand.Type.FullHouse;
import static com.jsyryczynski.Hand.Type.HighCard;
import static com.jsyryczynski.Hand.Type.OnePair;
import static com.jsyryczynski.Hand.Type.ThreeOfKind;
import static com.jsyryczynski.Hand.Type.TwoPair;

import java.util.Collections;
import java.util.HashMap;
import lombok.AllArgsConstructor;

/**
 *
 */
public class Hand implements Comparable<Hand>{
    public static String cardValues = "AKQT98765432J";
    enum Type {
        FiveOfKind,
        FourOfKind,
        FullHouse,
        ThreeOfKind,
        TwoPair,
        OnePair,
        HighCard
    };

    public String cards;
    public Long bid;

    public Type type;

    public Hand(String cards, Long bid) {
        this.cards = cards;
        this.bid = bid;

        type = calcType();
    }

    private Type calcType() {
        HashMap<Character, Integer> cardsMap = new HashMap<>();
        // init
        for (int idx = 0; idx < cards.length(); ++idx) {
            Character c = cards.charAt(idx);
            cardsMap.put(c, 0);
        }

        // count
        int jokerCount = 0;
        int maxFrequency = 0;
        for (int idx = 0; idx < cards.length(); ++idx) {
            Character c = cards.charAt(idx);
            if (c.equals('J')) {
                jokerCount++;
            }
            else {
                int frequency = cardsMap.get(c) + 1;
                if (frequency > maxFrequency) {
                    maxFrequency = frequency;
                }
                cardsMap.put(c, frequency);
            }
        }


        if (maxFrequency + jokerCount == 5) {
            return FiveOfKind;
        }
        else if (maxFrequency + jokerCount == 4) {
            return FourOfKind;
        }
        else if ((cardsMap.containsValue(3) && cardsMap.containsValue(2) ) ||
                (Collections.frequency(cardsMap.values(), 2) == 2 && jokerCount == 1)) {
            return FullHouse;
        }
        else if (maxFrequency + jokerCount == 3) {
            return ThreeOfKind;
        }
        else if (Collections.frequency(cardsMap.values(), 2) == 2) {
            return TwoPair;
        }
        else if (cardsMap.containsValue(2) || jokerCount == 1) {
            return OnePair;
        }
        else {
            return HighCard;
        }
    }

    @Override
    public int compareTo(Hand o) {
        if (o.type != type) {
            return -1 * type.compareTo(o.type);
        }
        else {
            for (int idx = 0; idx < 5; ++idx) {
                Character oc = o.cards.charAt(idx);
                Character tc = cards.charAt(idx);

                var valueOther = cardValues.indexOf(oc);
                var valueThis = cardValues.indexOf(tc);

                if (valueOther != valueThis) {
                    return Integer.compare(valueOther, valueThis);
                }
            }
        }
        // should not reach this
        return CharSequence.compare(cards, o.cards);
    }
}
