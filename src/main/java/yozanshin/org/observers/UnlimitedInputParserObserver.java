package yozanshin.org.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.extern.java.Log;
import yozanshin.org.domain.ClarityRowInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log
public class UnlimitedInputParserObserver implements Observer<ClarityRowInfo> {

    private String host;
    @Getter
    private Set<String> hostsConnectedTo;
    @Getter
    private Set<String> hostsConnectedFrom;
    private ConcurrentHashMap<String, Integer> connectionsPerHost;
    @Getter
    private String mostConnectedHost;

    public UnlimitedInputParserObserver(final String host) {
        this.host = host;
        hostsConnectedTo = new HashSet<>();
        hostsConnectedFrom = new HashSet<>();
        connectionsPerHost = new ConcurrentHashMap<>();
    }

    @Override
    public void onSubscribe(final Disposable disposable) {
        log.info("Observer subscribed");
    }

    @Override
    public void onNext(final ClarityRowInfo fileRow) {
        addToCollectionsIfConnected(fileRow);
        addToConnectionsPerHost(fileRow);
    }

    @Override
    public void onError(final Throwable throwable) {
        log.severe("Unable to parse file");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        mostConnectedHost = calculateMostConnectedHost();
        log.info("<<<Logs report>>>");
        log.info("Hosts connected to " +host +":");
        log.info(hostsConnectedTo.toString());
        log.info("Hosts connected from " +host +":");
        log.info(hostsConnectedFrom.toString());
        log.info("Most connected host: " +mostConnectedHost);
        log.info("<<<------>>>");
    }

    private void addToCollectionsIfConnected(final ClarityRowInfo fileRow) {
        addToConnectedToIfConnected(fileRow);
        addToConnectedFromIfConnected(fileRow);
    }

    private void addToConnectionsPerHost(final ClarityRowInfo fileRow) {
        connectionsPerHost.put(
                fileRow.getHostFrom(),
                connectionsPerHost.getOrDefault(fileRow.getHostFrom(), 0) + 1
        );
    }

    private void addToConnectedToIfConnected(final ClarityRowInfo fileRow) {
        if (host.equals(fileRow.getHostTo())) {
            hostsConnectedTo.add(fileRow.getHostFrom());
        }
    }

    private void addToConnectedFromIfConnected(final ClarityRowInfo fileRow) {
        if (host.equals(fileRow.getHostFrom())) {
            hostsConnectedTo.add(fileRow.getHostTo());
        }
    }

    private String calculateMostConnectedHost() {

        return connectionsPerHost.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("<No matching host>");
    }
}
