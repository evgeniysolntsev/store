package service;

import dto.ItemDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static db.CacheDb.itemList;
import static db.CacheDb.itemMap;

public abstract class ItemService {
    public static final String ITEMS_CSV = "items.csv";
    public static String[] ITEM_HEADER = {"id", "title", "price", "description", "instagramLikeUrl", "reservation", "hide"};

    public synchronized static void removeOrReservation(
            final List<String> ids, boolean isReservation
    ) throws IOException {
        if (ids == null || ids.isEmpty()) return;
        final Reader in = new FileReader(ITEMS_CSV);
        final Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(ITEM_HEADER)
                .withFirstRecordAsHeader()
                .parse(in);
        try (CSVPrinter printer = new CSVPrinter(new PrintWriter(ITEMS_CSV), CSVFormat.DEFAULT.withHeader(ITEM_HEADER))) {
            for (final CSVRecord record : records) {
                final String id = record.get("id");
                final String title = record.get("title");
                final String price = record.get("price");
                final String description = record.get("description");
                final String instagramLikeUrl = record.get("instagramLikeUrl");
                String reservation = record.get("reservation");
                String hide = record.get("hide");
                if (ids.contains(id)) {
                    if (isReservation) {
                        reservation = "true";
                        ItemDto itemDto = itemMap.get(id);
                        itemDto.setReservation(reservation);
                    } else {
                        hide = "true";
                        itemMap.remove(id);
                        itemList = new ArrayList<>(itemMap.values());
                    }
                }
                printer.printRecord(
                        id,
                        title,
                        price,
                        description,
                        instagramLikeUrl,
                        reservation,
                        hide
                );
            }
        }
    }

    public synchronized static List<ItemDto> findById(
            final List<String> ids
    ) throws IOException {
        if (ids == null || ids.isEmpty()) new ArrayList<>();
        List<ItemDto> list = new ArrayList<>();
        final Reader in = new FileReader(ITEMS_CSV);
        final Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(ITEM_HEADER)
                .withFirstRecordAsHeader()
                .parse(in);
        for (final CSVRecord record : records) {
            final String id = record.get("id");
            final String title = record.get("title");
            final String price = record.get("price");
            final String description = record.get("description");
            final String instagramLikeUrl = record.get("instagramLikeUrl");
            String reservation = record.get("reservation");
            String hide = record.get("hide");
            ItemDto i = new ItemDto();
            i.setId(id);
            i.setTitle(title);
            i.setPrice(price);
            i.setDescription(description);
            i.setInstagramLikeUrl(instagramLikeUrl);
            i.setReservation(reservation);
            i.setHide(hide);
            if (ids.contains(id)) {
                list.add(i);
            }
        }

        return list;
    }
}
