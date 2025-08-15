package org.example.lc.beisen.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.example.lc.beisen.BeisenException;
import org.example.lc.beisen.assessment.Assessment;
import org.example.lc.beisen.assessment.model.AccountToken;
import org.example.lc.beisen.assessment.model.ActivityInfo;
import org.example.lc.beisen.assessment.model.ActivityInfoList;
import org.example.lc.beisen.assessment.model.ActivityTesteeResult;
import org.example.lc.beisen.assessment.model.ActivityTesteeResultList;
import org.example.lc.beisen.assessment.model.ActivityTesteeState;
import org.example.lc.beisen.assessment.model.ActivityTesteeStateList;
import org.example.lc.beisen.assessment.model.BeisenSymbol;
import org.example.lc.beisen.assessment.model.EmailSNParameter;
import org.example.lc.beisen.assessment.model.OpenAPIRemindResult;
import org.example.lc.beisen.assessment.model.SerialNumber;
import org.example.lc.beisen.assessment.model.TemplateList;
import org.example.lc.beisen.assessment.model.TestCompletedMessage;
import org.example.lc.beisen.assessment.model.TestCompletedMessageList;
import org.example.lc.beisen.assessment.model.TesteeInfoParameter;
import org.example.lc.beisen.assessment.model.TesteeInfoParameterList;
import org.example.lc.beisen.assessment.model.TesteeResult;
import org.example.lc.beisen.assessment.model.TesteeSN;
import org.example.lc.beisen.assessment.model.TesteeInfo;

import java.util.Map;

public class assessTest {


	public static void main(String[] args) {
		
        Assessment assess = new Assessment();
		AccountToken  token = new AccountToken("614849","15b26885542e463c9a686f1ecdea4f72",
				"d30e216edcda41348b7756b68aa5c263","jiekou@jingbeifang.com");
		try {
			//第一步.获取租户下所有测评活动列表，返回结果按照创建时间倒序排列，page_size最大100
//			ActivityInfoList activities = assess.getActivityList(token, 10, 1);
//			System.out.println(activities.activity_list.size());
//            List<EmailSNParameter> emailSnList = new ArrayList<EmailSNParameter>();
//            EmailSNParameter emailSNParameter = new EmailSNParameter();
//            emailSNParameter.email = "chen.li009@northking.net";
//            emailSNParameter.sn = "6301493256845";
//            emailSnList.add(emailSNParameter);
//            ActivityTesteeResultList result = assess.getTesteeResultByEmailAndSN(token,1104675 , emailSnList);
            // String emailss = "caizp@qq.com,276500647@qq.com,529272089@qq.com";
            // ActivityTesteeResultList result22 = assess.getActivityTesteeResultList(token, emailss);


//
//            //TODO 根据活动名称选择 需要发送邀请的测评活动（和客户沟通好 活动名称规则）
//            ActivityInfo activityDetail = assess.getActivityDetail(token, activities.activity_list.get(0).id);
//            System.out.println(activityDetail.id);
//
            //第二步.邀请受测者(不发送邮件和短信),一次最多邀请1000人
            TesteeInfoParameterList userList = new TesteeInfoParameterList();
            TesteeInfoParameter p = new TesteeInfoParameter();
            SimpleDateFormat f =new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
            // Date birthday=new Date();
            // SimpleDateFormat birthdayFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            p = new TesteeInfoParameter();
            p.name = "chen.li009";
            p.email = "chen.li009@northking.net";
            p.mobile = "13436800328";
            p.department = "";
            userList.add(p);
            Date d = new Date();
            Date d1 = new Date(d.getYear(),d.getMonth(),d.getDay()+30,d.getHours(),d.getMinutes(),d.getSeconds());
            TesteeResult inviteTesteeResult = assess.sendEmailInviteTestee(token, 1105094, f.format(d), f.format(d1), "Assess.Invitation", userList);
            System.out.println(inviteTesteeResult.testee_invite_result_list.size());
            //TODO 根据返回的sn 和 测评地址 ISV发送 短信 或邮件 邀请
			
		} catch (BeisenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 获取作答结果
//		try
//		{
//			//***************************第一种方式**************************************************
//            //*通过作答完成消息获取作答结果，每次轮询最多返回2W条，可满足大部分ISV需求。特殊客户    *
//            //*可多次调取。                                                                         *
//            //*优势:作答结果变动也可以发消息，ISV更新旧数据。                                       *
//            //*略施：消息只能被获取一次。如果获取结果失败，或者是保存数据出现异常，无法重新获取消息 *
//            //*注意：1.完成消息为所有客户的完成消息。2.获取一个小时以前的作答结果                   *
//            //***************************************************************************************
//
//			ActivityTesteeResultList allResult = assess.getTesteeResultByCompletedMessageEx(token, 1000);
//            //TODO 保存作答结果，更新作答结果
//
//
//            //***************************第二种方式**************************************************
//            //*通过邮箱取作答状态，再根据状态获取作答结果，每次最多50个人的结果                     *
//            //*优势: 获取结果失败或保存数据失败，可根据作答完成状态再次获取。                       *
//            //*略施：需要ISV预先知道做测评人的邮箱                                                  *
//            //*注意：1.接口为get方式请求，有长度有限制，邮箱50以内。                                *
//            //***************************************************************************************
//
//            String emails = "caizp@qq.com,276500647@qq.com,529272089@qq.com";
//            ActivityTesteeStateList resultState = assess.getActivityTesteeStateList(token, emails);
//            for (ActivityTesteeState item : resultState)
//            {
//                List<EmailSNParameter> emailSnList = new ArrayList<EmailSNParameter>();
//                for (TesteeSN emailInfos : item.testee_list)
//                {
//                    for (SerialNumber sn : emailInfos.sn_list)
//                    {
//                        if (sn.state == "3")//3:已完成
//                        {
//                        	EmailSNParameter emailSNParameter = new EmailSNParameter();
//                        	emailSNParameter.email = emailInfos.email;
//                        	emailSNParameter.sn = sn.sn;
//                            emailSnList.add(emailSNParameter);
//                        }
//                    }
//                }
//
//                ActivityTesteeResultList result = assess.getTesteeResultByEmailAndSN(token, item.activity_id, emailSnList);
//                //TODO 保存作答结果，更新作答结果
//            }
//
//
//            //***************************第三种方式**************************************************
//            //*通过邮箱直接取作答结果，每次最多50个人的结果,可以作为第一种方式的补救方案            *
//            //*优势: 调用简单，如果作答完成则返回结果否则不返回该人结果                             *
//            //*略施：1.需要预先知道测评人的邮箱。2.需要多次调取。3.接口性能较低                     *
//            //*注意：1.接口为get方式请求，有长度有限制，邮箱50以内。                                *
//            //***************************************************************************************
//
//            ActivityTesteeResultList result2 = assess.getActivityTesteeResultList(token, emails);
//            //TODO 保存作答结果，更新作答结果
//
//		}catch(BeisenException e){
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
