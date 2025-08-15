package org.example.lc.beisen.assessment.model;

import java.util.Date;

public class TesteeInfo
{
    //性别
    private static String[] SexS = { "", "女", "男" };
    private static String[] SexV = { "", "0", "1" };
    // 学历
    private static String[] EducationS = { "", "本科", "硕士研究生", "高中及以下", "中技（中专/技校/职高）", "大专", "MBA", "博士研究生","初中","小学" };
    private static String[] EducationV = { "", "1", "2", "3", "4", "5", "6", "7","8","9" };
    //政治面貌
    private static String[] PolicyS = { "", "中共党员(含预备党员)", "团员", "民主党派", "无党派人士(群众)", "其他" };
    private static String[] PolicyV = { "", "1", "2", "3", "4", "5" };
    // 工作经验
    private static String[] WorkYearS = { "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "应届毕业生", "在读学生" };
    private static String[] WorkYearV = { "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "98", "99" };

    /// <summary>
    /// 校正属性信息
    /// </summary>
    /// <returns></returns>
    public void Correction()
    {
        this.sex = Conver(this.sex, SexS, SexV);
        this.education = Conver(this.education, EducationS, EducationV);
        this.policy = Conver(this.policy, PolicyS, PolicyV);
        this.workyear = Conver(this.workyear, WorkYearS, WorkYearV);
        this.remark=remark==null?"":BeisenSymbol.urlEnCode(remark);
    }

    private String Conver(String value, String[] array, String[] array1)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (value == array[i])
                return array1[i];
        }

        return "";
    }

    public String sex ;
    /// <summary>
    /// 出生日期
    /// </summary>
    public Date birthday ;
    /// <summary>
    /// 学历
    /// </summary>
    public String education;
    /// <summary>
    ///政治面貌
    /// </summary>
    public String policy ;
    /// <summary>
    /// 省份城市
    /// </summary>
    public String area ;
    /// <summary>
    /// 行业
    /// </summary>
    public String industry ;
    /// <summary>
    /// 职位
    /// </summary>
    public String jobs ;
    /// <summary>
    /// 工作经验
    /// </summary>
    public String workyear ;
    /// <summary>
    /// 电话
    /// </summary>
    public String tel ;
    /// <summary>
    /// 毕业院校
    /// </summary>
    public String university ;
    /// <summary>
    /// 所属专业
    /// </summary>
    public String professional ;

    /// <summary>
    /// 备注
    /// </summary>
    public String remark ;
}