package clustercode.test.integration;

import clustercode.api.rest.v1.dto.Task;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TaskListIT {

    @Rule
    public ClustercodeContainer instance1 = new ClustercodeContainer(7700)
            .withOutputLogger(log);

    @BeforeClass
    public static void setup() throws IOException {
        var target = Paths.get("input", "0", "120s_blank.mp4");
        var doneFile = target.resolveSibling("120s_blank.mp4.done");
        if (Files.exists(doneFile)) Files.delete(doneFile);
    }

    @Test
    public void taskList_ShouldBeEmpty_WhenNoneFound() throws TimeoutException, InterruptedException {
        log.info("Waiting...");
        instance1.waitUntilLineStartsWith("Invoking: [/usr/bin/ffmpeg,");

        log.info("Assert");
        Thread.sleep(5000);
        var result = instance1.httpGet("/api/v1/tasks").readEntity(new GenericType<List<Task>>() {
        });
        Thread.sleep(5000);
        assertThat(result)
                .isNotEmpty();
    }

}
