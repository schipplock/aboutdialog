/*
 * Copyright 2022 Andreas Schipplock
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.schipplock.gui.swing.dialogs;

import de.schipplock.gui.swing.ahreflabel.AhrefLabel;
import de.schipplock.gui.swing.svgicon.SvgIconManager;
import de.schipplock.gui.swing.svgicon.SvgIcons;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static java.lang.String.format;

public class AboutDialog extends JDialog {

    private String customSvgIconPath;

    private final JPanel textPanel;

    private final Set<JLabel> labels = new LinkedHashSet<>();

    private final JButton closeButton = new JButton(localize("aboutdialog.closebutton.title"));

    public AboutDialog(JFrame owner, boolean modal) {
        super(owner, "", modal);

        var layoutConstraints = System.getenv("MIGLAYOUT_CONSTRAINTS");

        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setIconImages(SvgIconManager.getBuiltinWindowIconImages(SvgIcons.SVGICON_INFO_SQUARE_FILL, "#17181c"));

        closeButton.addActionListener(e -> dispose());

        var copyButton = new JButton(localize("aboutdialog.copybutton.title"));

        copyButton.addActionListener(e -> {
            var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            var builder = new StringBuilder();
            labels.forEach(label -> builder.append(format("%s%n%n", label.getText())));
            var content = new StringSelection(builder.toString());
            clipboard.setContents(content, content);
        });

        var mainPanel = new JPanel(new MigLayout(layoutConstraints));

        var itemPanel = new JPanel(new MigLayout(layoutConstraints));

        var iconPanel = new JPanel(new MigLayout(layoutConstraints)) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (customSvgIconPath == null) {
                    g.drawImage(SvgIconManager.getBuiltinIcon(SvgIcons.SVGICON_INFO, new Dimension(getWidth(), getWidth()), "#17181c").getImage(), 0, 0, this);
                } else {
                    g.drawImage(SvgIconManager.getIcon(customSvgIconPath, new Dimension(getWidth(), getWidth())).getImage(), 0, 0, this);
                }
            }
        };

        textPanel = new JPanel(new MigLayout(layoutConstraints));

        itemPanel.add(iconPanel, "pushx, growx, pushy, growy");
        itemPanel.add(textPanel, "w 70%, pushy, growy");

        var actionPanel = new JPanel(new MigLayout(layoutConstraints));
        actionPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        actionPanel.add(copyButton, "right, pushx");
        actionPanel.add(closeButton, "right");

        var itemScrollPane = new JScrollPane(itemPanel);
        itemScrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        mainPanel.add(itemScrollPane, "span, pushx, growx, pushy, growy");
        mainPanel.add(actionPanel, "pushx, growx");

        getRootPane().setDefaultButton(closeButton);
        getContentPane().add(mainPanel);
    }

    public AboutDialog(JFrame owner) {
        this(owner, false);
    }

    public AboutDialog() {
        this(null);
    }

    public AboutDialog size(Dimension size) {
        setPreferredSize(size);
        setMinimumSize(size);
        return this;
    }

    public AboutDialog center() {
        GraphicsDevice screen = MouseInfo.getPointerInfo().getDevice();
        Rectangle r = screen.getDefaultConfiguration().getBounds();
        int x = (r.width - this.getWidth()) / 2 + r.x;
        int y = (r.height - this.getHeight()) / 2 + r.y;
        setLocation(x, y);
        return this;
    }

    public AboutDialog icon(String iconPath) {
        customSvgIconPath = iconPath;
        setIconImages(SvgIconManager.getWindowIconImages(iconPath));
        return this;
    }

    public AboutDialog title(String title, String htmlColor) {
        setTitle(title);
        var label = new JLabel(format("<html><font size=\"4\"><b color=\"%s\">%s</b></font></html>", htmlColor, title));
        labels.add(label);
        textPanel.add(label, "pushx, growx, wrap");
        return this;
    }

    public AboutDialog text(String text, String htmlColor) {
        var label = new JLabel(format("<html><font size=\"3\" color=\"%s\">%s</font></html>", htmlColor, text));
        labels.add(label);
        textPanel.add(label, "aligny top, pushx, growx, pushy, wrap");
        return this;
    }

    public AboutDialog copyright(String copyrightText, String htmlColor) {
        var label = new JLabel(format("<html><font size=\"2\" color=\"%s\">Copyright © %s</font></html>", htmlColor, copyrightText));
        labels.add(label);
        textPanel.add(label, "pushx, growx, wrap");
        return this;
    }

    public AboutDialog website(String url, String urlText, String htmlColor) {
        var label = new AhrefLabel(url, urlText, null, htmlColor, 2);
        labels.add(label);
        textPanel.add(label, "pushx, growx");
        return this;
    }

    @Override
    public void setVisible(boolean b) {
        super.pack();
        closeButton.requestFocus();
        super.setVisible(b);
    }

    private String localize(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/MessagesBundle");
        return bundle.getString(key);
    }
}
