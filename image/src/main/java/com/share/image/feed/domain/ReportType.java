package com.share.image.feed.domain;

public enum ReportType {
    AD("광고"), PORN("음란물"), SPAM("도배"), INSULT("욕설/부적절한 언어"), VIOLENCE("폭력"), DEFAMATION("명예 훼손/저작권 침해"), ILLEGALITY("불법"), THREAT("비방/위협"), ETC("기타");

    private final String reason;

    ReportType(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}