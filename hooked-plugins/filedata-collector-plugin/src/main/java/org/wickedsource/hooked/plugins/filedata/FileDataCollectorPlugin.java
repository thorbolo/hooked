package org.wickedsource.hooked.plugins.filedata;

import org.wickedsource.hooked.plugins.filedata.collector.CollectorPlugin;
import org.wickedsource.hooked.plugins.filedata.collector.CommittedFile;
import org.wickedsource.hooked.plugins.filedata.notifier.FileMetrics;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Tom Hombergs <tom.hombergs@gmail.com>
 */
public class FileDataCollectorPlugin implements CollectorPlugin {

    @Override
    public FileMetrics analyzeCommittedFiles(List<CommittedFile> committedFiles) {
        FileMetrics resultList = new FileMetrics();
        for (CommittedFile file : committedFiles) {
            try {
                FileMetrics metrics = analyzeFile(file);
                resultList.join(metrics);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }

    private FileMetrics analyzeFile(CommittedFile file) throws IOException {
        String filename = file.getMetaData().getPath();
        FileDataMetrics metrics = new FileDataMetrics();
        BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(file.getContent())));
        String line;
        while ((line = in.readLine()) != null) {
            metrics.addBytes(filename, (long) (line.length() + 1)); // +1 for newline
            metrics.addLines(filename, 1l);
            if ("".equals(line.trim())) {
                metrics.addEmptyLines(filename, 1l);
            }
        }
        // correction since the last line does not contain a newline character
        metrics.addBytes(filename, (long) -1);
        return metrics;
    }

}