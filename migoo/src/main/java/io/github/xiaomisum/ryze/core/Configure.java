package io.github.xiaomisum.ryze.core;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Objects;

public class Configure {

    private String version;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public static class Report {

        private boolean enable;

        private String theme;

        private Timeline timeline;

        private boolean offline;

        private String output;

        private String reporter;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getTheme() {
            return StringUtils.isBlank(theme) ? "DARK" : theme.toUpperCase(Locale.ROOT);
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public Timeline getTimeline() {
            return Objects.isNull(timeline) ? new Timeline() : timeline;
        }

        public void setTimeline(Timeline timeline) {
            this.timeline = timeline;
        }

        public boolean isOffline() {
            return offline;
        }

        public void setOffline(boolean offline) {
            this.offline = offline;
        }

        public String getOutput() {
            return StringUtils.isBlank(output) ? "./out-put" : output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        public String getReporter() {
            return StringUtils.isBlank(reporter) ? "xyz.migoo.report.StandardReport" : reporter;
        }

        public void setReporter(String reporter) {
            this.reporter = reporter;
        }

    }

    public static class Timeline {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
