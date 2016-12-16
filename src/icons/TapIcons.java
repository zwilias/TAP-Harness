package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class TapIcons {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, TapIcons.class);
    }

    public static final Icon Tap = load("/xyz/zwilias/idea/tap/icons/tap.png");
}
