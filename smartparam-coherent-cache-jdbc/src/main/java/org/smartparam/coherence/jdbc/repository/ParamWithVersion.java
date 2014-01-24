package org.smartparam.coherence.jdbc.repository;

public class ParamWithVersion {

    private String name;

    private Long version;

    public ParamWithVersion(String name, Long version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public Long getVersion() {
        return version;
    }
}
