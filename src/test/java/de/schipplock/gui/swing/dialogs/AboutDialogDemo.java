package de.schipplock.gui.swing.dialogs;

import de.schipplock.gui.swing.lafmanager.LAFManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AboutDialogDemo {
    public static void main(String[] args) {
        LAFManager.create().setLookAndFeelByName("FlatLaf IntelliJ");
        SwingUtilities.invokeLater(() -> {
            new AboutDialog()
                    .size(new Dimension(450, 425))
                    .icon("logo.svg")
                    .title("StromZettel 0.0.1", "#18262e")
                    .text(getAboutTxt(), "#18262e")
                    .copyright("2020-2022 SomeCompany", "#18262e")
                    .website("https://schipplock.de", "https://schipplock.de", "#18262e")
                    .center()
                    .setVisible(true);
        });
    }

    public static String getAboutTxt() {
        var aboutTxt = "";
        try {
            aboutTxt = Files.readString(Path.of(AboutDialogDemo.class.getClassLoader().getResource("about.html").toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return aboutTxt;
    }
}
