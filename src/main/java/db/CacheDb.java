package db;

import dto.ItemDto;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static helpers.ItemsHelper.fillCacheFromCsv;

public class CacheDb {

    public volatile static List<ItemDto> list = new LinkedList<>();

    static {
        try {
            fillCacheFromCsv(null, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
