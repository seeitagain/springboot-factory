package org.example.lc.beisen.assessment.model;

public class BeisenSymbol {
	private static String[] BS_Sign = { "+", " ", "/", "?", "%", "#", "&", "=" };
    private static String[] BS_Sign_Alias = { "BS_Plus", "BS_Blank", "BS_Slash", "BS_Question", "BS_Percent", "BS_Sharp", "BS_And", "BS_Equal" };
    public static String urlEnCode(String url)
    {
        for (int i = 0; i < BS_Sign.length; i++)
        {
            url=url.replace(BS_Sign[i], BS_Sign_Alias[i]);
        }
        return url;
    }

    public static String urlDeCode(String url)
    {
        for (int i = 0; i < BS_Sign.length; i++)
        {
            url = url.replace(BS_Sign_Alias[i], BS_Sign[i]);
        }
        return url;
    }
}
