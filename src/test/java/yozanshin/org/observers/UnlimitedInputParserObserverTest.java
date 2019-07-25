package yozanshin.org.observers;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import yozanshin.org.domain.ClarityRowInfo;
import yozanshin.org.observables.LogFileObservableFactory;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class UnlimitedInputParserObserverTest {

    private final String filePath;
    private UnlimitedInputParserObserver observer;
    private Observable<ClarityRowInfo> observable;

    public UnlimitedInputParserObserverTest() throws Exception {
        filePath = Paths.get(ClassLoader.getSystemResource("test-log-file.txt").toURI()).toAbsolutePath().toString();
    }

    @Test
    public void testParseFilesFromFile() {

        observer = new UnlimitedInputParserObserver("Host1");
        observable = LogFileObservableFactory.buildParseDataObservable(filePath, 1, 10);
        Disposable disposable = observable.subscribe(observer::onNext, observer::onError, observer::onComplete);
        while (!disposable.isDisposed()) {}

        assertThat(observer.getHostsConnectedTo()).containsExactlyInAnyOrder(
                "Host1", "Host2", "Host3", "Host4",
                "Host5", "Host6", "Host7", "Host8", "Host9", "Host10"
        );
        assertThat(observer.getHostsConnectedFrom()).isEmpty();
        assertThat(observer.getMostConnectedHost()).isEqualTo("Host2");
    }
}