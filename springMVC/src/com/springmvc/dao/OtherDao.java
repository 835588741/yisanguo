package com.springmvc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class OtherDao
{
	String[] strs2 = new String[] { "赌场护卫", "山野樵夫", "外乡游客", "赌坊老板" };
	String[] strdesc2 = new String[] { "一个赌场护卫正在巡视猎场，还一路哼着歌正巧立马路过你这边。", "一名山野樵夫莫名来到你面前，应该是正巧路过来砍柴的。",
			"一名外乡游客来到此地游览山色风光，还独立吟诗作对", "赌坊老板也正在狩猎之中，气着高头大马，拿着精良弓箭，哼唱着小二郎。" };
	String[] strs = new String[] { "松鼠", "兔子", "飞鹰", "野猪", "野狼", "梅花鹿", "大巨熊", "凶恶猛虎", "大型金钱豹", "墨玉麒麟" };
	String[] strdesc = new String[] { "一只可爱的小松鼠飞快地蹿到你面前的草地上，还没注意到你的利箭已经悄悄瞄准它了。", "一只白白的小兔子，虽然属于狩猎低等品，但也聊胜于无。",
			"你注意到一只飞鹰飞掠而来，暗自搭弓引箭。", "一只肥硕的野猪拖着笨重的身躯闯入你的视线内，你暗自一阵欢喜。", "一只饥饿的野狼拖着疲惫的身躯来到你的视线范围内，你连忙打起精神。",
			"一只色彩斑斓的梅花麋鹿一蹦一跳进入你的伏击圈内。", "一只独眼的大巨熊挥舞着比你大腿还粗的双臂（前肢）朝你奔来，你吓得一个哆嗦。",
			"一声虎啸，你吓得心头一惊，正想用手捂住双眼，差点手中弓箭脱手而落，你一头冷汗，原来是一只凶恶猛虎觅食而来。", "一只大型金钱豹飞奔过来，躺在此地休息。",
			"你一眨眼间发现居然有一只墨玉麒麟在此地降落，这可是预兆着祥瑞的神兽啊！" };
	Random random = new Random();
	@Autowired
	private JdbcTemplate jdbcTemplate;
	String[] joke = new String[] {
			"解府家丁正在炒菜，一阵阵香气扑鼻而来，这时解府副总管关切地问你‘在钓鱼台玩了这么久，饿了吧’，你心想是不是要请我吃饭了，于是不好意思地小声‘嗯’了一声，然后解府副总管满面笑容说‘饿了就赶紧回自己家吃饭吧’ (⊙﹏⊙)b",
			"无双妖道见你投缘，便对你说到‘你今天买不起新鲜水果，就攒钱过几天买，你图便宜买下的烂水果，吃起来时候根本就是负担和垃圾，绝不会为你带来任何愉悦，现在得不到就忍，就等，就去努力争取，人生短短几十年，爱和吃能别凑合就别凑合，你的心和胃都很小 ，别亏欠它们。’",
			"解府老家丁，笑着谈说起小时候的往事：小时候，我娘不止一次跟我说，亲嘴会怀孕，亲嘴会怀孕，结果我家猫跳起来抢肉吃的时候好死不死亲到了我的嘴，过了几个月它还生下了三只小猫„„出于责任感，有我一口肉吃，我就不会让那三只小猫吃素！",
			"解府副总管骄傲地大声对你们这边说到：小时候本总管就是极其聪明过人，有一次先生问：一人问：如果你不小心马上要掉进万丈悬崖底下，只让你说三个字，你会说什么？ 有人回答：救命阿救救我、草泥马、要死了。。。之类的。 这时本总管只默默回了一句：筋斗云。。。 ！",
			"古语有云：窗户纸别捅破，人与人间的信任，就像是纸片，一旦破损，就不会再回到原来的样子。",
			"无双妖道：那一年，秃驴和师太牵手了，只剩下老道我一人。",
			"一生只谈三次恋爱最好，一次懵懂，一次刻骨，一次一生。谈的太多会比较，无法确定；经历太多会麻木，不再相信爱情，行尸走肉，最后与不爱的人结婚，无法发自内心的爱对方，日常表现的应付，对方则抱怨你不够关心和不顾家，最后这失败的爱情，让你在遗憾和凑合中走完一生。",
			"“我爱你”的含义是：无论贫穷，富贵，生老，病死，天灾，人祸我都不离弃的爱你。当你说出这三个字的时候，你是否有足够的勇气和顽强的毅力去承受他的人生。爱，不要轻易说出口。",
			"一个小男孩拿着一张假钱走进了玩具店，准备买一架玩具飞机。 服务员阿姨说：“小朋友，你的钱不是真的。 ”小男孩反问道：“阿姨，难道你的飞机是真的？”",
			"据说失眠的人捞十分钟许愿瓶就能睡着了。",
			"你东摸摸西摸摸，结果啥都没捞着。",
			"你把手伸到许愿池里一通瞎摸，心里还想着万一能摸到个好宝贝就发家致富咯，摸了一会，突然，感觉手指头一紧，你疼地哎呀一声，原来是一只乌龟咬住了你的手指。",
			"你把手伸到许愿池里一通瞎摸，心里还想着万一能摸到个好宝贝就发家致富咯，摸了一会，突然摸到一个瓶子打开一看，里面一纸条写到：你敢叫我一声‘孙悟空’吗? 你一声冷哼道：这哪家熊孩子，叫就叫，孙悟空！孙悟空！孙悟空！这时你把纸条再往下看，只见下面还写着一句话，‘你孙爷爷在此!’",
			"你帅气地用网子一捞，提上来一看，啥也没有。" };

	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	public String wish(String content, String name, String uuid)
	{
		String wid = UUID.randomUUID().toString().replaceAll("-", "");
		try
		{
			jdbcTemplate.update("INSERT INTO wish(`wid`, `content`, `uuid`, `name`, `time_str`, `time`) VALUES ('"
					+ wid + "', '" + content + "', '" + uuid + "', '" + name + "', NOW(), UNIX_TIMESTAMP());");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "你以 [" + name + "] 的身份，成功投放了一个许愿瓶!");
			return jsonObject.toString();
		}
		catch (Exception e)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "投放许愿瓶失败!");
			return jsonObject.toString();
		}
	}

	public String getwish(String uuid)
	{
		try
		{
			Random random = new Random();
			int rand = random.nextInt(12);
			if (rand == 1 || rand == 2 || rand == 3)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", joke[random.nextInt(joke.length)]);
				return jsonObject.toString();
			}

			SqlRowSet rowSet = jdbcTemplate
					.queryForRowSet("select wid,content,name,time_str,uuid from wish where uuid!='" + uuid
							+ "' and time < UNIX_TIMESTAMP() - 180 ORDER BY rand() limit 1");
			if (rowSet.next())
			{
				String mes = rowSet.getString("content");
				String time_str = rowSet.getString("time_str");
				String name = rowSet.getString("name");
				String wid = rowSet.getString("wid");
				String uuidOfWish = rowSet.getString("uuid");
				// jdbcTemplate.update("delete from wish where wid = '"+wid+"'");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("uuid", uuidOfWish);
				jsonObject.put("name", name);
				jsonObject.put("wid", wid);
				jsonObject.put("message", "你把手伸入许愿池里，捞啊捞，捞啊捞，突然摸到一个瓶子,你捞上来打开瓶子里面有个纸条：<br/><br/><font color='red'>"
						+ mes + "</font><br/>" + time_str);
				return jsonObject.toString();
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "已经没有没有瓶子了");
			return jsonObject.toString();
		}
		catch (Exception e)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "获取失败，未知原因。");
			return jsonObject.toString();
		}
	}

	public String dowakuangorhuilu(String uuid, int type)
	{
		Random random = new Random();

		if (type == 1)
		{
			if (random.nextInt(8) == 1)
			{
				jdbcTemplate
						.update("update person set redvalue=redvalue-1 where redvalue >= 5 and uuid='" + uuid + "'");
				int red = jdbcTemplate.queryForInt("select redvalue from person where uuid='" + uuid + "'");
				if (red >= 5)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 201);
					jsonObject
							.put("message",
									"你累的气喘吁吁的，肠子都悔青了，只叹不该乱杀人，突然你手中的铁锹似乎砸到金属的声音，你俯下身子，用手扒开土层，喜出望外泪流满面，终于挖到一块铜矿了，立马恭恭敬敬上交给天牢，被减免了1点红名。");
					return jsonObject.toString();
				}
				else
				{
					jdbcTemplate.update("update person set areaid=1 where uuid='" + uuid + "'");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("message", "你终于赎清了自己的罪责，可以堂堂正正出狱了。");
					return jsonObject.toString();
				}
			}
			else
			{
				int r = random.nextInt(20);
				if (r == 15)
				{
					if (random.nextInt(2) == 1)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject
								.put("message",
										"呼哧呼哧~~~，你不断挥舞铁锹挖地，‘吭’一声，似乎铁锹跑碰到什么坚硬的金属发出声音，你赶紧扒开泥土一看究竟，结果发现不是铜矿，只是一块白银而已，你气的把银块丢弃到一旁。");
						return jsonObject.toString();
					}
					else
					{
						jdbcTemplate.update("update person set money=money+100 where uuid='" + uuid + "'");
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject
								.put("message",
										"呼哧呼哧~~~，你不断挥舞铁锹挖地，‘吭’一声，似乎铁锹跑碰到什么坚硬的金属发出声音，你赶紧扒开泥土一看究竟，结果发现不是铜矿，只是袋铜板而已，你气的丢弃到一旁,但片刻之后，你一拍脑门，才想起来，这玩意比铜矿更珍贵呀，于是赶紧趁着周围狱卒没注意，把这一百个铜板放进自己口袋里。");
						return jsonObject.toString();
					}
				}
				else if (r == 14)
				{
					if (random.nextInt(3) == 1)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 201);
						jsonObject
								.put("message",
										"你正辛苦挖着矿，但越往下挖越感觉泥土松了，正想着，结果一铁锹下去，你脚下的地面突然塌了，你掉下了地下一个洞，你心想这应该就是以前的囚犯试图越狱而挖的密道，连忙在密道往前爬，爬到尽头，发现尽头处好亮啊，你一声哈哈老子终于出狱了。好不容易爬到尽头，只见俩狱卒就在尽头的出口处等你，你顿时就吓尿了，一个狱卒朝你大喝了一声‘想越狱没门！再敢跑，打断你的腿!’");
						return jsonObject.toString();
					}
					else
					{
						jdbcTemplate.update("update person set areaid=12 where uuid='" + uuid + "'");
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", 200);
						jsonObject
								.put("message",
										"你正辛苦挖着矿，但越往下挖越感觉泥土松了，正想着，结果一铁锹下去，你脚下的地面突然塌了，你掉下了地下一个洞，你心想这应该就是以前的囚犯试图越狱而挖的密道，连忙在密道往前爬，爬到尽头，发现尽头处好亮啊，你一声哈哈老子终于出狱了.");
						return jsonObject.toString();
					}
				}
				else
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("message", "呼哧呼哧~~~，你挥舞着铁锹，不停歇的挖矿，只盼能早日出狱。");
					return jsonObject.toString();
				}
			}
		}
		else
		{
			long money = jdbcTemplate.queryForLong("select money from person where uuid='" + uuid + "'");
			if (money < 10000)
			{
				jdbcTemplate.update("update person set redvalue=redvalue+1 where uuid='" + uuid + "'");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message",
						"‘小子，好样的！还敢耍老子玩！你身上根本就没有十两银子！因为你戏弄了牢头，牢头举报你公然贿赂，你的罪行被加重了，红名+1’,铁栏杆，铁窗户，铁心儿，你悲伤过度，不禁唱起歌儿来。。。");
				return jsonObject.toString();
			}

			int rand = random.nextInt(3);
			if (rand == 1)
			{
				jdbcTemplate.update("update person set areaid=1 where uuid='" + uuid + "'");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject
						.put("message",
								"你偷偷贿赂了10两银子给牢头，牢头也心领神会，亲手给你一闷棍，看的你眼冒金星昏厥过去，随后，牢头把你判定为染病身亡，叫人给你抬出了监狱扔到乱葬岗了，半夜你醒过来拍拍身上的土，径自回家去客栈休息了。");
				return jsonObject.toString();
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject
						.put("message",
								"你嬉皮笑脸地凑近牢头，扫了一眼四周无人，以迅雷不及掩耳之势，抓住牢头的手，牢头心头一惊，看了你一样，伸手就要拔刀了，你连忙道‘别别别’，你手一松，手中握着的10两银子到了牢头手里，牢头面露微笑点点头，突来一拳打得你鼻歪眼斜，当场倒地，钱也没牢头没收了。");
				return jsonObject.toString();
			}
		}
	}

	public String sendSystemMessage(String receiver_uuid, String keyword)
	{
		try
		{
			if (keyword != null)
			{
				keyword = "有人回复了你的许愿瓶：" + keyword;
			}

			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 发系统消息提示
			String mid = UUID.randomUUID().toString().replaceAll("-", "");
			String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
					+ "values("
					+ "'"
					+ mid
					+ "','0000','"
					+ receiver_uuid
					+ "','"
					+ keyword
					+ "','"
					+ sdf.format(new Date(System.currentTimeMillis()))
					+ "','false','许愿瓶消息',"
					+ System.currentTimeMillis() + ",2)";
			jdbcTemplate.update(notiMassgae);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "回复成功!");
			return jsonObject.toString();
		}
		catch (Exception e)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未知原因，回复失败!");
			return jsonObject.toString();
		}
	}

	public String shelie(String uuid, String sign, int level, String type)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select sheliecount,name,money from person where uuid='" + uuid
				+ "'");
		if (rowSet.next())
		{
			int sheliecount = rowSet.getInt("sheliecount");
			if (sheliecount < 1)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("data", sheliecount);
				jsonObject.put("message", "赌坊老板: 你的体力已经不够了，还是先回去休息一下，明天再来吧。");
				return jsonObject.toString();
			}
			long money = rowSet.getLong("money");
			if (money < 2100)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("data", sheliecount);
				jsonObject.put("message", "赌坊老板：身上连二两银子的没有了，告诉这猎场是富贵人家玩的地儿，不是你这种穷人可以涉足的！赶紧离开这里！");
				return jsonObject.toString();
			}

			jdbcTemplate.update("update person set sheliecount=sheliecount-1 where uuid='" + uuid + "'");

			String name = "野兽";
			if ("1".equals(type))
			{
				name = strs[level];
			}
			else if ("2".equals(type))
			{
				name = strs2[level];
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 405);
				jsonObject.put("data", sheliecount);
				jsonObject.put("message", "你眼花缭乱的，加上手脚又不灵巧，一箭都射偏到九霄云外去了。");
				return jsonObject.toString();
			}

			if ("1".equals(type))
			{

				if (random.nextInt(2) == 0)
				{
					// 扣五丝文
					jdbcTemplate.update("update person set money=money - 20 where uuid='" + uuid + "'");

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 200);
					jsonObject.put("data", sheliecount);
					jsonObject.put("message", "你一只飞箭疾驰射出，正中目标【" + name + "】，但结果只是赌坊老板设置的假野兽");
					return jsonObject.toString();
				}

				jdbcTemplate.update("update person set money=money+" + (level * 50) + " where uuid='" + uuid + "'");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("data", sheliecount);
				jsonObject.put("message", "你一只飞箭疾驰射出，正中目标【" + name + "】。获得赌坊老板的彩金：[ " + ((level + 1) * 100) + " ] 个铜板");
				return jsonObject.toString();
			}
			else
			{
				if (random.nextInt(4) == 0)
				{
					jdbcTemplate.update("update person set money=money - 20 where uuid='" + uuid + "'");

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 201);
					jsonObject.put("data", sheliecount);
					jsonObject.put("message", "你眼一花，手一抖，手中箭镞已然飞射而出，你箭法太烂，再加上眼神不济，呵呵，射偏了可不止一点点，白白浪费了一支箭,而且不偏不倚却正中【"
							+ name + "】大腿，你心知闯祸了，一声哎呀‘我还是赶紧跑了吧’，脚下如抹油一般，一溜烟功夫跑出了几里地逃之夭夭了。");
					return jsonObject.toString();
				}

				jdbcTemplate.update("update person set money=money - 1020 where uuid='" + uuid + "'");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("data", sheliecount);
				jsonObject.put("message", "你眼一花，手一抖，手中箭镞已然飞射而出，你烂得无以复加的箭法，不偏不倚却正中【" + name
						+ "】的屁股， 你一声哎呀心知闯祸‘我还是赶紧跑了吧’，熟料顿感后脑被木棒重击，一时昏厥。醒来后你人家让你赔偿了 [ 一两银子 ] 的医药费。");
				return jsonObject.toString();
			}
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到用户信息。");
			return jsonObject.toString();
		}
	}

	public String pinBigOrSmall(String uuid, int money)
	{
		SqlRowSet rowSet = jdbcTemplate
				.queryForRowSet("select name,money,(select money from house where hid='0001') as zjmoney from person where uuid='"
						+ uuid + "'");
		if (rowSet.next())
		{
			long allmoney = rowSet.getLong("money");
			long zjallmoney = rowSet.getLong("zjmoney");

			if (allmoney < 20000)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "穷的就快剩裤子了还赌！身上那几两银子还是留着养家糊口吧！");
				return jsonObject.toString();
			}

			if (money * 1000 > allmoney)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "你身上的钱不够本次下注！赌场保镖瞅了你一眼，看样子是准备在你没钱时把你轰出去。");
				return jsonObject.toString();
			}
			if (zjallmoney < money * 1000)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 201);
				jsonObject.put("message", "赌场资金不足，没有足够资金支付闲家本局下注的赌资，本局作废。");
				return jsonObject.toString();
			}

			Random random = new Random();
			int zj = random.nextInt(6);
			String txtzj = (zj + 1) + " 点";

			int xj = random.nextInt(6);
			String txtxj = (xj + 1) + " 点";

			String result = "";
			int resultCode = 0;

			if (zj > xj)
			{
				resultCode = 1;
				result = "庄家:【" + txtzj + "】,闲家:【" + txtxj + "】,庄家大，本局庄家赢。";
			}
			else if (zj == xj)
			{
				if (zj == 1)
				{
					resultCode = 0;
					result = "庄家:【" + txtzj + "】,闲家:【" + txtxj + "】,同点数为22禁手，本局无输赢。";
				}
				else if (zj == 2)
				{
					resultCode = 0;
					result = "庄家:【" + txtzj + "】,闲家:【" + txtxj + "】,同点数为33禁手，本局无输赢。";
				}
				else if (zj == 0)
				{
					resultCode = 0;
					result = "庄家:【" + txtzj + "】,闲家:【" + txtxj + "】,同点数为11禁手，本局无输赢。";
				}
				else if (zj == 3)
				{
					resultCode = 0;
					result = "庄家:【" + txtzj + "】,闲家:【" + txtxj + "】,同点数为44禁手，本局无输赢。";
				}
				else
				{
					resultCode = 1;
					result = "庄家:【" + txtzj + "】,闲家:【" + txtxj + "】,同点数，本局庄家权重赢。";
					// resultCode = 0;
					// result =
					// "庄家:【"+txtzj+"】,闲家:【"+txtxj+"】,同点数庄家赢，但赌场老板表态这次就算了。";
				}
			}
			else if (xj > zj)
			{
				resultCode = 2;
				result = "庄家:【" + txtzj + "】,闲家:【" + txtxj + "】,闲家大，本局闲家赢。";
			}

			if (resultCode == 1)
			{
				jdbcTemplate.update("update house set money = money + " + (money * 1000) + " where hid='0001'");
				jdbcTemplate.update("update person set money = money - " + (money * 1000) + " where uuid='" + uuid
						+ "'");
			}
			else if (resultCode == 2)
			{
				jdbcTemplate.update("update house set money = money - " + (money * 1000) + " where hid='0001'");
				jdbcTemplate.update("update person set money = money + " + (money * 1000) + " where uuid='" + uuid
						+ "'");
			}

			long zjmoney = jdbcTemplate.queryForLong("select money from house where hid='0001'");
			long xjmoney = jdbcTemplate.queryForLong("select money from person where uuid='" + uuid + "'");

			zjmoney = zjmoney / 1000;
			xjmoney = xjmoney / 1000;

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("zj", zj);
			jsonObject.put("xj", xj);
			jsonObject.put("zjmoney", zjmoney);
			jsonObject.put("xjmoney", xjmoney);
			jsonObject.put("txtzj", txtzj);
			jsonObject.put("txtxj", txtxj);
			jsonObject.put("message", result);
			return jsonObject.toString();
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 201);
			jsonObject.put("message", "未查询到你的用户信息!你不会是黑户口吧？");
			return jsonObject.toString();
		}
	}

	public synchronized String getPai(String hid, String uuid, long money)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select *,(select money from person where uuid='"+uuid+"') as money,(select name from person where uuid=housemasterid) as zjname from player_house where hid='" + hid + "'");
		if (rowSet.next())
		{
			long currmoney = rowSet.getLong("housecurrendmoney");
			long moneyUUID = rowSet.getLong("money");
			String housemasterid = rowSet.getString("housemasterid");
			String zjname = rowSet.getString("zjname");
			
			if (currmoney < money * 1000)
			{
				// 1.取消桌主信息
				jdbcTemplate.update("update person set money=money+" + currmoney + " where uuid='" + housemasterid
						+ "'");
				jdbcTemplate.update("delete from player_house where hid='" + hid + "'");
				sendSystemMessageForDuchang(housemasterid, "你的赌坊坐庄资金亏损严重，已经自动撤庄，剩余银两已经打入你的账号。");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 202);
				jsonObject.put("message", "庄家资金亏损无法严重，无法承担本次赌注，本次操作取消。");
				return jsonObject.toString();
			}
			
			if(moneyUUID < money * 1000)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 208);
				jsonObject.put("message", "["+zjname+"] 一把抓住你的衣领把你如小鸡一般拎出去了，你身上没有几个子了。");
				return jsonObject.toString();
			}

			String[] pai = new String[] { "天牌", "地牌", "人牌" };
			Random random = new Random();
			int zj = random.nextInt(3);
			int xj = random.nextInt(3);

			if (zj == xj)
			{
				long zjmoney = jdbcTemplate.queryForLong("select housecurrendmoney from player_house where hid='"+hid+"'");
				long xjmoney = jdbcTemplate.queryForLong("select money from person where uuid='" + uuid + "'");

				zjmoney = zjmoney / 1000;
				xjmoney = xjmoney / 1000;
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 200);
				jsonObject.put("zj", pai[zj]);
				jsonObject.put("xj", pai[xj]);
				jsonObject.put("zjmoney", zjmoney);
				jsonObject.put("xjmoney", xjmoney);
				jsonObject.put("zjname", zjname);
				jsonObject.put("message", "牌色相同，无输赢。");
				return jsonObject.toString();
			}
			else if (zj == 0)
			{
				if (xj == 1)
				{
					// 減除
					jdbcTemplate.update("update person set money=money-"+(money*1000)+" where uuid='"+uuid+"'");
					jdbcTemplate.update("update player_house set housecurrendmoney=housecurrendmoney+"+(money*1000)+" where hid='"+hid+"'");
					
					long zjmoney = jdbcTemplate.queryForLong("select housecurrendmoney from player_house where hid='"+hid+"'");
					long xjmoney = jdbcTemplate.queryForLong("select money from person where uuid='" + uuid + "'");

					zjmoney = zjmoney / 1000;
					xjmoney = xjmoney / 1000;
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("zjmoney", zjmoney);
					jsonObject.put("xjmoney", xjmoney);
					jsonObject.put("zjname", zjname);
					jsonObject.put("code", 200);
					jsonObject.put("zj", pai[zj]);
					jsonObject.put("xj", pai[xj]);
					jsonObject.put("message", "庄家天牌，闲家地牌，庄家赢！");
					return jsonObject.toString();
				}
				else if (xj == 2)
				{
					// 減除
					jdbcTemplate.update("update person set money=money+"+(money*1000)+" where uuid='"+uuid+"'");
					jdbcTemplate.update("update player_house set housecurrendmoney=housecurrendmoney-"+(money*1000)+" where hid='"+hid+"'");
					
					long zjmoney = jdbcTemplate.queryForLong("select housecurrendmoney from player_house where hid='"+hid+"'");
					long xjmoney = jdbcTemplate.queryForLong("select money from person where uuid='" + uuid + "'");

					zjmoney = zjmoney / 1000;
					xjmoney = xjmoney / 1000;
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("zjmoney", zjmoney);
					jsonObject.put("xjmoney", xjmoney);
					jsonObject.put("zjname", zjname);
					jsonObject.put("code", 200);
					jsonObject.put("zj", pai[zj]);
					jsonObject.put("xj", pai[xj]);
					jsonObject.put("message", "庄家天牌，闲家人牌，闲家赢！");
					return jsonObject.toString();
				}
			}
			else if (zj == 1)
			{
				if (xj == 0)
				{
					// 減除
					jdbcTemplate.update("update person set money=money+"+(money*1000)+" where uuid='"+uuid+"'");
					jdbcTemplate.update("update player_house set housecurrendmoney=housecurrendmoney-"+(money*1000)+" where hid='"+hid+"'");
					
					long zjmoney = jdbcTemplate.queryForLong("select housecurrendmoney from player_house where hid='"+hid+"'");
					long xjmoney = jdbcTemplate.queryForLong("select money from person where uuid='" + uuid + "'");

					zjmoney = zjmoney / 1000;
					xjmoney = xjmoney / 1000;
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("zjmoney", zjmoney);
					jsonObject.put("xjmoney", xjmoney);
					jsonObject.put("zjname", zjname);
					jsonObject.put("code", 200);
					jsonObject.put("zj", pai[zj]);
					jsonObject.put("xj", pai[xj]);
					jsonObject.put("message", "庄家地牌，闲家天牌，闲家赢！");
					return jsonObject.toString();
				}
				else if (xj == 2)
				{
					// 減除
					jdbcTemplate.update("update person set money=money-"+(money*1000)+" where uuid='"+uuid+"'");
					jdbcTemplate.update("update player_house set housecurrendmoney=housecurrendmoney+"+(money*1000)+" where hid='"+hid+"'");
					
					long zjmoney = jdbcTemplate.queryForLong("select housecurrendmoney from player_house where hid='"+hid+"'");
					long xjmoney = jdbcTemplate.queryForLong("select money from person where uuid='" + uuid + "'");

					zjmoney = zjmoney / 1000;
					xjmoney = xjmoney / 1000;
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("zjmoney", zjmoney);
					jsonObject.put("xjmoney", xjmoney);
					jsonObject.put("zjname", zjname);
					jsonObject.put("zj", pai[zj]);
					jsonObject.put("xj", pai[xj]);
					jsonObject.put("code", 200);
					jsonObject.put("message", "庄家地牌，闲家人牌，庄家赢！");
					return jsonObject.toString();
				}
			}
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "赌场负责翻牌的工作人员手一抖，不小心把牌给撕了，本次只能作罢。");
		return jsonObject.toString();
	}

	public String metohousemaster(String uuid, long money, int type)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select *,(select money from person where uuid='" + uuid
				+ "') as money from player_house where housemasterid ='" + uuid + "'");
		if (rowSet.next())
		{
			// else if(type == 3)
			// {
			// jdbcTemplate.update("update player_house set ");
			// JSONObject jsonObject = new JSONObject();
			// jsonObject.put("code", 203);
			// jsonObject.put("message",
			// "你成功追加了资金"+money+"两白银(最低100两起价，当你撤庄时，多于坐庄注入资金的那部分收入将分给赌场25%的利润。比如：坐庄出资100两，撤庄收入共500两，则多余的400两需支付25%的场地费100两)");
			// return jsonObject.toString();
			// }

			long houseinitmoney = rowSet.getLong("houseinitmoney");
			long housecurrendmoney = rowSet.getLong("housecurrendmoney");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("houseinitmoney", houseinitmoney / 1000);
			jsonObject.put("housecurrendmoney", housecurrendmoney / 1000);
			return jsonObject.toString();
		}

		if (type == 2)
		{
			long moneyCount = jdbcTemplate.queryForLong("select money from person where uuid='"+uuid+"'");
			if (moneyCount < money * 1000)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 202);
				jsonObject.put("message", "赌场老板丹枫公子一看你没那么多钱，认定你是来消遣他的，眼看就要招呼手下群殴你，你一个机灵赶紧撒腿就跑。");
				return jsonObject.toString();
			}

			String hid = UUID.randomUUID().toString().replaceAll("-", "");
			//在庄台表插入一条坐庄数据
			jdbcTemplate
					.update("insert into player_house(hid,housemasterid,houseinitmoney,housecurrendmoney,createtime) value('"
							+ hid + "','" + uuid + "'," + (money * 1000) + "," + (money * 1000) + ",UNIX_TIMESTAMP())");
			// 扣除坐庄的钱
			jdbcTemplate.update("update person set money=money-"+money*1000+" where uuid='"+uuid+"'");
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "你成功注入资金"+money+"两银子坐庄。可以再次点击我要坐庄来查看庄台收支情况，以及撤庄（必须满一个小时才能撤庄）");
			return jsonObject.toString();
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject
				.put("message",
						"你想要出多少钱来坐庄？(最低200两起价，当你撤庄时(必须满一个小时才能撤庄)，多于坐庄注入资金的那部分收入将分给赌场老板 丹枫公子25%的利润，。比如：坐庄出资200两，撤庄收入共600两，则多余的400两需支付25%的场地费100两)");
		return jsonObject.toString();
	}

	public String cancelhousemaster(String uuid)
	{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from player_house where housemasterid ='" + uuid
				+ "' and createtime < UNIX_TIMESTAMP()-3600");
		if (rowSet.next())
		{
			String hid = rowSet.getString("hid");
			long houseinitmoney = rowSet.getLong("houseinitmoney");
			long housecurrendmoney = rowSet.getLong("housecurrendmoney");
			long diff = housecurrendmoney - houseinitmoney;
			long suishou = 0;
			if (diff > 100)
			{
				suishou = (long) (diff * 0.25);
				// 把钱打入
				jdbcTemplate.update("update person set money=money+" + suishou + " where uuid='0005'");
			}

			housecurrendmoney = housecurrendmoney - suishou;
			jdbcTemplate.update("update person set money=money+" + housecurrendmoney + " where uuid='" + uuid + "'");
			jdbcTemplate.update("delete from player_house where housemasterid='" + uuid + "' and hid='" + hid + "'");

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 200);
			jsonObject.put("message", "你已经成功撤庄，[ 注入本金:" + houseinitmoney + ",撤庄资金" + housecurrendmoney + "] 收支情况:"
					+ diff+"  赌场税支付:"+suishou);
			return jsonObject.toString();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 201);
		jsonObject.put("message", "坐庄时间不能少于1小时就撤庄");
		return jsonObject.toString();
	}

	public String gethousemasterlist()
	{
		// 只加载创建时间在七天内的桌位并且资金至少还剩20两地
		String sql = "select *,(select name from person where uuid=housemasterid) as name from player_house where createtime > UNIX_TIMESTAMP() - 604800 and housecurrendmoney > 20000";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		JSONArray arrayLeave = JSONArray.fromObject(list);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		jsonObject.put("data", arrayLeave);
		return jsonObject.toString();
	}

	public Object sendSystemMessageForDuchang(String receiver_uuid, String keyword)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 发系统消息提示
		String mid = UUID.randomUUID().toString().replaceAll("-", "");
		String notiMassgae = "insert into message(mid,senderid,receiveid,message,sendtime,readflag,sendername,ordertime,type) "
				+ "values("
				+ "'"
				+ mid
				+ "','0000','"
				+ receiver_uuid
				+ "','"
				+ keyword
				+ "','"
				+ sdf.format(new Date(System.currentTimeMillis()))
				+ "','false','赌坊消息',"
				+ System.currentTimeMillis()
				+ ",2)";
		jdbcTemplate.update(notiMassgae);
		return null;
	}
}
