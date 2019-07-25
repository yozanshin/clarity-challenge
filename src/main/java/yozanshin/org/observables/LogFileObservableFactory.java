package yozanshin.org.observables;


import io.reactivex.Observable;
import lombok.extern.java.Log;
import yozanshin.org.config.ApplicationConstants;
import yozanshin.org.domain.ClarityRowInfo;

import java.io.BufferedReader;
import java.io.FileReader;

@Log
public class LogFileObservableFactory {

    public static Observable<ClarityRowInfo> buildParseDataObservable(final String filePath,
                                                                      final long initDatetime,
                                                                      final long endDatetime) {

        return Observable.<String>create(eventConsumer -> {

                log.info("Reading file " +filePath +" ...");

                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

                    String fileRow;
                    while ((fileRow = reader.readLine()) != null) {
                        eventConsumer.onNext(fileRow);
                    }
                    eventConsumer.onComplete();

                } catch (Exception ex) {

                    log.severe("Unable to parse provided file");
                    ex.printStackTrace();
                    eventConsumer.onError(ex);
                }
        })
                .filter(row -> !"".equals(row))
                .map(LogFileObservableFactory::mapRowToClarityLogRow)
                .filter(fileRow -> LogFileObservableFactory.filterByTimestamp(fileRow, initDatetime, endDatetime));
    }

    public static Observable<ClarityRowInfo> buildUnlimitedInputObservable(final String filePath) {

        return Observable.<String>create(eventConsumer -> {

            log.info("Reading file " +filePath +" indefinitely ...");

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

                String fileRow;
                while (true) {

                    if ((fileRow = reader.readLine()) != null) {
                        eventConsumer.onNext(fileRow);
                    } else {
                        Thread.sleep(1000L); //To avoid unnecessary execution time
                    }
                }

            } catch (Exception ex) {

                log.severe("Unable to parse provided file");
                ex.printStackTrace();
                eventConsumer.onError(ex);
            }

        })
                .filter(row -> !"".equals(row))
                .map(LogFileObservableFactory::mapRowToClarityLogRow);
    }

    private static ClarityRowInfo mapRowToClarityLogRow(final String row) {

        String[] items = row.split(ApplicationConstants.ROW_ITEMS_SEPARATOR);
        return ClarityRowInfo.builder()
                .unixTimestamp(Long.valueOf(items[0]))
                .hostFrom(items[1])
                .hostTo(items[2])
                .build();
    }

    private static boolean filterByTimestamp(final ClarityRowInfo fileRow,
                                             final long initFileStamp,
                                             final long endDatetime) {

        return fileRow.getUnixTimestamp() >= initFileStamp && fileRow.getUnixTimestamp() <= endDatetime;
    }
}
