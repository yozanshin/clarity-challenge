package yozanshin.org.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.extern.java.Log;
import yozanshin.org.domain.ClarityRowInfo;

import java.util.HashSet;
import java.util.Set;

@Log
public class FileParserObserver implements Observer<ClarityRowInfo> {

    @Getter
    private final Set<String> hostsConnected = new HashSet<>();
    private final String host;

    public FileParserObserver(final String host) {
        this.host = host;
    }

    @Override
    public void onSubscribe(final Disposable disposable) {
        log.info("Host-connected observer subscribed");
    }

    @Override
    public void onNext(final ClarityRowInfo fileRow) {

        addHostToCollectionIfConnected(fileRow);
    }

    @Override
    public void onError(final Throwable throwable) {
        log.severe("Unable to consume event");
    }

    @Override
    public void onComplete() {

        log.info("<<<Log file parsed>>>");
        log.info("Hosts connected to " +host +":");
        log.info(hostsConnected.toString());
        log.info("<<<------>>>");
    }

    private void addHostToCollectionIfConnected(final ClarityRowInfo fileRow) {

        if(this.host.equals(fileRow.getHostTo())) {
            hostsConnected.add(fileRow.getHostFrom());
        }
    }
}
