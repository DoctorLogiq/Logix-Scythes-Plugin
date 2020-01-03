package com.plugins.drlogiq.scythes;

@SuppressWarnings({"SwitchStatementWithTooFewBranches", "DuplicateBranchesInSwitch"})
public enum Versions
{
    UNKNOWN("Unknown"),
    V0_1("0.1");

    private String versionString;

    Versions(String versionString)
    {
        this.versionString = versionString;
    }

    public static Versions fromString(String string)
    {
        switch (string)
        {
            default:
            {
                LogixScythes.logError("Unknown version string \"" + string + "\"");
                return Versions.UNKNOWN;
            }
            case "0.1": return Versions.V0_1;
        }
    }

    public String getVersionString()
    {
        return versionString;
    }

    public static boolean isLowerThan(Versions current, Versions compare)
    {
        switch (current)
        {
            default: return false;
            case V0_1:
            {
                switch (compare)
                {
                    default: return false;
                    case V0_1: return false;
                }
            }
        }
    }
}