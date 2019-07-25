package yozanshin.org.services;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import lombok.extern.java.Log;
import yozanshin.org.domain.ClarityRowInfo;
import yozanshin.org.observables.LogFileObservableFactory;
import yozanshin.org.observers.UnlimitedInputParserObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Log
public class UnlimitedInputParser {

    public void generateLogsReportFromPreviousLogFiles(final String logsFolder,
                                                       final String host) {

        long currentTimestamp = System.currentTimeMillis();
        long oneHourAgoTimestamp = currentTimestamp - (1000 * 60 * 60);

        File folder = new File(logsFolder);
        Observable<ClarityRowInfo> observable = Observable.empty();
        for (File file : folder.listFiles()) {
            observable = observable.mergeWith(
                    LogFileObservableFactory.buildParseDataObservable(
                            file.getAbsolutePath(), oneHourAgoTimestamp, currentTimestamp
                    )
            );
        }

        UnlimitedInputParserObserver observer = new UnlimitedInputParserObserver(host);
        Disposable disposable = observable.subscribe(observer::onNext, observer::onError, observer::onComplete);

        while (!disposable.isDisposed()) {
            try {
                Thread.sleep(2000L); //To avoid unnecessary execution time
            } catch (InterruptedException ex) {
                log.severe("Unexpected error parsing files in folder " + logsFolder);
                ex.printStackTrace();
            }
        }
    }

    public void generateLogReportEveryHourFromLogFile(final String filePath,
                                                      final String host) {

        Observable<Observable<ClarityRowInfo>> observable = LogFileObservableFactory
                .buildUnlimitedInputObservable(filePath)
                .window(1, TimeUnit.HOURS);

        Disposable disposable =
                observable.subscribe(windowedObservable ->
                        windowedObservable.subscribe(new UnlimitedInputParserObserver(host))
                );

        while (!disposable.isDisposed()) {
            try {
                Thread.sleep(2000L); //To avoid unnecessary execution time
            } catch (InterruptedException ex) {
                log.severe("Unexpected error parsing unlimited file " + filePath);
                ex.printStackTrace();
            }
        }
    }
}
