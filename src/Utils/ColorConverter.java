package Utils;

public class ColorConverter {
    public static int rgbToHex(int r, int g, int b) {
        return argbToHex(255, r, g, b);
    }

    public static int argbToHex(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int[] hexToRgb(int hex) {
        int[] rgb = new int[3];
        rgb[0] = (hex >> 16) & 0xff;
        rgb[1] = (hex >> 8) & 0xff;
        rgb[2] = hex & 0xff;
        return rgb;
    }

    public static int[] hexToArgb(int hex) {
        int[] argb = new int[4];
        argb[0] = (hex >> 24) & 0xff;
        argb[1] = (hex >> 16) & 0xff;
        argb[2] = (hex >> 8) & 0xff;
        argb[3] = hex & 0xff;
        return argb;
    }

    public static int getAlpha(int hex) {
        return (hex >> 24) & 0xff;
    }

    public static int getRed(int hex) {
        return (hex >> 16) & 0xff;
    }

    public static int getGreen(int hex) {
        return (hex >> 8) & 0xff;
    }

    public static int getBlue(int hex) {
        return hex & 0xff;
    }
}
