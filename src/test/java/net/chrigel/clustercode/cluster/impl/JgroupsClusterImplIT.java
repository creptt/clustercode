package net.chrigel.clustercode.cluster.impl;

import net.chrigel.clustercode.task.MediaCandidate;
import net.chrigel.clustercode.test.MockedFileBasedUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class JgroupsClusterImplIT implements MockedFileBasedUnitTest {

    private JgroupsClusterImpl subject;

    @Mock
    private JgroupsClusterSettings settings;
    @Mock
    private MediaCandidate candidate;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new JgroupsClusterImpl(settings, Clock.systemDefaultZone());
        when(settings.getJgroupsConfigFile()).thenReturn("udp.xml");
        when(settings.getClusterName()).thenReturn("clustercode");
        when(settings.getBindingPort()).thenReturn(5000);
        when(settings.isIPv4Preferred()).thenReturn(true);
        when(settings.getBindingAddress()).thenReturn("");
        when(settings.getHostname()).thenReturn("");
    }

    @Test
    public void setTask_ShouldAcceptTask() throws Exception {
        Path source = createPath("0", "movie.mp4");
        when(candidate.getSourcePath()).thenReturn(source);

        subject.joinCluster();
        subject.setTask(candidate);
        Thread.sleep(100);
        assertThat(subject.isQueuedInCluster(candidate)).isTrue();
    }


}
