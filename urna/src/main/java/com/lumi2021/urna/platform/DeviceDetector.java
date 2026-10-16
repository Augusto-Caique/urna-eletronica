package com.lumi2021.urna.platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class DeviceDetector {
    private static final Path[] DEVICE_TREE_MODEL_PATHS = {
            Path.of("/proc/device-tree/model"),
            Path.of("/sys/firmware/devicetree/base/model")
    };

    public static DeviceType detect() {
        String os = System
                .getProperty("os.name", "")
                .toLowerCase(Locale.ROOT);

        if (os.contains("win")) return DeviceType.DESKTOP;
        if (os.contains("linux")) return detectLinuxDevice();

        return DeviceType.UNKNOWN;
    }

    private static DeviceType detectLinuxDevice() {
        for (Path path : DEVICE_TREE_MODEL_PATHS) {
            if (!Files.isReadable(path)) continue;

            try {
                String model = new String(Files.readAllBytes(path))
                        .replace("\0", "")
                        .trim()
                        .toLowerCase(Locale.ROOT);
                if (model.contains("raspberry pi")) return DeviceType.RASPBERRY_PI;

            } catch (IOException ignored) {
                // Ignored; tries next file
            }
        }

        return DeviceType.DESKTOP;
    }
}
