package org.smartparam.coherence.jdbc.repository;

import org.smartparam.coherence.jdbc.DatabaseTest;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class JdbcParamVersionRepositoryTest extends DatabaseTest {

    @Test
    public void shouldInitializeNewParamWithVersionEqualTo1() {
        // given
        JdbcParamVersionRepository versionRepository = get(JdbcParamVersionRepository.class);

        // when
        Long versionInitialized = versionRepository.incrementVersion("newParam");

        // then
        assertThat(versionInitialized).isEqualTo(1);
        assertThat(versionRepository.versionOfParam("newParam")).isEqualTo(1);
    }

    @Test
    public void shouldIncrementParamVersion() {
        // given
        JdbcParamVersionRepository versionRepository = get(JdbcParamVersionRepository.class);
        versionRepository.incrementVersion("someParam");

        // when
        Long versionInitialized = versionRepository.incrementVersion("someParam");

        // then
        assertThat(versionInitialized).isEqualTo(2);
        assertThat(versionRepository.versionOfParam("someParam")).isEqualTo(2);
    }

    @Test
    public void shouldReturnNullAsVersionOfNonexistentParam() {
        // given
        JdbcParamVersionRepository versionRepository = get(JdbcParamVersionRepository.class);

        // when
        Long nonexistentParam = versionRepository.versionOfParam("someParam");

        // then
        assertThat(nonexistentParam).isNull();
    }

    @Test
    public void shouldReturnVersionsOfAllParams() {
        // given
        JdbcParamVersionRepository versionRepository = get(JdbcParamVersionRepository.class);
        versionRepository.incrementVersion("first");
        versionRepository.incrementVersion("second");
        versionRepository.incrementVersion("second");

        // when
        Map<String, Long> allVersions = versionRepository.versionOfAllParams();

        // then
        assertThat(allVersions).containsOnly(entry("first", 1L), entry("second", 2L));
    }

}
