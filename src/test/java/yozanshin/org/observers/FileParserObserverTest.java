package yozanshin.org.observers;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import yozanshin.org.domain.ClarityRowInfo;
import yozanshin.org.observables.LogFileObservableFactory;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParserObserverTest {

    private final String filePath;
    private FileParserObserver observer;
    private Observable<ClarityRowInfo> observable;

    public FileParserObserverTest() throws Exception {
        filePath = Paths.get(ClassLoader.getSystemResource("test-log-file.txt").toURI()).toAbsolutePath().toString();
    }

    @Test
    public void testListConnectedHosts() {

        observable = LogFileObservableFactory.buildParseDataObservable(filePath, 1, 5);
        observer = new FileParserObserver("Host1");
        Disposable disposable = observable.subscribe(observer::onNext, observer::onError, observer::onComplete);
        while (!disposable.isDisposed()) {}
        assertThat(observer.getHostsConnected()).contains("Host1", "Host2", "Host3", "Host4", "Host5");

        observable = LogFileObservableFactory.buildParseDataObservable(filePath, 195, 205);
        observer = new FileParserObserver("Host2");
        disposable = observable.subscribe(observer::onNext, observer::onError, observer::onComplete);
        while (!disposable.isDisposed()) {}
        assertThat(observer.getHostsConnected()).contains("Host95", "Host96", "Host97", "Host98", "Host99", "Host100");

        observable = LogFileObservableFactory.buildParseDataObservable(filePath, 195, 205);
        observer = new FileParserObserver("Host3");
        disposable = observable.subscribe(observer::onNext, observer::onError, observer::onComplete);
        while (!disposable.isDisposed()) {}
        assertThat(observer.getHostsConnected()).contains("Host1", "Host2", "Host3", "Host4", "Host5");
    }

    @Test
    public void testListConnectedHostsNoHostsInRange() {

        observable = LogFileObservableFactory.buildParseDataObservable(filePath, 195, 205);
        observer = new FileParserObserver("Host1");
        Disposable disposable = observable.subscribe(observer::onNext, observer::onError, observer::onComplete);
        while (!disposable.isDisposed()) {}
        assertThat(observer.getHostsConnected()).isEmpty();
    }
}