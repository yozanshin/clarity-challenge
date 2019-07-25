package yozanshin.org.services;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import lombok.extern.java.Log;
import yozanshin.org.domain.ClarityRowInfo;
import yozanshin.org.observables.LogFileObservableFactory;
import yozanshin.org.observers.FileParserObserver;

@Log
public class FileParser {

    public void getConnectedHostsInFileByHOstAndPeriod(final String filePath,
                                                       final String host,
                                                       final long initDatetime,
                                                       final long endDatetime) {

        Observable<ClarityRowInfo> observable = LogFileObservableFactory
                .buildParseDataObservable(filePath, initDatetime, endDatetime);

        FileParserObserver observer = new FileParserObserver(host);

        Disposable disposable = observable.subscribe(observer::onNext, observer::onError, observer::onComplete);

        while (!disposable.isDisposed()) {
            try {
                Thread.sleep(2000L); //To avoid unnecessary execution time
            } catch (InterruptedException ex) {
                log.severe("Unexpected error parsing file " + filePath);
                ex.printStackTrace();
            }
        }
    }
}
