public class UserAgent {
    private final String osType;
    private final String browser;

    public UserAgent(String userAgentString) {
        this.osType = parseOSType(userAgentString);
        this.browser = parseBrowser(userAgentString);
    }

    public String getOsType() { return osType; }
    public String getBrowser() { return browser; }

    private String parseOSType(String userAgent) {
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Macintosh") || userAgent.contains("Mac OS")) return "macOS";
        if (userAgent.contains("Linux") || userAgent.contains("X11")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iPhone") || userAgent.contains("iPad")) return "iOS";
        return "Other";
    }

    private String parseBrowser(String userAgent) {
        if (userAgent.contains("Edg/") || userAgent.contains("EdgA/")) return "Edge";
        if (userAgent.contains("Firefox/")) return "Firefox";
        if (userAgent.contains("Chrome/") && !userAgent.contains("Edg/")) return "Chrome";
        if (userAgent.contains("OPR/") || userAgent.contains("Opera/")) return "Opera";
        if (userAgent.contains("Safari/") && !userAgent.contains("Chrome/")) return "Safari";
        return "Other";
    }
}
