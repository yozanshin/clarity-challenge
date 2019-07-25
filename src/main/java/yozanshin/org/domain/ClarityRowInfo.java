package yozanshin.org.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ClarityRowInfo {

    private long unixTimestamp;
    private String hostFrom;
    private String hostTo;
}
